package com.lhwdev.ktui.plugin.compiler

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration


@AutoService(ComponentRegistrar::class)
class UiComponentRegistrar : ComponentRegistrar {
	override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
		log("Hello, UiComponentRegistrar!")
		
		// StorageComponentContainerContributor.registerExtension(project, WidgetAnnotationChecker())
		
		// StorageComponentContainerContributor.registerExtension(project, TryCatchWidgetChecker())
		
		IrGenerationExtension.registerExtension(project, UiIrGenerationExtension())
	}
}
