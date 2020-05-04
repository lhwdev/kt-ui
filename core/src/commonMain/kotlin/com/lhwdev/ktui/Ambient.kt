package com.lhwdev.ktui

import com.lhwdev.ktui.elements.DynamicAmbientElement
import com.lhwdev.ktui.elements.StaticAmbientElement


fun <T> ambientOf(defaultValue: (() -> T)? = null): Ambient<T> = DynamicAmbient(defaultValue)

fun <T> staticAmbientOf(defaultValue: (() -> T)? = null): Ambient<T> = StaticAmbient(defaultValue)


@WidgetUtil
operator fun <T> Ambient<T>.unaryPlus() = currentScope().currentElement.ambient(this)


// TODO: diagnostic

abstract class Ambient<T>(val defaultValue: (() -> T)?) {
	abstract fun createElement(): Element<T>
}

internal class StaticAmbient<T>(defaultValue: (() -> T)?) : Ambient<T>(defaultValue) {
	override fun createElement() = StaticAmbientElement(this)
}

internal class DynamicAmbient<T>(defaultValue: (() -> T)?) : Ambient<T>(defaultValue) {
	override fun createElement() = DynamicAmbientElement(this)
}


@Widget
inline fun <T> Provide(ambient: Ambient<T>, value: T, child: @Widget () -> Unit) {
	val scope = currentScope()
	scope.startWithElement(nextId(), state = value) { ambient.createElement() }
	if(scope.start(0, sInternalEmptyAttrs, -1) != 0)
		child() // child() is inlined though AmbientElement is proxy, so causes error
	scope.end()
	scope.end()
}
