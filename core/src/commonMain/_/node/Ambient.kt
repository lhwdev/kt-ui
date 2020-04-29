package com.lhwdev.ktui.node

import com.lhwdev.ktui.Ambient
import com.lhwdev.ktui.Element
import com.lhwdev.ktui.Mergeable


data class AmbientData<T : Any>(val ambient: Ambient<T>, val value: T)


data class AmbientNode(val data: AmbientData<*>) : ElementNode() {
	override fun createElement() = object : Element() {
		override fun updateAmbients(parentAmbients: Set<AmbientData<*>>) {
			// Be aware: don't use parentAmbients.contains or etc: equals means both ambient and value to be equal
			val ancestorAmbient = parentAmbients.find { data.ambient === it.ambient }
			@Suppress("UNCHECKED_CAST")
			super.updateAmbients(
				if(ancestorAmbient == null) parentAmbients + data
				else parentAmbients.transform {
					if(it.ambient === data.ambient) AmbientData(ambient = data.ambient as Ambient<Any>,
						value = tryMerge(data.value, ancestorAmbient.value))
					else it
				})
		}
	}
}


private fun tryMerge(ancestor: Any, descendant: Any) =
	@Suppress("UNCHECKED_CAST")
	if(ancestor is Mergeable<*>) (ancestor as Mergeable<Any>).merge(descendant)
	else descendant

private inline fun <T> Set<T>.transform(block: (T) -> T): Set<T> {
	val newSet = LinkedHashSet<T>(size)
	forEach { newSet.add(block(it)) }
	return newSet
}

//private fun <T> Set<T>.replace(from: T, to: T) = transform { if(it == from) to else it }
