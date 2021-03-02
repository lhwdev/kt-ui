package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


class UiIrPhaseKey<T>


data class UiIrContext(val pluginContext: IrPluginContext, val moduleFragment: IrModuleFragment) {
	val parameters: MutableMap<UiIrPhaseKey<*>, Any?> = mutableMapOf()
}
