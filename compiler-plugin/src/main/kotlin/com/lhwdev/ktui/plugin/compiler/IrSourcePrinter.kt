@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ObsoleteDescriptorBasedAPI::class)

package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.IrComponent
import com.lhwdev.ktui.plugin.compiler.util.IrComponentVisitorVoidWithContext
import com.lhwdev.ktui.plugin.compiler.util.dumpPreview
import com.lhwdev.ktui.plugin.compiler.util.pluginContext
import org.jetbrains.kotlin.backend.common.ScopeWithIr
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContextImpl
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.descriptors.IrBuiltIns
import org.jetbrains.kotlin.ir.descriptors.IrSimpleBuiltinOperatorDescriptorImpl
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrIfThenElseImpl
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtLambdaArgument
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.renderer.DescriptorRenderer
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.Variance
import java.util.IdentityHashMap
import java.util.Locale
import java.util.Stack


/**
 * Aims to provide useful information to debug.
 */

private val IrMemberAccessExpression<*>.valueArguments: List<IrExpression?>
	get() = object : AbstractList<IrExpression?>() {
		override val size = valueArgumentsCount
		override fun get(index: Int) = getValueArgument(index)
	}


private val IrMemberAccessExpression<*>.typeArguments: List<IrType?>
	get() = object : AbstractList<IrType?>() {
		override val size = typeArgumentsCount
		override fun get(index: Int) = getTypeArgument(index)
	}

private fun IrType.replaceAnnotations(newAnnotations: List<IrConstructorCall>): IrType = when(this) {
	is IrSimpleType -> IrSimpleTypeImpl(
		originalKotlinType,
		classifier,
		hasQuestionMark,
		arguments,
		newAnnotations,
		abbreviation
	)
	is IrErrorType -> IrErrorTypeImpl(
		originalKotlinType,
		newAnnotations,
		(this as? IrErrorTypeImpl)?.variance ?: Variance.INVARIANT
	)
	is IrDynamicType -> IrDynamicTypeImpl(
		originalKotlinType,
		newAnnotations,
		(this as? IrDynamicTypeImpl)?.variance ?: Variance.INVARIANT
	)
	else -> this
}

private fun IrType.replaceWithStarProjections(): IrType =
	if(this is IrSimpleType) IrSimpleTypeImpl(
		originalKotlinType,
		classifier,
		hasQuestionMark,
		List(arguments.size) { IrStarProjectionImpl },
		annotations,
		abbreviation
	)
	else this

private fun IrType.checkSubtypeOf(parent: IrType, irBuiltins: IrBuiltIns): Boolean =
	if(this is IrSimpleType && parent is IrSimpleType) {
		if(classifier is IrClassSymbol && parent.classifier is IrClassSymbol) replaceWithStarProjections().isSubtypeOf(
			parent.replaceWithStarProjections(),
			irBuiltins
		)
		else true // TODO: check this case
	} else this == parent


fun IrElement.patchDeclarationParentsChildren() {
	accept(object : IrElementVisitorVoid {
		override fun visitElement(element: IrElement) {
			val parent = element
			if(parent is IrDeclarationParent) parent.acceptChildren(object : IrElementVisitorVoid {
				override fun visitElement(element: IrElement) {
					element.patchDeclarationParents(parent)
				}
			}, null)
			else element.acceptChildren(this, null)
		}
	}, null)
}

fun IrElement.dumpSrcColored(
	context: IrPluginContext = pluginContext,
	config: SourcePrintConfig = SourcePrintConfig.default
) = buildString {
//		patchDeclarationParentsChildren()
	acceptVoid(IrAllSourcePrinterVisitor(this, context, config))
}

fun IrElement.logSrcColored(
	context: IrPluginContext = pluginContext,
	config: SourcePrintConfig = SourcePrintConfig.default
) {
//	patchDeclarationParentsChildren()
	fixIndents {
		IrAllSourcePrinterVisitor(object : Appendable {
			override fun append(csq: CharSequence): Appendable {
				logInternalWithoutNewline(csq.toString(), color = "")
				return this
			}
			
			override fun append(csq: CharSequence, start: Int, end: Int) =
				append(csq.substring(start, end))
			
			override fun append(c: Char) = append("$c")
		}, context, config).let { acceptVoid(it) }
		logln()
	}
}

fun IrElement.dumpSrcHeadColored(
	context: IrPluginContext = pluginContext,
	config: SourcePrintConfig = SourcePrintConfig.default
) = buildString {
//		patchDeclarationParentsChildren()
	acceptVoid(IrDeclarationHeadPrinterVisitor(this, context, config))
}

fun IrElement.logSrcHeadColored(
	context: IrPluginContext = pluginContext,
	config: SourcePrintConfig = SourcePrintConfig.default
) {
//	patchDeclarationParentsChildren()
	fixIndents {
		IrDeclarationHeadPrinterVisitor(object : Appendable {
			override fun append(csq: CharSequence): Appendable {
				logInternalWithoutNewline(csq.toString(), color = "")
				return this
			}
			
			override fun append(csq: CharSequence, start: Int, end: Int) =
				append(csq.substring(start, end))
			
			override fun append(c: Char) = append("$c")
		}, context, config).let { acceptVoid(it) }
		logln()
	}
}

fun IrType.dumpColored(
	context: IrPluginContext = pluginContext,
	config: SourcePrintConfig = SourcePrintConfig.default
) = buildString {
	IrAllSourcePrinterVisitor(this, context, config).apply {
		print()
	}
}

fun String.decapitalizeFirst() = first().toLowerCase() + drop(1)


private val sDefaultFilterOrigins = listOf(IrDeclarationOrigin.FAKE_OVERRIDE)

data class SourcePrintConfig(
	val allowSpecialMarker: Boolean = true,
	val debug: Boolean = false,
	val originsToFilter: List<IrDeclarationOrigin> = sDefaultFilterOrigins,
	val printAllOrigins: Boolean = false,
	val allowNewLine: Boolean = true,
	val autoBind: Boolean = true
) {
	companion object {
		val default = SourcePrintConfig()
		val debug = SourcePrintConfig(debug = true)
	}
}


private class Printer(val out: Appendable, val indentUnit: CharSequence) {
	private var indents = 0
	private var indentCache = ""
	private var hasIndent = false
	
	@Suppress("NOTHING_TO_INLINE")
	inline fun print(vararg contents: CharSequence) {
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


private class IrAllSourcePrinterVisitor(out: Appendable, context: IrPluginContext, config: SourcePrintConfig) :
	IrSourcePrinterVisitor(out, context, config) {
	override val renderContent: Boolean get() = true
	override val renderBody: Boolean get() = true
}

private class IrDeclarationHeadPrinterVisitor(out: Appendable, context: IrPluginContext, config: SourcePrintConfig) :
	IrSourcePrinterVisitor(out, context, config) {
	override val renderContent: Boolean get() = false
	override val renderBody: Boolean get() = false
	override val allowAnnotationsNewLine: Boolean get() = false
	
	override fun visitBlockBody(body: IrBlockBody) {
	}
	
	// showing default value on function declaration etc. is good?? removing this may break some codes.. -> handle
//	override fun visitExpressionBody(body: IrExpressionBody) {
//		super.visitExpressionBody(body)
//	}
}


/**
 * Log the ir tree in the form of the Kotlin code.
 * Note that the result should generally be used for the sake of debugging.
 * If you enable debug flag on the constructor, then it prints some useful information and code
 * errors(not all the errors, only some errors so far).
 */
@OptIn(ObsoleteDescriptorBasedAPI::class)
private abstract class IrSourcePrinterVisitor(
	out: Appendable,
	val context: IrPluginContext,
	val config: SourcePrintConfig
) : IrComponentVisitorVoidWithContext<ScopeWithIr>() {
	override fun createScope(declaration: IrSymbolOwner) = ScopeWithIr(Scope(declaration.symbol), declaration)
	
	private val irBuiltIns: IrBuiltIns = context.irBuiltIns
	private val linker = if(context is IrPluginContextImpl) context.linker else null // TODO
	private val IrSymbol.ownerOrNull: IrSymbolOwner? get() = if(isBound) owner else null
	private val <T : IrSymbolOwner> IrBindableSymbol<*, T>.ownerOrNull: T? get() = if(isBound) owner else null
	private val <T : IrSymbol> T.bound: T
		get() {
			if(!isBound && config.autoBind) {
				linker?.getDeclaration(this)
			}
			return this
		}
	
	private fun KotlinType.toIrType(): IrType = context.typeTranslator.translateType(this)
	
	private val printer: Printer = Printer(out, "\t")
	
	val debug get() = config.debug
	val allowSpecialMarker get() = config.allowSpecialMarker
	
	private var lastBlock = false
	private var currentIndex = 0
	
	private enum class SpaceType { none, compact, space }
	
	private var lastSpace = SpaceType.none
	
	val labels = IdentityHashMap<IrReturnTargetSymbol, String>()
	
	
	class Group(val selfType: Type, var inherit: GroupInherit) {
		val localOverrides by lazy { Stack<Pair<Type, Type>>() }
	}
	
	class GroupInherit(
		val isAnnotationCall: Boolean = false,
		val isNotCall: Boolean = false,
		val overrideAllType: Type? = null,
		val stylePrefix: AnsiItem? = null,
		val localOverrides: List<Pair<Type, Type>>? = null,
		val printIntAsBinary: Boolean = false
	) {
		fun merge(other: GroupInherit) = GroupInherit(
			isAnnotationCall = other.isAnnotationCall || isAnnotationCall,
			isNotCall = other.isNotCall || isNotCall,
			overrideAllType = other.overrideAllType ?: overrideAllType,
			stylePrefix = other.stylePrefix ?: stylePrefix,
			localOverrides = merge(other.localOverrides, localOverrides) { a, b -> b + a },
			printIntAsBinary = other.printIntAsBinary || printIntAsBinary
		)
	}
	
	
	val groupStack = Stack<Group>().apply {
		push(Group(Type.none, GroupInherit()))
	}
	inline val currentGroup: Group get() = groupStack.peek()
	inline var currentInherit
		get() = currentGroup.inherit
		set(value) {
			currentGroup.inherit = value
		}
	
	fun mergeInherit(other: GroupInherit) {
		val group = currentGroup
		group.inherit = group.inherit.merge(other)
	}
	
	enum class Type(val ansi: AnsiItem?) {
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
	private val regexSpaceSpace = Regex("([+\\-*/=]|\\+=|-=|/=|\\*=|\\|=|&=|\\?:|->|&&|\\|\\|)")
	private val regexCompactCompact = Regex("([.($]|\\?\\.|\\.\\.|::)")
	private val regexCompactNone = Regex("([>\\[]|!!|\\++|--)")
	private val regexNoneCompact = Regex("[<\\]]")
	
	
	fun print(content: CharSequence, type: Type) {
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
			
			
			printer.print(
				(if(hasHeadSpace) " " else "") +
					((group.inherit.stylePrefix + newType.ansi)?.toString() ?: "") +
					content
			)
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
	
	fun compact() {
		lastSpace = SpaceType.compact
	}

//	fun println() {
//		printer.println()
//		lastBlock = false
//		lastSpace = SpaceType.none
//	}

//	fun println(content: Any?, type: Type) {
//		print(content, type)
//		println()
//	}
	
	inline fun group(
		prefix: CharSequence,
		postfix: CharSequence,
		type: Type = Type.separator,
		inherit: GroupInherit = currentGroup.inherit,
		addIndent: Boolean = false,
		newLine: String? = null,
		block: () -> Unit
	) {
		group({ print(prefix, type) }, { print(postfix, type) }, type, inherit, addIndent, newLine, block)
	}
	
	inline fun group(
		prefix: () -> Unit,
		postfix: () -> Unit,
		type: Type = Type.separator,
		inherit: GroupInherit = currentGroup.inherit,
		addIndent: Boolean = false,
		newLine: String? = null,
		block: () -> Unit
	) {
		groupStack.push(Group(type, inherit))
		prefix()
		if(addIndent) printer.pushIndent()
		if(newLine != null) none(newLine)
		
		try {
			block()
		} finally {
			groupStack.pop()
			if(addIndent) printer.popIndent()
			if(newLine != null) none(newLine)
			postfix()
		}
	}
	
	inline fun group(type: Type = Type.separator, inherit: GroupInherit = currentGroup.inherit, addIndent: Boolean = false, block: () -> Unit) {
		groupStack.push(Group(type, inherit))
		if(addIndent) printer.pushIndent()
		
		try {
			block()
		} finally {
			groupStack.pop()
			if(addIndent) printer.popIndent()
		}
	}
	
	abstract val renderContent: Boolean
	abstract val renderBody: Boolean
	
	inline fun renderContent(block: () -> Unit) {
		if(renderContent) block()
	}
	
	inline fun renderBody(block: () -> Unit) {
		if(renderBody) block()
	}
	
	inline fun stylePrefix(ansi: AnsiItem, block: () -> Unit) {
		group(inherit = GroupInherit(stylePrefix = ansi), block = block)
	}
	
	inline fun stylePrefix(ansi: AnsiItem) {
		mergeInherit(GroupInherit(stylePrefix = ansi))
	}
	
	inline fun indented(block: () -> Unit) {
		group(addIndent = true, block = block)
	}
	
	inline fun emptyGroup(prefix: CharSequence, postfix: CharSequence, type: Type = Type.separator) {
		print(prefix, type)
		print(postfix, type)
	}
	
	inline fun groupExpr(block: () -> Unit) {
		// TODO: insert ( ) if necessary
		printSeparator("(")
		block()
		printSeparator(")")
	}
	
	inline fun bracedBlock(newLine: String?, type: Type = Type.separator, block: () -> Unit) {
		group("{", "}", type, addIndent = true, newLine = newLine) {
			block()
		}
	}
	
	inline fun bracedBlockBody(newLine: String? = declarationNewLine, type: Type = Type.separator, block: () -> Unit) {
		bracedBlock(newLine, type, block)
	}
	
	inline fun bracedBlockCode(newLine: String? = statementNewLine, type: Type = Type.separator, block: () -> Unit) {
		bracedBlock(newLine, type, block)
	}
	
	inline fun parenGroup(
		addIndent: Boolean = false,
		newLine: String? = null,
		type: Type = Type.separator,
		block: () -> Unit
	) {
		group("(", ")", type, addIndent = addIndent, newLine = newLine) {
			block()
		}
	}
	
	inline fun <R> provideInherit(other: GroupInherit, block: () -> R): R {
		val group = currentGroup
		val last = group.inherit
		group.inherit = last.merge(other)
		return try {
			block()
		} finally {
			group.inherit = last
		}
	}
	
	inline fun overrideAllType(type: Type, block: () -> Unit) {
		provideInherit(GroupInherit(overrideAllType = type), block)
	}
	
	inline fun specialMarker(block: () -> Unit) {
		if(allowSpecialMarker) overrideAllType(Type.specialMarker, block)
	}
	
	inline fun overrideType(target: Type, with: Type, block: () -> Unit) =
		group(inherit = GroupInherit(localOverrides = listOf(target to with)), block = block)
	
	inline fun <R> overrideTypeLocal(target: Type, with: Type, block: () -> R): R {
		val localOverrides = currentGroup.localOverrides
		
		localOverrides.push(target to with)
		return try {
			block()
		} finally {
			localOverrides.pop()
		}
	}
	
	inline fun print(content: Any?, type: Type) {
		print(content.toString(), type)
	}
	
	inline fun print(type: Type, block: () -> CharSequence) {
		print(block(), type)
	}
	
	fun printSpace() {
		printer.print(" ")
		lastBlock = false
		lastSpace = SpaceType.compact
	}
	
	fun IrElement.print() {
		acceptVoid(this@IrSourcePrinterVisitor)
	}
	
	inline fun <T> Iterable<T>.iterate(block: (element: T, hasNext: Boolean) -> Unit) {
		val iterator = iterator()
		var lastHasNext = iterator.hasNext()
		while(lastHasNext) {
			val next = iterator.next()
			lastHasNext = iterator.hasNext()
			block(next, lastHasNext)
		}
	}
	
	fun Iterable<IrElement>.printJoin(separator: CharSequence, separatorType: Type = Type.separator) =
		group {
			var indexBefore = currentIndex
			
			iterate { element, hasNext ->
				element.print()
				if(hasNext && indexBefore != currentIndex) {
					print(separator, separatorType)
					indexBefore = currentIndex
				}
			}
		}
	
	inline fun <T> Iterable<T>.printJoin(separator: CharSequence, separatorType: Type = Type.separator, block: (T) -> Unit) {
		var indexBefore = currentIndex
		
		iterate { element, hasNext ->
			block(element)
			if(hasNext && indexBefore != currentIndex) {
				print(separator, separatorType)
				indexBefore = currentIndex
			}
		}
	}
	
	inline fun <T> List<T>.printJoin(separator: () -> Unit, block: (T) -> Unit) {
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
	inline fun CharSequence.print(type: Type) {
		print(this, type)
	}
	
	inline fun printKeyword(content: Any?) {
		print(content, Type.keyword)
	}
	
	val allowedNotEscapedNames = Regex("[A-Za-z_]([A-Za-z_0-9])*")
	
	fun printIdentifier(content: Any?, type: Type = Type.identifier) {
		var text = content.toString()
		if(text.first() == '<') // special
			print(text, type)
		else {
			if(!allowedNotEscapedNames.matches(text))
				text = "`$text`"
			print(text, type)
		}
	}
	
	inline fun printSeparator(content: Any?) {
		print(content, Type.separator)
	}
	
	@JvmName("printWithReceiver")
	inline fun Any?.print(type: Type) {
		print(this.toString(), type)
	}
	
	inline fun unknown(content: CharSequence) {
		print(content, Type.unknown)
	}
	
	fun debug(content: CharSequence) {
		print(content, Type.debug)
	}
	
	fun none(content: CharSequence) {
		print(content, Type.none)
	}
	
	inline fun debug(block: () -> Unit) {
		if(debug) provideInherit(GroupInherit(stylePrefix = Type.debug.ansi), block)
	}
	
	inline fun ifDebug(block: () -> Unit) {
		if(debug) block()
	}
	
	fun errorScope() {
		mergeInherit(GroupInherit(overrideAllType = Type.error))
	}
	
	fun printError(content: CharSequence) {
		print(content, Type.error)
	}
	
	fun DescriptorVisibility.print(default: DescriptorVisibility = DescriptorVisibilities.PUBLIC) = when(this) {
		default -> ""
		DescriptorVisibilities.PUBLIC -> "public"
		// DescriptorVisibilities.DEFAULT_VISIBILITY // == public
		DescriptorVisibilities.PRIVATE -> "private"
		DescriptorVisibilities.INTERNAL -> "internal"
		DescriptorVisibilities.PROTECTED -> "protected"
		DescriptorVisibilities.LOCAL -> ""
		DescriptorVisibilities.INHERITED -> ""
		DescriptorVisibilities.INVISIBLE_FAKE -> ""
		else -> "TODO: ${this.name.toLowerCase(Locale.ROOT)}"
	}.print(Type.keyword)
	
	
	fun Modality.print(default: Modality = Modality.FINAL) = when(this) {
		default -> ""
		Modality.FINAL -> "final"
		Modality.SEALED -> "sealed"
		Modality.OPEN -> "open"
		Modality.ABSTRACT -> "abstract"
	}.print(Type.keyword)
	
	fun List<IrTypeParameter>.print() {
		if(isNotEmpty()) {
			group("<", ">", Type.separator) {
				printJoin(",")
			}
		}
	}
	
	fun IrType.print() {
		when(this) {
			is IrDynamicType -> {
				annotations.printAnnotations(null)
				print("dynamic", Type.lightKeyword)
			}
			is IrErrorType -> {
				annotations.printAnnotations(null)
				unknown("[ERROR]")
			}
			is IrSimpleType -> {
				fun IrSimpleType.printContent() {
					if(isFunction()) {
						val extensionAnnotation = kotlinPackageFqn.child(Name.identifier("ExtensionFunctionType"))
						val parameterNameAnnotation = kotlinPackageFqn.child(Name.identifier("ParameterName"))
						val isFirstExtension = annotations.hasAnnotation(extensionAnnotation)
						val realAnnotations =
							annotations.filter { (it.symbol.descriptor as ConstructorDescriptor).constructedClass.fqNameSafe != extensionAnnotation }
						realAnnotations.printAnnotations(null)
						if(realAnnotations.isNotEmpty()) printSpace()
						
						val parameters =
							arguments.dropLast(1 /* return type */).let { if(isFirstExtension) it.drop(1) else it }
						val returnType = arguments.last()
						
						if(isFirstExtension) {
							arguments.first().printTypeArgument()
							printSeparator(".")
						}
						
						parenGroup {
							parameters.printJoin(",") { parameter ->
								val type = parameter.typeOrNull
								val parameterName = type?.getAnnotation(parameterNameAnnotation)
								
								if(parameterName != null) {
									print((parameterName.getValueArgument(0) as IrConst<*>).value, Type.valueParameter)
									printSeparator(":")
								}
								
								if(type == null) parameter.printTypeArgument()
								else {
									val newAnnotations = type.annotations.filter { it.symbol != parameterName?.symbol }
									type.replaceAnnotations(newAnnotations).print()
								}
							}
						}
						
						printSeparator("->")
						returnType.printTypeArgument()
						return
					}
					
					annotations.printAnnotations(null)
					
					classifier.print()
					
					if(arguments.isNotEmpty()) group("<", ">") {
						arguments.printJoin(",") {
							it.printTypeArgument()
						}
					}
					
					if(hasQuestionMark)
						print("?", Type.importantSeparator)
				}
				
				val abbreviation = abbreviation
				if(abbreviation != null) {
					abbreviation.annotations.printAnnotations(null)
					print(abbreviation.typeAlias.descriptor.name, Type.typeAlias)
					if(arguments.isNotEmpty()) group("<", ">") {
						abbreviation.arguments.printJoin(",") {
							it.printTypeArgument()
						}
					}
					if(hasQuestionMark) print("?", Type.specialOperator)
					
					overrideAllType(Type.specialMarker) {
						parenGroup {
							printSeparator("=")
							printContent()
						}
					}
				} else printContent()
			}
			
			else -> unknown("[UNKNOWN: $this]")
		}
	}
	
	fun IrClassifierSymbol.print() {
		when(this) {
			is IrClassSymbol ->
				print(
					descriptor.name,
					if(descriptor.kind == ClassKind.INTERFACE) Type.interfaceClass else Type.classClass
				)
			is IrTypeParameterSymbol -> {
				print(descriptor.name, Type.typeParameter)
				descriptor.printOrigin()
			}
			else -> error(toString())
		}
	}
	
	fun IrTypeArgument.printTypeArgument() {
		when(this) {
			is IrStarProjection -> "*".print(Type.specialOperator)
			
			is IrTypeProjection -> {
				print(variance.label, Type.lightKeyword)
				type.print()
			}
			
			else -> unknown("UNKNOWN[$this]")
		}
	}
	
	open val allowAnnotationsNewLine get() = true
	
	fun List<IrConstructorCall>.printAnnotations(newLine: String?) {
		if(isNotEmpty()) {
			overrideType(Type.identifier, Type.annotationClass) {
				if(size == 1) {
					print("@", Type.annotationClass)
					single().printCallAnnotation()
				} else {
					group("@[", "]", Type.annotationClass) {
						printJoin(",") {
							it.printCallAnnotation()
						}
					}
				}
			}
			
			if(allowAnnotationsNewLine) newLine?.let { none(it) }
		}
	}
	
	val filesGap = if(config.allowNewLine) "\n\n\n" else " /* FILES GAP */ "
	val declarationGap = if(config.allowNewLine) "\n\n" else " "
	val declarationNewLine = if(config.allowNewLine) "\n" else " "
	val statementGap = if(config.allowNewLine) "\n" else "; "
	val statementNewLine = if(config.allowNewLine) "\n" else "; "
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	override fun visitElement(element: IrElement) {
		unknown(element::class.java.simpleName)
		if(element is IrComponent<*>) {
			unknown("(irComponent)")
			element.dumpPreview().print()
		}
		debug { element.check() }
	}
	
	@Suppress("UNUSED_PARAMETER")
	fun onError(target: IrElement, error: Throwable) {
		printError("[error: $target]")
		// throw error // DEBUG
	}
	
	inline fun <T : IrElement> element(element: T, block: T.() -> Unit) = group {
		try {
			element.block()
		} catch(e: Throwable) {
			onError(element, e)
		}
	}
	
	inline fun <T : IrStatement> statement(statement: T, block: T.() -> Unit) = group {
		try {
			statement.block()
		} catch(e: Throwable) {
			onError(statement, e)
		}
	}
	
	inline fun <T : IrDeclaration> declare(declaration: T, block: T.() -> Unit) {
		statement(declaration, block)
		
		if(debug) {
			val parent = scopeStack.getOrNull(scopeStack.lastIndex - 1)
			if(parent != null && declaration.parent != parent)
				printError("<- unexpected parent: ${declaration.parent.dumpSrcHeadColored(context, config)}")
		}
	}
	
	inline fun <T : IrExpression> expr(expression: T, block: T.() -> Unit) = group {
		try {
			expression.block()
		} catch(e: Throwable) {
			onError(expression, e)
		}
	}
	
	fun printLineComment(content: String, type: Type = Type.comment) {
		if(config.allowNewLine) {
			print("// $content", type)
			none("\n")
		} else printBlockComment(content, type)
	}
	
	fun printBlockComment(content: String, type: Type = Type.comment) {
		print("/* $content */", type)
	}
	
	fun DeclarationDescriptor.printOrigin() {
		if(config.printAllOrigins)
			containingDeclaration?.run {
				val name = name.asString()
				var newName = when(this) {
					is PackageFragmentDescriptor -> {
						if(fqName == IrBuiltIns.KOTLIN_INTERNAL_IR_FQN) return // EQEQ, CHECK_NULL etc.
						"package $fqName"
					} // TODO
					is ModuleDescriptor -> "module $name" // ??
					else -> name
				}
				when(newName) {
					"<init>" -> newName = "constructor"
				}
				printSpace()
				debug("of $newName")
			}
	}
	
	
	///// validation
	val set = mutableSetOf<IrElement>()
	val checkedTypes = mutableSetOf<IrType>()
	
	
	///// declarations
	
	fun IrElement.check() {
		if(set.contains(this)) printError(" <- duplicate IR node")
		set.add(this)
	}
	
	fun ensureTypesEqual(actualType: IrType, expectedType: IrType) {
		if(actualType != expectedType) {
			printError(" <- unexpected type: expected ${expectedType.render()}, got ${actualType.render()}")
		}
	}
	
	fun IrExpression.ensureNullable() {
		if(!type.isNullable())
			printError(" <- expected a nullable type, got ${type.render()}")
	}
	
	fun IrExpression.ensureTypeIs(expectedType: IrType) {
		ensureTypesEqual(type, expectedType)
	}
	
	fun IrSymbol.ensureBound(expression: IrExpression) {
		if(!this.isBound && expression.type !is IrDynamicType) {
			printError("<- unbound symbol $this")
		}
	}
	
	fun IrOverridableDeclaration<*>.checkOverrides() {
		for(overriddenSymbol in overriddenSymbols) {
			val overriddenDeclaration = overriddenSymbol.ownerOrNull as? IrDeclarationWithVisibility
				?: continue
			if(overriddenDeclaration.visibility == DescriptorVisibilities.PRIVATE) {
				printError("<- overrides private declaration ${DescriptorRenderer.COMPACT.render(overriddenDeclaration.descriptor)}")
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
	
	fun checkType(type: IrType) {
		if(type in checkedTypes)
			return
		
		when(type) {
			is IrSimpleType -> {
				if(!type.classifier.isBound) group {
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
		printLineComment("module ${declaration.name}")
		declaration.files.printJoin(filesGap)
		
		debug { declaration.check() }
	}
	
	override fun visitFileNew(declaration: IrFile) = element(declaration) {
		printLineComment("file $name ($fqName)")
		declarations.printJoin(declarationGap)
		debug { check() }
	}
	
	override fun visitFieldNew(declaration: IrField) = declare(declaration) {
		super.visitFieldNew(this)
		
		debug {
			check()
		}
	}
	/// class
	
	override fun visitClassNew(declaration: IrClass): Unit = declare(declaration) {
		val isEnum = kind == ClassKind.ENUM_CLASS
		
		// head
		annotations.printAnnotations(declarationNewLine)
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
				(it.visibility == DescriptorVisibilities.PUBLIC || isEnum) &&
				!(it.isInline || it.isExpect || it.isExternal)
			
			if(!isCompactConstructor) {
				printSpace()
				it.printFunctionHead()
			}
			
			parenGroup(type = Type.separator) {
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
						delegatedMembers[
							context.symbolTable.referenceClassifier(it.descriptor.containingDeclaration!! as ClassDescriptor)
						] = delegatedMember
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
						group {
							errorScope()
							superType.print()
							debug(" <- super not class")
						}
						return@printJoin
					}
					when(classSymbol.descriptor.kind) {
						ClassKind.INTERFACE -> {
							superType.print()
							try {
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
							} catch(e: Throwable) {
								printKeyword("by")
								printError("[error]")
							}
						}
						else -> {
							// class Abc : OtherClass() /* <- this */, Interface1, ...
							
							// BLOCK_BODY
							//    DELEGATING_CONSTRUCTOR_CALL 'CLASS CLASS name:OtherClass ...'
							//      ... (arguments)
							//    INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:Abc ...'
							
							val delegatingConstructorCall =
								primaryConstructor!!.body?.statements?.firstOrNull() as? IrDelegatingConstructorCall
							
							superType.print() // OtherClass
							delegatingConstructorCall?.printCallValueArguments()
							// delegatingConstructorCall?.printCallTypeArguments() // already printed in type
						}
					}
				}
				
				debug {
					if(superTypes.count { it.classOrNull?.descriptor?.kind != ClassKind.INTERFACE } > 1) {
						errorScope()
						printError(" <- multiple class inheritance")
						parenGroup {
							superTypes.filter { it.classOrNull?.descriptor?.kind != ClassKind.INTERFACE }.printJoin(",") {
								it.print()
							}
						}
					}
				}
			}
			
			
			// body
			if(!renderContent) return
			val declarationsToShow = declarations
				.filter { it != primaryConstructor && it !in constructorProperties }
				.filter { it.origin != IrDeclarationOrigin.FAKE_OVERRIDE && it.origin != IrDeclarationOrigin.DELEGATED_MEMBER && it.origin != IrDeclarationOrigin.DELEGATE }
			
			if(isEnum) bracedBlockBody {
				declarationsToShow.filterIsInstance<IrEnumEntry>().printJoin({
					printSeparator(",")
					print(statementGap, Type.none)
				}) { entry ->
					visitEnumEntry(entry)
				}
			}
			else if(declarationsToShow.isNotEmpty()) bracedBlockBody {
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
					parenGroup {
						missingDescriptors.printJoin(",") {
							debug(DescriptorRenderer.COMPACT.render(it))
						}
					}
				}
			}
		}
	}
	
	override fun visitEnumEntry(declaration: IrEnumEntry) = declare(declaration) {
		val call = (declaration.initializerExpression as IrExpressionBody).expression as IrEnumConstructorCall
		visitEnumConstructorCall(call)
		declaration.correspondingClass?.let {
			bracedBlockBody {
				it.declarations
					.filter { it.origin !in config.originsToFilter && it !is IrConstructor }
					.printJoin(statementGap)
			}
		}
	}
	
	fun List<IrProperty>.correspondingProperty(param: IrValueParameter) = find {
		if(it.name == param.name) {
			val init = it.backingField?.initializer?.expression as? IrGetValue
			init?.origin == IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER
		} else false
	}
	
	
	/// function
	
	// fun hello|
	fun IrFunction.printFunctionHead() {
		annotations.printAnnotations(declarationNewLine)
		
		var isOverride = false
		var defaultVisibility = DescriptorVisibilities.PUBLIC
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
	// NOT check for renderBody: it should have checked in advance
	override fun visitBlockBody(body: IrBlockBody) = bracedBlockBody {
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
	
	fun IrElement.printBody() {
		if(renderBody) print() else when(this) {
			is IrBlockBody -> bracedBlock(newLine = null) { printSkip() }
			is IrExpression -> {
				printSeparator("=")
				printSkip()
			}
			is IrSyntheticBody -> bracedBlock(newLine = null) { printSkip() }
			else -> printSkip()
		}
	}
	
	fun IrElement.printOrSkip() {
		if(renderBody) print()
		else printSkip()
	}
	
	fun IrElement.printBodyOrSkip() {
		renderBody { print() }
	}
	
	fun printSkip() {
		print("...", Type.comment)
	}
	
	// functions
	
	override fun visitFunctionNew(declaration: IrFunction): Unit = declare(declaration) {
		when(this) {
			is IrSimpleFunction -> {
				if(origin in config.originsToFilter) return
				printFunctionHead()
				valueParameters.printValueParameters()
				printFunctionReturnType()
				body?.printBody()
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
				val delegation = (body as? IrBlockBody)?.statements?.getOrNull(0) as? IrDelegatingConstructorCall
				if(delegation != null) {
					printSpace()
					print(":", Type.separator)
					
					val callName =
						if(delegation.symbol == currentClassOrNull?.scope?.scopeOwnerSymbol) "this"
						else "super"
					
					print(callName, Type.lightKeyword)
					delegation.printCallValueArguments()
				}
				
				body?.printBody()
			}
			else -> unknown("[unexpected function $this]")
		}
		
		debug {
			check()
			
			if(this is IrSimpleFunction) {
				// but all getter/setters are handled by visitProperty: this just checks in case
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
					parenGroup {
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
					parenGroup {
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
			body.printBody()
			
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
		
		debug { check() }
	}
	
	
	/// member property
	
	override fun visitPropertyNew(declaration: IrProperty) = declare(declaration) {
		val isOverride = descriptor.overriddenDescriptors.isNotEmpty()
		
		val backingField = backingField
		val definedGetter = getter?.takeUnless { it.origin == IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR }
		val definedSetter = setter?.takeUnless { it.origin == IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR }
		
		annotations.printAnnotations(declarationNewLine)
		
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
		getter?.extensionReceiverParameter?.type?.let {
			it.print()
			printSeparator(".")
		}
		
		print(name, Type.property)
		printSeparator(":")
		printSpace()
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
				
				backingField.initializer!!.printBody()
			}
		}
		
		if(!isDelegated) {
			if(definedSetter != null) none(declarationNewLine) // val prop: Type get() = ...\n set() = ... seems a little bit weird?
			
			if(definedGetter != null || definedSetter != null) indented {
				if(definedGetter != null) {
					print("get", Type.lightKeyword)
					definedGetter.valueParameters.printValueParameters() // having valueParameters is impossible in frontend, but possible in IR
					definedGetter.body?.printBody()
				}
				
				if(definedSetter != null) {
					print("set", Type.lightKeyword)
					parenGroup {
						if(definedSetter.valueParameters.size == 1)
							print(definedSetter.valueParameters.single().name, Type.valueParameter)
						else definedSetter.valueParameters.printValueParameters() // having more valueParameters is impossible in frontend, but possible in IR
					}
					definedSetter.body?.printBody()
				}
			}
		}
		
		debug { check() }
	}
	
	
	/// other
	
	fun List<IrValueParameter>.printValueParameters() = parenGroup {
		printJoin(",")
	}
	
	override fun visitValueParameterNew(declaration: IrValueParameter): Unit = declare(declaration) {
		annotations.printAnnotations(null)
		
		val isVararg = isVararg
		if(isVararg) printKeyword("vararg")
		if(isCrossinline) printKeyword("crossinline")
		if(isNoinline) printKeyword("noinline")
		
		print(name, Type.valueParameter)
		printSeparator(":")
		printSpace()
		
		type.print()
		
		// just print even if renderBody is true
		defaultValue?.print() // defaultValue is ExpressionBody, `= ...`
		
		debug { check() }
	}
	
	
	override fun visitTypeParameter(declaration: IrTypeParameter) = declare(declaration) {
		print(declaration.name, Type.typeParameter)
		
		val isNonEmpty = declaration.superTypes.isNotEmpty() &&
			!declaration.superTypes[0].isNullableAny()
		
		if(isNonEmpty) {
			printSeparator(":")
			declaration.superTypes.printJoin(",") { it.print() }
		}
		
		debug { declaration.check() }
	}
	
	
	override fun visitScript(declaration: IrScript) = declare(declaration) {
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
			!renderBody -> true
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
			if(renderBody) initializer.print()
			else printSkip()
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
		delegate.initializer!!.printOrSkip()
		
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
		val function = symbol.bound.owner
		val descriptor = symbol.descriptor
		val isInfix = descriptor.isInfix
		val receiver = anyReceiver
		val name = function.name.asString()
		
		if(descriptor.isOperator || descriptor is IrSimpleBuiltinOperatorDescriptorImpl) {
			if(name == "not") {
				// `a !== b` looks like `not(equals(a, b))`
				val arg = receiver!!
				if(arg is IrCall) {
					val fn = arg.symbol.descriptor
					if(fn is IrSimpleBuiltinOperatorDescriptorImpl) {
						when(fn.name.asString()) {
							"equals", "EQEQ", "EQEQEQ" -> provideInherit(GroupInherit(isNotCall = true)) {
								arg.print()
								return
							}
						}
					}
				}
			}
			
			val operatorName = getOperatorFromName(name)
			fun printOperator(type: Type = Type.specialOperator) {
				printSpace()
				print(operatorName, type)
				descriptor.printOrigin()
				printSpace()
			}
			
			when(name) {
				// unary prefix
				"unaryPlus", "unaryMinus", "not" -> {
					printOperator()
					printCallReceiver(false)
				}
				// unary postfix
				"inc", "dec" -> {
					printCallReceiver(false)
					printOperator()
				}
				// invoke
				"invoke" -> {
					printCallReceiver(false)
					printCallValueArguments()
					descriptor.printOrigin()
				}
				// get indexer
				"get" -> {
					printCallReceiver(false)
					printCallValueArguments("[", "]", trailingLambdaCapable = false)
					descriptor.printOrigin()
				}
				// set indexer
				"set" -> {
					printCallReceiver(false)
					group("[", "]") {
						printCallValueArguments(function, valueArguments.dropLast(1))
					}
					print("=", Type.specialOperator)
					descriptor.printOrigin()
					valueArguments.last()!!.print()
				}
				// builtin static operators
				"greater", "less", "lessOrEqual", "greaterOrEqual", "EQEQ", "EQEQEQ", "ANDAND", "OROR" -> {
					getValueArgument(0)?.print()
					printOperator()
					getValueArgument(1)?.print()
				}
				"iterator", "hasNext", "next", "compareTo" -> {
					printCallReceiver()
					printOperator(Type.function)
					printCallValueArguments()
				}
				else -> {
					// component n
					if(name.startsWith("component")) {
						receiver?.printAsReceiver()
						printOperator(Type.function)
						parenGroup {}
					} else {
						// else binary
						printCallReceiver(false)
						printOperator()
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
			
			group {
				if(intAsBinary && receiver is IrConst<*>)
					mergeInherit(GroupInherit(printIntAsBinary = true))
				printCallReceiver(false)
			}
			printSpace() // ensure spaces around infix function
			print(name, Type.function)
			descriptor.printOrigin()
			printCallTypeArguments()
			printSpace()
			group {
				val argument = getValueArgument(0)!!
				if(intAsBinary && argument is IrConst<*>)
					mergeInherit(GroupInherit(printIntAsBinary = true))
				argument.print()
			}
			return
		}
		
		printCallReceiver()
		val prop = symbol.ownerOrNull?.correspondingPropertySymbol?.ownerOrNull
		
		when {
			prop != null -> {
				val propName = prop.name.asString()
				print(propName, Type.property)
				if(symbol.ownerOrNull == prop.setter) {
					print("=", Type.specialOperator)
					descriptor.printOrigin()
					getValueArgument(0)?.print()
				}
			}
			
			origin == IrStatementOrigin.EXCLEXCL -> {
				// expr!! -> irCall(-> kotlin.internal.ir/CHECK_NOT_NULL, valueArguments = [expr], origin = EXCLEXCL)
				valueArguments.single()!!.print()
				print("!!", Type.specialOperator)
			}
			
			origin == IrStatementOrigin.GET_PROPERTY && valueArgumentsCount == 0 -> {
				// here, some bridge: java getValue()
				val propName = function.name.asString()
				val realPropNamePascal = when {
					propName.startsWith("get") -> propName.drop(3)
					propName.startsWith("is") -> propName.drop(2)
					else -> {
						print(propName, Type.function)
						descriptor.printOrigin()
						printCallTypeArguments()
						printError("<- GET_PROPERTY without appropriate name: $propName")
						return
					}
				}
				print(realPropNamePascal.decapitalizeFirst(), Type.property)
			}
			origin == IrStatementOrigin.EQ -> {
				// here, some bridge: java setValue(...)
				val propName = function.name.asString()
				require(propName.startsWith("set"))
				print(propName.drop(3).decapitalizeFirst(), Type.property)
				print("=", Type.specialOperator)
				descriptor.printOrigin()
				valueArguments.single()!!.print()
			}
			else -> {
				print(function.name, Type.function)
				descriptor.printOrigin()
				printCallTypeArguments()
				printCallValueArguments()
			}
		}
		
		debug {
			check()
			
			val returnType = function.returnType
			if(returnType is IrSimpleType &&
				returnType.classifier is IrClassSymbol &&
				returnType.arguments.isEmpty()
			) {
				ensureTypeIs(returnType)
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
	
	override fun visitEnumConstructorCall(expression: IrEnumConstructorCall) = expr(expression) {
		print((expression.symbol.owner.parent as IrDeclarationWithName).name, Type.enumClass)
		expression.printCallTypeArguments() // ??
		if(expression.valueArgumentsCount != 0)
			expression.printCallValueArguments()
		
	}
	
	override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall) = expr(expression) {
		// normally, should be handled in advance in visitClassNew
		// this is the best here
		printKeyword("super")
		expression.printCallValueArguments()
		
		debug {
			expression.ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	override fun visitInstanceInitializerCall(expression: IrInstanceInitializerCall) = expr(expression) {
		unknown("<init>()")
		
		debug {
			expression.ensureTypeIs(irBuiltIns.unitType)
			expression.classSymbol.ensureBound(expression)
		}
	}
	
	fun IrConstructorCall.printCallAnnotation() {
		group(inherit = GroupInherit(isAnnotationCall = true)) {
			print()
		}
	}
	
	fun IrFunctionAccessExpression.printCallReceiver(printDot: Boolean = true) {
		val dispatchReceiver = dispatchReceiver
		val extensionReceiver = extensionReceiver
		val dispatchIsSpecial = dispatchReceiver.let {
			it is IrGetValue && it.symbol.descriptor.name.isSpecial
		}
		val extensionIsSpecial = extensionReceiver.let {
			it is IrGetValue && it.symbol.descriptor.name.isSpecial
		}
		
		fun IrExpression.doPrint() {
			if(printDot) printAsReceiver()
			else print()
		}
		
		when {
			dispatchReceiver != null && !dispatchIsSpecial -> dispatchReceiver.doPrint()
			extensionReceiver != null && !extensionIsSpecial -> extensionReceiver.doPrint()
		}
		
		debug {
			val function = symbol.bound.owner
			val expectedDispatch = function.dispatchReceiverParameter?.type
			val actualDispatch = dispatchReceiver?.type
			if(expectedDispatch == null) {
				if(actualDispatch != null) printError(" <- unexpected dispatchReceiver")
			} else {
				if(actualDispatch == null) printError(" <- missing dispatchReceiver")
				else if(!actualDispatch.checkSubtypeOf(expectedDispatch, irBuiltIns)) {
					printError(" <- dispatchReceiver type not match")
					parenGroup {
						expectedDispatch.print()
						none(" <== ")
						actualDispatch.print()
					}
				}
			}
			
			val expectedExtension = function.extensionReceiverParameter?.type
			val actualExtension = extensionReceiver?.type
			if(expectedExtension == null && actualExtension != null)
				printError(" <- unexpected extensionReceiver")
			if(expectedExtension != null && actualExtension == null)
				printError(" <- missing extensionReceiver")
			if(expectedExtension == null) {
				if(actualExtension != null) printError(" <- unexpected extensionReceiver")
			} else {
				if(actualExtension == null) printError(" <- missing extensionReceiver")
				else if(!actualExtension.checkSubtypeOf(expectedExtension, irBuiltIns)) {
					printError(" <- extensionReceiver type not match")
					parenGroup {
						expectedExtension.print()
						none(" <== ")
						actualExtension.print()
					}
				}
			}
		}
	}
	
	fun IrFunctionAccessExpression.printCallTypeArguments() {
		val needToExplicitTypeParameters = typeArguments.any { type ->
			// not actually, but just a tiny stub?
			type != null && symbol.descriptor.valueParameters.none { it.type == type.toKotlinType() }
		}
		
		if(needToExplicitTypeParameters) group("<", ">") {
			typeArguments.printJoin(",") { (it ?: TODO("hOI!!")).print() }
		}
	}
	
	fun IrFunctionAccessExpression.printCallValueArguments(prefix: CharSequence = "(", postfix: CharSequence = ")", trailingLambdaCapable: Boolean = true) {
		printCallValueArguments(symbol.bound.owner, valueArguments, prefix, postfix, trailingLambdaCapable)
	}
	
	fun printCallValueArguments(
		function: IrFunction, valueArguments: List<IrExpression?>,
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
		
		if(arguments.isNotEmpty() || trailingLambda == null) group(prefix, postfix) {
			var useParameterName = false
			arguments.withIndex()
				.filter { argument -> (argument.value != null).also { if(!it) useParameterName = true } }
				.printJoin(",") {
					val parameter = function.valueParameters[it.index]
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
							parameterType.classOrNull != null &&
							!argumentType.checkSubtypeOf(parameterType, irBuiltIns)) {
							errorScope()
							debug("<- type mismatch")
							parenGroup {
								parameter.type.print()
								debug(" <== ")
								argument.type.print()
							}
						}
					}
				}
		}
		
		debug {
			if(function.valueParameters.size != valueArguments.size) {
				errorScope()
				none(
					if(function.valueParameters.size > valueArguments.size) " <- valueParameters not provided:"
					else " <- too much valueArguments:"
				)
				none(function.dumpSrcHeadColored(context, config))
				none(" << ")
				none(valueArguments.size.toString())
			} else {
				val notProvided = function.valueParameters.filterIndexed { i, parameter ->
					parameter.defaultValue == null && valueArguments[i] == null
				}
				
				if(notProvided.isNotEmpty()) {
					errorScope()
					none("<- no argument: ")
					
					parenGroup {
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
	
	fun intAsBinaryString(value: Int): String {
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
	
	override fun visitLocalDelegatedPropertyReference(
		expression: IrLocalDelegatedPropertyReference
	): Unit = expr(expression) {
		dispatchReceiver?.print()
		print("::", Type.importantSeparator)
		print(symbol.descriptor.name, Type.localVariable)
		debug { expression.check() }
	}
	
	override fun visitFunctionReference(expression: IrFunctionReference): Unit = expr(expression) {
		dispatchReceiver?.print()
		print("::", Type.importantSeparator)
		print(symbol.descriptor.name, Type.function)
		symbol.descriptor.printOrigin()
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
//		val superQualifiedSymbol = superQualifierSymbol
		
		receiver?.printAsReceiver()
		
		print(symbol.descriptor.name, Type.property)
		symbol.descriptor.printOrigin()
		
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
		symbol.descriptor.printOrigin()
		print("=", Type.specialOperator)
		value.print()
		
		debug {
			check()
			ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	
	override fun visitGetEnumValue(expression: IrGetEnumValue) = expr(expression) {
		val descriptor = symbol.descriptor
		print(descriptor.containingDeclaration.name, Type.enumClass) // EnumClass
		printSeparator(".") // .
		print(descriptor.name, Type.identifier) // ENUM_VALUE
		debug { expression.check() }
	}
	
	override fun visitGetObjectValue(expression: IrGetObjectValue) = expr(expression) {
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
			val parent = expression.symbol.bound.owner.parent
			if(parent is IrFunction && parent != currentFunction) {
				// in case of lambda argument, its name is the name of the callee function:
				// a.run { b.apply { this@run } }
				if(parent.origin == IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA) {
					val psi = parent.descriptor.findPsi()
					val name =
						((((psi?.parent?.parent as? KtLambdaArgument)?.parent) as? KtCallExpression)?.calleeExpression as? KtNameReferenceExpression)?.getReferencedName()
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
		
		
		group {
			// highlight closure
			val parent = symbol.bound.owner.parent
			val currentFunction = currentFunctionOrNull?.irElement
			if(currentFunction != null && parent != currentFunction)
				stylePrefix(Ansi.italic)
			
			print(symbol.descriptor.name, symbol.type)
			symbol.descriptor.printOrigin()
		}
		
		debug {
			check()
			ensureTypeIs(symbol.run { if(isBound) owner.type else descriptor.type.toIrType() })
		}
	}
	
	override fun visitSetValue(expression: IrSetValue) = expr(expression) {
		print(symbol.descriptor.name, symbol.type)
		symbol.descriptor.printOrigin()
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
					group {
						item.print()
					}
				}
				else -> group("\${", "}") {
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
		if(returnTargetSymbol.descriptor.name.asString() != "<anonymous>" || label != null) {
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
					parenGroup {
						type.print()
					}
				}
				
				if(function.returnType != returnType) {
					printError(" <- [ir] returnType of the function does not match with the type")
					parenGroup {
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
		if(currentInherit.isAnnotationCall) group("[", "]") {
			expression.elements.printJoin(",")
		}
		else group {
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
		// suspend information is not visible for source code
		debug { printBlockComment("suspend") }
		expression.result.print() // TODO
	}
	
	override fun visitSuspensionPoint(expression: IrSuspensionPoint) {
		// suspend information is not visible for source code
		debug { printBlockComment("suspend") }
		expression.result.print() // TODO
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
							parenGroup {
								branch.condition.print()
							}
						}
						isElse ->
							printKeyword("else")
						
						else -> {
							printKeyword("else if")
							parenGroup {
								branch.condition.print()
							}
						}
					}
					
					if(singleLine)
						branch.result.print()
					else bracedBlockCode {
						branch.result.print()
					}
				}
			}
			else -> {
				// stay updated with IrStatementOrigin.WHEN branch of visitBlock
				
				printKeyword("when")
				bracedBlockCode {
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
						if(result.braceNeeded) bracedBlockCode {
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
	override fun visitBreak(jump: IrBreak) = expr(jump) {
		printKeyword("break")
		jump.label?.let {
			print("@", Type.label)
			print(it, Type.label)
		}
		
		debug {
			jump.check()
			jump.ensureTypeIs(irBuiltIns.nothingType)
		}
	}
	
	override fun visitContinue(jump: IrContinue) = expr(jump) {
		printKeyword("continue")
		jump.label?.let {
			print("@", Type.label)
			print(it, Type.label)
		}
		
		debug {
			jump.check()
			jump.ensureTypeIs(irBuiltIns.nothingType)
		}
	}
	
	override fun visitWhileLoop(loop: IrWhileLoop) = expr(loop) {
		loop.label?.let {
			print("@", Type.label)
			print(it, Type.label)
		}
		printKeyword("while")
		parenGroup {
			loop.condition.print()
		}
		bracedBlockCode {
			loop.body?.print()
		}
		
		debug {
			loop.check()
			loop.ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	override fun visitDoWhileLoop(loop: IrDoWhileLoop) = expr(loop) {
		printKeyword("do")
		
		bracedBlockCode {
			loop.body?.print()
		}
		printKeyword("while")
		parenGroup {
			loop.condition.print()
		}
		
		debug {
			loop.check()
			loop.ensureTypeIs(irBuiltIns.unitType)
		}
	}
	
	/// try/catch
	
	override fun visitTry(aTry: IrTry) = expr(aTry) {
		printKeyword("try")
		bracedBlockCode {
			aTry.tryResult.print()
		}
		if(aTry.catches.isNotEmpty()) {
			aTry.catches.printJoin("")
		}
		aTry.finallyExpression?.let {
			printKeyword("finally")
			bracedBlockCode {
				it.print()
			}
		}
		
		debug {
			aTry.check()
			if(aTry.catches.isEmpty() && aTry.finallyExpression == null)
				printError(" <- unnecessary try")
		}
	}
	
	override fun visitCatch(aCatch: IrCatch) = element(aCatch) {
		printKeyword("catch")
		parenGroup { catchParameter.name.print(Type.valueParameter) }
		
		bracedBlockCode {
			result.print()
		}
		
		debug { aCatch.check() }
	}
	
	override fun visitThrow(expression: IrThrow) = expr(expression) {
		printKeyword("throw")
		expression.value.print()
		
		debug {
			expression.check()
			expression.ensureTypeIs(irBuiltIns.nothingType)
		}
	}
	
	/// others
	
	val IrBranch.isElse get() = this is IrElseBranch || condition.let { it is IrConst<*> && it.value == true }
	
	val IrMemberAccessExpression<*>.anyReceiver get() = dispatchReceiver ?: extensionReceiver
	
	// containers
	override fun visitBlock(expression: IrBlock): Unit = expr(expression) {
		fun printDefault() {
			unknown("irBlock")
			if(origin != null) parenGroup(type = Type.unknown) {
				unknown("origin = $origin")
			}
			
			bracedBlockCode {
				statements.printJoin(statementGap)
			}
		}
		
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
					val call = rhs.branches.last().result as IrCall
					lhs.initializer?.print()
					print("?.", Type.specialOperator)
					// handle property(though there is only get, not set)
					val prop = call.symbol.ownerOrNull?.correspondingPropertySymbol?.ownerOrNull
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
					
					parenGroup {
						if(variable.origin == IrDeclarationOrigin.IR_TEMPORARY_VARIABLE) // when(...)
							variable.initializer!!.print()
						else variable.print() // when(val NAME = ...)
					}
					
					bracedBlockCode {
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
							if(result.braceNeeded) bracedBlockCode {
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
					
					whileLoop.label?.let {
						print(it, Type.label)
						print("@", Type.label)
					}
					
					printKeyword("for")
					parenGroup {
						if(destructionDeclarations.isEmpty())
							print(looperDeclaration.name, Type.localVariable)
						else parenGroup { // TODO: support val (_ <- this, name..) = ...
							destructionDeclarations.printJoin(",") {
								print(
									(it as IrVariable).name,
									Type.localVariable
								)
							}
						}
						printKeyword("in")
						(iterator.initializer as IrCall).let {
							it.dispatchReceiver ?: it.extensionReceiver ?: error("no expression")
						}.print()
					}
					
					bracedBlockCode {
						original.print()
					}
				}
				
				else -> if(origin != null && debug) printDefault() else {
					if(this is IrReturnableBlock) {
						val labelName = "label${labels.size}"
						labels[symbol] = labelName
						print("run", Type.specialOperator)
						debug { debug("(<- IrReturnableBlock)") }
						print("@$labelName", Type.label)
						bracedBlockCode {
							statements.printJoin(statementGap)
						}
					} else statements.printJoin(statementGap)
				}
			}
		} catch(e: Throwable) { // by default, these values with these origins are expected, but some lowerings may change them
			printDefault()
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
						if(expression.returnTargetSymbol != this@printAsLambda.symbol)
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
		
		group(prefix = {
			print("{", Type.braces)
			printSpace()
			val parameters = valueParameters
			if(parameters.isNotEmpty() && parameters.singleOrNull()?.name?.toString() != "it") {
				val destructuringParameters = mutableMapOf<Int, MutableMap<Int, IrValueDeclaration>>()
				if(statements != null) while(true) { // quite complex logic finding for parameter destructuring
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
				parameters.printJoin(",") { parameter ->
					val destructuring = destructuringParameters[parameter.index]
					if(destructuring == null) parameter.print()
					else parenGroup {
						(0..destructuring.keys.maxOrNull()!!).printJoin(",") {
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
		}, addIndent = true, newLine = statementNewLine) {
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
			bracedBlockBody {
				printableDeclarations.printJoin(declarationGap)
			}
		}
	}
	
	
	override fun visitComposite(expression: IrComposite) { // generally added by
		if(expression.origin != null && debug)
			unknown("irComposite(origin = ${expression.origin})")
		else unknown("irComposite")
		
		bracedBlockCode {
			expression.statements.printJoin(statementGap)
		}
		
		debug { expression.check() }
	}
	
	// dynamic
	
	override fun visitDynamicMemberExpression(expression: IrDynamicMemberExpression) = expr(expression) {
		receiver.print()
		printSeparator(".")
		print(memberName, Type.dynamicMember)
		
		debug { check() }
	}
	
	override fun visitDynamicOperatorExpression(expression: IrDynamicOperatorExpression): Unit = expr(expression) {
		when(operator) {
			IrDynamicOperator.INVOKE -> {
				groupExpr {
					receiver.print()
				}
				parenGroup {
					arguments.printJoin(",")
				}
			}
			IrDynamicOperator.ARRAY_ACCESS -> {
				groupExpr {
					receiver.print()
				}
				group("[", "]") {
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
