package com.lhwdev.ktui.plugin.compiler.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Note that this can cause leak if the result of [block] depends on the result of [by].
 */
fun <T> cacheBy(by: () -> Any?, block: () -> T) = object : ReadOnlyProperty<Any?, T> {
	private var hasLast = false
	private var lastBy: Any? = null
	private var cache: T? = null
	
	private fun makeCache(): T {
		val value = block()
		cache = value
		return value
	}
	
	override fun getValue(thisRef: Any?, property: KProperty<*>): T = if(hasLast) {
		val newBy = by()
		if(lastBy == newBy) @Suppress("UNCHECKED_CAST") (cache as T)
		else {
			lastBy = newBy
			makeCache()
		}
	} else {
		lastBy = by()
		makeCache()
	}
}
