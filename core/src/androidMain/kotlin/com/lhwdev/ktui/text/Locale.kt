package com.lhwdev.ktui.text

import java.util.Locale as JLocale


actual class Locale(val platformLocale: JLocale) {
	actual constructor(language: String) : this(JLocale(language))
	
	
	actual companion object {
		actual val current get() = Locale(JLocale.getDefault())
	}
	
	/**
	 * The ISO 639 compliant language code.
	 */
	actual val language get() = platformLocale.language
	
	
	/**
	 * The ISO 3166 compliant region code.
	 */
	actual val region get() = platformLocale.country
	
	/**
	 * The name that can be used to display the locale name in user-friendly way.
	 */
	actual val displayName get() = platformLocale.displayName
}
