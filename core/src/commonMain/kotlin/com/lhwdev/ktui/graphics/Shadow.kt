package com.lhwdev.ktui.graphics

import com.lhwdev.ktui.Color
import com.lhwdev.ktui.Immutable
import com.lhwdev.ktui.lerp
import com.lhwdev.ktui.utils.lerp


/**
 * A single shadow.
 */
@Immutable
data class Shadow(
	val color: Color = Color(0xff000000),
	val position: Position = Position.zero,
	val blurRadius: Float = 0f
) {
	companion object {
		val none = Shadow()
	}
}

/**
 * Linearly interpolate two [Shadow]s.
 */
fun lerp(start: Shadow, stop: Shadow, fraction: Float): Shadow {
	return Shadow(
		lerp(start.color, stop.color, fraction),
		lerp(start.position, stop.position, fraction),
		lerp(start.blurRadius, stop.blurRadius, fraction)
	)
}
