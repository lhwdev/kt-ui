package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.diagnostics.Severity.ERROR


object UiErrors {
	@JvmField
	val widgetInvocationInNonWidget = DiagnosticFactory0.create<PsiElement>(ERROR)
	
	// TODO: intercept try .. catch and remove this
	@JvmField
	val illegalTryCatchAroundWidget = DiagnosticFactory0.create<PsiElement>(ERROR)
	
	
	init {
		// https://github.com/JetBrains/kotlin/commit/453008e488a63a0c7df0e13a48d694949dd34e38
		// put @JvmField and rely on this function to attach the default error message.
		// perhaps will get fixed some day..?
		Errors.Initializer.initializeFactoryNamesAndDefaultErrorMessages(UiErrors::class.java, UiDefaultErrorMessages)
	}
}
