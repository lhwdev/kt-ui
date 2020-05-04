package com.lhwdev.ktui

import com.lhwdev.ktui.utils.lerp


inline class Color(private val value: UInt) {
	companion object {
		val black = Color(0xff000000u)
	}
	
	
	constructor(value: Long) : this(value.toUInt()) // temporary
	
	constructor(alpha: Int, red: Int, green: Int, blue: Int) : this((
		alpha.toUInt() shl 24)
		or (red.toUInt() shl 16)
		or (green.toUInt() shl 8)
		or (blue.toUInt()))
	
	
	fun toUInt() = value
	
	
	val alpha get() = (value shr 24).toInt()
	val red get() = (value shr 16 and 0xffu).toInt()
	val green get() = (value shr 8 and 0xffu).toInt()
	val blue get() = (value and 0xffu).toInt()
}


fun argb(alpha: Int, red: Int, green: Int, blue: Int): Color = Color(alpha, red, green, blue)

fun lerp(start: Color, end: Color, fraction: Float) =
	Color(lerp(start.alpha, end.alpha, fraction),
		lerp(start.red, end.red, fraction),
		lerp(start.green, end.green, fraction),
		lerp(start.blue, end.blue, fraction))
