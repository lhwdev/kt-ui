package com.lhwdev.ktui

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump


class CLP : CommandLineProcessor {
	override val pluginId get() = "com.asmx.ui"
	
	override val pluginOptions = emptyList<CliOption>()
}

class IrDumper : ComponentRegistrar {
	override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
		IrGenerationExtension.registerExtension(project, object : IrGenerationExtension {
			override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
				println(moduleFragment.dump())
			}
		})
	}
}
