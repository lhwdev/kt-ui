package com.lhwdev.ktui.elements

import com.lhwdev.ktui.graphics.Canvas
import com.lhwdev.ktui.graphics.Rect


interface DrawScope : Canvas {
	val bound: Rect
	
	val left get() = bound.left
	val top get() = bound.top
	val right get() = bound.right
	val bottom get() = bound.bottom
}
