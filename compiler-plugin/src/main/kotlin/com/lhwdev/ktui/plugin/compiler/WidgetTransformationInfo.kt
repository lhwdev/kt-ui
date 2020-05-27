package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol


data class WidgetTransformationInfo(
	val kind: WidgetTransformationKind,
	val function: IrFunction,
	val infoForCall: WidgetTransformationInfoForCall,
	val realParameters: List<IrValueParameter>,
	val buildScopeParameter: IrValueParameter, val idParameter: IrValueParameter,
	val changedParameters: List<IrValueParameter>, val defaultParameter: IrValueParameter?
) {
	override fun toString(): String {
		return "WidgetTransformationInfo(kind=$kind, function=`${function.descriptor.dump()}`, realParameters=[${realParameters.joinToString { it.name.toString() }}], buildScopeParameter=${buildScopeParameter.name}, idParameter=${idParameter.name}, changedParameters=[${changedParameters.joinToString { it.name.toString() }}], defaultParameter=${defaultParameter?.name})"
	}
}


data class WidgetTransformationInfoForCall(
	val kind: WidgetTransformationKind,
	val functionSymbol: IrFunctionSymbol,
	val realParameters: List<IrValueParameterSymbol>,
	val buildScopeParameter: IrValueParameterSymbol, val idParameter: IrValueParameterSymbol,
	val changedParameters: List<IrValueParameterSymbol>, val defaultParameter: IrValueParameterSymbol?
) {
	lateinit var dirty: List<IrValueDeclaration>
}
