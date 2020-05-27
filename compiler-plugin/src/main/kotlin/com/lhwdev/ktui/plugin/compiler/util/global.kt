package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext


@PublishedApi
internal var providedContext: IrPluginContext? = null

val pluginContext: IrPluginContext get() = providedContext!!

inline val context get() = pluginContext

inline val builtIns get() = context.builtIns
inline val irBuiltIns get() = context.irBuiltIns


inline fun provideContext(context: IrPluginContext, block: () -> Unit) {
	providedContext = context
	try {
		block()
	} finally {
		providedContext = null
	}
}
