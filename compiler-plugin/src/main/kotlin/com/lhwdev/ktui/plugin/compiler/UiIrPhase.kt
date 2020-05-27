package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


interface UiIrPhase : IrScope {
	val phaseName: String? get() = null
	
	fun lower()
	
	override val pluginContext: IrPluginContext get() = currentTransformation().pluginContext
	override val moduleFragment: IrModuleFragment get() = currentTransformation().moduleFragment
	override val target: IrElement get() = currentTransformation().target
}

abstract class AbstractUiIrPhase : UiIrPhase {
	val currentLowering = currentTransformation()
	override val pluginContext = currentLowering.pluginContext
	override val moduleFragment = currentLowering.moduleFragment
	override val target = currentLowering.target
}

inline fun uiIrPhase(name: String? = null, crossinline block: UiIrPhase.(target: IrElement) -> Unit) =
	object : UiIrPhase {
		override val phaseName: String? get() = name
		val currentLowering = currentTransformation()
		
		override fun lower() {
			val lowering = currentTransformation()
			block(lowering.target)
		}
		
		override val pluginContext = currentLowering.pluginContext
		override val moduleFragment = currentLowering.moduleFragment
		override val target = currentLowering.target
	}
