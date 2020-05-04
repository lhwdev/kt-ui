package com.lhwdev.ktui

import com.lhwdev.ktui.elements.DrawElement
import com.lhwdev.ktui.graphics.Canvas


@Widget
inline fun Draw(crossinline block: Canvas.() -> Unit) {
	currentScope().apply {
		startWithElement(nextId()) {
			object : DrawElement() {
				override fun Canvas.onDraw() {
					block()
				}
			}
		}
		end()
	}
}
