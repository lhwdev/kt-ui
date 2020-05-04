package com.lhwdev.ktui.text.style


/**
 * Defines a horizontal line to be drawn on the text.
 */
data class TextDecoration internal constructor(val mask: Int) {
	companion object {
		val none: TextDecoration = TextDecoration(0x0)
		
		/**
		 * Draws a horizontal line below the text.
		 */
		val underline: TextDecoration = TextDecoration(0x1)
		
		/**
		 * Draws a horizontal line over the text.
		 */
		val lineThrough: TextDecoration = TextDecoration(0x2)
		
		/**
		 * Creates a decoration that includes all the given decorations.
		 *
		 * @param decorations The decorations to be added
		 */
		fun combine(decorations: List<TextDecoration>): TextDecoration {
			val mask = decorations.fold(0) { acc, decoration ->
				acc or decoration.mask
			}
			return TextDecoration(mask)
		}
	}
	
	/**
	 * Check whether this [TextDecoration] contains the given decoration.
	 *
	 * @param other The [TextDecoration] to be checked.
	 */
	fun contains(other: TextDecoration): Boolean {
		return (mask or other.mask) == mask
	}
	
	override fun toString(): String {
		if(mask == 0) {
			return "TextDecoration.none"
		}
		val values: MutableList<String> = mutableListOf()
		
		if((mask and underline.mask) != 0)
			values.add("underline")
		if((mask and lineThrough.mask) != 0)
			values.add("lineThrough")
		
		return if((values.size == 1)) "TextDecoration.${values[0]}"
		else "TextDecoration.combine([${values.joinToString()}])"
	}
}
