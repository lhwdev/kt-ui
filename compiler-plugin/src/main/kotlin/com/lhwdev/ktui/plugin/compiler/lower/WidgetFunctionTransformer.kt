package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.*
import com.lhwdev.ktui.plugin.compiler.util.IrComponentTransformerVoid
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.util.hasAnnotation


val sWidgetFunctionMapping = UiIrPhaseKey<Map<IrFunctionSymbol, IrWidgetFunctionSymbol>>()


class WidgetFunctionTransformer : UiIrPhase, FileLoweringPass, IrComponentTransformerVoid() {
	private val mapping = mutableMapOf<IrFunctionSymbol, IrWidgetFunctionSymbol>()
	
	override fun lower(irFile: IrFile) {
		provideParam(sWidgetFunctionMapping, mapping)
		irFile.transformChildrenVoid()
	}
	
	override fun visitFunctionExpression(expression: IrFunctionExpression): IrExpression {
		if(expression.function.annotations.hasAnnotation(UiLibraryFqNames.Widget))
			return IrWidgetLambda(transform(expression.function), expression.type, expression.origin)
		
		return super.visitFunctionExpression(expression)
	}
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction): IrStatement {
		if(declaration.annotations.hasAnnotation(UiLibraryFqNames.Widget))
			return transform(declaration)
		
		return super.visitFunction(declaration)
	}
	
	fun transform(declaration: IrSimpleFunction): IrWidgetFunction {
		log3("found widget function: ${declaration.dumpSrcHeadColored()}")
		val widgetFunction = IrWidgetFunction.transform(declaration)
		mapping[declaration.symbol] = widgetFunction.symbol
		return widgetFunction
	}
}
