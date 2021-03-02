package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import kotlin.system.measureTimeMillis


interface IrScope {
	val pluginContext: IrPluginContext
	val moduleFragment: IrModuleFragment
	
	operator fun UiIrPhase.unaryPlus() {
		val name = phaseName ?: this::class.simpleName
		log6("== == Transformation Phase: $name == ==")
		val time = measureTimeMillis {
			doLower()
		}
		log6("== == End Transformation: $name | took $time ms == ==")
		log("\n")
	}
}


fun IrScope(pluginContext: IrPluginContext, moduleFragment: IrModuleFragment) =
	object : IrScope {
		override val pluginContext = pluginContext
		override val moduleFragment = moduleFragment
	}
