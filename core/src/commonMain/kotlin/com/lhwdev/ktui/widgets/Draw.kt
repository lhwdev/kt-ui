package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.currentScope
import com.lhwdev.ktui.elements.DrawElement
import com.lhwdev.ktui.graphics.Canvas
import com.lhwdev.ktui.leafElement
import com.lhwdev.ktui.nextId


@Widget
inline fun Draw(crossinline block: Canvas.() -> Unit) {
	currentScope().leafElement(nextId()) {
		object : DrawElement() {
			override fun Canvas.onDraw() {
				block()
			}
		}
	}
}
