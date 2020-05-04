package com.lhwdev.ktui.elements

import com.lhwdev.ktui.graphics.Canvas


sealed class UiElement : WidgetElement()


abstract class DrawElement : UiElement() {
	abstract fun Canvas.onDraw()
}
