package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContextImpl
import org.jetbrains.kotlin.ir.linkage.IrDeserializer
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import java.util.WeakHashMap


interface IrPluginContextWithLinker : IrPluginContext {
	val linker: IrDeserializer
}


private val sLinkerCache = WeakHashMap<IrPluginContext, IrDeserializer>()

val IrPluginContext.linker: IrDeserializer
	get() = when(this) {
		is IrPluginContextImpl -> linker
		is IrPluginContextWithLinker -> linker
		else -> sLinkerCache[this] ?: error("wtf")
	}

fun provideLinker(pluginContext: IrPluginContext, linker: IrDeserializer) {
	sLinkerCache[pluginContext] = linker
}

fun IrPluginContext.bindSymbol(symbol: IrSymbol) {
	val linker = linker
	linker.getDeclaration(symbol)
	linker.postProcess()
}
