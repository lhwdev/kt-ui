package com.lhwdev.ktui.text.font

import com.lhwdev.ktui.Immutable


@Immutable
interface Font {
	val name: String
	
	val weight: FontWeight
	
	val style: FontStyle
}


/**
 * Create a [FontFamily] from this single [Font].
 */
fun Font.asFontFamily() = fontFamily(this)
