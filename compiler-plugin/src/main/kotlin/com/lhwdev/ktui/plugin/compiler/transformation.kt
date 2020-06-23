package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.provideContext
import org.jetbrains.kotlin.ir.IrElement


private val sEmptyScope = object : UiIrPhase {
	override fun lower() {
	}
}


fun UiIrContext.transformations(name: String = "", block: UiIrPhase.() -> Unit): IrElement {
	sCurrentTransformation = this
	provideContext(pluginContext) {
		try {
			sEmptyScope.apply {
				+object : AbstractUiIrPhase() {
					override val phaseName = name
					
					override fun lower() {
						block()
					}
				}
			}
		} finally {
			sCurrentTransformation = null
		}
	}
	return target
}

private var sCurrentTransformation: UiIrContext? = null

fun currentTransformation() = sCurrentTransformation!!
