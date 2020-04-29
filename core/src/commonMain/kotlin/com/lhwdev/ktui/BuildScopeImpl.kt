package com.lhwdev.ktui


// Widget implementation v2
// TODO: if a Widget throws an exception and get caught by ancestor call stack: then end is not called


private val sEmptyKey = Any()
private const val sAllChanged = 0xffffffff.toInt()


class BuildScopeImpl(val root: Root) : BuildScope() {
	override var currentElement: Element = root
	private var index = 0
	private var lastElementCreator: ElementCreator? = null
	
	
	private fun locateWidgetAndBringNextIfPossible(id: Int, key: Any?): Element? { // TODO: key delegation
		val children = currentElement.children
		for(i in index until children.size) {
			val child = children[i]
			@Suppress("SuspiciousEqualsCombination")
			if(id == child.id && (key === sEmptyKey || key == child.attrs[child.keyIndex])) {
				if(i != index) {// this is not the next element
					children.removeAt(i)
					children.add(index, child)
				}
				
				return child
			}
		}
		return null
	}
	
	private fun createElement(id: Int, attrs: Array<Any?>, keyIndex: Int): Element {
		val creator = lastElementCreator
		return (if(creator != null) {
			lastElementCreator = null
			creator(id, keyIndex)
		} else WidgetElement(id, keyIndex)).also {
			it.attrs = attrs
			it.initialize(root, currentElement)
		}
	}
	
	private fun resolveChanges(attrsBefore: Array<Any?>, attrs: Array<Any?>, state: Int): Int {
		inline fun stateAt(index: Int) = (state shr (index * 2)) and 0x3
		
		var changes = 0
		
		for(i in attrs.indices) {
			val changedNow = when(val stateNow = stateAt(i)) {
				0x0 /* 00 */ -> attrsBefore[i] != attrs[i]
				0x2 /* 10 */ -> false
				0x3 /* 11 */ -> true
				else -> error("Unexpected value $stateNow")
			}
			
			changes = changes or ((if(changedNow) 1 else 0) shl i)
		}
		
		return changes
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
	override fun start(idState: Long, attrs: Array<Any?>, keyIndex: Int): Int {
		val id = idState.toInt() // [0]~[31]
		val last = locateWidgetAndBringNextIfPossible(id, if(keyIndex == -1) sEmptyKey else attrs[keyIndex])
		
		if(last == null) { // newly created or the key differs
			val element = createElement(id, attrs, keyIndex)
			currentElement.children.add(index, element)
			currentElement = element
			return sAllChanged
		}
		
		val state = idState.shr(32).toInt()
		
		val changes = resolveChanges(last.attrs, attrs, state)
		last.attrs = attrs
		currentElement = last
		
		// in this case, the widget is already inserted and brought to the appropriate index([index])
		
		return changes
	}
	
	override fun end(returnValue: Any?) {
		currentElement.apply {
			this.returnValue = returnValue
			stateUpdated()
		}
		end()
	}
	
	override fun end() {
		currentElement = currentElement.parent!!
		index++
	}
	
	override fun endSkip(): Any? {
		val skipped = currentElement
		end()
		skipped.skipBuilding()
		return skipped.returnValue
	}
	
	override fun assignNextElement(elementCreator: ElementCreator) {
		lastElementCreator = elementCreator
	}
}
