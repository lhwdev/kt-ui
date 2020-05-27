package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.irBuiltIns
import com.lhwdev.ktui.plugin.compiler.util.toIrType
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


class WidgetFunctionParamTransformer : IrElementTransformerVoid(), UiIrPhase {
	override fun lower() {
		+WidgetTypeTransformer()
		target.transform(this, null)
	}
	
	override fun visitFunction(declaration: IrFunction) = with(declaration) {
		widgetMarker?.let {
			return super.visitFunction(withWidgetParameter(it))
		}
		
		super.visitFunction(this)
	}
	
	private fun IrFunction.withWidgetParameter(kind: WidgetTransformationKind): IrFunction {
		if(isExpect) return this
		
		val fn = copyLight()
		val realParams = valueParameters
		
		val buildScope = fn.addValueParameter(Widgets.buildScope, UiLibraryDescriptors.buildScope.defaultType.toIrType())
		val id = fn.addValueParameter(Widgets.id, irBuiltIns.intType)
		
		val changed = List(widgetChangedParamsCount(realParams.size)) { i ->
			fn.addValueParameter(Widgets.changed + if(i == 0) "" else "$i", irBuiltIns.intType)
		}
		
		val default = if(realParams.any { it.defaultValue != null }) fn.addValueParameter(Widgets.default, irBuiltIns.intType) else null
		
		val infoForCall = WidgetTransformationInfoForCall(kind, fn.symbol, realParams.map { it.symbol }, buildScope.symbol, id.symbol, changed.map { it.symbol }, default?.symbol)
		
		uiTrace.record(UiWritableSlices.WIDGET_TRANSFORMATION_INFO, fn,
			WidgetTransformationInfo(kind, fn, infoForCall, realParams, buildScope, id, changed, default))
		uiTrace.record(UiWritableSlices.WIDGET_TRANSFORMATION_INFO_FOR_CALL, this, infoForCall)
		
		return fn
	}
}
