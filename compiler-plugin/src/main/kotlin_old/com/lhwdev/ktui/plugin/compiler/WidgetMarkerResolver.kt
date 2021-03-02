package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


enum class EffectiveInlineState { none, crossInline, inline }

val IrFunctionSymbol.effectiveInlineState
	get() = if(descriptor.isInline) EffectiveInlineState.inline else
		irScope.uiTrace[UiWritableSlices.IS_INLINE_FUNCTION, this] ?: EffectiveInlineState.none

val IrFunctionSymbol.isEffectiveInline get() = effectiveInlineState == EffectiveInlineState.inline


class WidgetMarkerResolver : IrElementTransformerVoid(), UiIrPhase {
	override fun lower() {
		target.transformChildrenVoid()
	}
	
	override fun visitFunction(declaration: IrFunction): IrStatement {
		declaration.widgetTransformationKind?.let {
			uiTrace.record(UiWritableSlices.WIDGET_KIND, declaration, it)
		}
		
		return super.visitFunction(declaration)
	}
	
	// fun Proxy(child: @Widget () -> Unit)
	// Proxy {} <- this lambda is not marked as widget basically
	override fun visitFunctionAccess(
		expression: IrFunctionAccessExpression
	): IrExpression = with(expression) {
		val descriptor = symbol.descriptor
		if(descriptor.isWidget()) {
			for((index, argument) in valueArguments.withIndex().filter { it.value != null }) {
				val lambda = argument!!.asLambdaFunction()
				
				if(lambda != null && descriptor.valueParameters[index].type.isWidget())
					uiTrace.record(UiWritableSlices.WIDGET_KIND, lambda, WidgetTransformationKind.innerWidget)
			}
		}
		
		if(descriptor.isInline) {
			for((index, parameter) in descriptor.valueParameters.withIndex()) {
				val argument = getValueArgument(index)
				if(!parameter.isNoinline && argument is IrFunctionExpression)
					uiTrace.record(
						UiWritableSlices.IS_INLINE_FUNCTION, argument.function.symbol,
						if(parameter.isCrossinline) EffectiveInlineState.crossInline else EffectiveInlineState.inline
					)
			}
		}
		
		super.visitFunctionAccess(this)
	}
}
