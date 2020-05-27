package com.lhwdev.ktui

import com.lhwdev.ktui.elements.SingleAmbientElement


fun <T> ambientOf(defaultValue: (() -> T)? = null): Ambient<T> = DynamicAmbient(defaultValue)

fun <T> staticAmbientOf(defaultValue: (() -> T)? = null): Ambient<T> = StaticAmbient(defaultValue)


// TODO: diagnostic - if, e.g., getting ambient failed, error log won't show the name of ambient.

@Widget
inline fun <T> Provide(ambient: Ambient<T>, value: T, child: @Widget () -> Unit) {
	val scope = currentScope()
	scope.element(nextId(), state = value, elementCreator = { SingleAmbientElement(ambient.createValue(value)) }) {
		scope.widget(0) {
			child() // child() is inlined but AmbientElement is proxy, so causes error
		}
	}
}

/*
 * Ambient structure:
 * Ambient -> AmbientElement -> AmbientValue
 *            (single, set)   (static, dynamic)
 *
 * ambientOf() -> DynamicAmbient
 *
 * Provide(dynamicElement) -> SingleAmbientElement(dynamicAmbient.createValue(...))
 * Provide(a, b, c, ...) -> AmbientSetElement(a.createValue(), ...)
 *
 *
 * - Ambient -> AmbientElement: externally like Provide
 *
 * - AmbientElement -> AmbientValue: hold
 *
 * Generic type association:
 * - Ambient<T> -> AmbientElement -> AmbientValue<T>
 * - 1 ambient = 1 ambientValue
 *
 * - AmbientValue holds providedValue -> if value has changed, call rebuild
 */


sealed class Ambient<T>(val defaultValue: (() -> T)?) {
	@WidgetUtil
	operator fun <T> invoke() = currentScope().currentElement.ambient(this)
	
	abstract fun createValue(value: T): AmbientValue<T>
}


abstract class AmbientValue<T>(val ambient: Ambient<T>, private var value: T) : Mergeable<AmbientValue<T>> {
	val providedValue: T get() = value
	
	fun get(consumer: Element<*>): T {
		getValue(consumer)
		return value
	}
	
	open fun getValue(consumer: Element<*>) {}
	
	// this should be called only when the value is updated
	open fun valueUpdated(thisElement: Element<*>, newValue: T) {
		value = newValue
	}
	
	abstract fun mergeFrom(from: AmbientValue<T>): AmbientValue<T>
	
	override fun merge(other: AmbientValue<T>) =
		if(ambient === other.ambient) other.mergeFrom(this) else other
}


/// Static
internal class StaticAmbient<T>(defaultValue: (() -> T)?) : Ambient<T>(defaultValue) {
	override fun createValue(value: T) = StaticAmbientValue(this, value)
}

internal class StaticAmbientValue<T>(ambient: Ambient<T>, value: T) : AmbientValue<T>(ambient, value) {
	override fun valueUpdated(thisElement: Element<*>, newValue: T) {
		super.valueUpdated(thisElement, newValue)
		if(thisElement.isAttached)
			thisElement.removeChildren()
	}
	
	override fun mergeFrom(from: AmbientValue<T>) =
		StaticAmbientValue(ambient, from.providedValue.mergeIfMergeable(providedValue))
}

/// Dynamic
internal class DynamicAmbient<T>(defaultValue: (() -> T)?) : Ambient<T>(defaultValue) {
	override fun createValue(value: T) = DynamicAmbientValue(this, value)
}

internal class DynamicAmbientValue<T>(ambient: Ambient<T>, value: T) : AmbientValue<T>(ambient, value) {
	private val dependents = mutableSetOf<Element<*>>()
	
	override fun getValue(consumer: Element<*>) {
		dependents += consumer
	}
	
	override fun valueUpdated(thisElement: Element<*>, newValue: T) {
		super.valueUpdated(thisElement, newValue)
		TODO()
	}
	
	override fun mergeFrom(from: AmbientValue<T>) =
		DynamicAmbientValue(ambient, from.providedValue.mergeIfMergeable(providedValue))
}


data class AmbientItem<T>(val ambient: Ambient<T>, val value: T) : Mergeable<AmbientItem<T>> {
	override fun merge(other: AmbientItem<T>): AmbientItem<T> =
		if(ambient === other.ambient) AmbientItem(ambient, value.mergeIfMergeable(other.value))
		else other
}

interface Temp : Mergeable<Temp>

class AmbientSet(private val ambients: Set<AmbientValue<*>>) : Mergeable<AmbientSet>, Set<AmbientValue<*>> by ambients {
	constructor(vararg ambients: AmbientValue<*>) : this(ambients.toSet())
	
	override fun merge(other: AmbientSet): AmbientSet {
		val from = associateBy { it.ambient }
		val to = other.associateBy { it.ambient }
		val allAmbients = from.keys + to.keys
		return AmbientSet(allAmbients.map { ambient -> from[ambient].mergeIfMergeable(to[ambient])!! }.toSet())
	}
}


@Widget
/* inline */ fun <T> Provide(ambientSet: AmbientSet, child: @Widget () -> Unit) {
	val scope = currentScope()
	val staticAmbients = ambientSet.filter { it.ambient is StaticAmbient<*> }
	val dynamicAmbients = ambientSet.filter { it.ambient is DynamicAmbient<*> }
	
	
}

