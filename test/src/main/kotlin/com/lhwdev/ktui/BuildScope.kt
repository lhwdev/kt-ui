@file:Suppress("UNUSED_PARAMETER")

package com.lhwdev.ktui


fun <T> hi(param: (Int, String) -> T) {
	param(1, "ho")
}


class BuildScope {
	// entry point from the plugin
	fun start(locationId: Int) {
		TODO()
	}
	
	// entry point from the plugin
	fun end() {
		TODO()
	}
	
	// entry point from the plugin
	fun startReplaceableGroup(locationId: Int, groupId: Int) {
		TODO()
	}
	
	// entry point from the plugin
	fun endReplaceableGroup() {
		TODO()
	}
	
	// entry point from the plugin
	fun startRemovableGroup(locationId: Int) {
		TODO()
	}
	
	// entry point from the plugin
	fun endRemovableGroup() {
		TODO()
	}
	
	// entry point from the plugin
	fun end(restartBlock: (scope: BuildScope) -> Unit) {
		TODO()
	}
	
	// entry point from the plugin
	fun startExpr(locationId: Int) {
		TODO()
	}
	
	// entry point from the plugin
	fun endExpr() {
		TODO()
	}
	
	// entry point from the plugin
	fun isChanged(param: Any?): Boolean {
		TODO()
	}
}
