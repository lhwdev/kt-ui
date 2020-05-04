package com.lhwdev.ktui.text

import com.lhwdev.ktui.Color
import com.lhwdev.ktui.Immutable
import com.lhwdev.ktui.Mergeable
import com.lhwdev.ktui.graphics.Paint
import com.lhwdev.ktui.graphics.Shadow
import com.lhwdev.ktui.text.font.FontFamily
import com.lhwdev.ktui.text.font.FontStyle
import com.lhwdev.ktui.text.font.FontWeight
import com.lhwdev.ktui.text.style.TextDecoration


@Immutable
data class TextStyle(
	val color: Color? = null,
	val fontSize: Float = -1f,
	val fontWeight: FontWeight? = null,
	val fontStyle: FontStyle? = null,
//	val fontSynthesis: FontSynthesis? = null,
	val fontFamily: FontFamily? = null,
	val fontFeatureSettings: String? = null,
	val letterSpacing: Float = -1f,
	val baselineShift: BaselineShift? = null,
	val textScaleX: Float = -1f,
	val textSkewX: Float = -1f,
	val localeList: LocaleList? = null,
	val background: Color = Color.Unset,
	val textDecoration: TextDecoration? = null,
	val shadow: Shadow? = null,
	val textAlign: TextAlign? = null,
//	val textDirectionAlgorithm: TextDirectionAlgorithm? = null,
	val lineHeight: Float = -1f,
	val textIndent: TextIndent? = null,
	
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
