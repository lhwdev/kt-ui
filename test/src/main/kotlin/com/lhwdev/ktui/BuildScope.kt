package com.lhwdev.ktui


class BuildScope {
	// entry point from the plugin
	fun start(id: Int) {
		TODO()
	}
	
	// entry point from the plugin
	fun end() {
		TODO()
	}
	
	fun end(restartBlock: (scope: BuildScope) -> Unit) {
		TODO()
	}
	
	// entry point from the plugin
	fun startExpr(id: Int) {
		TODO()
	}
	
	// entry point from the plugin
	fun endExpr() {
		TODO()
	}
	
	fun isChanged(param: Any?): Boolean {
		TODO()
	}
}
