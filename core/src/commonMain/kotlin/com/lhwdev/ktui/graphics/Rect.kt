package com.lhwdev.ktui.graphics

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Represents a floating-point rectangle, which is positioned and sized.
 */
data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) {
	companion object {
		fun sized(x: Float, y: Float, width: Float, height: Float): Rect =
			Rect(x, y, x + width, y + height)
		
		fun sized(position: Position, size: Size): Rect =
			Rect(position.x, position.y, size.width, size.height)
	}
	
	
	val width get() = right - left
	
	val height get() = bottom - top
	
	val longestSide get() = max(width, height)
	val shortestSide get() = min(width, height)
	
	val center get() = Position((left + right) / 2, (top + bottom) / 2)
	
	val distance get() = sqrt(pow(width) + pow(height))
	
	fun extendTo(other: Rect): Rect =
		Rect(min(left, other.left), min(top, other.top), max(right, other.right), max(bottom, other.bottom))
	
	fun intersects(point: Position): Boolean = point.x in left..right && point.y in top..bottom
	
	fun intersects(other: Rect): Boolean =
		!(right <= other.left || other.right <= left || bottom <= other.top || other.bottom <= top)
	
	operator fun contains(inner: Rect): Boolean =
		inner.left >= left && inner.top >= top && inner.right <= right && inner.bottom <= bottom
	
	@Suppress("NOTHING_TO_INLINE")
	inline operator fun contains(inner: Position): Boolean = intersects(inner)
}
