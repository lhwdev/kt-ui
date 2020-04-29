package com.lhwdev.ktui.plugin.compiler

import kotlin.system.measureTimeMillis


fun UiIrContext.transformations(block: TransformationsScope.() -> Unit) {
	sCurrentLowering = this
	try {
		TransformationsScope().block()
	} finally {
		sCurrentLowering = null
	}
}

class TransformationsScope {
	operator fun UiIrPhase.unaryPlus() {
		val name = phaseName ?: this::class.simpleName
		log6("== == Transformation Phase: $name == ==")
		val time = measureTimeMillis {
			lower()
		}
		log6("== == End Transformation: $name | took $time ms == ==")
		log("\n")
	}
}

private var sCurrentLowering: UiIrContext? = null

fun currentLowering() = sCurrentLowering!!

