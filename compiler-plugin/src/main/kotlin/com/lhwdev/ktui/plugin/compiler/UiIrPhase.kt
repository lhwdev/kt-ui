package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.IrElement


interface UiIrPhase {
	val phaseName: String? get() = null
	
	fun lower()
}

inline fun uiIrPhase(name: String? = null, crossinline block: IrScope.(target: IrElement) -> Unit) =
	object : UiIrPhase {
		override val phaseName: String? get() = name
		
		override fun lower() {
			val lowering = currentLowering()
			IrScope(lowering.pluginContext, lowering.moduleFragment).block(lowering.target)
		}
	}
