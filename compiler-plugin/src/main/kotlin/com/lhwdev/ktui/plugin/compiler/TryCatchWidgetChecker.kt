package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.psi.KtTryExpression
import org.jetbrains.kotlin.resolve.calls.checkers.CallChecker
import org.jetbrains.kotlin.resolve.calls.checkers.CallCheckerContext
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall


open class TryCatchWidgetChecker : CallChecker, StorageComponentContainerContributor {
	override fun registerModuleComponents(
		container: StorageComponentContainer,
		platform: TargetPlatform,
		moduleDescriptor: ModuleDescriptor
	) {
		if(!platform.isJvm()) return
		container.useInstance(this)
	}
	
	override fun check(
		resolvedCall: ResolvedCall<*>,
		reportOn: PsiElement,
		context: CallCheckerContext
	) {
		val trace = context.trace
		val call = resolvedCall.call.callElement
		val shouldBeTag =
			WidgetAnnotationChecker.get(call.project).shouldInvokeAsTag(trace, resolvedCall)
		if(shouldBeTag == WidgetKind.widget) {
			var walker: PsiElement? = call
			while(walker != null) {
				val parent = walker.parent
				if(parent is KtTryExpression) {
					if(walker == parent.tryBlock)
						trace.report(UiErrors.illegalTryCatchAroundWidget.on(parent.tryKeyword!!))
				}
				walker = try {
					walker.parent
				} catch(e: Throwable) {
					null
				}
			}
		}
	}
}
