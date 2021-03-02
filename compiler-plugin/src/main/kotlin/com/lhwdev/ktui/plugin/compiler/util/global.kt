package com.lhwdev.ktui.plugin.compiler.util

import com.lhwdev.ktui.plugin.compiler.UiLibrary
import com.lhwdev.ktui.plugin.compiler.currentContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import java.util.WeakHashMap


inline val pluginContext: IrPluginContext get() = currentContext.pluginContext

inline val irBuiltIns get() = pluginContext.irBuiltIns

private val sUiLibraryCache = WeakHashMap<IrPluginContext, UiLibrary>()

val UiLibrary: UiLibrary get() = sUiLibraryCache.getOrPut(pluginContext) { UiLibrary(pluginContext) }
