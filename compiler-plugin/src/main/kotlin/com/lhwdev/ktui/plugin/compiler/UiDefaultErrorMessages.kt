package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.diagnostics.rendering.DiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.rendering.DiagnosticParameterRenderer
import org.jetbrains.kotlin.diagnostics.rendering.RenderingContext


object UiDefaultErrorMessages : DefaultErrorMessages.Extension {
	private val MAP = DiagnosticFactoryToRendererMap("Compose")
	override fun getMap() =
		MAP
	
	val OUR_STRING_RENDERER = object : DiagnosticParameterRenderer<String> {
		override fun render(obj: String, renderingContext: RenderingContext): String {
			return obj
		}
	}
	
	init {
		MAP.put(UiErrors.widgetInvocationInNonWidget,
			"Functions which invoke @Widget functions must be marked with the @Widget annotation")
		
		MAP.put(UiErrors.widgetInvocationInWidgetUtil,
			"@WidgetUtil functions cannot invoke @Widget functions")
		
		MAP.put(UiErrors.widgetUtilInvocationInNonWidget,
			"Functions which invoke @WidgetUtil functions must be marked with the @Widget annotation or @WidgetUtil annotation")
	}
}
