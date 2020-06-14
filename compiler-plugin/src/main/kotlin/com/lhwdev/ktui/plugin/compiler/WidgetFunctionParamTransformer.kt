package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.irBuiltIns
import com.lhwdev.ktui.plugin.compiler.util.irConstructorCall
import com.lhwdev.ktui.plugin.compiler.util.scope
import com.lhwdev.ktui.plugin.compiler.util.toIrType
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.descriptors.WrappedValueParameterDescriptor
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.types.isPrimitiveType
import org.jetbrains.kotlin.ir.types.withHasQuestionMark
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import java.util.Stack


class WidgetFunctionParamTransformer : IrElementTransformerVoid(), UiIrPhase {
	private val remapStack = Stack<Map<IrValueParameterSymbol, IrValueParameterSymbol>>()
	
	override fun lower() {
		+WidgetTypeTransformer()
		target.transform(this, null)
	}
	
	override fun visitFunction(declaration: IrFunction) = with(declaration) {
		widgetMarker?.let {
			return withWidgetParameter(it)
		}
		
		super.visitFunction(this)
	}
	
	private fun IrFunction.withWidgetParameter(kind: WidgetTransformationKind): IrFunction {
		if(isExpect) return this
		
		val fn = copyLight()
		val originalParams = valueParameters
		val realParams = originalParams.map {
			if(it.defaultValue == null) it
			else {
				val descriptor = WrappedValueParameterDescriptor()
				
				IrValueParameterImpl(it.startOffset, it.endOffset, it.origin,
					symbol = IrValueParameterSymbolImpl(descriptor), name = it.name, index = it.index,
					type = if(!it.type.isNullable() && !it.type.isPrimitiveType()) it.type.withHasQuestionMark(true) else it.type,
					varargElementType = it.varargElementType, isCrossinline = it.isCrossinline, isNoinline = it.isNoinline
				).apply {
					descriptor.bind(this)
					annotations += scope.irConstructorCall(UiLibraryDescriptors.defaultParameter.constructors.single())
					// defaultValue = null // don't let kotlin compiler handle defaultValue; we already handled
				}
			}
		}
		
		fn.valueParameters = fn.valueParameters.toMutableList().also {
			realParams.forEachIndexed { i, parameter -> it[i] = parameter }
		}
		
		val buildScope = fn.addValueParameter(Widgets.buildScope, UiLibraryDescriptors.buildScope.defaultType.toIrType())
		
		val changed = List(widgetChangedParamsCount(originalParams.size)) { i ->
			fn.addValueParameter(Widgets.changed + if(i == 0) "" else "$i", irBuiltIns.intType)
		}
		
		val default = if(originalParams.any { it.defaultValue != null }) fn.addValueParameter(Widgets.default, irBuiltIns.intType) else null
		
		val infoForCall = WidgetTransformationInfoForCall(kind = kind, functionSymbol = fn.symbol, realParameters = realParams.map { it.symbol },
			buildScopeParameter = buildScope.symbol, changedParameters = changed.map { it.symbol }, defaultParameter = default?.symbol)
		
		uiTrace.record(UiWritableSlices.WIDGET_TRANSFORMATION_INFO, fn,
			WidgetTransformationInfo(kind, fn, infoForCall, originalParams, realParams, buildScope, changed, default))
		uiTrace.record(UiWritableSlices.WIDGET_TRANSFORMATION_INFO_FOR_CALL, this, infoForCall)
		
		remapStack.push(originalParams.mapIndexed { i, original -> original.symbol to realParams[i].symbol }.toMap())
		fn.body?.transform(this@WidgetFunctionParamTransformer, null)
		remapStack.pop()
		
		return fn
	}
	
	override fun visitGetValue(expression: IrGetValue): IrExpression {
		remapStack.asReversed().forEach {
			val symbol = it[expression.symbol]
			if(symbol != null) return IrGetValueImpl(expression.startOffset, expression.endOffset, symbol, expression.origin)
		}
		return super.visitGetValue(expression)
	}
}
