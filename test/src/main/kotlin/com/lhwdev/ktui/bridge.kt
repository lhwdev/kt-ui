@file:Suppress("NOTHING_TO_INLINE", "unused", "UNUSED_PARAMETER")

package com.lhwdev.ktui


@Widget
inline fun currentScope(): BuildScope = error("Implemented in the compiler plugin")

@Widget
fun nextId(): Int = error("Implemented in the compiler plugin")

/**
 * Just purely 'calls' the widget.
 */
fun BuildScope.bridgeCall(widget: @Widget () -> Unit, changed: Int) {
	error("Implemented in the compiler plugin")
}
