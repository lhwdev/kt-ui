package com.lhwdev.ktui.text.font


/**
 * Defines a font family. It can be constructed via a generic font family such as serif, sans-serif
 * (i.e. FontFamily("sans-serif"). It can also be constructed by a set of custom fonts.
 */
sealed class FontFamily constructor(val canLoadSynchronously: Boolean) {
	companion object {
		/**
		 * Font family with low contrast and plain stroke endings.
		 *
		 * @see [CSS sans-serif](https://www.w3.org/TR/css-fonts-3/#sans-serif)
		 */
		val sansSerif = GenericFontFamily("sans-serif")
		
		/**
		 * The formal text style for scripts.
		 *
		 * @see [CSS serif](https://www.w3.org/TR/css-fonts-3/#serif)
		 */
		val serif = GenericFontFamily("serif")
		
		/**
		 * Font family where glyphs have the same fixed width.
		 *
		 * @see [CSS monospace](https://www.w3.org/TR/css-fonts-3/#monospace)
		 */
		val monospace = GenericFontFamily("monospace")
		
		/**
		 * Cursive, hand-written like font family.
		 *
		 * If the device doesn't support this font family, the system will fallback to the
		 * default font.
		 *
		 * @see [CSS cursive](https://www.w3.org/TR/css-fonts-3/#cursive)
		 */
		val cursive = GenericFontFamily("cursive")
	}
}



/**
 * A base class of [FontFamily]s that is created from file sources.
 */
sealed class FileBasedFontFamily : FontFamily(false)

/**
 * A base class of [FontFamily]s installed on the system.
 */
sealed class SystemFontFamily : FontFamily(true)

/**
 * Defines a font family with list of [Font].
 */
data class FontListFontFamily(val fonts: List<Font>) : FileBasedFontFamily(), List<Font> by fonts {
	init {
		check(fonts.isNotEmpty()) { "At least one font should be passed to FontFamily" }
		
		check(fonts.distinctBy { Pair(it.weight, it.style) }.size == fonts.size) {
			"There cannot be two fonts with the same FontWeight and FontStyle in the same " +
				"FontFamily"
		}
	}
}
/**
 * Construct a font family that contains list of custom font files.
 *
 * @param fonts list of font files
 */
fun fontFamily(fonts: List<Font>) = FontListFontFamily(fonts)

/**
 * Construct a font family that contains list of custom font files.
 *
 * @param fonts list of font files
 */
fun fontFamily(vararg fonts: Font) = FontListFontFamily(fonts.asList())

/**
 * Defines a font family with an generic font family name.
 *
 * If the platform cannot find the passed generic font family, use the platform default one.
 *
 * @param name a generic font family name, e.g. "serif", "sans-serif"
 * @see FontFamily.sansSerif
 * @see FontFamily.serif
 * @see FontFamily.monospace
 * @see FontFamily.cursive
 */
class GenericFontFamily internal constructor(val name: String) : SystemFontFamily()

/**
 * Defines a default font family.
 */
internal class DefaultFontFamily : SystemFontFamily()

/**
// * Defines a font family that is already loaded Typeface.
// *
// * @param typeface A typeface instance.
// */
//data class LoadedFontFamily(val typeface: Typeface) : FontFamily(true)
//
///**
// * Construct a font family that contains loaded font family: Typeface.
// *
// * @param typeface A typeface instance.
// */
//fun fontFamily(typeface: Typeface) = LoadedFontFamily(typeface)
