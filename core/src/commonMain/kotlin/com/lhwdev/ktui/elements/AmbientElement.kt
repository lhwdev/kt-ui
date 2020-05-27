package com.lhwdev.ktui.elements

import com.lhwdev.ktui.Ambient
import com.lhwdev.ktui.AmbientSet
import com.lhwdev.ktui.AmbientValue


@PublishedApi
internal abstract class AmbientElement<T> : ProxyElement<T>(), SpecialElement {
	abstract fun getValue(ambient: Ambient<*>): AmbientValue<*>?
}

@PublishedApi
internal class SingleAmbientElement<T>(val ambientValue: AmbientValue<T>) : AmbientElement<T>() {
	override fun getValue(ambient: Ambient<*>) =
		(if(ambient === ambientValue.ambient) ambientValue else null)
}

@PublishedApi
internal class AmbientSetElement(val ambientSet: AmbientSet) : AmbientElement<AmbientSet>() {
	override fun getValue(ambient: Ambient<*>) = ambientSet.find { it.ambient === ambient }
}

//internal abstract class AmbientElement<T>(val ambient: Ambient<T>) : ProxyElement<T>(), SpecialElement {
//	private var privateValue: T? = null
//
//	val providedValue: T get() = state
//
//	@Suppress("UNCHECKED_CAST")
//	val mergedValue: T
//		get() = privateValue as T
//
//	abstract fun getValue(consumer: Element<*>): T
//
//	override fun updateAmbients(thisAmbient: Set<AmbientElement<*>>, childrenAmbient: Set<AmbientElement<*>>) {
//		val newAmbients = mutableMapOf<Ambient<*>, AmbientElement<*>>()
//		childrenAmbient.associateByTo(newAmbients) { it.ambient }
//
//		val last = newAmbients[ambient]
//
//		@Suppress("UNCHECKED_CAST")
//		privateValue = last?.mergedValue.mergeIfMergeable(providedValue) as T
//
//		newAmbients[ambient] = this
//		super.updateAmbients(thisAmbient, newAmbients.values.toSet())
//	}
//}
//
//internal class StaticAmbientElement<T>(ambient: StaticAmbient<T>) : AmbientElement<T>(ambient) {
//	override fun getValue(consumer: Element<*>) = mergedValue
//
//	override fun stateUpdated(newState: T) {
//		super.stateUpdated(newState)
//
//		removeChildren()
//	}
//}
//
//internal class DynamicAmbientElement<T>(ambient: DynamicAmbient<T>) : AmbientElement<T>(ambient) {
//	private val observers = mutableSetOf<Element<*>>()
//
//	override fun getValue(consumer: Element<*>): T {
//		observers += consumer // this is wrong!
//		return mergedValue
//	}
//
//	override fun stateUpdated(newState: T) {
//		super.stateUpdated(newState)
//
//		TODO()
//	}
//}
//
//
//internal class AmbientSetElement(ambient: AmbientSet) : ProxyElement<AmbientSet>() {
//	private var privateValue: T? = null
//
//}
