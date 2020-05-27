package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


class WidgetMarkerTransformer : IrElementTransformerVoid(), UiIrPhase {
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
	override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
		val descriptor = expression.symbol.descriptor
		if(descriptor.isWidget()) {
			for((index, argument) in expression.valueArguments.withIndex().filter { it.value != null }) {
				val lambda = argument!!.asLambdaFunction()
				
				if(lambda != null && descriptor.valueParameters[index].isWidget())
					uiTrace.record(UiWritableSlices.WIDGET_KIND, lambda, WidgetTransformationKind.innerWidget)
			}
			
			return super.visitFunctionAccess(expression)
		}
		
		return super.visitFunctionAccess(expression)
	}
}
