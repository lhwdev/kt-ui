package com.lhwdev.ktui.elements

import com.lhwdev.ktui.EMPTY
import com.lhwdev.ktui.Element


open class WidgetElement : BaseElement<WidgetState>() {
	override val children = mutableListOf<Element<*>>()
	
	val attrs get() = state.attrs
	val keyIndex get() = state.keyIndex
	
	var returnValue
		get() = state.returnValue
		set(value) {
			state.returnValue = value
		}
	
	
	private var itemIndex = 0
	private val items by lazy(LazyThreadSafetyMode.NONE) { arrayListOf<Any?>() }
	
	override fun stateUpdated(newState: WidgetState) {
		super.stateUpdated(newState)
		
		itemIndex = 0
	}
	
	fun nextItem() = if(itemIndex < items.size) items[itemIndex++] else EMPTY // TODO: `items.size` initializes the lazy
	
	fun updateItem(item: Any?) {
		items[itemIndex - 1] = item // should be called after nextItem()
	}
}
