@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui


@Widget
@InlineWidget // if @InlineWidget does not work, this works strangely
inline fun currentId() = currentScope().currentElement.id


fun test() {
	currentId()
	
}

