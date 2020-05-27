package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import kotlin.system.measureTimeMillis


interface IrScope {
	val target: IrElement
	val pluginContext: IrPluginContext
	val plugin get() = pluginContext
	val moduleFragment: IrModuleFragment
	
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


fun IrScope(target: IrElement, pluginContext: IrPluginContext, moduleFragment: IrModuleFragment) =
	object : IrScope {
		override val target = target
		override val pluginContext = pluginContext
		override val moduleFragment = moduleFragment
	}
