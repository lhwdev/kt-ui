package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.lower


private val sEmptyScope = object : UiIrPhase {}


fun UiIrContext.transformations(name: String = "", block: UiIrPhase.() -> Unit) {
	val last = sCurrentContext
	sCurrentContext = this
	
	try {
		sEmptyScope.apply {
			+object : AbstractUiIrPhase(), UiIrRunPhase {
				override val phaseName = name
				
				override fun lower() {
					block()
				}
			}
		}
	} finally {
		sCurrentContext = last
	}
}

fun UiIrPhase.doLower() {
	if(this is UiIrRunPhase) lower()
	if(this is FileLoweringPass) lower(moduleFragment)
}

private var sCurrentContext: UiIrContext? = null

val currentContext get() = sCurrentContext!!
