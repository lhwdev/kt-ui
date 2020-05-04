package com.lhwdev.ktui.text.font


interface Font {
	val name: String
	
	val weight: FontWeight
	
	val style: FontStyle
}


/**
 * Create a [FontFamily] from this single [Font].
 */
fun Font.asFontFamily() = fontFamily(this)
