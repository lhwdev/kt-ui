package com.lhwdev.ktui.text


expect class Locale(language: String) {
	companion object {
		val current: Locale
	}
	
	
	/**
	 * The ISO 639 compliant language code.
	 */
	val language: String
	
	/**
	 * The ISO 3166 compliant region code.
	 */
	val region: String
	
	/**
	 * The name that can be used to display the locale name in user-friendly way.
	 */
	val displayName: String
}
