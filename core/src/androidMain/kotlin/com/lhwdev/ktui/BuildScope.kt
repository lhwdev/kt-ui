@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui


actual fun stateChanged(a: Any?, b: Any?) = when {
	a == null -> b == null
	b == null -> false
//		a === b -> true // done by equals(..)
	a::class.java.componentType?.let { it == b::class.java.componentType } ?: false ->
		arrayEquals(a, b)
	else -> a == b
}





