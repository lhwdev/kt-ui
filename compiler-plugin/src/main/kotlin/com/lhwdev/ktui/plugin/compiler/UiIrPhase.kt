package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


interface UiIrPhase : IrScope {
	val phaseName: String? get() = null
	
	val currentLowering: UiIrContext get() = currentContext
	override val pluginContext: IrPluginContext get() = currentContext.pluginContext
	override val moduleFragment: IrModuleFragment get() = currentContext.moduleFragment
}

abstract class AbstractUiIrPhase : UiIrPhase {
	final override val currentLowering = currentContext
	final override val pluginContext = currentLowering.pluginContext
	final override val moduleFragment = currentLowering.moduleFragment
}

interface UiIrRunPhase : UiIrPhase {
	fun lower()
}

@Suppress("UNCHECKED_CAST")
fun <T> UiIrPhase.param(key: UiIrPhaseKey<T>): T = currentLowering.parameters[key] as T

fun <T> UiIrPhase.provideParam(key: UiIrPhaseKey<T>, value: T) {
	currentLowering.parameters[key] = value
}
