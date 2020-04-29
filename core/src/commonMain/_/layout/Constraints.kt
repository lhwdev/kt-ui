package com.lhwdev.ktui.layout


data class Constraints(val minWidth: Float = 0f,
	val maxWidth: Float = Float.POSITIVE_INFINITY,
	val minHeight: Float = 0f,
	val maxHeight: Float = Float.POSITIVE_INFINITY) {
	companion object {
		fun tight(width: Float, height: Float) =
			Constraints(width, width, height, height)
		
		fun tightWidth(width: Float) = Constraints(minWidth = width, maxWidth = width)
		
		fun tightHeight(height: Float) = Constraints(minHeight = height, maxHeight = height)
	}
}
