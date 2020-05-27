package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrGenerator
import org.jetbrains.kotlin.ir.builders.Scope


interface IrElementScope : IrGenerator {
	override val context: IrPluginContext get() = pluginContext
	val startOffset: Int
	val endOffset: Int
}

interface IrBuilderScope : IrElementScope {
	val scope: Scope
}

interface IrStatementsScope<T> : IrBuilderScope {
	operator fun IrStatement.unaryPlus()
	fun build(): T
}
