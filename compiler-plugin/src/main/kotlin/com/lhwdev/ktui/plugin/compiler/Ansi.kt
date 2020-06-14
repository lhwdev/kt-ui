@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui.plugin.compiler


object Ansi {
	val reset = AnsiItem("0")
	
	val black = AnsiItem("30") // black
	val red = AnsiItem("31") // red
	val green = AnsiItem("32") // green
	val yellow = AnsiItem("33") // yellow
	val blue = AnsiItem("34") // blue
	val purple = AnsiItem("35") // purple
	val cyan = AnsiItem("36") // cyan
	val white = AnsiItem("37") // white
	
	// high intensity
	val brightBlack = AnsiItem("90") // black
	val brightRed = AnsiItem("91") // red
	val brightGreen = AnsiItem("92") // green
	val brightYellow = AnsiItem("93") // yellow
	val brightBlue = AnsiItem("94") // blue
	val brightPurple = AnsiItem("95") // purple
	val brightCyan = AnsiItem("96") // cyan
	val brightWhite = AnsiItem("97") // white
	
	val bold = AnsiItem("1")
	val italic = AnsiItem("3")
	val underlined = AnsiItem("4")
	val framed = AnsiItem("51")
	val encircled = AnsiItem("52")
	val overlined = AnsiItem("53")
	val dim = AnsiItem("2")
	val strike = AnsiItem("9")
}

inline class AnsiItem(val ansiString: String) {
	inline operator fun plus(other: AnsiItem) = AnsiItem("$ansiString;${other.ansiString}")
	inline fun build() = "\u001b[0;${ansiString}m"
}

inline fun ansi(builder: Ansi.() -> AnsiItem) = Ansi.builder().build()

inline fun ansiItem(builder: Ansi.() -> AnsiItem) = Ansi.builder()

fun AnsiItem?.build() = this?.build() ?: ""

operator fun AnsiItem?.plus(other: AnsiItem?) = when {
	this == null -> other
	other == null -> this
	else -> this + other
}
