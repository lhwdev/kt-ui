@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui.plugin.compiler


enum class AnsiColor(val order: Int, val isBright: Boolean = false) {
	// reset
	reset(-1), // -1 = special
	
	// normal colors
	black(0), red(1), green(2), yellow(3),
	blue(4), purple(5), cyan(6), white(7),
	
	// bright colors
	blackBright(0, isBright = true), redBright(1, isBright = true),
	greenBright(2, isBright = true), yellowBright(3, isBright = true),
	blueBright(4, isBright = true), purpleBright(5, isBright = true),
	cyanBright(6, isBright = true), whiteBright(7, isBright = true);
	
	companion object {
		fun foregroundOfOrder(order: Int) = values()[order + 1]
	}
}

val AnsiColor.foreground get() = AnsiForeground(this)
val AnsiColor.background get() = AnsiBackground(this)

object Ansi {
	val reset = AnsiNumberElement(0)
	
	val bold = AnsiNumberElement(1)
	val italic = AnsiNumberElement(3)
	val underlined = AnsiNumberElement(4)
	val framed = AnsiNumberElement(51)
	val encircled = AnsiNumberElement(52)
	val overlined = AnsiNumberElement(53)
	val dim = AnsiNumberElement(2)
	val strike = AnsiNumberElement(9)
	
	val black = AnsiColor.black.foreground
	val red = AnsiColor.red.foreground
	val green = AnsiColor.green.foreground
	val yellow = AnsiColor.yellow.foreground
	val blue = AnsiColor.blue.foreground
	val purple = AnsiColor.purple.foreground
	val cyan = AnsiColor.cyan.foreground
	val white = AnsiColor.white.foreground
	
	// high intensity
	val brightBlack = AnsiColor.blackBright.foreground
	val brightRed = AnsiColor.redBright.foreground
	val brightGreen = AnsiColor.greenBright.foreground
	val brightYellow = AnsiColor.yellowBright.foreground
	val brightBlue = AnsiColor.blueBright.foreground
	val brightPurple = AnsiColor.purpleBright.foreground
	val brightCyan = AnsiColor.cyanBright.foreground
	val brightWhite = AnsiColor.whiteBright.foreground
}

fun AnsiElement(ansiString: String): AnsiItem = when(val ansi = ansiString.toIntOrNull()) {
	null -> AnsiString(ansiString)
	in 30..37 /* foreground */ -> AnsiForeground(enumValues<AnsiColor>()[ansi - 30])
	in 40..47 /* background */ -> AnsiBackground(enumValues<AnsiColor>()[ansi - 40])
	in 90..97 /* bright foreground */ -> AnsiForeground(enumValues<AnsiColor>()[ansi - 90 + 8])
	in 100..107 /* bright background */ -> AnsiBackground(enumValues<AnsiColor>()[ansi - 100 + 8])
	else -> AnsiString(ansiString)
}

//inline class AnsiItem(val ansiString: String, a: Int) {
//	inline operator fun plus(other: AnsiItem) = AnsiItem("$ansiString;${other.ansiString}")
//	inline fun build() = "\u001b[0;${ansiString}m"
//}

sealed class AnsiItem {
	abstract fun buildInternal(): String
	override fun toString() = "\u001b[${buildInternal()}m"
}

class AnsiItemList(val items: List<AnsiItem>) : AnsiItem() {
	override fun buildInternal() = items.joinToString(separator = ";") { it.buildInternal() }
}

sealed class AnsiElement : AnsiItem()

sealed class AnsiNumberElement : AnsiElement() {
	abstract val number: Int
	override fun buildInternal() = "$number"
}

fun AnsiNumberElement(number: Int): AnsiNumberElement = AnsiNumberElementImpl(number)

private class AnsiNumberElementImpl(override val number: Int) : AnsiNumberElement()

class AnsiForeground(val color: AnsiColor) : AnsiNumberElement() {
	override val number get() = if(color == AnsiColor.reset) 39 else (if(color.isBright) 90 else 30) + color.order
}

class AnsiBackground(val color: AnsiColor) : AnsiNumberElement() {
	override val number get() = if(color == AnsiColor.reset) 49 else (if(color.isBright) 100 else 40) + color.order
}

class AnsiString(val ansiString: String) : AnsiElement() {
	override fun buildInternal() = ansiString
}

operator fun AnsiItem.plus(other: AnsiItem) =
	if(this is AnsiItemList) AnsiItemList(items + other)
	else AnsiItemList(listOf(this, other))

fun ansi(builder: Ansi.() -> AnsiItem) = Ansi.builder().toString()

inline fun ansiItem(builder: Ansi.() -> AnsiItem) = Ansi.builder()

fun AnsiItem?.buildInternal() = this?.buildInternal() ?: ""

operator fun AnsiItem?.plus(other: AnsiItem?) = when {
	this == null -> other
	other == null -> this
	else -> this + other
}


// TODO: support full parsing + nested arguments(ex: 58;r;g;b <- one element)
fun parseAnsi(ansiString: String): AnsiItem =
	AnsiItemList(ansiString.removePrefix("\u001b[").removeSuffix("m").split(';').map { AnsiElement(it) })

data class AnsiStyle(
	val foreground: AnsiColor? = null,
	val background: AnsiColor? = null,
	val bold: Boolean? = null,
	val italic: Boolean? = null,
	val underlined: Boolean? = null,
	val framed: Boolean? = null,
	val encircled: Boolean? = null,
	val overlined: Boolean? = null,
	val dim: Boolean? = null,
	val strike: Boolean? = null
)

class AnsiStyleBuilder {
	var foreground: AnsiColor? = null
	var background: AnsiColor? = null
	var bold: Boolean? = null
	var italic: Boolean? = null
	var underlined: Boolean? = null
	var framed: Boolean? = null
	var encircled: Boolean? = null
	var overlined: Boolean? = null
	var dim: Boolean? = null
	var strike: Boolean? = null
	
	fun build() = AnsiStyle(
		foreground = foreground,
		background = background,
		bold = bold,
		italic = italic,
		underlined = underlined,
		framed = framed,
		encircled = encircled,
		overlined = overlined,
		dim = dim,
		strike = strike
	)
}

fun AnsiItem.toStyle(): AnsiStyle = AnsiStyleBuilder().apply {
	fun AnsiItem.process() {
		when(this) {
			is AnsiItemList -> items.onEach { process() }
			is AnsiForeground -> foreground = color
			is AnsiBackground -> background = color
			Ansi.bold -> bold = true
			Ansi.italic -> italic = true
			Ansi.underlined -> underlined = true
			Ansi.framed -> framed = true
			Ansi.encircled -> encircled = true
			Ansi.overlined -> overlined = true
			Ansi.dim -> dim = true
			Ansi.strike -> strike = true
			else -> Unit // TODO
		}
	}
	
	
}.build()
