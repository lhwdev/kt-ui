package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.util.IrElementScope
import com.lhwdev.ktui.plugin.compiler.util.irBitAnd
import com.lhwdev.ktui.plugin.compiler.util.irGet
import com.lhwdev.ktui.plugin.compiler.util.irInt
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import kotlin.math.max


fun changedParameterCount(count: Int) = max((count - 1) / 32 + 1, 1)


/**
 * [width] must be a divisor of `32` (`width | 32`), and the type of all value should be [Int].
 */
class IrBitDataSetMapping(val variables: List<IrValueDeclaration>, val width: Int) {
	val dataPerVariable = Int.SIZE_BITS / width
	
	fun valueAt(index: Int) = variables[index / dataPerVariable]
	
	fun irBitAtUnshifted(index: Int, scope: IrElementScope) = with(scope) {
		val leftShiftsNeeded = index % dataPerVariable
		irBitAnd(irGet(valueAt(index)), irInt(leftShiftsNeeded * width)) to leftShiftsNeeded
	}
}
