@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui


@Widget
@InlineWidget // if @InlineWidget does not work, this works strangely
inline fun currentId() = currentScope().currentElement.id


@Widget
inline fun <R> currentScope(block: BuildScope.() -> R) = currentScope().block()
