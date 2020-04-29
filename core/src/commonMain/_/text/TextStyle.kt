package com.lhwdev.ktui.text

import com.lhwdev.ktui.Color
import com.lhwdev.ktui.Mergeable
import com.lhwdev.ktui.graphics.Paint
import com.lhwdev.ktui.graphics.Shadow
import com.lhwdev.ktui.text.font.FontFamily
import com.lhwdev.ktui.text.font.FontStyle
import com.lhwdev.ktui.text.font.FontWeight
import com.lhwdev.ktui.text.style.TextDecoration


data class TextStyle(
	val color: Color? = null,
	val foreground: Paint? = color?.let { Paint { this.color = it } },
	val backgroundColor: Color? = null,
	val background: Paint? = backgroundColor?.let { Paint { this.color = it } },
	val fontSize: Float = -1f,
	val fontSizeScale: Float = -1f,
	val fontWeight: FontWeight? = null,
	val fontStyle: FontStyle? = null,
	val fontFamily: FontFamily? = null,
	/** in unit em, which is `final font size * letterSpacing` */
	val letterSpacing: Float = Float.MIN_VALUE,
	val baselineShift: Float = Float.MIN_VALUE,
	val locale: Locale? = null,
	val decoration: TextDecoration? = null,
	val shadow: List<Shadow>? = null
) : Mergeable<TextStyle> {
	override fun merge(other: TextStyle) = TextStyle()
}
