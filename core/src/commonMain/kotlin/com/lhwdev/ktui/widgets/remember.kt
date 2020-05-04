package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.*
import kotlin.reflect.KProperty


@Widget
inline fun <T> remember(initialValue: () -> T) = currentScope {
	var result = nextItem()
	if(result === EMPTY) {
		result = initialValue()
		updateItem(result)
	}
	
	@Suppress("UNCHECKED_CAST")
	result as T
}

@Widget
inline fun <T> remember(vararg keys: Any?, initialValue: () -> T) = currentScope {
	val lastKeys = nextItem()
	val changed = lastKeys === EMPTY || !arrayEquals(lastKeys as Array<*>, keys)
	updateItem(keys)
	
	var result = nextItem()
	if(result === EMPTY || changed) {
		result = initialValue()
		updateItem(result)
	}
	
	@Suppress("UNCHECKED_CAST")
	result as T
}


@Widget
inline fun <T> state(initialValue: () -> T) =
	remember { State(initialValue(), currentScope().currentElement) } // this works properly of compiler plugin implemented properly

@Widget
inline fun <T> state(vararg keys: Array<Any?>, initialValue: () -> T) =
	remember(*keys) { State(initialValue(), currentScope().currentElement) } // this works properly of compiler plugin implemented properly

class State<T>(private var privateValue: T, private val currentElement: Element<*>) {
	var value: T
		get() = privateValue
		set(value) {
			privateValue = value
			currentElement.requestRebuild()
		}
	
	
	operator fun getValue(property: KProperty<*>, receiver: Any?) = value
	
	operator fun setValue(property: KProperty<*>, receiver: Any?, newValue: T) {
		value = newValue
	}
}



