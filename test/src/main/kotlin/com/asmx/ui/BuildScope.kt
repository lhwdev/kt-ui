package com.lhwdev.ktui


class BuildScope {
	fun start(idState: Long, attrs: Array<Any?>, keyIndex: Int): Int {
		TODO()
	}
	
	
	// called before [start].
	fun assignNextElement(element: (id: Int, keyIndex: Int) -> Element) {
		TODO()
	}
	
	
	// entry point from the plugin
	fun end(returnValue: Any?) {
		TODO()
	}
	
	// entry point from the plugin
	fun end() {
		TODO()
	}
	
	// entry point from the plugin
	fun endSkip(): Any? {
		TODO()
	}
}
