@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.irBuiltIns
import com.lhwdev.ktui.plugin.compiler.util.symbol
import com.lhwdev.ktui.plugin.compiler.util.toIrType
import org.jetbrains.kotlin.backend.common.IrElementVisitorVoidWithContext
import org.jetbrains.kotlin.backend.common.ScopeWithIr
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.descriptors.IrSimpleBuiltinOperatorDescriptorImpl
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrIfThenElseImpl
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtLambdaArgument
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.typeUtil.isSubtypeOf
import org.jetbrains.kotlin.types.typeUtil.replaceArgumentsWithStarProjections
import java.util.IdentityHashMap
import java.util.Locale
import java.util.Stack


fun IrElement.dumpSrcColored(allowSpecialMarker: Boolean = true, debug: Boolean = false) =
	buildString {
		patchDeclarationParents()
		acceptVoid(IrSourcePrinterVisitor(this, allowSpecialMarker, debug))
	}

fun IrElement.logSrcColored(allowSpecialMarker: Boolean = true, debug: Boolean = false) {
	patchDeclarationParents()
	fixIndents {
		IrSourcePrinterVisitor(object : Appendable {
			override fun append(csq: CharSequence): Appendable {
				logInternalWithoutNewline(csq.toString(), color = "")
				return this
			}
			
			override fun append(csq: CharSequence, start: Int, end: Int) =
				append(csq.substring(start, end))
			
			override fun append(c: Char) = append("$c")
		}, allowSpecialMarker, debug).let { acceptVoid(it) }
		logln()
	}
}

fun String.decapitalizeFirst() = first().toLowerCase() + drop(1)


private class Printer(val out: Appendable, val indentUnit: CharSequence) {
	private var indents = 0
	private var indentCache = ""
	private var hasIndent = false
	
	fun print(vararg contents: CharSequence) {
		contents.forEach { print(it) }
	}
	
	fun print(content: CharSequence) {
		if('\n' in content) {
			content.split("\n").forEachIndexed { index, line ->
				if(index != 0) println()
				print(line)
			}
			return
		}
		
		if(content.isEmpty()) return
		
		if(!hasIndent) {
			hasIndent = true
			out.append(indentCache)
		}
		
		out.append(content)
	}
	
	fun println() {
		out.append("\n")
		hasIndent = false
	}
	
	fun pushIndent() {
		indents++
		updateIndents()
	}
	
	fun popIndent() {
		indents--
		updateIndents()
	}
	
	private fun updateIndents() {
		indentCache = buildString {
			for(i in 0 until indents) append(indentUnit)
		}
	}
}

inline fun <T : Any> merge(a: T?, b: T?, merger: (T, T) -> T): T? = when {
	a == null -> b
	b == null -> a
	else -> merger(a, b)
}


/**
 * Log the ir tree in the form of the Kotlin code.
 * Note that the result should generally be used for the sake of debugging.
 * If you enable debug flag on the constructor, then it prints some useful information and code
 * errors(not all the errors, only some errors).
 */
private class IrSourcePrinterVisitor(out: Appendable, val allowSpecialMarker: Boolean = true, val debug: Boolean = false) : IrElementVisitorVoidWithContext() {
	private val printer = Printer(out, "\t")
	private var lastBlock = false
	private var currentIndex = 0
	
	private enum class SpaceType { none, compact, space }
	
	private var lastSpace = SpaceType.none
	
	private val labels = IdentityHashMap<IrReturnTargetSymbol, String>()
	
	
	class Grouping(val selfType: Type, var inherit: GroupingInherit) {
		val localOverrides by lazy { Stack<Pair<Type, Type>>() }
	}
	
	class GroupingInherit(
		val isAnnotationCall: Boolean = false,
		val isNotCall: Boolean = false,
		val overrideAllType: Type? = null,
		val stylePrefix: AnsiItem? = null,
		val localOverrides: List<Pair<Type, Type>>? = null,
		val printIntAsBinary: Boolean = false
	) {
		fun merge(other: GroupingInherit) = GroupingInherit(
			isAnnotationCall = other.isAnnotationCall || isAnnotationCall,
			isNotCall = other.isNotCall || isNotCall,
			overrideAllType = other.overrideAllType ?: overrideAllType,
			stylePrefix = other.stylePrefix ?: stylePrefix,
			localOverrides = merge(other.localOverrides, localOverrides) { a, b -> b + a },
			printIntAsBinary = other.printIntAsBinary || printIntAsBinary
		)
	}
	
	
	private val groupingStack = Stack<Grouping>().apply {
		push(Grouping(Type.none, GroupingInherit()))
	}
	private inline val currentGroup get() = groupingStack.peek()
	private inline var currentInherit
		get() = currentGroup.inherit
		set(value) {
			currentGroup.inherit = value
		}
	
	private fun mergeInherit(other: GroupingInherit) {
		val group = currentGroup
		group.inherit = group.inherit.merge(other)
	}
	
	private inline fun <R> withScope(scope: ScopeWithIr, block: () -> R): R {
		allScopes.add(scope)
		return try {
			block()
		} finally {
			allScopes.removeAt(allScopes.lastIndex)
		}
	}
	
	private enum class Type(val ansi: AnsiItem?) {
		none(null),
		keyword(Ansi.purple),
		lightKeyword(Ansi.brightPurple),
		identifier(Ansi.brightWhite),
		property(Ansi.brightWhite),
		function(Ansi.brightBlue),
		localVariable(Ansi.brightWhite),
		comment(Ansi.green),
		numberLiteral(Ansi.brightGreen),
		stringLiteral(Ansi.brightGreen),
		otherLiteral(Ansi.brightGreen),
		separator(Ansi.cyan),
		importantSeparator(Ansi.reset),
		specialOperator(Ansi.brightCyan),
		braces(Ansi.brightCyan),
		annotationClass(Ansi.brightPurple),
		enumClass(Ansi.yellow),
		classClass(Ansi.yellow),
		objectClass(Ansi.yellow),
		interfaceClass(Ansi.brightGreen),
		typeAlias(classClass.ansi),
		typeParameter(Ansi.cyan),
		valueParameter(Ansi.brightRed),
		label(Ansi.reset),
		dynamicMember(ansiItem { red + underlined }),
		unknown(Ansi.red),
		debug(ansiItem { brightBlack }),
		error(ansiItem { red + bold }),
		specialMarker(Ansi.brightBlack)
	}
	
	
	private val regexNeedsSpace = Regex("([a-zA-Z0-9_$]+([ a-zA-Z0-9_\$]+)?|[+*\\-/]|==|===|!=|!==|>=|<=)")
	private val regexCompactSpace = Regex("[),:?\\]]")
	private val regexSpaceNone = Regex("[{]")
	private val regexNoneSpace = Regex("[}]")
	private val regexSpaceSpace = Regex("([+\\-*/=<>]|\\+=|-=|/=|\\*=|\\|=|&=|\\?:|->|&&|\\|\\|)")
	private val regexCompactCompact = Regex("([.($]|\\?\\.|\\.\\.|::)")
	private val regexCompactNone = Regex("([>\\[]|!!|\\++|--)")
	private val regexNoneCompact = Regex("[<\\]]")
	
	
	private fun print(content: CharSequence, type: Type) {
		if(content.isEmpty()) return
		if(!allowSpecialMarker && type == Type.specialMarker) return
		
		val group = currentGroup
		val newType = group.inherit.overrideAllType
			?: group.localOverrides.lastOrNull { it.first == type }?.second
			?: group.inherit.localOverrides?.lastOrNull { it.first == type }?.second
			?: type
		
		fun doPrint(headType: SpaceType, tailType: SpaceType) {
			val hasHeadSpace = when(lastSpace) {
				SpaceType.none -> when(headType) {
					SpaceType.none -> false
					SpaceType.compact -> false
					SpaceType.space -> true
				}
				SpaceType.compact -> when(headType) {
					SpaceType.none -> false
					SpaceType.compact -> false
					SpaceType.space -> false
				}
				SpaceType.space -> when(headType) {
					SpaceType.none -> true
					SpaceType.compact -> false
					SpaceType.space -> true
				}
			}
			
			if(hasHeadSpace) printer.print(" ")
			printer.print((group.inherit.stylePrefix + newType.ansi).build(), content)
			lastSpace = tailType
		}
		
		when {
			regexCompactCompact.matches(content) -> doPrint(SpaceType.compact, SpaceType.compact)
			regexNoneSpace.matches(content) -> doPrint(SpaceType.none, SpaceType.space)
			regexSpaceNone.matches(content) -> doPrint(SpaceType.space, SpaceType.none)
			regexCompactSpace.matches(content) -> doPrint(SpaceType.compact, SpaceType.space)
			regexSpaceSpace.matches(content) -> doPrint(SpaceType.space, SpaceType.space)
			regexCompactNone.matches(content) -> doPrint(SpaceType.compact, SpaceType.none)
			regexNoneCompact.matches(content) -> doPrint(SpaceType.none, SpaceType.compact)
			lastBlock -> doPrint(SpaceType.space, SpaceType.none)
			else -> doPrint(SpaceType.none, SpaceType.none)
		}
//		printer.print(AnsiGraphics.RESET)
		lastBlock = regexNeedsSpace.matches(content)
		currentIndex += content.length
	}
	
	private fun compact() {
		lastSpace = SpaceType.compact
	}
	
	private fun println() {
		printer.println()
		lastBlock = false
		lastSpace = SpaceType.none
	}
	
	private fun println(content: Any?, type: Type) {
		print(content, type)
		println()
	}
	
	private inline fun grouping(prefix: CharSequence, postfix: CharSequence, type: Type = Type.separator, inherit: GroupingInherit = currentGroup.inherit, addIndent: Boolean = false, newLine: Boolean = false, block: () -> Unit) {
		grouping({ print(prefix, type) }, { print(postfix, type) }, type, inherit, addIndent, newLine, block)
	}
	
	private inline fun grouping(prefix: () -> Unit, postfix: () -> Unit, type: Type = Type.separator, inherit: GroupingInherit = currentGroup.inherit, addIndent: Boolean = false, newLine: Boolean = false, block: () -> Unit) {
		groupingStack.push(Grouping(type, inherit))
		prefix()
		if(addIndent) printer.pushIndent()
		if(newLine) println()
		
		try {
			block()
		} finally {
			groupingStack.pop()
			if(addIndent) printer.popIndent()
			if(newLine) println()
			postfix()
		}
	}
	
	private inline fun grouping(type: Type = Type.separator, inherit: GroupingInherit = currentGroup.inherit, addIndent: Boolean = false, block: () -> Unit) {
		groupingStack.push(Grouping(type, inherit))
		if(addIndent) printer.pushIndent()
		
		try {
			block()
		} finally {
			groupingStack.pop()
			if(addIndent) printer.popIndent()
		}
	}
	
	private inline fun stylePrefix(ansi: AnsiItem, block: () -> Unit) {
		grouping(inherit = GroupingInherit(stylePrefix = ansi), block = block)
	}
	
	private inline fun stylePrefix(ansi: AnsiItem) {
		mergeInherit(GroupingInherit(stylePrefix = ansi))
	}
	
	private inline fun indented(block: () -> Unit) {
		grouping(addIndent = true, block = block)
	}
	
	private inline fun emptyGrouping(prefix: CharSequence, postfix: CharSequence, type: Type = Type.separator) {
		print(prefix, type)
		print(postfix, type)
	}
	
	private inline fun groupExpr(block: () -> Unit) {
		// TODO: insert ( ) if necessary
		printSeparator("(")
		block()
		printSeparator(")")
	}
	
	private inline fun bracedBlock(newLine: Boolean = true, type: Type = Type.separator, block: () -> Unit) {
		grouping("{", "}", type, addIndent = true, newLine = newLine) {
			block()
		}
	}
	
	private inline fun groupParen(addIndent: Boolean = false, newLine: Boolean = false, type: Type = Type.separator, block: () -> Unit) {
		grouping("(", ")", type, addIndent = addIndent, newLine = newLine) {
			block()
		}
	}
	
	private inline fun <R> provideInherit(other: GroupingInherit, block: () -> R): R {
		val group = currentGroup
		val last = group.inherit
		group.inherit = last.merge(other)
		return try {
			block()
		} finally {
			group.inherit = last
		}
	}
	
	private inline fun overrideAllType(type: Type, block: () -> Unit) {
		provideInherit(GroupingInherit(overrideAllType = type), block)
	}
	
	private inline fun specialMarker(block: () -> Unit) {
		if(allowSpecialMarker) overrideAllType(Type.specialMarker, block)
	}
	
	private inline fun overrideType(target: Type, with: Type, block: () -> Unit) =
		grouping(inherit = GroupingInherit(localOverrides = listOf(target to with)), block = block)
	
	private inline fun <R> overrideTypeLocal(target: Type, with: Type, block: () -> R): R {
		val localOverrides = currentGroup.localOverrides
		
		localOverrides.push(target to with)
		return try {
			block()
		} finally {
			localOverrides.pop()
		}
	}
	
	private inline fun print(content: Any?, type: Type) {
		print(content.toString(), type)
	}
	
	private inline fun print(type: Type, block: () -> CharSequence) {
		print(block(), type)
	}
	
	private fun printSpace() {
		printer.print(" ")
		lastBlock = false
		lastSpace = SpaceType.compact
	}
	
	private fun IrElement.print() {
		acceptVoid(this@IrSourcePrinterVisitor)
	}
	
	private inline fun <T> Iterable<T>.iterate(block: (element: T, hasNext: Boolean) -> Unit) {
		val iterator = iterator()
		var lastHasNext = iterator.hasNext()
		while(lastHasNext) {
			val next = iterator.next()
			lastHasNext = iterator.hasNext()
			block(next, lastHasNext)
		}
	}
	
	private fun Iterable<IrElement>.printJoin(separator: CharSequence, separatorType: Type = Type.separator) =
		grouping {
			var indexBefore = currentIndex
			
			iterate { element, hasNext ->
				element.print()
				if(hasNext && indexBefore != currentIndex) {
					print(separator, separatorType)
					indexBefore = currentIndex
				}
			}
		}
	
	private inline fun <T> Iterable<T>.printJoin(separator: CharSequence, separatorType: Type = Type.separator, block: (T) -> Unit) {
		var indexBefore = currentIndex
		
		iterate { element, hasNext ->
			block(element)
			if(hasNext && indexBefore != currentIndex) {
				print(separator, separatorType)
				indexBefore = currentIndex
			}
		}
	}
	
	private inline fun <T> List<T>.printJoin(separator: () -> Unit, block: (T) -> Unit) {
		var indexBefore = currentIndex
		val last = lastIndex
		
		forEachIndexed { index, element ->
			block(element)
			if(index != last && indexBefore != currentIndex) {
				separator()
				indexBefore = currentIndex
			}
		}
	}
	
	@JvmName("printWithReceiver")
	private inline fun CharSequence.print(type: Type) {
		print(this, type)
	}
	
	private inline fun printKeyword(content: Any?) {
		print(content, Type.keyword)
	}
	
	private inline fun printIdentifier(content: Any?) {
		print(content, Type.identifier)
	}
	
	private inline fun printSeparator(content: Any?) {
		print(content, Type.separator)
	}
	
	@JvmName("printWithReceiver")
	private inline fun Any?.print(type: Type) {
		print(this.toString(), type)
	}
	
	private inline fun unknown(content: CharSequence) {
		print(content, Type.unknown)
	}
	
	private fun debug(content: CharSequence) {
		print(content, Type.debug)
	}
	
	private fun none(content: CharSequence) {
		print(content, Type.none)
	}
	
	private inline fun debug(block: () -> Unit) {
		if(debug) provideInherit(GroupingInherit(stylePrefix = Type.debug.ansi), block)
	}
	
	private fun errorScope() {
		mergeInherit(GroupingInherit(overrideAllType = Type.error))
	}
	
	private fun printError(content: CharSequence) {
		print(content, Type.error)
	}
	
	private fun Visibility.print(default: Visibility = Visibilities.PUBLIC) = when(this) {
		default -> ""
		Visibilities.PUBLIC -> "public"
		// Visibilities.DEFAULT_VISIBILITY // == public
		Visibilities.PRIVATE -> "private"
		Visibilities.INTERNAL -> "internal"
		Visibilities.PROTECTED -> "protected"
		Visibilities.LOCAL -> ""
		Visibilities.INHERITED -> ""
		Visibilities.INVISIBLE_FAKE -> ""
		else -> "TODO: ${this.name.toLowerCase(Locale.ROOT)}"
	}.print(Type.keyword)
	
	
	private fun Modality.print(default: Modality = Modality.FINAL) = when(this) {
		default -> ""
		Modality.FINAL -> "final"
		Modality.SEALED -> "sealed"
		Modality.OPEN -> "open"
		Modality.ABSTRACT -> "abstract"
	}.print(Type.keyword)
	
	private fun List<IrTypeParameter>.print() {
		if(isNotEmpty()) {
			grouping("<", ">", Type.separator) {
				printJoin(",")
			}
		}
	}
	
	private fun IrType.print() {
		when(this) {
			is IrDynamicType -> {
				annotations.printAnnotations(false)
				print("dynamic", Type.lightKeyword)
			}
			is IrErrorType -> {
				annotations.printAnnotations(false)
				unknown("[ERROR]")
			}
			is IrSimpleType -> {
				fun IrSimpleType.printContent() {
					if(isFunction()) {
						val extensionAnnotation = kotlinPackageFqn.child(Name.identifier("ExtensionFunctionType"))
						val parameterNameAnnotation = kotlinPackageFqn.child(Name.identifier("ParameterName"))
						val isFirstExtension = annotations.hasAnnotation(extensionAnnotation)
						val realAnnotations = annotations.filter { (it.symbol.descriptor as ConstructorDescriptor).constructedClass.fqNameSafe != extensionAnnotation }
						realAnnotations.printAnnotations(false)
						if(realAnnotations.isNotEmpty()) printSpace()
						
						val parameters = arguments.dropLast(1 /* return type */).let { if(isFirstExtension) it.drop(1) else it }
						val returnType = arguments.last()
						
						if(isFirstExtension) {
							arguments.first().printTypeArgument()
							printSeparator(".")
						}
						
						groupParen {
							parameters.printJoin(",") {
								val type = it.typeOrNull
								val parameterName = type?.getAnnotation(parameterNameAnnotation)
								
								if(parameterName != null) {
									print((parameterName.valueArguments.single() as IrConst<*>).value, Type.valueParameter)
									printSeparator(":")
								}
								
								if(type == null) it.printTypeArgument()
								else {
									val newAnnotations = type.annotations.filter { it.symbol.descriptor.fqNameSafe != parameterNameAnnotation }
									type.replaceAnnotations(newAnnotations).print()
								}
							}
						}
						
						printSeparator("->")
						returnType.printTypeArgument()
						return
					}
					
					annotations.printAnnotations(false)
					
					classifier.print()
					
					if(arguments.isNotEmpty()) grouping("<", ">") {
						arguments.printJoin(",") {
							it.printTypeArgument()
						}
					}
					
					if(hasQuestionMark)
						print("?", Type.importantSeparator)
				}
				
				val abbreviation = abbreviation
				if(abbreviation != null) {
					abbreviation.annotations.printAnnotations(false)
					print(abbreviation.typeAlias.descriptor.name, Type.typeAlias)
					if(arguments.isNotEmpty()) grouping("<", ">") {
						abbreviation.arguments.printJoin(",") {
							it.printTypeArgument()
						}
					}
					if(hasQuestionMark) print("?", Type.specialOperator)
					
					overrideAllType(Type.specialMarker) {
						groupParen {
							printSeparator("=")
							printContent()
						}
					}
				} else printContent()
			}
			
			else -> unknown("[UNKNOWN: $this]")
		}
	}
	
	private fun IrClassifierSymbol.print() {
		when(this) {
			is IrClassSymbol ->
				print(descriptor.name, if(descriptor.kind == ClassKind.INTERFACE) Type.interfaceClass else Type.classClass)
			is IrTypeParameterSymbol -> print(descriptor.name, Type.typeParameter)
			else -> error(toString())
		}
	}
	
	private fun IrTypeArgument.printTypeArgument() {
		when(this) {
			is IrStarProjection -> "*".print(Type.specialOperator)
			
			is IrTypeProjection -> {
				print(variance.label, Type.lightKeyword)
				type.print()
			}
			
			else -> unknown("UNKNOWN[$this]")
		}
	}
	
	fun List<IrConstructorCall>.printAnnotations(newLine: Boolean) {
		if(isNotEmpty()) {
			overrideType(Type.identifier, Type.annotationClass) {
				if(size == 1) {
					print("@", Type.annotationClass)
					single().printCallAnnotation()
				} else {
					grouping("@[", "]", Type.annotationClass) {
						printJoin(",") {
							it.printCallAnnotation()
						}
					}
				}
			}
			
			if(newLine) println()
		}
	}
	
	val filesGap = "\n\n\n"
	val declarationGap = "\n\n"
	val statementGap = "\n"
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	override fun visitElement(element: IrElement) {
		unknown(element::class.java.simpleName)
		debug { element.check() }
	}
	
	private inline fun <T> statement(statement: T, block: T.() -> Unit) = grouping {
		statement.block()
	}
	
	private inline fun <T> declare(declaration: T, block: T.() -> Unit) =
		statement(declaration, block)
	
	private inline fun <T> expr(expression: T, block: T.() -> Unit) = grouping {
		expression.block()
	}
	
	
	///// validation
	val set = mutableSetOf<IrElement>()
	val checkedTypes = mutableSetOf<IrType>()
	
	
	///// declarations
	
	private fun IrElement.check() {
		if(set.contains(this)) printError(" <- duplicate IR node")
		set.add(this)
		
		// Nothing to do.
	}
	
	private fun ensureTypesEqual(actualType: IrType, expectedType: IrType) {
		if(actualType != expectedType) {
			printError(" <- unexpected type: expected ${expectedType.render()}, got ${actualType.render()}")
		}
	}
	
	private fun IrExpression.ensureNullable() {
		if(!type.isNullable())
			printError(" <- expected a nullable type, got ${type.render()}")
	}
	
	private fun IrExpression.ensureTypeIs(expectedType: IrType) {
		ensureTypesEqual(type, expectedType)
	}
	
	private fun IrSymbol.ensureBound(expression: IrExpression) {
		if(!this.isBound && expression.type !is IrDynamicType) {
			printError("<- unbound symbol $this")
		}
	}
	
	override fun visitFunctionAccess(expression: IrFunctionAccessExpression) {
		super.visitFunctionAccess(expression)
		
		expression.symbol.ensureBound(expression)
	}
	
	private fun IrOverridableDeclaration<*>.checkOverrides() {
		for(overriddenSymbol in overriddenSymbols) {
			val overriddenDeclaration = overriddenSymbol.ownerOrNull as? IrDeclarationWithVisibility
				?: continue
			if(overriddenDeclaration.visibility == Visibilities.PRIVATE) {
				printError("<- overrides private declaration ${overriddenDeclaration.descriptor.dump()}")
			}
		}
	}
	
	override fun visitExpression(expression: IrExpression) {
		super.visitExpression(expression)
		
		debug {
			expression.check()
			checkType(expression.type)
		}
	}
	
	private fun checkType(type: IrType) {
		if(type in checkedTypes)
			return
		
		when(type) {
			is IrSimpleType -> {
				if(!type.classifier.isBound) grouping {
					errorScope()
					debug(" <- Type: ")
					type.print()
					debug(" has unbound classifier")
				}
			}
		}
		
		checkedTypes.add(type)
	}
	/// >= file
	
	override fun visitModuleFragment(declaration: IrModuleFragment) {
		println("// module ${declaration.name}", Type.comment)
		declaration.files.printJoin(filesGap)
		
		debug { declaration.check() }
	}
	
	override fun visitFileNew(declaration: IrFile) {
		println("// file ${declaration.name} (${declaration.fqName})", Type.comment)
		declaration.declarations.printJoin(declarationGap)
		debug { declaration.check() }
	}
	
	override fun visitFieldNew(declaration: IrField) = declare(declaration) {
		super.visitFieldNew(this)
		
		debug {
			check()
			checkOverrides()
		}
	}
	/// class
	
	override fun visitClassNew(declaration: IrClass): Unit = declare(declaration) {
		val isEnum = kind == ClassKind.ENUM_CLASS
		
		// head
		annotations.printAnnotations(true)
		visibility.print()
		modality.print()
		
		if(isCompanion) printKeyword("companion")
		if(isData) printKeyword("data")
		if(isExpect) printKeyword("expect")
		if(isExternal) printKeyword("external")
		if(isFun) printKeyword("fun")
		if(isInline) printKeyword("inline")
		if(isInner) printKeyword("inner")
		
		
		when(kind) {
			ClassKind.CLASS -> "class"
			ClassKind.INTERFACE -> "interface"
			ClassKind.ENUM_CLASS -> "enum class"
			ClassKind.ENUM_ENTRY -> error("it must have handled already")
			ClassKind.ANNOTATION_CLASS -> "annotation class"
			ClassKind.OBJECT -> "object"
		}.print(Type.keyword)
		
		symbol.print()
		typeParameters.print()
		
		val constructorProperties = mutableSetOf<IrProperty>()
		
		primaryConstructor?.let { it ->
			val isCompactConstructor = it.annotations.isEmpty() &&
				(it.visibility == Visibilities.PUBLIC || isEnum) &&
				!(it.isInline || it.isExpect || it.isExternal)
			
			if(!isCompactConstructor) {
				printSpace()
				it.printFunctionHead()
			}
			
			grouping("(", ")", Type.separator) {
				val properties = declarations.mapNotNull { it as? IrProperty }
				
				it.valueParameters.printJoin(",") { parameter ->
					val property = properties.correspondingProperty(parameter)
					if(property != null) {
						constructorProperties.add(property)
						print(if(property.isVar) "var" else "val", Type.keyword)
						overrideType(Type.identifier, Type.property) {
							parameter.print()
						}
					} else parameter.print()
				}
			}
			
			// identifying delegations is hard for IR
			// class Hi : Class by expr
			// into ->
			// field origin = DELEGATE, ...
			// |- initializer = expr
			// ...
			// <all members in Class> origin = DELEGATED_MEMBER, overridden: <member ..> in Class
			val delegatedMembers = mutableMapOf<IrSymbol, IrOverridableDeclaration<*>>()
			declarations.filterIsInstance<IrOverridableDeclaration<*>>()
				.filter { it.origin == IrDeclarationOrigin.DELEGATED_MEMBER }.forEach { delegatedMember ->
					delegatedMember.overriddenSymbols.forEach {
						delegatedMembers[(it.descriptor.containingDeclaration!! as ClassDescriptor).symbol] = delegatedMember
					}
				}
			
			var superTypes = superTypes.filter { !it.isAny() }
			if(isEnum) {
				val enumFqName = kotlinPackageFqn.child(Name.identifier("Enum"))
				superTypes = superTypes.filter { it.classOrNull?.descriptor?.fqNameSafe != enumFqName }
			}
			if(kind == ClassKind.ANNOTATION_CLASS) {
				val annotationFqName = kotlinPackageFqn.child(Name.identifier("Annotation"))
				superTypes = superTypes.filter { it.classOrNull?.descriptor?.fqNameSafe != annotationFqName }
			}
			
			if(superTypes.isNotEmpty()) {
				printSpace()
				print(":", Type.separator)
				superTypes.printJoin(",") { superType ->
					val classSymbol = superType.classOrNull ?: run {
						grouping {
							errorScope()
							superType.print()
							debug(" <- super not class")
						}
						return@printJoin
					}
					when(classSymbol.descriptor.kind) {
						ClassKind.INTERFACE -> {
							superType.print()
							val delegatedMember = delegatedMembers[classSymbol]
							if(delegatedMember != null) {
								fun IrBody.asExpression() = when(val last = statements.last()) {
									is IrReturn -> last.value
									is IrExpression -> last
									else -> error("$last")
								}
								
								val toGetDelegate = when(delegatedMember) {
									is IrProperty -> {
										val getterCall = delegatedMember.getter!!.body!!.asExpression() as IrCall /* getter <get-???> */
										getterCall.dispatchReceiver
									}
									is IrFunction -> {
										val call = delegatedMember.body!!.asExpression() as IrCall
										call.dispatchReceiver
									}
									else -> error("TODO? unexpected delegated member $delegatedMember")
								} as IrGetField
								printKeyword("by")
								toGetDelegate.symbol.owner.initializer!!.expression.print()
							}
						}
						else -> {
							// class Abc : OtherClass() /* <- this */, Interface1, ...
							
							// BLOCK_BODY
							//    DELEGATING_CONSTRUCTOR_CALL 'CLASS CLASS name:OtherClass ...'
							//      ... (arguments)
							//    INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:Abc ...'
							
							val delegatingConstructorCall = primaryConstructor!!.body?.statements?.firstOrNull() as? IrDelegatingConstructorCall
							
							superType.print() // OtherClass
							delegatingConstructorCall?.printCallValueArguments()
//							delegatingConstructorCall?.printCallTypeArguments() // already printed in type
						}
					}
				}
				
				debug {
					if(superTypes.count { it.classOrNull?.descriptor?.kind != ClassKind.INTERFACE } > 1) {
						errorScope()
						printError(" <- multiple class inheritance")
						groupParen {
							superTypes.filter { it.classOrNull?.descriptor?.kind != ClassKind.INTERFACE }.printJoin(",") {
								it.print()
							}
						}
					}
				}
			}
			
			
			// body
			val declarationsToShow = declarations
				.filter { it != primaryConstructor && it !in constructorProperties }
				.filter { it.origin != IrDeclarationOrigin.FAKE_OVERRIDE && it.origin != IrDeclarationOrigin.DELEGATED_MEMBER && it.origin != IrDeclarationOrigin.DELEGATE }
			
			if(isEnum) bracedBlock {
				declarationsToShow.filterIsInstance<IrEnumEntry>().printJoin({
					printSeparator(",")
					print(statementGap, Type.none)
				}) { entry ->
					val call = (entry.initializerExpression as IrExpressionBody).expression as IrEnumConstructorCall
					print(entry.name, Type.enumClass)
					call.printCallTypeArguments() // ??
					if(call.valueArgumentsCount != 0)
						call.printCallValueArguments()
					
					entry.correspondingClass?.let {
						bracedBlock {
							it.declarations
								.filter { it.origin != IrDeclarationOrigin.FAKE_OVERRIDE }
								.filter { it !is IrConstructor }
								.printJoin(statementGap)
						}
					}
				}
			}
			else if(declarationsToShow.isNotEmpty()) bracedBlock {
				declarationsToShow.printJoin(declarationGap)
			}
		}
		
		debug {
			check()
			if(!declaration.isAnnotationClass) {
				// Check that all functions and properties from memberScope are present in IR
				// (including FAKE_OVERRIDE ones).
				
				val allDescriptors = declaration.descriptor.unsubstitutedMemberScope
					.getContributedDescriptors().filterIsInstance<CallableMemberDescriptor>()
				
				val presentDescriptors = declaration.declarations.map { it.descriptor }
				
				val missingDescriptors = allDescriptors - presentDescriptors
				
				if(missingDescriptors.isNotEmpty()) {
					errorScope()
					debug(" <- missing descriptors")
					groupParen {
						missingDescriptors.printJoin(",") {
							debug(it.dump())
						}
					}
				}
			}
		}
	}
	
	override fun visitEnumEntry(declaration: IrEnumEntry) {
		// ignore, already handled by visitClass
	}
	
	private fun List<IrProperty>.correspondingProperty(param: IrValueParameter) = find {
		if(it.name == param.name) {
			val init = it.backingField?.initializer?.expression as? IrGetValue
			init?.origin == IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
		} else false
	}
	
	
	/// function
	
	// fun hello|
	fun IrFunction.printFunctionHead() {
		annotations.printAnnotations(true)
		
		var isOverride = false
		var defaultVisibility = Visibilities.PUBLIC
		(this as? IrSimpleFunction)?.let {
			if(overriddenSymbols.isNotEmpty()) {
				isOverride = true
				defaultVisibility = overriddenSymbols.first().descriptor.visibility // TODO: first()?
			}
			modality.print(if(isOverride) Modality.OPEN else Modality.FINAL)
			if(isTailrec) printKeyword("tailrec")
			if(isSuspend) printKeyword("suspend")
			if(isOperator) printKeyword("operator")
		}
		visibility.print(defaultVisibility)
		
		if(isExpect) printKeyword("expect")
		if(isExternal) printKeyword("external")
		if(isInline) printKeyword("inline")
		
		if(this is IrSimpleFunction) printKeyword("fun")
		
		extensionReceiverParameter?.let {
			it.type.print()
			print(".", Type.importantSeparator)
		}
		
		val printName = when {
			this is IrConstructor -> "constructor"
			name.isSpecial -> ""
			else -> name.identifier
		}
		print(printName, Type.function)
	}
	
	fun IrFunction.printFunctionReturnType() {
		if(!returnType.isUnit()) {
			print(":", Type.separator)
			returnType.print()
		}
	}
	
	// body
	override fun visitBlockBody(body: IrBlockBody) = bracedBlock {
		val statements = body.statements
		if(statements.isNotEmpty()) {
			statements.printJoin(statementGap, Type.none)
		}
		
		debug { body.check() }
	}
	
	override fun visitExpressionBody(body: IrExpressionBody) {
		print("=", Type.separator)
		body.expression.print()
		
		debug { body.check() }
	}
	
	// functions
	
	val functionOriginsToSkip = arrayOf(IrDeclarationOrigin.FAKE_OVERRIDE, IrDeclarationOrigin.GENERATED_DATA_CLASS_MEMBER)
	
	override fun visitFunctionNew(declaration: IrFunction): Unit = declare(declaration) {
		when(this) {
			is IrSimpleFunction -> {
				if(origin in functionOriginsToSkip) return
				printFunctionHead()
				valueParameters.printValueParameters()
				printFunctionReturnType()
				body?.print()
			}
			
			is IrConstructor -> {
				// other constructors: (primary constructor: does not come here: handled in visitClass)
				// DELEGATING_CONSTRUCTOR_CALL 'constructor <init> () [primary] ...' (or calling super)
				// ... other statements
				
				// delegating_constructor_call - constructor() : super() / this()
				// other constructors: (primary constructor: does not come here: handled in visitClass)
				// DELEGATING_CONSTRUCTOR_CALL 'constructor <init> () [primary] ...' (or calling super)
				// ... other statements
				
				printFunctionHead()
				valueParameters.printValueParameters()
				
				// delegating_constructor_call - constructor() : super() / this()
				val delegation = (body as? IrBlockBody)?.statements?.get(0) as? IrDelegatingConstructorCall
				if(delegation != null) {
					printSpace()
					print(":", Type.separator)
					
					val callName =
						if(delegation.symbol == currentClass!!.scope.scopeOwnerSymbol) "this"
						else "super"
					
					print(callName, Type.lightKeyword)
					delegation.printCallValueArguments()
				}
				
				body?.print()
			}
		}
		
		debug {
			check()
			
			if(this is IrSimpleFunction) {
				val property = correspondingPropertySymbol?.ownerOrNull
				if(property != null && property.getter != this && property.setter != this) {
					printError(" <- orphaned property getter/setter")
				}
				
				checkOverrides()
			}
			
			if(dispatchReceiverParameter?.type is IrDynamicType) {
				printError(" <- dispatch receivers with 'dynamic' type are not allowed")
			}
			
			val inconsistentValueParameters = valueParameters.withIndex().filterIndexed { i, parameter -> parameter.value.index != i }
			if(inconsistentValueParameters.isNotEmpty()) {
				printError(" <- inconsistent valueParameters index ")
				inconsistentValueParameters.printJoin(",") {
					print(it.value.name, Type.valueParameter)
					groupParen {
						print(it.index, Type.identifier)
						print("=", Type.specialOperator)
						printError(it.value.index.toString())
					}
				}
			}
			
			
			val inconsistentTypeParameters = typeParameters.withIndex().filterIndexed { i, parameter -> parameter.value.index != i }
			if(inconsistentTypeParameters.isNotEmpty()) {
				printError(" <- inconsistent valueParameters index ")
				inconsistentTypeParameters.printJoin(",") {
					print(it.value.name, Type.valueParameter)
					groupParen {
						print(it.index, Type.identifier)
						print("=", Type.specialOperator)
						printError(it.value.index.toString())
					}
				}
			}
		}
	}
	
	// (in class) init { ... }
	override fun visitAnonymousInitializerNew(declaration: IrAnonymousInitializer) =
		declare(declaration) {
			print("init", Type.lightKeyword)
			body.print()
			
			debug { check() }
		}
	
	
	/// typealias
	
	override fun visitTypeAlias(declaration: IrTypeAlias) = declare(declaration) {
		visibility.print()
		
		if(isActual) printKeyword("actual")
		printKeyword("typealias")
		
		print(name, Type.typeAlias)
		typeParameters.print()
		printSeparator("=")
		expandedType.print()
		
		debug { declaration.check() }
	}
	
	
	/// member property
	
	override fun visitPropertyNew(declaration: IrProperty) = declare(declaration) {
		val isOverride = descriptor.overriddenDescriptors.isNotEmpty()
		
		val backingField = backingField
		val definedGetter = getter?.takeUnless { it.origin == IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR }
		val definedSetter = setter?.takeUnless { it.origin == IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR }
		
		annotations.printAnnotations(true)
		
		visibility.print()
		modality.print()
		
		if(isOverride) printKeyword("override")
		if(isExpect) printKeyword("expect")
		if(isExternal) printKeyword("external")
		if(isLateinit) printKeyword("lateinit")
		
		printKeyword(if(isVar) "var" else "val")
		
		// in case of extension property, structure is like:
		//  PROPERTY name:a visibility:public modality:FINAL [val]
		//    FUN name:<get-a> visibility:public modality:FINAL <> ($receiver:<unbound IrClassPublicSymbolImpl>) returnType:<unbound IrClassPublicSymbolImpl>
		//      correspondingProperty: PROPERTY name:a visibility:public modality:FINAL [val]
		// >>   $receiver: VALUE_PARAMETER name:<this> type:<unbound IrClassPublicSymbolImpl> <- this
		//      BLOCK_BODY
		getter?.extensionReceiverParameter?.type?.print()
		
		print(name, Type.property)
		printSeparator(":")
		descriptor.type.toIrType().print()
		
		// 2. body
		
		
		// 2-1. no backing field
		// property
		//  |- getter
		// (|- setter)
		// getter & setter are printed below
		
		if(backingField != null) {
			if(isDelegated) {
				// 2-2. delegated property
				// property
				// |- isDelegated = true
				// |- backingField = origin:DELEGATED_PROPERTY ...
				
				print("by", Type.lightKeyword)
				backingField.initializer!!.expression.print()
			} else if(isLateinit || isExpect) {
				// 2-3. lateinit / expect
				// property
				//  |- isLateinit / isExpect = true
				
				// do nothing
			} else {
				// 2-4. normal backing field
				// property
				// |- backingField = origin:BACKING_FIELD ...
				// (|- getter)
				// (|- setter)
				
				print("=", Type.specialOperator)
				backingField.initializer!!.expression.print()
			}
		}
		
		if(!isDelegated) {
			if(definedSetter != null) println() // val prop: Type get() = ...\n set() = ... seems a little bit weird?
			
			if(definedGetter != null || definedSetter != null) indented {
				if(definedGetter != null) {
					print("get", Type.lightKeyword)
					definedGetter.valueParameters.printValueParameters() // having valueParameters is impossible in frontend, but possible in IR
					definedGetter.body?.print()
				}
				
				if(definedSetter != null) {
					print("set", Type.lightKeyword)
					groupParen {
						if(definedSetter.valueParameters.size == 1)
							print(definedSetter.valueParameters.single().name, Type.valueParameter)
						else definedSetter.valueParameters.printValueParameters() // having more valueParameters is impossible in frontend, but possible in IR
					}
					definedSetter.body?.print()
				}
			}
		}
		
		debug { check() }
	}
	
	
	/// other
	
	private fun List<IrValueParameter>.printValueParameters() = groupParen {
		printJoin(",")
	}
	
	override fun visitValueParameterNew(declaration: IrValueParameter): Unit = with(declaration) {
		annotations.printAnnotations(false)
		
		val isVararg = isVararg
		if(isVararg) printKeyword("vararg")
		if(isCrossinline) printKeyword("crossinline")
		if(isNoinline) printKeyword("noinline")
		
		print(name, Type.valueParameter)
		printSeparator(":")
		printSpace()
		
		type.print()
		
		defaultValue?.print() // defaultValue is ExpressionBody, `= ...`
		
		debug { check() }
	}
	
	
	override fun visitTypeParameter(declaration: IrTypeParameter) {
		print(declaration.name, Type.typeParameter)
		
		val isNonEmpty = declaration.superTypes.isNotEmpty() &&
			!declaration.superTypes[0].isNullableAny()
		
		if(isNonEmpty) {
			printSeparator(":")
			declaration.superTypes.printJoin(",") { it.print() }
		}
		
		debug { declaration.check() }
	}
	
	
	override fun visitScript(declaration: IrScript) {
		unknown("<<SCRIPT>>")
		super.visitScript(declaration)
		
		debug { declaration.check() }
	}
	
	
	///// local declarations
	
	override fun visitVariable(declaration: IrVariable) = declare(declaration) {
		if(isLateinit) printKeyword("lateinit")
		if(isConst) printKeyword("const")
		
		printKeyword(if(isVar) "var" else "val")
		
		if(origin == IrDeclarationOrigin.IR_TEMPORARY_VARIABLE) stylePrefix(Ansi.italic) { print(name, Type.localVariable) }
		else print(name, Type.localVariable)
		
		val initializer = initializer
		val isTypeSpecifyNeeded = when { // explicit is good for debug
			initializer == null -> true
			initializer is IrConstructorCall -> false // val myClass: MyClass <- indeed unnecessary = MyClass()
			initializer is IrConst<*> &&
				initializer.kind.let { it == IrConstKind.Int || it == IrConstKind.String || it == IrConstKind.Float || it == IrConstKind.Double || it == IrConstKind.Boolean } -> false
			else -> true
		}
		
		if(isTypeSpecifyNeeded) {
			printSeparator(":")
			type.print()
		}
		
		if(initializer != null) {
			print("=", Type.specialOperator)
			initializer.print()
		}
		
		debug { check() }
	}
	
	override fun visitLocalDelegatedProperty(
		declaration: IrLocalDelegatedProperty
	) = with(declaration) {
		printKeyword(if(isVar) "var" else "val")
		
		printIdentifier(name)
		
		printSeparator(":")
		type.print()
		
		print("by", Type.lightKeyword)
		delegate.initializer!!.print()
		
		debug { check() }
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	///// statements / expressions
	
	
	fun IrExpression.printAsReceiver() {
		if(isThis) return
		print()
		printSeparator(".")
	}
	
	/// call
	
	fun getOperatorFromName(name: String): String {
		val isInNotCall = currentInherit.isNotCall
		return when(name) {
			"contains" -> "in"
			"equals" -> if(isInNotCall) "!=" else "=="
			"plus" -> "+"
			"not" -> "!"
			"minus" -> "-"
			"times" -> "*"
			"div" -> "/"
			"rem" -> "%"
			"rangeTo" -> ".."
			"plusAssign" -> "+="
			"minusAssign" -> "-="
			"timesAssign" -> "*="
			"divAssign" -> "/="
			"remAssign" -> "%="
			"inc" -> "++"
			"dec" -> "--"
			"greater" -> ">"
			"less" -> "<"
			"lessOrEqual" -> "<="
			"greaterOrEqual" -> ">="
			"EQEQ" -> if(isInNotCall) "!=" else "=="
			"EQEQEQ" -> if(isInNotCall) "!==" else "==="
			"ANDAND" -> "&&"
			"OROR" -> "||"
			// no names for
			"invoke", "get", "set" -> ""
			"iterator", "hasNext", "next" -> name
			else -> "[operator $name]"
		}
	}
	
	override fun visitCall(expression: IrCall) = expr(expression) {
		// in case of builtin operators
		val descriptor = symbol.descriptor
		val isInfix = descriptor.isInfix
		val receiver = anyReceiver
		val name = descriptor.name.asString()
		
		if(descriptor.isOperator || descriptor is IrSimpleBuiltinOperatorDescriptorImpl) {
			if(name == "not") {
				// `a !== b` looks like `not(equals(a, b))`
				val arg = receiver!!
				if(arg is IrCall) {
					val fn = arg.symbol.owner
					if(fn.descriptor is IrSimpleBuiltinOperatorDescriptorImpl) {
						when(fn.name.asString()) {
							"equals", "EQEQ", "EQEQEQ" -> provideInherit(GroupingInherit(isNotCall = true)) {
								arg.print()
								return
							}
						}
					}
				}
			}
			
			val operatorName = getOperatorFromName(name)
			
			when(name) {
				// unary prefix
				"unaryPlus", "unaryMinus", "not" -> {
					print(operatorName, Type.specialOperator)
					receiver?.print()
				}
				// unary postfix
				"inc", "dec" -> {
					receiver?.print()
					print(operatorName, Type.specialOperator)
				}
				// invoke
				"invoke" -> {
					receiver?.print()
					printCallValueArguments()
				}
				// get indexer
				"get" -> {
					receiver?.print()
					printCallValueArguments("[", "]", trailingLambdaCapable = false)
				}
				// set indexer
				"set" -> {
					receiver?.print()
					grouping("[", "]") {
						printCallValueArguments(descriptor, valueArguments.dropLast(1))
					}
					print("=", Type.specialOperator)
					valueArguments.last()!!.print()
				}
				// builtin static operators
				"greater", "less", "lessOrEqual", "greaterOrEqual", "EQEQ", "EQEQEQ", "ANDAND", "OROR" -> {
					getValueArgument(0)?.print()
					print(operatorName, Type.specialOperator)
					getValueArgument(1)?.print()
				}
				"iterator", "hasNext", "next", "compareTo" -> {
					printCallPrefix()
					print(operatorName, Type.function)
					printCallValueArguments()
				}
				else -> {
					// component n
					if(name.startsWith("component")) {
						receiver?.printAsReceiver()
						print(operatorName, Type.function)
						groupParen {}
					} else {
						// else binary
						receiver?.print()
						print(operatorName, Type.specialOperator)
						valueArguments.printJoin(",") { it?.print() }
					}
				}
			}
			return
		}
		
		if(isInfix) {
			val intAsBinary = when(name) {
				"and", "or", "xor" -> true
				else -> false
			}
			
			grouping {
				if(intAsBinary && receiver is IrConst<*>)
					mergeInherit(GroupingInherit(printIntAsBinary = true))
				receiver!!.print()
			}
			printSpace() // ensure spaces around infix function
			print(name, Type.function)
			printCallTypeArguments()
			printSpace()
			grouping {
				val argument = getValueArgument(0)!!
				if(intAsBinary && argument is IrConst<*>)
					mergeInherit(GroupingInherit(printIntAsBinary = true))
				argument.print()
			}
			return
		}
		
		printCallPrefix()
		val prop = (symbol.ownerOrNull as? IrSimpleFunction)?.correspondingPropertySymbol?.ownerOrNull
		
		when {
			prop != null -> {
				val propName = prop.name.asString()
				print(propName, Type.property)
				if(symbol.ownerOrNull == prop.setter) {
					print("=", Type.specialOperator)
					getValueArgument(0)?.print()
				}
			}
			
			origin == IrStatementOrigin.EXCLEXCL -> {
				// expr!! -> irCall(-> kotlin.internal.ir/CHECK_NOT_NULL, valueArguments = [expr], origin = EXCLEXCL)
				valueArguments.single()!!.print()
				print("!!", Type.specialOperator)
			}
			
			origin == IrStatementOrigin.GET_PROPERTY -> {
				// here, some bridge: java getValue()
				val propName = descriptor.name.asString()
				val realPropNamePascal = when {
					propName.startsWith("get") -> propName.drop(3)
					propName.startsWith("is") -> propName.drop(2)
					else -> {
						print(descriptor.name, Type.function)
						printCallTypeArguments()
						printCallValueArguments()
						printError("<- GET_PROPERTY without appropriate name: $propName")
						return
					}
				}
				print(realPropNamePascal.decapitalizeFirst(), Type.property)
			}
			origin == IrStatementOrigin.EQ -> {
				// here, some bridge: java setValue(...)
				val propName = descriptor.name.asString()
				require(propName.startsWith("set"))
				print(propName.drop(3).decapitalizeFirst(), Type.property)
				print("=", Type.specialOperator)
				valueArguments.single()!!.print()
			}
			else -> {
				print(descriptor.name, Type.function)
				printCallTypeArguments()
				printCallValueArguments()
			}
		}
		
		debug {
			check()
			
			if((extensionReceiver == null) != (descriptor.extensionReceiverParameter == null))
				printError(" <- extensionReceiver " + if(extensionReceiver == null) "missing" else "unexpected")
			if((dispatchReceiver == null) != (descriptor.dispatchReceiverParameter == null))
				printError(" <- dispatchReceiver " + if(dispatchReceiver == null) "missing" else "unexpected")
			
			if(symbol.tryBind().isBound) {
				val function = symbol.owner
				
				val returnType = function.returnType
				if(returnType is IrSimpleType &&
					returnType.classifier is IrClassSymbol &&
					returnType.arguments.isEmpty()
				) {
					ensureTypeIs(returnType)
				}
			}
			
			superQualifierSymbol?.ensureBound(this)
		}
	}
	
	override fun visitConstructorCall(expression: IrConstructorCall) = expr(expression) {
//		printCallPrefix()
		print(symbol.descriptor.constructedClass.name, Type.classClass)
		printCallTypeArguments()
		if(currentInherit.isAnnotationCall && valueArgumentsCount == 0) // TODO: @A(B()) will shown: @A(B)
			return
		printCallValueArguments()
		
		debug { check() }
	}
	
	override fun visitEnumConstructorCall(expression: IrEnumConstructorCall) {
		// ignore; handled by visitClass
	}
	
	override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall) {
		// ignore
		
		debug {
			expression.ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	override fun visitInstanceInitializerCall(expression: IrInstanceInitializerCall) {
		// ignore
		
		debug {
			expression.ensureTypeIs(irBuiltIns.unitType)
			expression.classSymbol.ensureBound(expression)
		}
	}
	
	private fun IrConstructorCall.printCallAnnotation() {
		grouping(inherit = GroupingInherit(isAnnotationCall = true)) {
			print()
		}
	}
	
	fun IrFunctionAccessExpression.printCallPrefix() {
		val dispatchReceiver = dispatchReceiver
		val extensionReceiver = extensionReceiver
		val dispatchIsSpecial = dispatchReceiver.let {
			it is IrGetValue && it.symbol.descriptor.name.isSpecial
		}
		val extensionIsSpecial = extensionReceiver.let {
			it is IrGetValue && it.symbol.descriptor.name.isSpecial
		}
		
		when {
			dispatchReceiver != null && !dispatchIsSpecial -> dispatchReceiver.printAsReceiver()
			extensionReceiver != null && !extensionIsSpecial -> extensionReceiver.printAsReceiver()
		}
	}
	
	fun IrFunctionAccessExpression.printCallTypeArguments() {
		val needToExplicitTypeParameters = typeArguments.any { type ->
			// not actually, but just a tiny stub?
			type != null && symbol.descriptor.valueParameters.none { it.type == type.toKotlinType() }
		}
		
		if(needToExplicitTypeParameters) grouping("<", ">") {
			typeArguments.printJoin(",") { (it ?: TODO("hOI!!")).print() }
		}
	}
	
	fun IrFunctionAccessExpression.printCallValueArguments(prefix: CharSequence = "(", postfix: CharSequence = ")", trailingLambdaCapable: Boolean = true) {
		printCallValueArguments(symbol.descriptor, valueArguments, prefix, postfix, trailingLambdaCapable)
	}
	
	fun printCallValueArguments(
		descriptor: FunctionDescriptor, valueArguments: List<IrExpression?>,
		prefix: CharSequence = "(", postfix: CharSequence = ")",
		trailingLambdaCapable: Boolean = true
	) {
		val trailingLambda = valueArguments.lastOrNull()?.takeIf {
			trailingLambdaCapable && (
				it is IrFunctionExpression ||
					(it is IrBlock && it.origin == IrStatementOrigin.LAMBDA)
				)
		}
		
		val arguments = if(trailingLambda != null) valueArguments.dropLast(1) else valueArguments
		
		if(arguments.isNotEmpty() || trailingLambda == null) grouping(prefix, postfix) {
			var useParameterName = false
			arguments.withIndex()
				.filter { argument -> (argument.value != null).also { if(!it) useParameterName = true } }
				.printJoin(",") {
					val parameter = descriptor.valueParameters[it.index]
					val argument = it.value!!
					
					if(useParameterName) {
						parameter.name.print(Type.valueParameter)
						print("=", Type.valueParameter)
					}
					argument.print()
					debug {
						// TODO: support generics
						val argumentType = argument.type
						val parameterType = parameter.type
						if(argumentType.classOrNull != null &&
							parameterType.constructor.declarationDescriptor is ClassDescriptor &&
							!argumentType.toKotlinType().replaceArgumentsWithStarProjections().isSubtypeOf(parameterType.replaceArgumentsWithStarProjections())) {
							errorScope()
							debug("<- type mismatch (${parameter.type} = ${argument.type.toKotlinType()})")
						}
					}
				}
		}
		
		debug {
			if(descriptor.valueParameters.size != valueArguments.size) {
				errorScope()
				none(" <- valueParameters not provided:")
				print(descriptor.dump())
				none(" << ")
				none(valueArguments.size.toString())
			} else {
				val notProvided = descriptor.valueParameters.filterIndexed { i, parameter ->
					!parameter.declaresDefaultValue() && valueArguments[i] == null
				}
				
				if(notProvided.isNotEmpty()) {
					errorScope()
					none("<- no argument: ")
					
					groupParen {
						notProvided.printJoin(",") {
							print(it.name, Type.valueParameter)
						}
					}
				}
			}
		}
		
		trailingLambda?.print()
	}
	
	
	/// const
	override fun <T> visitConst(expression: IrConst<T>) {
		val type = when(expression.kind) {
			IrConstKind.Null, IrConstKind.Boolean, IrConstKind.Char -> Type.otherLiteral
			IrConstKind.Byte, IrConstKind.Short, IrConstKind.Int, IrConstKind.Long, IrConstKind.Float, IrConstKind.Double -> Type.numberLiteral
			IrConstKind.String -> Type.stringLiteral
		}
		
		val result = when(expression.kind) {
			is IrConstKind.Null -> "${expression.value}"
			is IrConstKind.Boolean -> "${expression.value}"
			is IrConstKind.Char -> "'${expression.value}'"
			is IrConstKind.Byte -> "${expression.value}"
			is IrConstKind.Short -> "${expression.value}"
			is IrConstKind.Int -> {
				if(currentInherit.printIntAsBinary) {
					intAsBinaryString(expression.value as Int)
				} else {
					"${expression.value}"
				}
			}
			is IrConstKind.Long -> "${expression.value}L"
			is IrConstKind.Float -> "${expression.value}f"
			is IrConstKind.Double -> "${expression.value}"
			is IrConstKind.String -> "\"${expression.value}\"" // TODO
		}
		print(result, type)
		
		debug {
			expression.check()
			
			val naturalType = when(expression.kind) {
				IrConstKind.Null -> {
					expression.ensureNullable()
					return
				}
				IrConstKind.Boolean -> irBuiltIns.booleanType
				IrConstKind.Char -> irBuiltIns.charType
				IrConstKind.Byte -> irBuiltIns.byteType
				IrConstKind.Short -> irBuiltIns.shortType
				IrConstKind.Int -> irBuiltIns.intType
				IrConstKind.Long -> irBuiltIns.longType
				IrConstKind.String -> irBuiltIns.stringType
				IrConstKind.Float -> irBuiltIns.floatType
				IrConstKind.Double -> irBuiltIns.doubleType
			}
			
			var irType = expression.type
			while(true) {
				val inlinedClass = irType.getInlinedClass() ?: break
				irType = getInlineClassUnderlyingType(inlinedClass)
			}
			ensureTypesEqual(irType, naturalType)
		}
	}
	
	private fun intAsBinaryString(value: Int): String {
		if(value == 0) return "0"
		var current = value
		var result = ""
		var length = 0
		while(current != 0 || length % 4 != 0) {
			if(length % 4 == 0 && length != 0) result = "_$result"
			val nextBit = current and 1 != 0
			current = current ushr 1
			result = "${if(nextBit) "1" else "0"}$result"
			length++
		}
		return "0b$result"
	}
	
	/// reference
	
	override fun visitClassReference(expression: IrClassReference) {
		expression.symbol.print()
		print("::", Type.importantSeparator)
		printKeyword("class")
		
		debug { expression.check() }
	}
	
	override fun visitPropertyReference(expression: IrPropertyReference): Unit = expr(expression) {
		dispatchReceiver?.print()
		print("::", Type.importantSeparator)
		print(symbol.descriptor.name, Type.property)
		debug { expression.check() }
	}
	
	override fun visitLocalDelegatedPropertyReference(expression: IrLocalDelegatedPropertyReference): Unit =
		expr(expression) {
			dispatchReceiver?.print()
			print("::", Type.importantSeparator)
			print(symbol.descriptor.name, Type.localVariable)
			debug { expression.check() }
		}
	
	override fun visitFunctionReference(expression: IrFunctionReference): Unit = expr(expression) {
		dispatchReceiver?.print()
		print("::", Type.importantSeparator)
		print(symbol.descriptor.name, Type.function)
		debug { expression.check() }
	}
	
	
	/// get / set
	
	// class
	override fun visitGetClass(expression: IrGetClass) = expr(expression) {
		argument.print()
		print("::", Type.specialOperator)
		printKeyword("class")
		debug { expression.check() }
	}
	
	// field
	override fun visitGetField(expression: IrGetField) = expr(expression) {
		val receiver = receiver
		val superQualifiedSymbol = superQualifierSymbol
		
		when {
			receiver != null -> receiver.printAsReceiver()
			
			superQualifiedSymbol != null -> { // TODO
				superQualifiedSymbol.print()
				printSeparator('.')
			}
		}
		
		print(symbol.descriptor.name, Type.property)
		
		debug {
			expression.check()
			val fieldType = expression.symbol.owner.type
			if(fieldType is IrSimpleType &&
				fieldType.classifier is IrClassSymbol &&
				fieldType.arguments.isEmpty()
			) {
				expression.ensureTypeIs(fieldType)
			}
		}
	}
	
	override fun visitSetField(expression: IrSetField) = expr(expression) {
		receiver?.printAsReceiver()
		print(symbol.descriptor.name, Type.property)
		print("=", Type.specialOperator)
		value.print()
		
		debug {
			check()
			ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	
	override fun visitGetEnumValue(expression: IrGetEnumValue) {
		print(expression.symbol.descriptor.name, Type.enumClass) // TODO
		debug { expression.check() }
	}
	
	override fun visitGetObjectValue(expression: IrGetObjectValue) {
		print(expression.symbol.descriptor.name, Type.objectClass)
		
		debug {
			expression.check()
			expression.ensureTypeIs(expression.symbol.createType(false, emptyList()))
		}
	}
	
	val IrExpression.isThis get() = this is IrGetValue && symbol.descriptor.name.asString() == "<this>"
	
	val IrValueSymbol.type
		get() = when(this) {
			is IrValueParameterSymbol -> Type.valueParameter
			else -> Type.localVariable
		}
	
	override fun visitGetValue(expression: IrGetValue) = expr(expression) {
		if(isThis) {
			// in case of this.~~~, should be handled by other functions like visitCall etc: see IrExpression.printAsReceiver()
			
			printKeyword("this")
			
			// this@<outer scope name?>
			val parent = expression.symbol.tryBind().owner.parent
			if(parent is IrFunction && parent != currentFunction) {
				// in case of lambda argument, its name is the name of the callee function:
				// a.run { b.apply { this@run } }
				if(parent.origin == IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA) {
					val psi = parent.descriptor.findPsi()
					val name = ((((psi?.parent?.parent as? KtLambdaArgument)?.parent) as? KtCallExpression)?.calleeExpression as? KtNameReferenceExpression)?.getReferencedName()
					name?.let {
						compact()
						print("@", Type.label)
						it.print(Type.label)
						return
					}
				}
				
				((symbol.descriptor.containingDeclaration as? FunctionDescriptor)?.name)
					?.takeIf { !it.isSpecial }
					?.let {
						compact()
						print("@", Type.label)
						print(it.identifier, Type.label)
						return
					}
			}
			return
		}
		
		
		grouping {
			// in case of closure
			val parent = symbol.tryBind().owner.parent
			val currentFunction = currentFunction?.irElement
			if(parent != null && currentFunction != null && parent != currentFunction)
				stylePrefix(Ansi.italic)
			
			print(symbol.descriptor.name, symbol.type)
		}
		
		debug {
			check()
			ensureTypeIs(symbol.run { if(isBound) owner.type else descriptor.type.toIrType() })
		}
	}
	
	override fun visitSetVariable(expression: IrSetVariable) = expr(expression) {
		print(symbol.descriptor.name, symbol.type)
		print("=", Type.specialOperator)
		value.print()
		
		debug {
			check()
			ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	
	/// string concatenation
	override fun visitStringConcatenation(expression: IrStringConcatenation) = expr(expression) {
		print("\"", Type.stringLiteral)
		arguments.forEach { item ->
			compact()
			when {
				item is IrConst<*> && item.kind == IrConstKind.String -> print(item.value.toString(), Type.stringLiteral)
				item is IrGetValue -> {
					print("\$", Type.specialOperator)
					grouping {
						item.print()
					}
				}
				else -> grouping("\${", "}") {
					item.print()
				}
				
			}
		}
		compact()
		print("\"", Type.stringLiteral)
		
		debug {
			check()
			ensureTypeIs(irBuiltIns.stringType)
		}
	}
	
	
	/// type operator
	override fun visitTypeOperator(expression: IrTypeOperatorCall) = expr(expression) {
		when(operator) {
			IrTypeOperator.IMPLICIT_COERCION_TO_UNIT, IrTypeOperator.IMPLICIT_CAST,
			IrTypeOperator.IMPLICIT_NOTNULL, IrTypeOperator.IMPLICIT_INTEGER_COERCION
			-> argument.print()
			IrTypeOperator.CAST -> {
				argument.print()
				printKeyword("as")
				typeOperand.print()
			}
			IrTypeOperator.SAFE_CAST -> {
				argument.print()
				printKeyword("as")
				print("?", Type.specialOperator)
				typeOperand.print()
			}
			IrTypeOperator.INSTANCEOF -> {
				argument.print()
				printKeyword("is")
				typeOperand.print()
			}
			IrTypeOperator.NOT_INSTANCEOF -> {
				argument.print()
				print("!", Type.specialOperator)
				printKeyword("is")
				typeOperand.print()
			}
			IrTypeOperator.SAM_CONVERSION -> argument.print()
			IrTypeOperator.IMPLICIT_DYNAMIC_CAST -> argument.print()
			
			else -> groupExpr {
				unknown("[$operator]")
				argument.print()
				unknown(",")
				typeOperand.print()
			}
		}
		
		debug {
			check()
			
			val naturalType = when(operator) {
				IrTypeOperator.CAST,
				IrTypeOperator.IMPLICIT_CAST,
				IrTypeOperator.IMPLICIT_NOTNULL,
				IrTypeOperator.IMPLICIT_COERCION_TO_UNIT,
				IrTypeOperator.IMPLICIT_INTEGER_COERCION,
				IrTypeOperator.SAM_CONVERSION,
				IrTypeOperator.IMPLICIT_DYNAMIC_CAST,
				IrTypeOperator.REINTERPRET_CAST,
				-> typeOperand
				
				IrTypeOperator.SAFE_CAST ->
					typeOperand.makeNullable()
				
				IrTypeOperator.INSTANCEOF, IrTypeOperator.NOT_INSTANCEOF ->
					irBuiltIns.booleanType
			}
			
			if(operator == IrTypeOperator.IMPLICIT_COERCION_TO_UNIT && !typeOperand.isUnit()) {
				errorScope()
				debug("<- implicitCoercionToUnit but typeOperand is not Unit")
				
			}
			
			ensureTypeIs(naturalType)
		}
	}
	
	
	/// function related
	
	
	override fun visitReturn(expression: IrReturn): Unit = expr(expression) {
		val value = value
		
		// only print the return statement directly if it is not a lambda
		val label = labels[returnTargetSymbol]
		if(returnTarget.name.asString() != "<anonymous>" || label != null) {
			printKeyword("return")
			if(label != null) {
				print("@", Type.label)
				print(label, Type.label)
			}
		}
		if(type.isUnit() || value.type.isUnit()) {
			if(value is IrGetObjectValue) return else value.print()
		} else {
			value.print()
		}
		
		debug {
			check()
			ensureTypeIs(irBuiltIns.nothingType)
			returnTargetSymbol.ensureBound(this)
		}
	}
	
	override fun visitFunctionExpression(
		expression: IrFunctionExpression
	): Unit = expr(expression) {
		when(origin) {
			IrStatementOrigin.LAMBDA -> function.printAsLambda()
			IrStatementOrigin.ANONYMOUS_FUNCTION -> {
				function.printFunctionHead()
				function.valueParameters.printValueParameters()
				function.printFunctionReturnType()
				function.body?.print()
			}
			else -> TODO()
		}
		
		debug {
			check()
			
			// whether type and function content has the same content
			val type = type
			if(type is IrSimpleType) {
				val paramsType = type.arguments.dropLast(1)
				val returnType = type.arguments.last().typeOrNull
				if(function.dispatchReceiverParameter != null) printError(" <- dispatchReceiverParameter != null")
				val parameters = listOfNotNull(function.extensionReceiverParameter) + function.valueParameters
				var valueParamsMatches = true
				if(paramsType.size != parameters.size) printError(" <- parameters count not match for IrFunctionExpression")
				else parameters.forEachIndexed { index, param ->
					if(param.type != paramsType.getOrNull(index)) valueParamsMatches = false
				}
				
				if(!valueParamsMatches) {
					printError(" <- [ir] valueParameters of the function does not match with the type")
					groupParen {
						type.print()
					}
				}
				
				if(function.returnType != returnType) {
					printError(" <- [ir] returnType of the function does not match with the type")
					groupParen {
						if(returnType == null) {
							debug("type projection ")
							type.arguments.last().printTypeArgument()
						} else returnType.print()
					}
				}
			}
		}
	}
	
	override fun visitVararg(expression: IrVararg) {
		if(currentInherit.isAnnotationCall) grouping("[", "]") {
			expression.elements.printJoin(",")
		}
		else grouping {
			expression.elements.printJoin(",")
		}
		
		debug { expression.check() }
	}
	
	override fun visitSpreadElement(spread: IrSpreadElement) {
		print("*", Type.lightKeyword)
		spread.expression.print()
		debug { spread.check() }
	}
	
	override fun visitSuspendableExpression(expression: IrSuspendableExpression) {
		TODO()
	}
	
	override fun visitSuspensionPoint(expression: IrSuspensionPoint) {
		TODO()
	}
	
	/// branch / control flows
	
	
	val IrExpression.braceNeeded get() = this is IrStatementContainer
	
	// if, else if, else / when / || / &&
	override fun visitWhen(expression: IrWhen) = expr(expression) {
		val isIf = origin == IrStatementOrigin.IF || this is IrIfThenElseImpl
		when {
			origin == IrStatementOrigin.OROR -> {
				val lhs = branches[0].condition
				val rhs = branches[1].result
				lhs.print()
				print("||", Type.specialOperator)
				rhs.print()
			}
			origin == IrStatementOrigin.ANDAND -> {
				val lhs = branches[0].condition
				val rhs = branches[0].result
				lhs.print()
				print("&&", Type.specialOperator)
				rhs.print()
			}
			isIf -> {
				val singleLine = branches.all { !it.result.braceNeeded }
				
				branches.forEachIndexed { index, branch ->
					val isElse = index == branches.size - 1 &&
						(branch.condition as? IrConst<*>)?.value == true
					when {
						index == 0 -> {
							printKeyword("if")
							groupParen {
								branch.condition.print()
							}
						}
						isElse ->
							printKeyword("else")
						
						else -> {
							printKeyword("else if")
							groupParen {
								branch.condition.print()
							}
						}
					}
					
					if(singleLine)
						branch.result.print()
					else bracedBlock {
						branch.result.print()
					}
				}
			}
			else -> {
				// stay updated with IrStatementOrigin.WHEN branch of visitBlock
				
				printKeyword("when")
				bracedBlock {
					branches.printJoin(statementGap) { branch ->
						val condition = branch.condition
						val result = branch.result
						val isElse = (condition as? IrConst<*>)?.value == true
						
						when {
							isElse -> {
								if(
									((result as? IrCall)?.symbol?.descriptor as? IrSimpleBuiltinOperatorDescriptorImpl)
										?.name?.toString() == "noWhenBranchMatchedException"
								) return@printJoin
								printKeyword("else")
							}
							condition is IrWhen && condition.origin == IrStatementOrigin.WHEN_COMMA -> {
								// condA, condB, ... ->
								// this syntax is deprecated: need to use || in the when without argument
								unknown("[deprecated syntax: Use '||' instead of commas in when-condition for 'when' without argument] ${condition.dump()}")
							}
							else -> condition.print()
						}
						
						printSeparator("->")
						if(result.braceNeeded) bracedBlock {
							result.print()
						} else result.print()
					}
				}
			}
		}
		
		debug {
			check()
			// in case of enum / sealed class, else branch operator(noWhenBranchMatchedException) is inserted
			if(!type.isUnit() && branches.none { it.isElse }) {
				printError(" <- no else branch")
			}
		}
	}

//	override fun visitElseBranch(branch: IrElseBranch) {
//		super.visitElseBranch(branch)
//	}
//
//	override fun visitBranch(branch: IrBranch) {
//		super.visitBranch(branch)
//	}
	
	// loops
	override fun visitBreak(jump: IrBreak) {
		printKeyword("break")
		if(jump.label != null) {
			print("@", Type.label)
			print(jump.label, Type.label)
		}
		
		debug {
			jump.check()
			jump.ensureTypeIs(irBuiltIns.nothingType)
		}
	}
	
	override fun visitContinue(jump: IrContinue) {
		printKeyword("continue")
		if(jump.label != null) {
			print("@", Type.label)
			print(jump.label, Type.label)
		}
		
		debug {
			jump.check()
			jump.ensureTypeIs(irBuiltIns.nothingType)
		}
	}
	
	override fun visitWhileLoop(loop: IrWhileLoop) {
		if(loop.label != null) {
			print(loop.label, Type.label)
			print("@", Type.label)
		}
		printKeyword("while")
		groupParen {
			loop.condition.print()
		}
		bracedBlock {
			loop.body?.print()
		}
		
		debug {
			loop.check()
			loop.ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	override fun visitDoWhileLoop(loop: IrDoWhileLoop) {
		printKeyword("do")
		
		bracedBlock {
			loop.body?.print()
		}
		printKeyword("while")
		groupParen {
			loop.condition.print()
		}
		
		debug {
			loop.check()
			loop.ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	/// try/catch
	
	override fun visitTry(aTry: IrTry) {
		printKeyword("try")
		bracedBlock {
			aTry.tryResult.print()
		}
		if(aTry.catches.isNotEmpty()) {
			aTry.catches.printJoin("")
		}
		aTry.finallyExpression?.let {
			printKeyword("finally")
			bracedBlock {
				it.print()
			}
		}
		
		debug {
			aTry.check()
			if(aTry.catches.isEmpty() && aTry.finallyExpression == null)
				printError(" <- unnecessary try")
		}
	}
	
	override fun visitCatch(aCatch: IrCatch) = with(aCatch) {
		printKeyword("catch")
		groupParen { parameter.name.print(Type.valueParameter) }
		
		bracedBlock {
			result.print()
		}
		
		debug { aCatch.check() }
	}
	
	override fun visitThrow(expression: IrThrow) {
		printKeyword("throw")
		expression.value.print()
		
		debug {
			expression.check()
			expression.ensureTypeIs(irBuiltIns.nothingType)
		}
	}
	
	/// others
	
	val IrBranch.isElse get() = this is IrElseBranch || condition.let { it is IrConst<*> && it.value == true }
	
	val IrMemberAccessExpression.anyReceiver get() = dispatchReceiver ?: extensionReceiver
	
	// containers
	override fun visitBlock(expression: IrBlock): Unit = expr(expression) {
		try {
			when(origin) {
				IrStatementOrigin.POSTFIX_INCR -> {
					val tmpVar = statements[0] as IrVariable
					val lhs = tmpVar.initializer ?: error("Expected initializer")
					lhs.print()
					print("++", Type.specialOperator)
				}
				IrStatementOrigin.POSTFIX_DECR -> {
					val tmpVar = statements[0] as IrVariable
					val lhs = tmpVar.initializer ?: error("Expected initializer")
					lhs.print()
					print("--", Type.specialOperator)
				}
				
				// this makes me have a headache(?)
//				IrStatementOrigin.PLUSEQ, IrStatementOrigin.MINUSEQ, IrStatementOrigin.DIVEQ,
//				IrStatementOrigin.MULTEQ, IrStatementOrigin.PERCEQ
//				-> {
//
//					// block type=kotlin.Unit origin=PLUSEQ
//					//  var IR_TEMPORARY_VARIABLE name:tmp0_this type:com.lhwdev.ktui.DynamicAmbientValue<T of com.lhwdev.ktui.DynamicAmbientValue> [val]
//					//    get_var '-> <this>: com.lhwdev.ktui.DynamicAmbientValue<T of com.lhwdev.ktui.DynamicAmbientValue> declared in com.lhwdev.ktui.DynamicAmbientValue.getValue' type=com.lhwdev.ktui.DynamicAmbientValue<T of com.lhwdev.ktui.DynamicAmbientValue> origin=null
//					//  call '-> public final fun plusAssign <T> (element: T of kotlin.collections.CollectionsKt.plusAssign): kotlin.Unit [inline,operator] declared in kotlin.collections.CollectionsKt' type=kotlin.Unit origin=PLUSEQ
//					//    <T>: com.lhwdev.ktui.Element<*>
//					//    $receiver: call '-> private final fun <get-dependents> (): kotlin.collections.MutableSet<com.lhwdev.ktui.Element<*>> declared in com.lhwdev.ktui.DynamicAmbientValue' type=kotlin.collections. MutableSet<com.lhwdev.ktui.Element<*>> origin=PLUSEQ
//					//      $this: get_var '-> val tmp0_this: com.lhwdev.ktui.DynamicAmbientValue<T of com. lhwdev.ktui.DynamicAmbientValue> [val] declared in com.lhwdev.ktui.DynamicAmbientValue.getValue' type=com.lhwdev.ktui.DynamicAmbientValue<T of com.lhwdev.ktui.DynamicAmbientValue> origin=null
//					//    element: get_var '-> consumer: com.lhwdev.ktui.Element<*> declared in com.lhwdev.ktui.DynamicAmbientValue.getValue' type=com.lhwdev.ktui.Element<*> origin=null
//					val target = statements[0] as IrVariable
//					val assignment = statements[0]
//
//					fun printOperator() {
//						val operator = when(origin) {
//							IrStatementOrigin.PLUSEQ -> "+="
//							IrStatementOrigin.MINUSEQ -> "-="
//							IrStatementOrigin.DIVEQ -> "/="
//							IrStatementOrigin.MULTEQ -> "*="
//							IrStatementOrigin.PERCEQ -> "%="
//							else -> error("?")
//						}
//
//						print(operator, Type.specialOperator)
//					}
//
//					fun IrExpression.printValue() {
//						this as IrCall
//						valueArguments.single()!!.print()
//					}
//
//					when(assignment) {
//						is IrSetVariable -> {
//							print(assignment.symbol.descriptor.name, Type.localVariable)
//							printOperator()
//							assignment.value.printValue()
//						}
//						is IrSetField -> {
//							assignment.receiver?.printAsReceiver()
//							print(assignment.symbol.descriptor.name, Type.property)
//							printOperator()
//							assignment.value.printValue()
//						}
//						is IrCall -> {
//							val prop = (assignment.symbol.ownerOrNull as? IrSimpleFunction)?.correspondingPropertySymbol?.ownerOrNull
//							assignment.printCallPrefix()
//
//							when {
//								prop != null -> {
//									val propName = prop.name.asString()
//									print(propName, Type.property)
//									if(assignment.symbol.ownerOrNull == prop.setter) {
//										printOperator()
//										assignment.getValueArgument(0)?.print()
//									}
//								}
//								origin == IrStatementOrigin.EQ -> {
//									// here, some bridge: java setValue(...)
//									val propName = assignment.symbol.descriptor.name.asString()
//									require(propName.startsWith("set"))
//									print(propName.drop(3).decapitalizeFirst(), Type.property)
//									printOperator()
//									assignment.valueArguments.single()!!.print()
//								}
//								else -> error("?")
//							}
//						}
//						else -> error("Unexpected assignment: $assignment(origin = $origin)\n${dumpColored()}")
//					}
//				}
				
				IrStatementOrigin.LAMBDA -> {
					val function = statements[0] as IrFunction
					function.printAsLambda()
				}
				
				IrStatementOrigin.OBJECT_LITERAL -> {
					val classImpl = statements[0] as IrClass
					classImpl.printAsObject()
				}
				
				IrStatementOrigin.SAFE_CALL -> {
					val lhs = statements[0] as IrVariable
					val rhs = statements[1] as IrWhen
					val call = rhs.branches.last().result as? IrCall
					if(call == null) {
						statements.printJoin(statementGap)
						return
					}
					lhs.initializer?.print()
					print("?.", Type.specialOperator)
					// handle property(though there is only get, not set)
					val prop = (call.symbol.ownerOrNull as? IrSimpleFunction)?.correspondingPropertySymbol?.ownerOrNull
					when {
						prop != null -> {
							val propName = prop.name.asString()
							print(propName, Type.property)
						}
						origin == IrStatementOrigin.GET_PROPERTY -> {
							// here, some bridge: java getValue()
							val propName = call.symbol.descriptor.name.asString()
							val realPropNamePascal = when {
								propName.startsWith("get") -> propName.drop(3)
								propName.startsWith("is") -> propName.drop(2)
								else -> {
									call.print()
									printError("<- GET_PROPERTY without appropriate name: $propName")
									return
								}
							}
							print(realPropNamePascal.decapitalizeFirst(), Type.property)
						}
						else -> {
							printIdentifier(call.symbol.descriptor.name)
							call.printCallTypeArguments()
							call.printCallValueArguments()
						}
					}
				}
				
				IrStatementOrigin.ELVIS -> {
					val lhs = (statements[0] as IrVariable).initializer!!
					val alternative = (statements[1] as IrWhen).branches.first { !it.isElse }.result
					
					lhs.print()
					print("?:", Type.specialOperator)
					alternative.print()
				}
				
				IrStatementOrigin.WHEN -> {
					val variable = statements[0] as IrVariable
					val original = statements[1] as IrWhen
					
					printKeyword("when")
					
					groupParen {
						if(variable.origin == IrDeclarationOrigin.IR_TEMPORARY_VARIABLE) // when(...)
							variable.initializer!!.print()
						else variable.print() // when(val NAME = ...)
					}
					
					bracedBlock {
						fun IrExpression.printValue() {
							when(this) {
								// a, b, in range, is Any ... ->
								is IrWhen -> {
									// ir tree is like:
									// branch
									//   if: ...
									//   then: const Boolean type=kotlin.Boolean value=true
									// branch
									//   if: const Boolean type=kotlin.Boolean value=true
									//   then: ...
									assert(origin == IrStatementOrigin.WHEN_COMMA)
									
									branches[0].condition.printValue()
									printSeparator(",")
									branches[1].also { assert(it.isElse) }.result.printValue()
								}
								
								is IrCall -> {
									when(val name = symbol.descriptor.name.asString()) {
										// VALUE
										"equals" -> valueArguments.single()!!.print()
										"EQEQ" -> valueArguments[1]!!.print()
										"contains" -> { // in VALUE
											printKeyword("in")
											valueArguments.single()!!.print()
										}
										
										"not" -> { // !in VALUE
											print("!", Type.specialOperator)
											dispatchReceiver!!.printValue()
										}
										
										else -> unknown(name)
									}
								}
								
								// is TYPE / !is TYPE
								is IrTypeOperatorCall -> when(operator) {
									IrTypeOperator.INSTANCEOF -> {
										printKeyword("is")
										typeOperand.print()
									}
									IrTypeOperator.NOT_INSTANCEOF -> {
										print("!", Type.specialOperator)
										printKeyword("is")
										typeOperand.print()
									}
									else -> error("unexpected operator $operator")
								}
								
								else -> unknown(this.toString())
							}
						}
						
						original.branches.printJoin(statementGap) { branch ->
							val condition = branch.condition
							val result = branch.result
							val isElse = (condition as? IrConst<*>)?.value == true
							
							if(isElse) {
								if(
									((result as? IrCall)?.symbol?.descriptor as? IrSimpleBuiltinOperatorDescriptorImpl)
										?.name?.toString() == "noWhenBranchMatchedException"
								) return@printJoin
								printKeyword("else")
							} else condition.printValue()
							
							printSeparator("->")
							if(result.braceNeeded) bracedBlock {
								result.print()
							} else result.print()
						}
					}
				}
				
				IrStatementOrigin.FOR_LOOP -> {
					// for(NAME in EXPR) ORIGINAL
					// into
					
					// block {
					//   val tmp0_iterator = EXPR.iterator()
					//   // destructuring variable declarations here: if exists
					//   while(tmp0_iterator.hasNext()) {
					//     block {
					//       val NAME = tmp0_iterator.next()
					//       block { ORIGINAL }
					//     }
					//   }
					// }
					val iterator = statements[0] as IrVariable
					val whileLoop = statements[1] as IrWhileLoop
					val whileBlock = whileLoop.body as IrBlock
					val looperDeclaration = whileBlock.statements[0] as IrVariable
					val destructionDeclarations = whileBlock.statements.drop(1).dropLast(1)
					val original = whileBlock.statements.last()
					
					printKeyword("for")
					groupParen {
						if(destructionDeclarations.isEmpty())
							print(looperDeclaration.name, Type.localVariable)
						else groupParen { // TODO: support val (_ <- this, name..) = ...
							destructionDeclarations.printJoin(",") { print((it as IrVariable).name, Type.localVariable) }
						}
						printKeyword("in")
						(iterator.initializer as IrCall).let {
							it.dispatchReceiver ?: it.extensionReceiver ?: error("no expression")
						}.print()
					}
					
					bracedBlock {
						original.print()
					}
				}
				
				else -> if(origin != null && debug) {
					unknown("irBlock(origin = $origin)")
					bracedBlock(type = Type.unknown) {
						statements.printJoin(statementGap)
					}
				} else {
					if(this is IrReturnableBlock) {
						val labelName = "label${labels.size}"
						labels[symbol] = labelName
						print("run", Type.specialOperator)
						debug { debug("(<- IrReturnableBlock)") }
						print("@$labelName", Type.label)
						bracedBlock {
							statements.printJoin(statementGap)
						}
					} else statements.printJoin(statementGap)
				}
			}
		} catch(e: Throwable) { // by default, these values with these origins are expected, but some lowerings may change them
			unknown("irBlock")
			bracedBlock(type = Type.unknown) {
				statements.printJoin(statementGap)
			}
		}
		
		debug { expression.check() }
	}
	
	fun IrFunction.printAsLambda(): Unit = withScope(ScopeWithIr(Scope(symbol), this)) {
		if(body?.statements?.let { if(it.size <= 1) null else it.dropLast(1) }?.any {
				var exists = false
				it.accept(object : IrElementVisitorVoid {
					override fun visitElement(element: IrElement) {
					}
					
					override fun visitReturn(expression: IrReturn) {
						if(expression.returnTarget != this@printAsLambda.descriptor)
							exists = true
					}
				}, null)
				exists
			} == true) {
			val label = "label${labels.size}"
			labels[symbol] = label
			print("$label@", Type.label)
		}
		
		val statements = body?.statements?.toMutableList()
		
		grouping(prefix = {
			print("{", Type.braces)
			printSpace()
			val parameters = valueParameters
			if(parameters.isNotEmpty() && parameters.singleOrNull()?.name?.toString() != "it") {
				val destructuringParameters = mutableMapOf<Int, MutableMap<Int, IrValueDeclaration>>()
				if(statements != null) while(true) {
					if(parameters.firstOrNull()?.name?.isSpecial != true) break
					val statement = statements.firstOrNull() as? IrVariable ?: break
					val initializer = statement.initializer as? IrCall ?: break
					val parameter = initializer.anyReceiver as? IrGetValue ?: break
					val name = parameter.symbol.descriptor.name.asString()
					if(!name.startsWith("<name for destructuring parameter ")) break
					val num = name.substring(34, name.length - 1).toIntOrNull() ?: break
					val cur = destructuringParameters[num]
						?: mutableMapOf<Int, IrValueDeclaration>().also { destructuringParameters[num] = it }
					cur[initializer.symbol.descriptor.name.asString().drop(9 /* "component" */).toInt() - 1] = statement
					statements.removeAt(0)
				}
				parameters.printJoin(",") {
					val destructuring = destructuringParameters[it.index]
					if(destructuring == null) it.print()
					else groupParen {
						(0..destructuring.keys.max()!!).printJoin(",") {
							val destructuringInner = destructuring[it]
							if(destructuringInner == null) print("_", Type.valueParameter)
							else print(destructuringInner.name, Type.valueParameter)
						}
					}
				}
				print("->", Type.separator)
			}
		}, postfix = {
			print("}", Type.braces)
		}, addIndent = true, newLine = true) {
			statements?.printJoin(statementGap)
		}
	}
	
	fun IrClass.printAsObject() {
		printKeyword("object")
		if(!name.isSpecial)
			print(name, Type.objectClass)
		
		if(superTypes.any { !it.isAny() }) {
			printSeparator(":")
			superTypes.printJoin(",") { it.print() }
		}
		val printableDeclarations = declarations
			.filter { it !is IrConstructor }
			.filter { it.origin != IrDeclarationOrigin.FAKE_OVERRIDE }
		if(printableDeclarations.isNotEmpty()) {
			bracedBlock {
				printableDeclarations.printJoin(statementGap)
			}
		}
	}
	
	
	override fun visitComposite(expression: IrComposite) { // already handled, or TODO: just statement??
		if(expression.origin != null && debug) {
			unknown("composite(origin = ${expression.origin})")
			bracedBlock {
				expression.statements.printJoin(statementGap)
			}
		} else expression.statements.printJoin(statementGap)
		
		debug { expression.check() }
	}
	
	// dynamic
	
	override fun visitDynamicMemberExpression(expression: IrDynamicMemberExpression) =
		expr(expression) {
			receiver.print()
			printSeparator(".")
			print(memberName, Type.dynamicMember)
			
			debug { check() }
		}
	
	override fun visitDynamicOperatorExpression(expression: IrDynamicOperatorExpression): Unit =
		expr(expression) {
			when(operator) {
				IrDynamicOperator.INVOKE -> {
					groupExpr {
						receiver.print()
					}
					groupParen {
						arguments.printJoin(",")
					}
				}
				IrDynamicOperator.ARRAY_ACCESS -> {
					groupExpr {
						receiver.print()
					}
					grouping("[", "]") {
						arguments.printJoin(",")
					}
				}
				IrDynamicOperator.PREFIX_INCREMENT -> {
					print("++", Type.specialOperator)
					groupExpr {
						receiver.print()
					}
				}
				IrDynamicOperator.PREFIX_DECREMENT -> {
					print("--", Type.specialOperator)
					groupExpr {
						receiver.print()
					}
				}
				else -> {
					groupExpr {
						receiver.print()
					}
					print(operator.image, Type.specialOperator)
					arguments.firstOrNull()?.print()
				}
			}
			
			debug { check() }
		}
}
