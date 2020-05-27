package com.lhwdev.ktui.text

import com.lhwdev.ktui.Immutable
import com.lhwdev.ktui.utils.lerp


@Immutable
data class BaselineShift(val multiplier: Float) {
	companion object {
		/**
		 * Default baseline shift for superscript.
		 */
		val superscript = BaselineShift(0.5f)
		
		/**
		 * Default baseline shift for subscript.
		 */
		val subscript = BaselineShift(-0.5f)
		
		/**
		 * Constant for no baseline shift.
		 */
		val none = BaselineShift(0.0f)
	}
}


/**
 * Linearly interpolate two [BaselineShift]s.
 */
fun lerp(start: BaselineShift, stop: BaselineShift, fraction: Float) =
	BaselineShift(lerp(start.multiplier, stop.multiplier, fraction))
