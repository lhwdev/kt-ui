package com.lhwdev.ktui.text.font

class Font(val name: String,
	val weight: FontWeight = FontWeight.normal,
	val style: FontStyle = FontStyle.normal)


/**
 * Create a [FontFamily] from this single [Font].
 */
fun Font.asFontFamily(): FontFamily {
	return FontFamily(this)
}
