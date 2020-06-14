package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol


enum class ParamState(val bits: Int) {
	Unknown(0b00),
	Same(0b01),
	Changed(0b10),
	Static(0b11)
}

const val PARAM_STATE_WIDTH = 2

const val PARAM_COUNT_PER_INT = 32 / PARAM_STATE_WIDTH


// if has no parameter, no dirty nor changed
fun widgetChangedParamsCount(realParams: Int) =
	realParams / PARAM_COUNT_PER_INT + 1 // why not Long?

fun indexesForParameter(index: Int) =
	index / PARAM_COUNT_PER_INT to index % PARAM_COUNT_PER_INT * PARAM_STATE_WIDTH

fun IrElementScope.stateOf(target: IrExpression, parameters: List<IrValueParameterSymbol>, dirtyList: List<IrValueSymbol>, shifts: Int): IrExpression? {
	val visitor = WidgetStaticObserver()
	val type = target.accept(visitor, null)
	val dependencies = visitor.dependencies
	
	log3("dependency analyze: $type / [${dependencies.joinToString { it.descriptor.name.toString() }}] for [${parameters.joinToString { it.descriptor.name.toString() }}]")
	if(type == DependType.static) return irInt(0b11 shl shifts)
	val aDependency = dependencies.singleOrNull() as? IrValueParameterSymbol
	if(aDependency != null) {
		val index = parameters.indexOf(aDependency)
		if(index != -1) {
			val (varIndex, bitIndex) = indexesForParameter(index + 1)
			return irAnd(irShiftBits(irGet(dirtyList[varIndex]), shifts - bitIndex), irInt(0b11))
		}
	}
	
	return null
}


// .. b2[0000 0000 .. 0000] b1[0000 0000 .. 0000]
class ParamStateList(val variables: List<IrValueParameterSymbol>) {
	
	// ($changedA shr 1) and 0b1100
	fun IrElementScope.shiftBitAt(index: Int, toIndex: Int): IrExpression {
		val deltaIndex = toIndex - index
		return irAnd(irShr(irGet(variables[index / PARAM_COUNT_PER_INT]), irInt(deltaIndex % PARAM_COUNT_PER_INT * 2)), irInt(0b11 shl toIndex))
	}
	
	// $changed and 0b1100..
	fun IrElementScope.bitAtUnshifted(index: Int) =
		irAnd(irGet(variables[index / PARAM_COUNT_PER_INT]), irInt(0b11 shl (index % PARAM_COUNT_PER_INT * 2)))
}
