package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.util.slicedMap.BasicWritableSlice
import org.jetbrains.kotlin.util.slicedMap.RewritePolicy
import org.jetbrains.kotlin.util.slicedMap.WritableSlice


object UiWritableSlices {
	val WIDGET_ANALYSIS: WritableSlice<KtElement, Boolean> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)
	
	val WIDGET_KIND: WritableSlice<IrFunction, WidgetTransformationKind> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)
	
	val WIDGET_TRANSFORMATION_INFO: WritableSlice<IrFunction, WidgetTransformationInfo> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)
	
	val WIDGET_TRANSFORMATION_INFO_FOR_CALL: WritableSlice<IrFunction, WidgetTransformationInfoForCall> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)
	
	val FCS_RESOLVED_CALL_WIDGET: WritableSlice<KtElement, Boolean> =
		BasicWritableSlice(RewritePolicy.DO_NOTHING)

//	val IS_WIDGET_CALL: WritableSlice<IrFunctionAccessExpression, WidgetKind> =
//		BasicWritableSlice(RewritePolicy.DO_NOTHING)
}
