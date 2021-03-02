package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.UiIrPhase
import com.lhwdev.ktui.plugin.compiler.dumpSrcColored
import com.lhwdev.ktui.plugin.compiler.log2
import com.lhwdev.ktui.plugin.compiler.param
import com.lhwdev.ktui.plugin.compiler.util.IrBuilderScope
import com.lhwdev.ktui.plugin.compiler.util.IrComponentTransformerVoidWithContext
import com.lhwdev.ktui.plugin.compiler.util.SimpleElementScope
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.utils.addToStdlib.lastIsInstanceOrNull


class WidgetCallTransformer : UiIrPhase, FileLoweringPass, IrComponentTransformerVoidWithContext<IrBuilderScope>() {
	private val mapping = param(sWidgetFunctionMapping)
	private val topWidgetScope get() = scopeStack.lastIsInstanceOrNull<IrWidgetScope>()
	
	override fun createScope(declaration: IrSymbolOwner): IrBuilderScope {
		val scope = Scope(declaration.symbol)
		return if(declaration is IrWidgetFunction) {
			IrWidgetScopeImpl(scope, declaration.buildScopeParameter, mapping)
		} else {
			SimpleElementScope(declaration, scope)
		}
	}
	
	override fun lower(irFile: IrFile) {
		irFile.transformChildrenVoid()
	}
	
	override fun visitCall(expression: IrCall): IrExpression {
		val scope = topWidgetScope
		
		// TODO: inspection
		// 1. normal widget
		if(IrWidgetCall.isWidgetCall(expression)) {
			log2("found widget call ${expression.dumpSrcColored()}")
			if(scope == null) error("widget call in not widget scope")
			return IrWidgetCall.transform(expression, scope)
		}
		
		// 2. lambda widget
		if(IrWidgetLambdaCall.isWidgetLambdaCall(expression)) {
			if(scope == null) error("widget lambda call in not widget scope")
			return IrWidgetLambdaCall.transform(expression, scope)
		}
		
		// 3. nothing
		return super.visitCall(expression)
	}
}
