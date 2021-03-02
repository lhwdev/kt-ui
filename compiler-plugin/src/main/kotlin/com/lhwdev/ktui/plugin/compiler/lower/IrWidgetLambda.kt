package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.util.IrComponentBuildScope
import com.lhwdev.ktui.plugin.compiler.util.IrComponentExpressionBase
import com.lhwdev.ktui.plugin.compiler.util.IrDeepCopyComponentTransformer
import com.lhwdev.ktui.plugin.compiler.util.callBuild
import com.lhwdev.ktui.plugin.compiler.withLog
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor


typealias IrWidgetLambdaSymbol = IrSimpleFunctionSymbol


class IrWidgetLambda(
	var function: IrWidgetFunction,
	override var type: IrType,
	val origin: IrStatementOrigin = IrStatementOrigin.LAMBDA
) : IrComponentExpressionBase<IrFunctionExpression>, IrExpression() {
	override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
		function.acceptChildren(visitor, data)
	}
	
	override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
		function = function.transform(transformer, data) as IrWidgetFunction
	}
	
	override fun deepCopyWithSymbols(transformer: IrDeepCopyComponentTransformer): IrWidgetLambda {
		return IrWidgetLambda(function.transform(transformer, null) as IrWidgetFunction, type, origin)
	}
	
	override fun IrComponentBuildScope.doBuild(): IrFunctionExpression = IrFunctionExpressionImpl(
		function.startOffset, function.endOffset.withLog(),
		type = type, function = function.callBuild(this), origin = origin
	)
}
