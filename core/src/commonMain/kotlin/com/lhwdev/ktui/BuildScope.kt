package com.lhwdev.ktui


typealias ElementCreator = (id: Int, keyIndex: Int) -> Element


// used only once and not recycled nor persisted
abstract class BuildScope {
	abstract val currentElement: Element
	
	// entry point from the plugin
	abstract fun start(idState: Long, attrs: Array<Any?>, keyIndex: Int): Int
	
	// entry point from the plugin
	abstract fun end(returnValue: Any?)
	
	// entry point from the plugin
	abstract fun end()
	
	// entry point from the plugin
	abstract fun endSkip(): Any?
	
	// called before [start].
	abstract fun assignNextElement(elementCreator: ElementCreator)
	
	@Widget
	fun add(elementCreator: ElementCreator) {
		assignNextElement(elementCreator)
		
		start(currentId().toLong() or 0xffffffff00000000uL.toLong(), )
	}
}
