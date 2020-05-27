package com.lhwdev.ktui.text

import com.lhwdev.ktui.Color
import com.lhwdev.ktui.Immutable
import com.lhwdev.ktui.Mergeable
import com.lhwdev.ktui.graphics.FrameworkPaint
import com.lhwdev.ktui.graphics.Shadow
import com.lhwdev.ktui.text.font.FontFamily
import com.lhwdev.ktui.text.font.FontStyle
import com.lhwdev.ktui.text.font.FontWeight
import com.lhwdev.ktui.text.style.TextDecoration


private val sNoneFloat = Float.NEGATIVE_INFINITY

private val sDefaultStyle = TextStyle(
	color = Color.black,
	fontSize = 14f,
	fontWeight = FontWeight.normal,
	fontStyle = FontStyle.normal,
	fontFamily = FontFamily.sansSerif,
	fontFeatureSettings = "",
	letterSpacing = 0f,
	baselineShift = BaselineShift.none,
	textScaleX = 1f,
	textSkewX = 0f,
	localeList = defaultLocaleList,
	background = Color.transparent,
	textDecoration = TextDecoration.none,
	shadow = Shadow.none,
	lineHeight = 1f,
)


private inline fun Float.or(block: () -> Float) = if(this == sNoneFloat) block() else this


internal expect fun TextStyle.toFrameworkPaintInternal(): FrameworkPaint


@Immutable
data class TextStyle(
	val color: Color? = null,
	val fontSize: Float = sNoneFloat,
	val fontWeight: FontWeight? = null,
	val fontStyle: FontStyle? = null,
//	val fontSynthesis: FontSynthesis? = null,
	val fontFamily: FontFamily? = null,
	val fontFeatureSettings: String? = null,
	val letterSpacing: Float = sNoneFloat,
	val baselineShift: BaselineShift? = null,
	val textScaleX: Float = sNoneFloat,
	val textSkewX: Float = sNoneFloat,
	val localeList: LocaleList? = null,
	val background: Color? = null,
	val textDecoration: TextDecoration? = null,
	val shadow: Shadow? = null,
//	val textAlign: TextAlign? = null,
//	val textDirectionAlgorithm: TextDirectionAlgorithm? = null,
	val lineHeight: Float = sNoneFloat,
//	val textIndent: TextIndent? = null,
) : Mergeable<TextStyle> {
	private val frameworkPaintCache by lazy(LazyThreadSafetyMode.NONE) { toFrameworkPaintInternal() }
	
	fun toFrameworkPaint() = frameworkPaintCache
	
	
	fun orDefault() = sDefaultStyle.merge(this)
	
	
	override fun merge(other: TextStyle) = TextStyle(
		color = other.color ?: color,
		fontSize = other.fontSize.or { fontSize },
		fontWeight = other.fontWeight ?: fontWeight,
		fontStyle = other.fontStyle ?: fontStyle,
		fontFamily = other.fontFamily ?: fontFamily,
		fontFeatureSettings = other.fontFeatureSettings ?: fontFeatureSettings,
		letterSpacing = other.letterSpacing.or { letterSpacing },
		baselineShift = other.baselineShift ?: baselineShift,
		textScaleX = other.textScaleX.or { textScaleX },
		textSkewX = other.textSkewX.or { textSkewX },
		localeList = other.localeList ?: localeList,
		background = other.background ?: background,
		textDecoration = other.textDecoration ?: textDecoration,
		shadow = other.shadow ?: shadow,
		lineHeight = other.lineHeight.or { lineHeight }
	)
}
