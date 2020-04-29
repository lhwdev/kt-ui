package com.lhwdev.ktui.node

import com.lhwdev.ktui.BuildScope
import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.currentScope


@Widget
inline fun nodeWidget(crossinline block: BuildScope.() -> Node) {
	currentScope().apply {
		annotation = block()
	}
}
