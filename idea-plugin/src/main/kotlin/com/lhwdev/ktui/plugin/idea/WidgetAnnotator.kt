package com.lhwdev.ktui.plugin.idea

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.lhwdev.ktui.plugin.compiler.UiLibraryNames
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.idea.caches.resolve.analyzeWithAllCompilerChecks
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall


val Annotated.isWidget get() = annotations.hasAnnotation(UiLibraryNames.Widget)


// Used to apply styles for calls to @Widget functions.
class WidgetAnnotator : Annotator {
	companion object TextAttributeRegistry {
		val WIDGET_CALL_TEXT_ATTRIBUTES_KEY: TextAttributesKey
		const val WIDGET_CALL_TEXT_ATTRIBUTES_NAME = "WidgetCallTextAttributes"
		private val ANALYSIS_RESULT_KEY = Key<AnalysisResult>(
			"WidgetAnnotator.DidAnnotateKey"
		)
		
		init {
			WIDGET_CALL_TEXT_ATTRIBUTES_KEY = TextAttributesKey.createTextAttributesKey(
				WIDGET_CALL_TEXT_ATTRIBUTES_NAME,
//				DefaultLanguageHighlighterColors.FUNCTION_CALL
			)
		}
	}
	
	override fun annotate(element: PsiElement, holder: AnnotationHolder) {
		if(element !is KtCallExpression) return
//		if (!isComposeEnabled(element)) return
		// AnnotationHolder.currentAnnotationSession applies to a single file.
		var analysisResult = holder.currentAnnotationSession.getUserData(
			ANALYSIS_RESULT_KEY
		)
		if(analysisResult == null) {
			val ktFile = element.containingFile as? KtFile ?: return
			analysisResult = ktFile.analyzeWithAllCompilerChecks()
			holder.currentAnnotationSession.putUserData(
				ANALYSIS_RESULT_KEY, analysisResult
			)
		}
		if(analysisResult.isError()) {
			throw ProcessCanceledException(analysisResult.error)
		}
		
		if(!shouldStyleCall(analysisResult.bindingContext, element)) return
		val elementToStyle = element.calleeExpression ?: return
		val annotation = holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
		annotation.range(elementToStyle.textRange)
		annotation.textAttributes(WIDGET_CALL_TEXT_ATTRIBUTES_KEY)
		annotation.create()
//		val annotation: Annotation = holder.createInfoAnnotation(elementToStyle.textRange, null)
	}
	
	private fun shouldStyleCall(bindingContext: BindingContext, element: KtCallExpression): Boolean {
		return element.getResolvedCall(bindingContext)?.candidateDescriptor?.isWidget == true
	}
}
