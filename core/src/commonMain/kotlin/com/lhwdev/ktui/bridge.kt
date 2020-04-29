@file:Suppress("unused", "UNUSED_PARAMETER")

package com.lhwdev.ktui


// DON'T ADD ANY DECLARATIONS HERE: WILL CAUSE COMPILER PLUGIN ERROR


@WidgetUtil
fun currentScope(): BuildScope = throw NotImplementedError("Implemented in compiler plugin")

fun BuildScope.bridgeCall(widget: @Widget () -> Unit) {
	throw NotImplementedError("Implemented in compiler plugin")
}
