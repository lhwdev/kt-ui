/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.SourceManager
import org.jetbrains.kotlin.ir.backend.js.JsIrBackendContext.Companion.KOTLIN_PACKAGE_FQN
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol
import org.jetbrains.kotlin.ir.symbols.IrVariableSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.renderer.DescriptorRenderer
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.Variance
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly
import org.jetbrains.kotlin.utils.Printer
import org.jetbrains.kotlin.utils.addIfNotNull
import java.util.Locale


private const val sReset = ConsoleColors.RESET
private const val sName = ConsoleColors.RESET + ConsoleColors.BOLD
private const val sIdentifier = ConsoleColors.BLUE
private const val sIdentifierDimmed = ConsoleColors.BLUE_BRIGHT
private const val sProperty = ConsoleColors.RED_BRIGHT
private const val sProperty2 = ConsoleColors.RED_BRIGHT
private const val sType = ConsoleColors.WHITE_BRIGHT
private const val sUnboundType = ConsoleColors.WHITE_BRIGHT
private const val sNumber = ConsoleColors.RED_BRIGHT
private const val sModality = ConsoleColors.PURPLE
private const val sVisibility = ConsoleColors.PURPLE
private const val sReference = ConsoleColors.GREEN_BRIGHT + "-> " + sReset
private const val sReferenceColor = sReset
private const val sDimmed = ConsoleColors.WHITE


// a little bit copied from RenderIrElement.kt
fun IrType.renderReadable() =
	"${renderTypeAnnotations(annotations)}${renderTypeInner()}".replace(sReset, sReset + sType) + sReset

fun DeclarationDescriptor.dump(): String = DescriptorRenderer.DEBUG_TEXT.render(this)

fun DeclarationDescriptor.dumpAsType() = DescriptorRenderer.ONLY_NAMES_WITH_SHORT_TYPES.render(this)

private fun IrType.renderTypeInner(): String = when(this) {
	is IrDynamicType -> "dynamic"
	is IrErrorType -> "IrErrorType"
	is IrSimpleType -> buildTrimEnd {
		append(classifier.renderClassifierFqn())
		if(arguments.isNotEmpty()) {
			append(
				arguments.joinToString(prefix = "$sDimmed<$sReset", postfix = "$sDimmed>$sReset", separator = ", ") {
					it.renderTypeArgument()
				}
			)
		}
		if(hasQuestionMark) {
			append('?')
		}
		abbreviation?.let {
			append(it.renderTypeAbbreviation())
		}
	}
	
	else -> "{${javaClass.simpleName} $this}"
}


private fun IrTypeArgument.renderTypeArgument() = when(this) {
	is IrStarProjection -> "*"
	
	is IrTypeProjection -> buildTrimEnd {
		append(sIdentifierDimmed)
		append(variance.label)
		append(sReset)
		if(variance != Variance.INVARIANT) append(' ')
		append(type.renderReadable())
	}
	
	else -> "IrTypeArgument[$this]"
}

private fun IrTypeAbbreviation.renderTypeAbbreviation() = buildString {
	append("{ ")
	append(renderTypeAnnotations(annotations))
	append(typeAlias.renderTypeAliasFqn())
	if(arguments.isNotEmpty()) {
		append(
			arguments.joinToString(prefix = "$sDimmed<$sReset", postfix = "$sDimmed>$sReset", separator = ", ") {
				it.renderTypeArgument()
			}
		)
	}
	if(hasQuestionMark) {
		append('?')
	}
	append(" }")
}


private inline fun buildTrimEnd(fn: StringBuilder.() -> Unit): String =
	buildString(fn).trimEnd()

private fun renderTypeAnnotations(annotations: List<IrConstructorCall>) = when {
	annotations.isEmpty() -> ""
	try {
		annotations.singleOrNull()?.symbol?.descriptor?.constructedClass?.fqNameSafe == KOTLIN_PACKAGE_FQN.child(
			Name.identifier(
				"ParameterName"
			)
		)
	} catch(e: Throwable) {
		false
	} ->
		(annotations.single().getValueArgument(0) as IrConst<*>).value.toString().let { "$it: " }
	else -> annotations.joinToString(
		prefix = "",
		postfix = " ",
		separator = " "
	) { "$sDimmed@[$sReset${renderAsAnnotation(it)}$sDimmed]$sReset" }
}

private fun renderAsAnnotation(irAnnotation: IrConstructorCall) = buildString {
	val annotationClassName = try {
		irAnnotation.symbol.owner.parentAsClass.name.asString()
	} catch(e: Exception) {
		"$sDimmed${irAnnotation.symbol.descriptor.dump()}$sReset"
	}
	append(annotationClassName)
	
	if(irAnnotation.valueArgumentsCount != 0) {
		val valueParameterNames = irAnnotation.getValueParameterNamesForDebug()
		var first = true
		append("(")
		for(i in 0 until irAnnotation.valueArgumentsCount) {
			if(first) {
				first = false
			} else {
				append(", ")
			}
			append(valueParameterNames[i])
			append(" = ")
			renderAsAnnotationArgument(irAnnotation.getValueArgument(i))
		}
		append(")")
	}
}

private fun StringBuilder.renderAsAnnotationArgument(irElement: IrElement?) {
	when(irElement) {
		null -> append("<null>")
		is IrConstructorCall -> renderAsAnnotation(irElement)
		is IrConst<*> -> {
			append('\'')
			append(irElement.value.toString())
			append('\'')
		}
		is IrVararg -> {
			appendListWith(irElement.elements, "[", "]", ", ") {
				renderAsAnnotationArgument(it)
			}
		}
		else -> append(irElement.dumpColored())
	}
}


// from DumpIrTree.kt
fun IrElement.dumpColored(normalizeNames: Boolean = false): String =
	try {
		StringBuilder().also { sb ->
			accept(DumpIrTreeVisitor(sb, normalizeNames), "")
			sb.setLength(sb.length - 1)
		}.toString()
	} catch(e: Exception) {
		"(Full dump is not available: ${e.message})\n" + render()
	}

fun IrElement.logDumpColored(baseColor: String = ConsoleColors.RESET, normalizeNames: Boolean = false) =
	fixIndents {
		try {
			accept(DumpIrTreeVisitor(object : Appendable {
				override fun append(csq: CharSequence) = append(csq, 0, csq.length)
				
				override fun append(csq: CharSequence, start: Int, end: Int): Appendable {
//				print("O${end - start}")
					csq.substring(start, end)
						.let { if(baseColor != ConsoleColors.RESET) it.replace(ConsoleColors.RESET, baseColor) else it }
						.let { logInternalWithoutNewline(it, color = baseColor) }
					return this
				}
				
				override fun append(c: Char) = append("$c")
			}, normalizeNames), "")
		} catch(e: Exception) {
			"(Full dump is not available: ${e.message})\n" + render()
		}
	}

fun IrFile.dumpTreesFromLineNumber(lineNumber: Int, normalizeNames: Boolean = false): String {
	val sb = StringBuilder()
	accept(DumpTreeFromSourceLineVisitor(fileEntry, lineNumber, sb, normalizeNames), null)
	return sb.toString()
}

private class DumpIrTreeVisitor(
	out: Appendable,
	normalizeNames: Boolean = false
) : IrElementVisitor<Unit, String> {
	
	private val printer = Printer(out, "  ")
	private val elementRenderer = RenderIrElementVisitor(normalizeNames)
	
	override fun visitElement(element: IrElement, data: String) {
		element.dumpLabeledElementWith(data) {
			if(element is IrAnnotationContainer) {
				dumpAnnotations(element)
			}
			element.acceptChildren(this@DumpIrTreeVisitor, "")
		}
	}
	
	override fun visitModuleFragment(declaration: IrModuleFragment, data: String) {
		declaration.dumpLabeledElementWith(data) {
			declaration.files.dumpElements()
		}
	}
	
	override fun visitFile(declaration: IrFile, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.declarations.dumpElements()
		}
	}
	
	override fun visitClass(declaration: IrClass, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.thisReceiver?.accept(this, "\$this")
			declaration.typeParameters.dumpElements()
			declaration.declarations.dumpElements()
		}
	}
	
	override fun visitTypeAlias(declaration: IrTypeAlias, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.typeParameters.dumpElements()
		}
	}
	
	override fun visitTypeParameter(declaration: IrTypeParameter, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
		}
	}
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.correspondingPropertySymbol?.dumpInternal("correspondingProperty")
			declaration.overriddenSymbols.dumpItems<IrSymbol>("overridden") {
				it.dump()
			}
			declaration.typeParameters.dumpElements()
			declaration.dispatchReceiverParameter?.accept(this, "\$this")
			declaration.extensionReceiverParameter?.accept(this, "\$receiver")
			declaration.valueParameters.dumpElements()
			declaration.body?.accept(this, "")
		}
	}
	
	private fun dumpAnnotations(element: IrAnnotationContainer) {
		element.annotations.dumpItems("annotations") { irAnnotation: IrConstructorCall ->
			printer.println(elementRenderer.renderAsAnnotation(irAnnotation))
		}
	}
	
	private fun IrSymbol.dump(label: String? = null) =
		printer.println(
			elementRenderer.renderSymbolReference(this).let {
				if(label != null) "$sProperty2$label:$sReset $it" else it
			}
		)
	
	override fun visitConstructor(declaration: IrConstructor, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.typeParameters.dumpElements()
			declaration.dispatchReceiverParameter?.accept(this, "\$outer")
			declaration.valueParameters.dumpElements()
			declaration.body?.accept(this, "")
		}
	}
	
	override fun visitProperty(declaration: IrProperty, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.backingField?.accept(this, "")
			declaration.getter?.accept(this, "")
			declaration.setter?.accept(this, "")
		}
	}
	
	override fun visitField(declaration: IrField, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.overriddenSymbols.dumpItems("overridden") {
				it.dump()
			}
			declaration.initializer?.accept(this, "")
		}
	}
	
	private fun List<IrElement>.dumpElements() {
		forEach { it.accept(this@DumpIrTreeVisitor, "") }
	}
	
	override fun visitErrorCallExpression(expression: IrErrorCallExpression, data: String) {
		expression.dumpLabeledElementWith(data) {
			expression.explicitReceiver?.accept(this, "receiver")
			expression.arguments.dumpElements()
		}
	}
	
	override fun visitEnumEntry(declaration: IrEnumEntry, data: String) {
		declaration.dumpLabeledElementWith(data) {
			dumpAnnotations(declaration)
			declaration.initializerExpression?.accept(this, "init")
			declaration.correspondingClass?.accept(this, "class")
		}
	}
	
	override fun visitMemberAccess(expression: IrMemberAccessExpression, data: String) {
		expression.dumpLabeledElementWith(data) {
			dumpTypeArguments(expression)
			expression.dispatchReceiver?.accept(this, "\$this")
			expression.extensionReceiver?.accept(this, "\$receiver")
			val valueParameterNames = expression.getValueParameterNamesForDebug()
			for(index in 0 until expression.valueArgumentsCount) {
				expression.getValueArgument(index)?.accept(this, valueParameterNames[index])
			}
		}
	}
	
	override fun visitConstructorCall(expression: IrConstructorCall, data: String) {
		expression.dumpLabeledElementWith(data) {
			dumpTypeArguments(expression)
			expression.outerClassReceiver?.accept(this, "\$outer")
			dumpConstructorValueArguments(expression)
		}
	}
	
	private fun dumpConstructorValueArguments(expression: IrConstructorCall) {
		val valueParameterNames = expression.getValueParameterNamesForDebug()
		for(index in 0 until expression.valueArgumentsCount) {
			expression.getValueArgument(index)?.accept(this, valueParameterNames[index])
		}
	}
	
	private fun dumpTypeArguments(expression: IrMemberAccessExpression) {
		val typeParameterNames = expression.getTypeParameterNames(expression.typeArgumentsCount)
		for(index in 0 until expression.typeArgumentsCount) {
			printer.println("<${typeParameterNames[index]}>: ${expression.renderTypeArgument(index)}")
		}
	}
	
	private fun dumpTypeArguments(expression: IrConstructorCall) {
		val typeParameterNames = expression.getTypeParameterNames(expression.typeArgumentsCount)
		for(index in 0 until expression.typeArgumentsCount) {
			val typeParameterName = typeParameterNames[index]
			val parameterLabel =
				if(index < expression.classTypeArgumentsCount)
					"${sProperty2}class:$sReset $typeParameterName"
				else
					typeParameterName
			printer.println("<$parameterLabel>: ${expression.renderTypeArgument(index)}")
		}
	}
	
	private fun IrMemberAccessExpression.getTypeParameterNames(expectedCount: Int): List<String> =
		if(this is IrDeclarationReference && symbol.isBound)
			symbol.owner.getTypeParameterNames(expectedCount)
		else
			getPlaceholderParameterNames(expectedCount)
	
	private fun IrSymbolOwner.getTypeParameterNames(expectedCount: Int): List<String> =
		if(this is IrTypeParametersContainer) {
			val typeParameters = if(this is IrConstructor) getFullTypeParametersList() else this.typeParameters
			(0 until expectedCount).map {
				if(it < typeParameters.size)
					typeParameters[it].name.asString()
				else
					"${it + 1}"
			}
		} else {
			getPlaceholderParameterNames(expectedCount)
		}
	
	private fun IrConstructor.getFullTypeParametersList(): List<IrTypeParameter> {
		val parentClass = try {
			parent as? IrClass ?: return typeParameters
		} catch(e: Exception) {
			return typeParameters
		}
		return parentClass.typeParameters + typeParameters
	}
	
	private fun IrMemberAccessExpression.renderTypeArgument(index: Int): String =
		getTypeArgument(index)?.renderReadable() ?: "<none>"
	
	override fun visitGetField(expression: IrGetField, data: String) {
		expression.dumpLabeledElementWith(data) {
			expression.receiver?.accept(this, "receiver")
		}
	}
	
	override fun visitSetField(expression: IrSetField, data: String) {
		expression.dumpLabeledElementWith(data) {
			expression.receiver?.accept(this, "receiver")
			expression.value.accept(this, "value")
		}
	}
	
	override fun visitWhen(expression: IrWhen, data: String) {
		expression.dumpLabeledElementWith(data) {
			expression.branches.dumpElements()
		}
	}
	
	override fun visitBranch(branch: IrBranch, data: String) {
		branch.dumpLabeledElementWith(data) {
			branch.condition.accept(this, "if")
			branch.result.accept(this, "then")
		}
	}
	
	override fun visitWhileLoop(loop: IrWhileLoop, data: String) {
		loop.dumpLabeledElementWith(data) {
			loop.condition.accept(this, "condition")
			loop.body?.accept(this, "body")
		}
	}
	
	override fun visitDoWhileLoop(loop: IrDoWhileLoop, data: String) {
		loop.dumpLabeledElementWith(data) {
			loop.body?.accept(this, "body")
			loop.condition.accept(this, "condition")
		}
	}
	
	override fun visitTry(aTry: IrTry, data: String) {
		aTry.dumpLabeledElementWith(data) {
			aTry.tryResult.accept(this, "try")
			aTry.catches.dumpElements()
			aTry.finallyExpression?.accept(this, "finally")
		}
	}
	
	override fun visitTypeOperator(expression: IrTypeOperatorCall, data: String) {
		expression.dumpLabeledElementWith(data) {
			expression.acceptChildren(this, "")
		}
	}
	
	override fun visitDynamicOperatorExpression(expression: IrDynamicOperatorExpression, data: String) {
		expression.dumpLabeledElementWith(data) {
			expression.receiver.accept(this, "receiver")
			for((i, arg) in expression.arguments.withIndex()) {
				arg.accept(this, i.toString())
			}
		}
	}
	
	private inline fun IrElement.dumpLabeledElementWith(label: String, body: () -> Unit) {
		printer.println(accept(elementRenderer, null).withLabel(label))
		indented(body)
	}
	
	private inline fun <T> Collection<T>.dumpItems(caption: String, renderElement: (T) -> Unit) {
		if(isEmpty()) return
		indented(caption) {
			forEach {
				renderElement(it)
			}
		}
	}
	
	private fun IrSymbol.dumpInternal(label: String? = null) {
		if(isBound)
			owner.dumpInternal(label)
		else
			printer.println("$sProperty2$label:$sReset ${sIdentifier}unbound$sReset ${javaClass.simpleName}")
	}
	
	private fun IrElement.dumpInternal(label: String? = null) {
		if(label != null) {
			printer.println("$sProperty2$label:$sReset ", accept(elementRenderer, null))
		} else {
			printer.println(accept(elementRenderer, null))
		}
		
	}
	
	private inline fun indented(label: String, body: () -> Unit) {
		printer.println("$sProperty2$label:$sReset")
		indented(body)
	}
	
	private inline fun indented(body: () -> Unit) {
		printer.pushIndent()
		body()
		printer.popIndent()
	}
	
	private fun String.withLabel(label: String) =
		if(label.isEmpty()) this else "$sProperty2$label:$sReset $this"
}

private class DumpTreeFromSourceLineVisitor(
	val fileEntry: SourceManager.FileEntry,
	private val lineNumber: Int,
	out: Appendable,
	normalizeNames: Boolean = false
) : IrElementVisitorVoid {
	private val dumper = DumpIrTreeVisitor(out, normalizeNames)
	
	override fun visitElement(element: IrElement) {
		if(fileEntry.getLineNumber(element.startOffset) == lineNumber) {
			element.accept(dumper, "")
			return
		}
		
		element.acceptChildrenVoid(this)
	}
}

private fun IrMemberAccessExpression.getValueParameterNamesForDebug(): List<String> {
	val expectedCount = valueArgumentsCount
	if(symbol.isBound) {
		val owner = symbol.owner
		if(owner is IrFunction) {
			return (0 until expectedCount).map {
				if(it < owner.valueParameters.size)
					owner.valueParameters[it].name.asString()
				else
					"${it + 1}"
			}
		}
	}
	return getPlaceholderParameterNames(expectedCount)
}

private fun getPlaceholderParameterNames(expectedCount: Int) =
	(1..expectedCount).map { "$it" }


// from RenderIrElement.kt

private fun IrElement.renderColored() =
	accept(RenderIrElementVisitor(), null)

private class RenderIrElementVisitor(private val normalizeNames: Boolean = false) : IrElementVisitor<String, Nothing?> {
	private val nameMap: MutableMap<IrVariableSymbol, String> = mutableMapOf()
	private var temporaryIndex: Int = 0
	
	private val IrVariable.normalizedName: String
		get() {
			if(!normalizeNames || (origin != IrDeclarationOrigin.IR_TEMPORARY_VARIABLE && origin != IrDeclarationOrigin.FOR_LOOP_ITERATOR))
				return name.asString()
			
			return nameMap.getOrPut(symbol) { "tmp_${temporaryIndex++}" }
		}
	
	
	fun renderSymbolReference(symbol: IrSymbol) = symbol.renderReference()
	
	fun renderAsAnnotation(irAnnotation: IrConstructorCall): String =
		StringBuilder().also { it.renderAsAnnotation(irAnnotation) }.toString()
	
	private fun StringBuilder.renderAsAnnotation(irAnnotation: IrConstructorCall) {
		append('@') // MODIFIED
		val annotationClassName = try {
			irAnnotation.symbol.owner.parentAsClass.name.asString()
		} catch(e: Exception) {
			"${sDimmed}unbound$sReset"
		}
		append(annotationClassName)
		
		if(irAnnotation.valueArgumentsCount == 0) return
		
		val valueParameterNames = irAnnotation.getValueParameterNamesForDebug()
		var first = true
		append("(")
		for(i in 0 until irAnnotation.valueArgumentsCount) {
			if(first) {
				first = false
			} else {
				append(", ")
			}
			append(valueParameterNames[i])
			append(" = ")
			renderAsAnnotationArgument(irAnnotation.getValueArgument(i))
		}
		append(")")
	}
	
	private fun StringBuilder.renderAsAnnotationArgument(irElement: IrElement?) {
		when(irElement) {
			null -> append("<null>")
			is IrConstructorCall -> renderAsAnnotation(irElement)
			is IrConst<*> -> {
				append('\'')
				append(irElement.value.toString())
				append('\'')
			}
			is IrVararg -> {
				appendListWith(irElement.elements, "[", "]", ", ") {
					renderAsAnnotationArgument(it)
				}
			}
			else -> append(irElement.accept(this@RenderIrElementVisitor, null))
		}
	}
	
	private inline fun buildTrimEnd(fn: StringBuilder.() -> Unit): String =
		buildString(fn).trimEnd()
	
	private inline fun <T> T.runTrimEnd(fn: T.() -> String): String =
		run(fn).trimEnd()
	
	private fun IrType.render() = renderReadable()
	
	private fun IrType.renderTypeInner() =
		when(this) {
			is IrDynamicType -> "dynamic"
			
			is IrErrorType -> "IrErrorType"
			
			is IrSimpleType -> buildTrimEnd {
				append(classifier.renderClassifierFqn())
				if(arguments.isNotEmpty()) {
					append(
						arguments.joinToString(prefix = "<", postfix = ">", separator = ", ") {
							it.renderTypeArgument()
						}
					)
				}
				if(hasQuestionMark) {
					append('?')
				}
				abbreviation?.let {
					append(it.renderTypeAbbreviation())
				}
			}
			
			else -> "{${javaClass.simpleName} $this}"
		}
	
	private fun IrTypeAbbreviation.renderTypeAbbreviation(): String =
		buildString {
			append("{ ")
			append(renderTypeAnnotations(annotations))
			append(typeAlias.renderTypeAliasFqn())
			if(arguments.isNotEmpty()) {
				append(
					arguments.joinToString(prefix = "<", postfix = ">", separator = ", ") {
						it.renderTypeArgument()
					}
				)
			}
			if(hasQuestionMark) {
				append('?')
			}
			append(" }")
		}
	
	private fun IrTypeArgument.renderTypeArgument(): String =
		when(this) {
			is IrStarProjection -> "*"
			
			is IrTypeProjection -> buildTrimEnd {
				append(variance.label)
				if(variance != Variance.INVARIANT) append(' ')
				append(type.render())
			}
			
			else -> "IrTypeArgument[$this]"
		}
	
	
	private fun renderTypeAnnotations(annotations: List<IrConstructorCall>) =
		if(annotations.isEmpty())
			""
		else
			annotations.joinToString(prefix = "", postfix = " ", separator = " ") { "@[${renderAsAnnotation(it)}]" }
	
	private fun IrSymbol.renderReference() =
		sReference +
			(if(isBound)
				owner.accept(symbolReferenceRenderer, null)
					.replace(ConsoleColors.RESET, ConsoleColors.RESET + sReferenceColor)
			else
				"${sDimmed}unbound$sReset ${descriptor.dumpAsType()}") +
			sReset
	
	private val symbolReferenceRenderer = BoundSymbolReferenceRenderer()
	
	private inner class BoundSymbolReferenceRenderer :
		IrElementVisitor<String, Nothing?> {
		
		override fun visitElement(element: IrElement, data: Nothing?) =
			element.accept(this@RenderIrElementVisitor, null)
		
		override fun visitVariable(declaration: IrVariable, data: Nothing?) =
			buildTrimEnd {
				if(declaration.isVar) append("var ") else append("val ")
				
				append(sProperty2)
				append(declaration.normalizedName)
				append(":$sReset ")
				append(declaration.type.renderReadable())
				append(' ')
				
				append(declaration.renderVariableFlags())
				
				renderDeclaredIn(declaration)
			}
		
		override fun visitValueParameter(declaration: IrValueParameter, data: Nothing?) =
			buildTrimEnd {
				append(sProperty2)
				append(declaration.name.asString())
				append(":$sReset ")
				append(declaration.type.renderReadable())
				append(' ')
				
				append(declaration.renderValueParameterFlags())
				
				renderDeclaredIn(declaration)
			}
		
		override fun visitFunction(declaration: IrFunction, data: Nothing?) =
			buildTrimEnd {
				append(declaration.visibility)
				append(' ')
				
				if(declaration is IrSimpleFunction) {
					append(declaration.modality.toString().toLowerCaseAsciiOnly())
					append(' ')
				}
				
				when(declaration) {
					is IrSimpleFunction -> append("fun ")
					is IrConstructor -> append("constructor ")
					else -> append("{${declaration.javaClass.simpleName}}")
				}
				
				append(declaration.name.asString())
				append(' ')
				
				renderTypeParameters(declaration)
				
				appendListWith(declaration.valueParameters, "(", ")", ", ") { valueParameter ->
					val varargElementType = valueParameter.varargElementType
					if(varargElementType != null) {
						append("vararg ")
						append(valueParameter.name.asString())
						append(": ")
						append(varargElementType.render())
					} else {
						append(valueParameter.name.asString())
						append(": ")
						append(valueParameter.type.renderReadable())
					}
				}
				
				if(declaration is IrSimpleFunction) {
					append(": ")
					append(declaration.returnType.render())
				}
				append(' ')
				
				when(declaration) {
					is IrSimpleFunction -> append(declaration.renderSimpleFunctionFlags())
					is IrConstructor -> append(declaration.renderConstructorFlags())
				}
				
				renderDeclaredIn(declaration)
			}
		
		private fun StringBuilder.renderTypeParameters(declaration: IrTypeParametersContainer) {
			if(declaration.typeParameters.isNotEmpty()) {
				appendListWith(declaration.typeParameters, "<", ">", ", ") { typeParameter ->
					append(typeParameter.name.asString())
				}
				append(' ')
			}
		}
		
		override fun visitProperty(declaration: IrProperty, data: Nothing?) =
			buildTrimEnd {
				append(declaration.visibility)
				append(' ')
				append(declaration.modality.toString().toLowerCaseAsciiOnly())
				append(' ')
				
				append(declaration.name.asString())
				
				val type = declaration.getter?.returnType ?: declaration.backingField?.type
				if(type != null) {
					append(": ")
					append(type.render())
				}
				
				append(' ')
				append(declaration.renderPropertyFlags())
			}
		
		override fun visitLocalDelegatedProperty(declaration: IrLocalDelegatedProperty, data: Nothing?): String =
			buildTrimEnd {
				if(declaration.isVar) append("var ") else append("val ")
				append(declaration.name.asString())
				append(": ")
				append(declaration.type.renderReadable())
				append(" by (...)")
			}
		
		private fun StringBuilder.renderDeclaredIn(irDeclaration: IrDeclaration) {
			append("declared in ")
			renderParentOfReferencedDeclaration(irDeclaration)
		}
		
		private fun StringBuilder.renderParentOfReferencedDeclaration(declaration: IrDeclaration) {
			val parent = try {
				declaration.parent
			} catch(e: Exception) {
				append("<no parent>")
				return
			}
			when(parent) {
				is IrPackageFragment -> {
					val fqn = parent.fqName.asString()
					append(if(fqn.isEmpty()) "<root>" else fqn)
				}
				is IrDeclaration -> {
					append(sDimmed)
					renderParentOfReferencedDeclaration(parent)
					append('.')
					append(sReset)
					if(parent is IrDeclarationWithName) {
						append(parent.name)
					} else {
						renderElementNameFallback(parent)
					}
				}
				else ->
					renderElementNameFallback(parent)
			}
		}
		
		private fun StringBuilder.renderElementNameFallback(element: Any) {
			append('{')
			append(element.javaClass.simpleName)
			append('}')
		}
	}
	
	override fun visitElement(element: IrElement, data: Nothing?): String =
		"?${sIdentifier}element$sReset? ${element::class.java.simpleName} $element"
	
	override fun visitDeclaration(declaration: IrDeclaration, data: Nothing?): String =
		"?${sIdentifier}declaration$sReset? ${declaration::class.java.simpleName} $declaration"
	
	override fun visitModuleFragment(declaration: IrModuleFragment, data: Nothing?): String =
		"${sIdentifier}module_fragment$sReset ${sProperty2}name:$sReset${declaration.name}"
	
	override fun visitExternalPackageFragment(declaration: IrExternalPackageFragment, data: Nothing?): String =
		"${sIdentifier}external_package_fragment$sReset ${sProperty2}fqName:$sReset${declaration.fqName}"
	
	override fun visitFile(declaration: IrFile, data: Nothing?): String =
		"${sIdentifier}file$sReset ${sProperty2}fqName:$sReset${declaration.fqName} ${sProperty2}fileName:$sReset${declaration.path}"
	
	override fun visitFunction(declaration: IrFunction, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}fun$sReset ${renderOriginIfNonTrivial()}"
		}
	
	override fun visitScript(declaration: IrScript, data: Nothing?) = "${sIdentifier}script$sReset"
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction, data: Nothing?): String =
		declaration.runTrimEnd {
			"$sIdentifierDimmed$visibility ${
				modality.toString().toLowerCase(Locale.getDefault())
			}$sReset ${sIdentifier}fun$sReset ${renderOriginIfNonTrivial()}$sName$name$sReset" +
				renderTypeParameters().let { if(it.isEmpty()) "" else "$it " } +
				renderValueParameterTypes() +
				": $sReset${returnType.render()} " +
				renderSimpleFunctionFlags()
		}
	
	private fun renderFlagsList(vararg flags: String?) =
		flags.filterNotNull().run {
			if(isNotEmpty())
				joinToString(prefix = "[", postfix = "] ", separator = ",")
			else
				""
		}
	
	private fun IrSimpleFunction.renderSimpleFunctionFlags(): String =
		renderFlagsList(
			"tailrec".takeIf { isTailrec },
			"inline".takeIf { isInline },
			"external".takeIf { isExternal },
			"suspend".takeIf { isSuspend },
			"expect".takeIf { isExpect },
			"fake_override".takeIf { isFakeOverride },
			"operator".takeIf { isOperator }
		)
	
	private fun IrFunction.renderTypeParameters() = if(typeParameters.isEmpty()) "" else
		typeParameters.joinToString(separator = ", ", prefix = "<", postfix = ">") { it.name.toString() }
	
	private fun IrFunction.renderValueParameterTypes(): String =
		ArrayList<String>().apply {
			addIfNotNull(dispatchReceiverParameter?.run { "\$${sProperty2}this: $sReset${type.renderReadable()}" })
			addIfNotNull(extensionReceiverParameter?.run { "\$${sProperty2}receiver: $sReset${type.renderReadable()}" })
			valueParameters.mapTo(this) { "${it.name}: ${it.type.renderReadable()}" }
		}.joinToString(separator = ", ", prefix = "(", postfix = ")")
	
	override fun visitConstructor(declaration: IrConstructor, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}constructor$sReset ${renderOriginIfNonTrivial()}" +
				"${sProperty2}visibility:$sVisibility$visibility$sReset " +
				renderTypeParameters() + " " +
				renderValueParameterTypes() + " " +
				"${sProperty2}returnType:$sReset${returnType.render()} " +
				renderConstructorFlags()
		}
	
	private fun IrConstructor.renderConstructorFlags() =
		renderFlagsList(
			"inline".takeIf { isInline },
			"external".takeIf { isExternal },
			"primary".takeIf { isPrimary },
			"expect".takeIf { isExpect }
		)
	
	override fun visitProperty(declaration: IrProperty, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}property$sReset ${renderOriginIfNonTrivial()}" +
				"${sProperty2}name:$sReset$name ${sProperty2}visibility:$sVisibility$visibility$sReset ${sProperty2}modality:$sModality$modality$sReset " +
				renderPropertyFlags()
		}
	
	private fun IrProperty.renderPropertyFlags() =
		renderFlagsList(
			"external".takeIf { isExternal },
			"const".takeIf { isConst },
			"lateinit".takeIf { isLateinit },
			"delegated".takeIf { isDelegated },
			"expect".takeIf { isExpect },
			"fake_override".takeIf { isFakeOverride },
			if(isVar) "var" else "val"
		)
	
	override fun visitField(declaration: IrField, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}field$sReset ${renderOriginIfNonTrivial()}${sProperty2}name:$sReset$name ${sProperty2}type:$sReset${type.render()} ${sProperty2}visibility:$sVisibility$visibility$sReset ${renderFieldFlags()}"
		}
	
	
	private fun IrField.renderFieldFlags() =
		renderFlagsList(
			"final".takeIf { isFinal },
			"external".takeIf { isExternal },
			"static".takeIf { isStatic },
			"fake_override".takeIf { isFakeOverride }
		)
	
	override fun visitClass(declaration: IrClass, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}class$sReset ${renderOriginIfNonTrivial()}" +
				"$kind ${sProperty2}name:$sReset$name ${sProperty2}modality:$sModality$modality$sReset ${sProperty2}visibility:$sVisibility$visibility$sReset " +
				renderClassFlags() +
				"${sProperty2}superTypes:$sReset[${superTypes.joinToString(separator = "; ") { it.render() }}]"
		}
	
	private fun IrClass.renderClassFlags() =
		renderFlagsList(
			"companion".takeIf { isCompanion },
			"inner".takeIf { isInner },
			"data".takeIf { isData },
			"external".takeIf { isExternal },
			"inline".takeIf { isInline },
			"expect".takeIf { isExpect },
			"fun".takeIf { isFun }
		)
	
	override fun visitVariable(declaration: IrVariable, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}var$sReset ${renderOriginIfNonTrivial()}${sProperty2}name:$sReset$normalizedName ${sProperty2}type:$sReset${type.render()} ${renderVariableFlags()}"
		}
	
	
	private fun IrVariable.renderVariableFlags(): String =
		renderFlagsList(
			"const".takeIf { isConst },
			"lateinit".takeIf { isLateinit },
			if(isVar) "var" else "val"
		)
	
	override fun visitEnumEntry(declaration: IrEnumEntry, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}enum_entry$sReset ${renderOriginIfNonTrivial()}${sProperty2}name:$sReset$name"
		}
	
	
	override fun visitAnonymousInitializer(declaration: IrAnonymousInitializer, data: Nothing?): String =
		"${sIdentifier}anonymous_initializer$sReset ${sProperty}isStatic=$sReset${declaration.isStatic}"
	
	override fun visitTypeParameter(declaration: IrTypeParameter, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}type_parameter$sReset ${renderOriginIfNonTrivial()}" +
				"${sProperty2}name:$sReset$name ${sProperty2}index:$sNumber$index$sReset ${sProperty2}variance:$sReset$variance " +
				"${sProperty2}superTypes:$sReset[${superTypes.joinToString(separator = "; ") { it.render() }}]"
		}
	
	override fun visitValueParameter(declaration: IrValueParameter, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}value_parameter$sReset ${renderOriginIfNonTrivial()}" +
				"${sProperty2}name:$sReset$name " +
				(if(index >= 0) "${sProperty2}index:$sNumber$index$sReset " else "") +
				"${sProperty2}type:$sReset${type.render()} " +
				(varargElementType?.let { "${sProperty2}varargElementType:$sReset${it.render()} " }
					?: "") +
				renderValueParameterFlags()
		}
	
	private fun IrValueParameter.renderValueParameterFlags(): String =
		renderFlagsList(
			"vararg".takeIf { varargElementType != null },
			"crossinline".takeIf { isCrossinline },
			"noinline".takeIf { isNoinline }
		)
	
	override fun visitLocalDelegatedProperty(declaration: IrLocalDelegatedProperty, data: Nothing?): String =
		declaration.runTrimEnd {
			"${sIdentifier}local_delegated_property$sReset ${declaration.renderOriginIfNonTrivial()}" +
				"${sProperty2}name:$sReset$name ${sProperty2}type:$sReset${type.render()} ${sProperty2}flags:$sReset${renderLocalDelegatedPropertyFlags()}"
		}
	
	override fun visitTypeAlias(declaration: IrTypeAlias, data: Nothing?): String =
		declaration.run {
			"${sIdentifier}typealias$sReset ${declaration.renderOriginIfNonTrivial()}" +
				"${sProperty2}name:$sReset$name ${sProperty2}visibility:$sVisibility$visibility$sReset ${sProperty2}expandedType:$sReset${expandedType.render()}" +
				renderTypeAliasFlags()
		}
	
	private fun IrTypeAlias.renderTypeAliasFlags(): String =
		renderFlagsList(
			"actual".takeIf { isActual }
		)
	
	private fun IrLocalDelegatedProperty.renderLocalDelegatedPropertyFlags() =
		if(isVar) "var" else "val"
	
	override fun visitExpressionBody(body: IrExpressionBody, data: Nothing?): String =
		"${sIdentifier}expression_body$sReset"
	
	override fun visitBlockBody(body: IrBlockBody, data: Nothing?): String =
		"${sIdentifier}block_body$sReset"
	
	override fun visitSyntheticBody(body: IrSyntheticBody, data: Nothing?): String =
		"${sIdentifier}synthetic_body$sReset ${sProperty}kind=$sReset${body.kind}"
	
	override fun visitExpression(expression: IrExpression, data: Nothing?): String =
		"? ${expression::class.java.simpleName} ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun <T> visitConst(expression: IrConst<T>, data: Nothing?): String =
		"${sIdentifier}const$sReset ${expression.kind} ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}value=$sReset${expression.value?.escapeIfRequired()}"
	
	private fun Any.escapeIfRequired() =
		when(this) {
			is String -> "\"${escapeStringCharacters(this)}\""
			is Char -> "'${escapeStringCharacters(this.toString())}'"
			else -> this
		}
	
	override fun visitVararg(expression: IrVararg, data: Nothing?): String =
		"${sIdentifier}vararg$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}varargElementType=$sReset${expression.varargElementType.render()}"
	
	override fun visitSpreadElement(spread: IrSpreadElement, data: Nothing?): String =
		"${sIdentifier}spread_element$sReset"
	
	override fun visitBlock(expression: IrBlock, data: Nothing?): String =
		"${sIdentifier}block$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitComposite(expression: IrComposite, data: Nothing?): String =
		"${sIdentifier}composite$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitReturn(expression: IrReturn, data: Nothing?): String =
		"${sIdentifier}return$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}from=$sReset'${expression.returnTargetSymbol.renderReference()}'"
	
	override fun visitCall(expression: IrCall, data: Nothing?): String =
		"${sIdentifier}call$sReset '${expression.symbol.renderReference()}' ${expression.renderSuperQualifier()}" +
			"${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	private fun IrCall.renderSuperQualifier(): String =
		superQualifierSymbol?.let { "${sProperty}superQualifier=$sReset'${it.renderReference()}' " }
			?: ""
	
	override fun visitConstructorCall(expression: IrConstructorCall, data: Nothing?): String =
		"${sIdentifier}constructor_call$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall, data: Nothing?): String =
		"${sIdentifier}delegating_constructor_call$sReset '${expression.symbol.renderReference()}'"
	
	override fun visitEnumConstructorCall(expression: IrEnumConstructorCall, data: Nothing?): String =
		"${sIdentifier}enum_constructor_call$sReset '${expression.symbol.renderReference()}'"
	
	override fun visitInstanceInitializerCall(expression: IrInstanceInitializerCall, data: Nothing?): String =
		"${sIdentifier}instance_initializer_call$sReset ${sProperty}classDescriptor=$sReset'${expression.classSymbol.renderReference()}'"
	
	override fun visitGetValue(expression: IrGetValue, data: Nothing?): String =
		"${sIdentifier}get_var$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitSetVariable(expression: IrSetVariable, data: Nothing?): String =
		"${sIdentifier}set_var$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitGetField(expression: IrGetField, data: Nothing?): String =
		"${sIdentifier}get_field$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitSetField(expression: IrSetField, data: Nothing?): String =
		"${sIdentifier}set_field$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitGetObjectValue(expression: IrGetObjectValue, data: Nothing?): String =
		"${sIdentifier}get_object$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitGetEnumValue(expression: IrGetEnumValue, data: Nothing?): String =
		"${sIdentifier}get_enum$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitStringConcatenation(expression: IrStringConcatenation, data: Nothing?): String =
		"${sIdentifier}string_concatenation$sReset ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitTypeOperator(expression: IrTypeOperatorCall, data: Nothing?): String =
		"${sIdentifier}type_op$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.operator} ${sProperty}typeOperand=$sReset${expression.typeOperand.render()}"
	
	override fun visitWhen(expression: IrWhen, data: Nothing?): String =
		"${sIdentifier}when$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
	
	override fun visitBranch(branch: IrBranch, data: Nothing?): String =
		"${sIdentifier}branch$sReset"
	
	override fun visitWhileLoop(loop: IrWhileLoop, data: Nothing?): String =
		"${sIdentifier}while$sReset ${sProperty}label=$sReset${loop.label} ${sProperty}origin=$sReset${loop.origin}"
	
	override fun visitDoWhileLoop(loop: IrDoWhileLoop, data: Nothing?): String =
		"${sIdentifier}do_while$sReset ${sProperty}label=$sReset${loop.label} ${sProperty}origin=$sReset${loop.origin}"
	
	override fun visitBreak(jump: IrBreak, data: Nothing?): String =
		"${sIdentifier}break$sReset ${sProperty}label=$sReset${jump.label} loop.${sProperty}label=$sReset${jump.loop.label}"
	
	override fun visitContinue(jump: IrContinue, data: Nothing?): String =
		"${sIdentifier}continue$sReset ${sProperty}label=$sReset${jump.label} loop.${sProperty}label=$sReset${jump.loop.label}"
	
	override fun visitThrow(expression: IrThrow, data: Nothing?): String =
		"${sIdentifier}throw$sReset ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitFunctionReference(expression: IrFunctionReference, data: Nothing?): String =
//		"${sIdentifier}function_reference$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}"
		"${sIdentifier}function_reference$sReset '${expression.symbol.renderReference()}' " +
			"${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin} " +
			"${sProperty}reflectionTarget=$sReset${renderReflectionTarget(expression)}"
	
	private fun renderReflectionTarget(expression: IrFunctionReference) =
		if(expression.symbol == expression.reflectionTarget)
			"<same>"
		else
			expression.reflectionTarget?.renderReference()
	
	override fun visitPropertyReference(expression: IrPropertyReference, data: Nothing?): String =
		buildTrimEnd {
			append("${sIdentifier}property_reference$sReset ")
			append("'${expression.symbol.renderReference()}' ")
			appendNullableAttribute("${sProperty}field=$sReset", expression.field) { "'${it.renderReference()}'" }
			appendNullableAttribute("${sProperty}getter=$sReset", expression.getter) { "'${it.renderReference()}'" }
			appendNullableAttribute("${sProperty}setter=$sReset", expression.setter) { "'${it.renderReference()}'" }
			append("${sProperty}type=$sReset${expression.type.renderReadable()} ")
			append("${sProperty}origin=$sReset${expression.origin}")
		}
	
	private inline fun <T : Any> StringBuilder.appendNullableAttribute(
		prefix: String,
		value: T?,
		toString: (T) -> String
	) {
		append(prefix)
		if(value != null) {
			append(toString(value))
		} else {
			append("null")
		}
		append(" ")
	}
	
	override fun visitLocalDelegatedPropertyReference(
		expression: IrLocalDelegatedPropertyReference,
		data: Nothing?
	): String =
		buildTrimEnd {
			append("${sIdentifier}local_delegated_property_reference$sReset ")
			append("'${expression.symbol.renderReference()}' ")
			append("${sProperty}delegate=$sReset'${expression.delegate.renderReference()}' ")
			append("${sProperty}getter=$sReset'${expression.getter.renderReference()}' ")
			appendNullableAttribute("${sProperty}setter=$sReset", expression.setter) { "'${it.renderReference()}'" }
			append("${sProperty}type=$sReset${expression.type.renderReadable()} ")
			append("${sProperty}origin=$sReset${expression.origin}")
		}
	
	override fun visitFunctionExpression(expression: IrFunctionExpression, data: Nothing?): String =
		buildTrimEnd {
			append("${sIdentifier}fun_expr$sReset ${sProperty}type=$sReset${expression.type.renderReadable()} ${sProperty}origin=$sReset${expression.origin}")
		}
	
	override fun visitClassReference(expression: IrClassReference, data: Nothing?): String =
		"${sIdentifier}class_reference$sReset '${expression.symbol.renderReference()}' ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitGetClass(expression: IrGetClass, data: Nothing?): String =
		"${sIdentifier}get_class$sReset ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitTry(aTry: IrTry, data: Nothing?): String =
		"${sIdentifier}try$sReset ${sProperty}type=$sReset${aTry.type.renderReadable()}"
	
	override fun visitCatch(aCatch: IrCatch, data: Nothing?): String =
		"${sIdentifier}catch$sReset ${sProperty}parameter=$sReset${aCatch.catchParameter.symbol.renderReference()}"
	
	override fun visitDynamicOperatorExpression(expression: IrDynamicOperatorExpression, data: Nothing?): String =
		"${sIdentifier}dyn_op$sReset ${sProperty}operator=$sReset${expression.operator} ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitDynamicMemberExpression(expression: IrDynamicMemberExpression, data: Nothing?): String =
		"${sIdentifier}dyn_member$sReset ${sProperty}memberName=$sReset'${expression.memberName}' ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitErrorDeclaration(declaration: IrErrorDeclaration, data: Nothing?): String =
		"${sIdentifier}error_decl$sReset ${declaration.descriptor::class.java.simpleName} " +
			descriptorRendererForErrorDeclarations.renderDescriptor(declaration.descriptor.original)
	
	override fun visitErrorExpression(expression: IrErrorExpression, data: Nothing?): String =
		"${sIdentifier}error_expr$sReset '${expression.description}' ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	override fun visitErrorCallExpression(expression: IrErrorCallExpression, data: Nothing?): String =
		"${sIdentifier}error_call$sReset '${expression.description}' ${sProperty}type=$sReset${expression.type.renderReadable()}"
	
	private val descriptorRendererForErrorDeclarations = DescriptorRenderer.ONLY_NAMES_WITH_SHORT_TYPES
}

private fun IrDeclaration.name(): String =
	descriptor.name.toString()

private fun DescriptorRenderer.renderDescriptor(descriptor: DeclarationDescriptor): String =
	if(descriptor is ReceiverParameterDescriptor)
		"this@${descriptor.containingDeclaration.name}: ${descriptor.type}"
	else
		render(descriptor)

private fun IrDeclaration.renderOriginIfNonTrivial(): String =
	if(origin != IrDeclarationOrigin.DEFINED) "$origin " else ""

private fun IrClassifierSymbol.renderClassifierFqn(): String =
	if(isBound)
		when(val owner = owner) {
			is IrClass -> owner.renderClassFqn()
			is IrTypeParameter -> owner.renderTypeParameterFqn()
			else -> "`unexpected classifier: ${owner.render()}`"
		}
	else
		"$sUnboundType${descriptor.dumpAsType()}$sReset"

private fun IrTypeAliasSymbol.renderTypeAliasFqn(): String =
	if(isBound)
		StringBuilder().also { owner.renderDeclarationFqn(it) }.toString()
	else
		"$sDimmed<${sIdentifierDimmed}unbound$sReset $this: ${this.descriptor.dump()}$sDimmed>$sReset"

private fun IrClass.renderClassFqn(): String =
	StringBuilder().also { renderDeclarationFqn(it) }.toString()

private fun IrTypeParameter.renderTypeParameterFqn(): String =
	StringBuilder().also { sb ->
		sb.append(name.asString())
		sb.append(" of ")
		renderDeclarationParentFqn(sb)
	}.toString()

private fun IrDeclaration.renderDeclarationFqn(sb: StringBuilder) {
	sb.append(sDimmed)
	renderDeclarationParentFqn(sb)
	sb.append('.')
	sb.append(sReset)
	if(this is IrDeclarationWithName) {
		sb.append(name.asString())
	} else {
		sb.append(this)
	}
}

private fun IrDeclaration.renderDeclarationParentFqn(sb: StringBuilder) {
	try {
		val parent = this.parent
		if(parent is IrDeclaration) {
			parent.renderDeclarationFqn(sb)
		} else if(parent is IrPackageFragment) {
			sb.append(parent.fqName.toString())
		}
	} catch(e: UninitializedPropertyAccessException) {
		sb.append("<uninitialized parent>")
	}
}


private fun IrTypeArgument.render() =
	when(this) {
		is IrStarProjection -> "*"
		is IrTypeProjection -> "$variance ${type.renderReadable()}"
		else -> throw AssertionError("Unexpected IrTypeArgument: $this")
	}

private inline fun <T> StringBuilder.appendListWith(
	list: List<T>,
	prefix: String,
	postfix: String,
	separator: String,
	renderItem: StringBuilder.(T) -> Unit
) {
	append(prefix)
	var isFirst = true
	for(item in list) {
		if(!isFirst) append(separator)
		renderItem(item)
		isFirst = false
	}
	append(postfix)
}


// escapeStringCharacters: converted from Intellij openapi
// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file:
// LICENSE file at https://github.com/JetBrains/intellij-community
private fun escapeStringCharacters(s: String): String {
	val buffer = StringBuilder(s.length)
	escapeStringCharacters(
		length = s.length,
		str = s,
		additionalChars = "\"",
		escapeSlash = true,
		escapeUnicode = true,
		buffer = buffer
	)
	return buffer.toString()
}

private fun escapeStringCharacters(
	length: Int,
	str: String,
	additionalChars: String?,
	escapeSlash: Boolean,
	escapeUnicode: Boolean,
	buffer: StringBuilder
): StringBuilder? {
	var prev = 0.toChar()
	for(idx in 0 until length) {
		val ch = str[idx]
		when(ch) {
			'\b' -> buffer.append("\\b")
			'\t' -> buffer.append("\\t")
			'\n' -> buffer.append("\\n")
			'\u000C' -> buffer.append("\\f")
			'\r' -> buffer.append("\\r")
			else -> if(escapeSlash && ch == '\\') {
				buffer.append("\\\\")
			} else if(additionalChars != null && additionalChars.indexOf(ch) > -1 && (escapeSlash || prev != '\\')) {
				buffer.append("\\").append(ch)
			} else if(escapeUnicode/* && !isPrintableUnicode(ch)*/) {
				val hexCode: CharSequence = Integer.toHexString(ch.toInt()).toUpperCase(Locale.getDefault())
				buffer.append("\\u")
				var paddingCount = 4 - hexCode.length
				while(paddingCount-- > 0) {
					buffer.append(0)
				}
				buffer.append(hexCode)
			} else {
				buffer.append(ch)
			}
		}
		prev = ch
	}
	return buffer
}
