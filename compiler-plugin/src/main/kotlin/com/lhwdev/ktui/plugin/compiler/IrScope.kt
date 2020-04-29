package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


interface IrScope {
	val pluginContext: IrPluginContext
	val moduleFragment: IrModuleFragment
}


fun IrScope(pluginContext: IrPluginContext, moduleFragment: IrModuleFragment) = object : IrScope {
	override val pluginContext = pluginContext
	override val moduleFragment = moduleFragment
}
