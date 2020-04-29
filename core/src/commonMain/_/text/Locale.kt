package com.lhwdev.ktui.text


expect class Locale(languageTag: String) {
	companion object {
		val current: Locale
	}
	
	
	/**
	 * The ISO 639 compliant language code.
	 */
	val language: String
	
	/**
	 * The ISO 15924 compliant 4-letter script code.
	 */
	val script: String
	
	/**
	 * The ISO 3166 compliant region code.
	 */
	val region: String
	
	/**
	 * Returns a IETF BCP47 compliant language tag representation of this Locale.
	 *
	 * @return A IETF BCP47 compliant language tag.
	 */
	fun toLanguageTag(): String
}
