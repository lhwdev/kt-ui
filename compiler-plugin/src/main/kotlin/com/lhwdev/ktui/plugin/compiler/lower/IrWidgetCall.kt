package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor


class IrWidgetCall private constructor(
	call: IrCall,
	val newSymbol: IrWidgetFunctionSymbol,
	var buildScopeArgument: IrExpression,
	var changedArguments: List<IrExpression>
) : IrComponentExpression<IrCall>, IrCallWrapper(call) {
	init {
		require(isWidgetCall(call)) { "not a widget call: no @Widget annotation" }
	}
	
	
	override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R = handleAccept(visitor, data) {
		visitor.visitCall(this, data)
	}
	
	override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
		super.acceptChildren(visitor, data)
		buildScopeArgument.accept(visitor, data)
		changedArguments.forEach { it.accept(visitor, data) }
	}
	
	override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
		super.transformChildren(transformer, data)
		buildScopeArgument = buildScopeArgument.transform(transformer, data)
		changedArguments = changedArguments.map { it.transform(transformer, data) }
	}
	
	override fun deepCopyWithSymbols(transformer: IrDeepCopyComponentTransformer): IrWidgetCall {
		transformChildren(transformer, null)
		return IrWidgetCall(this, newSymbol, buildScopeArgument, changedArguments)
	}
	
	override fun IrComponentBuildScope.doBuild(): IrCall = scope.irCall(
		newSymbol,
		valueArguments = valueArguments + buildScopeArgument + changedArguments,
		typeArguments = typeArguments,
		dispatchReceiver = dispatchReceiver, extensionReceiver = extensionReceiver,
		type = type, origin = origin
	)
	
	
	companion object {
		fun isWidgetCall(call: IrCall): Boolean = IrWidgetFunction.isWidget(call.symbol.owner)
		
		fun generateChangedArguments(call: IrCall, scope: IrWidgetScope): List<IrExpression> = // TODO
			emptyList()
		
		fun transform(original: IrCall, scope: IrWidgetScope): IrWidgetCall = IrWidgetCall(
			original,
			newSymbol = scope.widgetMapping[original.symbol]!!,
			buildScopeArgument = scope.irGet(scope.buildScope),
			changedArguments = generateChangedArguments(original, scope)
		)
	}
}
