package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrGenerator
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner


interface IrElementScope : IrGenerator {
	override val context: IrPluginContext get() = pluginContext
	val startOffset: Int
	val endOffset: Int
}

interface IrBuilderScope : IrElementScope {
	val scope: Scope
	val irElement: IrSymbolOwner get() = scope.scopeOwnerSymbol.owner
	override val startOffset: Int get() = irElement.startOffset
	override val endOffset: Int get() = irElement.endOffset
}

interface IrStatementsScope<T> : IrBuilderScope {
	operator fun IrStatement.unaryPlus()
	fun build(): T
}


val IrElementScope.factory get() = context.irFactory
val IrBuilderScope.factory get() = (scope.scopeOwnerSymbol.owner as? IrDeclaration)?.factory ?: context.irFactory
