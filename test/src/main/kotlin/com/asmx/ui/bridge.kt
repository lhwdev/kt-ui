@file:Suppress("NOTHING_TO_INLINE", "unused", "UNUSED_PARAMETER")

package com.lhwdev.ktui


@WidgetUtil
inline fun currentScope(): BuildScope = error("Implemented in the compiler plugin")

@Widget
fun nextId(): Int = error("Implemented in the compiler plugin")

fun BuildScope.bridgeCall(widget: @Widget () -> Unit) {
	error("Implemented in the compiler plugin")
}
