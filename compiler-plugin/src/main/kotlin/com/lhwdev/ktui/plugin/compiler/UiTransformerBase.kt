package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


abstract class UiTransformerBase : IrElementTransformerVoid(), UiIrPhase, IrScope {
	private val thisLowering = currentTransformation()
	
	override val pluginContext = thisLowering.pluginContext
	override val moduleFragment = thisLowering.moduleFragment
	override val target = thisLowering.target
	
	
	override fun lower() {
		target.transform(this, null)
	}
	
}
