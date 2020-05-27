package com.lhwdev.ktui

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@WidgetUtil
inline fun <T> remember(initialValue: () -> T) = currentScope {
	var result = nextItem()
	if(result === EMPTY) {
		result = initialValue()
		updateItem(result)
	}
	
	@Suppress("UNCHECKED_CAST")
	result as T
}

@WidgetUtil
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


@WidgetUtil
inline fun <T> state(initialValue: () -> T) =
	remember { State(initialValue(), currentScope().currentElement) } // this works properly of compiler plugin implemented properly

@WidgetUtil
inline fun <T> state(vararg keys: Array<Any?>, initialValue: () -> T) =
	remember(*keys) { State(initialValue(), currentScope().currentElement) } // this works properly of compiler plugin implemented properly

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
class State<T>(private var privateValue: T, private val currentElement: Element<*>) : ReadWriteProperty<Any?, T> {
	var value: T
		get() = privateValue
		set(value) {
			privateValue = value
			currentElement.requestRebuild()
		}
	
	
	override inline operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
	
	override inline operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		this.value = value
	}
}



