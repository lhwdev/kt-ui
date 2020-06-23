package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


data class UiIrContext(val pluginContext: IrPluginContext, val moduleFragment: IrModuleFragment, var target: IrElement)
