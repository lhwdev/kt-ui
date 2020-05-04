package com.lhwdev.ktui.elements

import com.lhwdev.ktui.Ambient
import com.lhwdev.ktui.DynamicAmbient
import com.lhwdev.ktui.Element
import com.lhwdev.ktui.StaticAmbient


internal abstract class AmbientElement<T>(val ambient: Ambient<T>) : ProxyElement<T>(), SpecialElement {
	val value: T  get() = state
	
	abstract fun getValue(consumer: Element<*>): T
	
	override fun updateAmbients(thisAmbient: Set<AmbientElement<*>>, childrenAmbient: Set<AmbientElement<*>>) {
		super.updateAmbients(thisAmbient, childrenAmbient + this)
	}
}

internal class StaticAmbientElement<T>(ambient: StaticAmbient<T>) : AmbientElement<T>(ambient) {
	override fun getValue(consumer: Element<*>) = value
	
	override fun stateUpdated(newState: T) {
		super.stateUpdated(newState)
		
		removeChildren()
	}
}

internal class DynamicAmbientElement<T>(ambient: DynamicAmbient<T>) : AmbientElement<T>(ambient) {
	private val observers = mutableSetOf<Element<*>>()
	
	override fun getValue(consumer: Element<*>): T {
		observers += consumer // this is wrong!
		return value
	}
	
	override fun stateUpdated(newState: T) {
		super.stateUpdated(newState)
		
		TODO()
	}
}
