package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.ElementScope
import com.lhwdev.ktui.plugin.compiler.util.IrElementTransformerVoidScoped
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner


sealed class WidgetOrScope<T : IrElement>(override val irElement: T, override val scope: Scope) : ElementScope<T>

class NormalScope(element: IrElement, scope: Scope) : WidgetOrScope<IrElement>(element, scope)

class WidgetFunctionScope(val info: WidgetTransformationInfo, function: IrFunction, scope: Scope) : WidgetOrScope<IrFunction>(function, scope)


abstract class IrWidgetElementTransformerVoidScoped : IrElementTransformerVoidScoped<WidgetOrScope<*>>() {
	override fun createScope(declaration: IrSymbolOwner) =
		(declaration as? IrFunction)?.widgetInfoMarker?.let { WidgetFunctionScope(it, declaration, Scope(declaration.symbol)) }
			?: NormalScope(declaration, Scope(declaration.symbol))
	
	val isInWidgetScope get() = currentScope is WidgetFunctionScope
	
	val widgetScope get() = currentScope as WidgetFunctionScope
	val widgetScopeOrNull get() = currentScope as? WidgetFunctionScope
}
