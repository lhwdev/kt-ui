package com.lhwdev.ktui.graphics

import com.lhwdev.ktui.Immutable
import com.lhwdev.ktui.utils.lerp
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt


@Immutable
data class Position(val x: Float, val y: Float) {
	companion object {
		val zero = Position(0f, 0f)
	}
	
	
	operator fun plus(other: Position): Position = Position(x + other.x, y + other.y)
	
	operator fun minus(other: Position): Position = Position(x - other.x, y - other.y)
	
	operator fun times(operand: Float): Position = Position(x * operand, y * operand)
	
	operator fun div(operand: Float): Position = Position(x / operand, y / operand)
	
	fun distance(other: Position): Float = sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
	
	operator fun unaryMinus(): Position = Position(-x, -y)
	
	fun scale(scaleX: Float, scaleY: Float = scaleX): Position = Position(x * scaleX, y * scaleY)
	
	
	override fun toString() = "(${x.toStringAsFixed(1)}, ${y.toStringAsFixed(1)})"
}


fun lerp(a: Position, b: Position, fraction: Float): Position =
	Position(lerp(a.x, b.x, fraction), lerp(a.y, b.y, fraction))


// actually this isn't a good implementation as I know well, but I am so lazy and foolish(?)
private fun Float.toStringAsFixed(digits: Int): String = (round(this * digits) / digits).toString()
