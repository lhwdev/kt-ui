package com.lhwdev.ktui.elements

import com.lhwdev.ktui.graphics.Canvas


// similar to View of Android(but all separated to DrawElement, LayoutElement, etc.)
/**
 * Do not inherit directly!
 */
interface UiElement


abstract class DrawElement : ProxyElement<Unit>(), UiElement {
	abstract fun Canvas.onDraw()
}


abstract class LayoutElement : WidgetElement(), UiElement {
	abstract fun onLayout()
}
