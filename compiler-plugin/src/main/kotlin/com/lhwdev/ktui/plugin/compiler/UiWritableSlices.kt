package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.util.slicedMap.BasicWritableSlice
import org.jetbrains.kotlin.util.slicedMap.RewritePolicy
import org.jetbrains.kotlin.util.slicedMap.WritableSlice

object UiWritableSlices {
	val WIDGET_ANALYSIS: WritableSlice<KtElement, WidgetKind> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)
	
	val FCS_RESOLVEDCALL_WIDGET: WritableSlice<KtElement, WidgetKind> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)
}
