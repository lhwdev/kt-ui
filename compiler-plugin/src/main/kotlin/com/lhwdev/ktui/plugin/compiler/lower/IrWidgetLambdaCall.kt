package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.UiLibraryFqNames
import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor


fun IrWidgetScope.mapWidgetLambdaSymbol(symbol: IrSimpleFunctionSymbol): IrWidgetLambdaSymbol {
	val newSize = symbol.owner.valueParameters.size + 1 /* buildScope */
	return irBuiltIns.function(newSize).getSimpleFunction("invoke")!!
}


class IrWidgetLambdaCall(
	call: IrCall,
	val newSymbol: IrWidgetLambdaSymbol,
	var buildScopeArgument: IrExpression,
	var changedArguments: List<IrExpression>
) : IrComponentExpression<IrCall>, IrCallWrapper(call) {
	init {
		require(isWidgetLambdaCall(call)) { "not a widget lambda call: no @Widget annotation" }
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
	
	override fun deepCopyWithSymbols(transformer: IrDeepCopyComponentTransformer): IrWidgetLambdaCall {
		transformChildren(transformer, null)
		return IrWidgetLambdaCall(this, newSymbol, buildScopeArgument, changedArguments)
	}
	
	
	override fun IrComponentBuildScope.doBuild(): IrCall = this@IrWidgetLambdaCall
	
	
	companion object {
		fun isWidgetLambdaCall(call: IrCall): Boolean =
			call.dispatchReceiver?.type?.hasAnnotation(UiLibraryFqNames.Widget) == true
		
		fun transform(original: IrCall, scope: IrWidgetScope): IrWidgetLambdaCall = IrWidgetLambdaCall(
			original,
			newSymbol = scope.mapWidgetLambdaSymbol(original.symbol),
			buildScopeArgument = scope.irGet(scope.buildScope),
			changedArguments = IrWidgetCall.generateChangedArguments(original, scope)
		)
	}
}
