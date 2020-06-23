package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


interface UiIrPhase : IrScope {
	val phaseName: String? get() = null
	
	fun lower()
	
	override val pluginContext: IrPluginContext get() = currentTransformation().pluginContext
	override val moduleFragment: IrModuleFragment get() = currentTransformation().moduleFragment
	override var target: IrElement
		get() = currentTransformation().target
		set(value) {
			currentTransformation().target = value
		}
}

abstract class AbstractUiIrPhase : UiIrPhase {
	val currentLowering = currentTransformation()
	override val pluginContext = currentLowering.pluginContext
	override val moduleFragment = currentLowering.moduleFragment
	override var target = currentLowering.target
		set(value) {
			field = value
			currentLowering.target = value
		}
}

inline fun uiIrPhase(name: String? = null, crossinline block: UiIrPhase.() -> Unit): UiIrPhase =
	object : AbstractUiIrPhase() {
		override val phaseName: String? get() = name
		
		override fun lower() {
			block()
		}
	}
