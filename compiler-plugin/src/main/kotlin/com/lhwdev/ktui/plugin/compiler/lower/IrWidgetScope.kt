package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.util.IrBuilderScope
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol


interface IrWidgetScope : IrBuilderScope {
	override val scope: Scope
	val buildScope: IrValueDeclaration
	
	val widgetMapping: Map<IrFunctionSymbol, IrWidgetFunctionSymbol>
}


class IrWidgetScopeImpl(
	override val scope: Scope,
	override val buildScope: IrValueDeclaration,
	override val widgetMapping: Map<IrFunctionSymbol, IrWidgetFunctionSymbol>
) : IrWidgetScope
