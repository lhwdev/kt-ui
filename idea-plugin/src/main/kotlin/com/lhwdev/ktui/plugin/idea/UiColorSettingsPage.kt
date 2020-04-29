package com.lhwdev.ktui.plugin.idea

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.jetbrains.kotlin.idea.highlighter.KotlinColorSettingsPage
import org.jetbrains.kotlin.idea.highlighter.KotlinHighlightingColors
import javax.swing.Icon


class UiColorSettingsPage : ColorSettingsPage {
	override fun getHighlighter(): SyntaxHighlighter {
		return KotlinColorSettingsPage().highlighter
	}
	
	override fun getAdditionalHighlightingTagToDescriptorMap() = hashMapOf(
		WidgetAnnotator.WIDGET_CALL_TEXT_ATTRIBUTES_NAME to WidgetAnnotator.WIDGET_CALL_TEXT_ATTRIBUTES_KEY,
		"ANNOTATION" to KotlinHighlightingColors.ANNOTATION,
		"KEYWORD" to KotlinHighlightingColors.KEYWORD,
		"FUNCTION_DECLARATION" to KotlinHighlightingColors.FUNCTION_DECLARATION,
		"FUNCTION_PARAMETER" to KotlinHighlightingColors.PARAMETER,
	)
	
	override fun getIcon(): Icon? = null
	
	override fun getAttributeDescriptors() = // TODO: this needs to be localized.
		arrayOf(AttributesDescriptor("Calls to @Widget functions",
			WidgetAnnotator.WIDGET_CALL_TEXT_ATTRIBUTES_KEY))
	
	override fun getColorDescriptors() = emptyArray<ColorDescriptor>()
	
	override fun getDisplayName() =// TODO: this needs to be localized.
		"kt-ui"
	
	override fun getDemoText(): String = """
		|<ANNOTATION>@Widget</ANNOTATION>
		|<KEYWORD>fun</KEYWORD> <FUNCTION_DECLARATION>Text</FUNCTION_DECLARATION>(<FUNCTION_PARAMETER>text</FUNCTION_PARAMETER>: <FUNCTION_PARAMETER>String</FUNCTION_PARAMETER>)
		|}
		|
		|<ANNOTATION>@Widget</ANNOTATION>
		|<KEYWORD>fun</KEYWORD> <FUNCTION_DECLARATION>Greeting</FUNCTION_DECLARATION>() {
		|    <WidgetCallTextAttributes>Text</WidgetCallTextAttributes>(<FUNCTION_PARAMETER>"Hello"</FUNCTION_PARAMETER>)
		|}""".trimMargin("|")
}
