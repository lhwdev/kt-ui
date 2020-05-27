package com.lhwdev.ktui

import com.lhwdev.ktui.elements.WidgetElement


// Widget implementation v3
// TODO: flat slot table


private const val sAllChanged = 0xffffffff.toInt()


class BuildScopeImpl(val root: Root) : BuildScope() {
	override var currentElement: Element<*> = root
	private var index
		get() = currentElement.index
		set(value) {
			currentElement.index = value
		}
	
	
	private inline val widgetElement get() = currentElement as WidgetElement
	
	
	private fun locateWidgetAndBringNextIfPossible(id: Int, key: Any?): Element<*>? { // TODO: key delegation
		val current = currentElement
		val children = current.children
		val index = index
		
		for(i in index until children.size) {
			val child = children[i]
			if(id == child.id && key == child.key) {
				if(i != index) {// this is not the next element
					current.removeChild(i)
					current.insertChild(index, child)
				}
				
				return child
			}
		}
		return null
	}
	
	/*
	 * idState:
	 * _  _  _  _ | _  _  _  _
	 * state(32~63)  id(0~31)
	 *
	 * [known changed]
	 * 00 - unknown
	 * 10 - known; unchanged
	 * 11 - known; changed
	 *
	 *  [32]  [33] - p1
	 *  [34]  [35] - p2
	 * ...
	 */
	@Suppress("UNCHECKED_CAST")
	override fun start(idState: Long, attrs: Array<Any?>, keyIndex: Int): Int {
		val id = idState.toInt() // [0]~[31]
		
		val last = startTransactWithElement(id, internalKeyOf(attrs, keyIndex)) as WidgetElement?
		if(last == null) {
			commitStartWithElementOnCreated(WidgetElement(), attrs)
			return sAllChanged
		}
		
		val state = idState.shr(32).toInt()
		val changes = internalResolveChanges(last.attrs, attrs, state, last.isDirty)
		
		commitStartWithElement(last, last.state, changes != 0)
		
		return changes
//		val last = locateWidgetAndBringNextIfPossible(id, if(keyIndex == -1) sEmptyKey else attrs[keyIndex])
//
//		if(last == null) { // newly created or the key differs
//			val element = createElement(id, attrs, keyIndex)
//			currentElement.insertChild(index, element)
//			currentElement = element
//			return sAllChanged
//		}
//
//		val state = idState.shr(32).toInt()
//
//		val changes = resolveChanges(last.attrs, attrs, state)
//		last.stateUpdated(attrs)
//		currentElement = last
//
//		// in this case, the widget is already inserted and brought to the appropriate index([index])
//
//		return changes
	}
	
	private var lastId: Int = 0
	private var lastKey: Any? = EMPTY
	
	override fun startTransactWithElement(id: Int, key: Any?): Element<*>? {
		val last = locateWidgetAndBringNextIfPossible(id, key)
		if(last == null) {
			lastId = id
			lastKey = key
		}
		
		return last
	}
	
	override fun <T> commitStartWithElement(last: Element<T>, state: T, isUpdated: Boolean) {
		// in this case, the widget is already inserted and brought to the appropriate index([index])
		if(isUpdated)
			last.stateUpdated(state)
		
		currentElement = last
	}
	
	override fun <T> commitStartWithElementOnCreated(element: Element<T>, state: T) {
		// newly created or the key differs
		element.initialize(lastId, state, root, currentElement)
		currentElement.insertChild(index, element)
		currentElement = element
	}
	
	override fun end() {
		currentElement = currentElement.parent!!
		index++
	}
	
	
	override fun startExpr(id: Long) {
		start(id, sInternalEmptyAttrs, -1) // TODO
	}
	
	override fun endExpr() {
		end() // TODO
	}
	
	
	override fun nextItem() = widgetElement.nextItem()
	
	override fun updateItem(item: Any?) {
		widgetElement.updateItem(item)
	}
}
