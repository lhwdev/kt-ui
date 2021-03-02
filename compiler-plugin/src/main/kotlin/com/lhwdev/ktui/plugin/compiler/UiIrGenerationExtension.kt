package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.lower.WidgetCallTransformer
import com.lhwdev.ktui.plugin.compiler.lower.WidgetFunctionTransformer
import com.lhwdev.ktui.plugin.compiler.util.BuildIrComponentsTransformer
import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment


class UiIrGenerationExtension : IrGenerationExtension {
	override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
		UiIrContext(pluginContext, moduleFragment).transformations("UiIrGenerationExtension") {
			+WidgetFunctionTransformer()
			+WidgetCallTransformer()
			+UiBuildComponentsPhase()
			
			moduleFragment.logSrcColored(pluginContext)
		}
	}
}


class UiBuildComponentsPhase : UiIrPhase, FileLoweringPass {
	override fun lower(irFile: IrFile) {
		val builder = BuildIrComponentsTransformer(irFile)
		builder.transform()
	}
}
