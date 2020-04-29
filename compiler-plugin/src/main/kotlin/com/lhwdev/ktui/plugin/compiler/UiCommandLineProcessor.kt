package com.lhwdev.ktui.plugin.compiler

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor


@AutoService(CommandLineProcessor::class)
class UiCommandLineProcessor : CommandLineProcessor {
	override val pluginId get() = PLUGIN_ID
	
	override val pluginOptions = emptyList<CliOption>()
}
