package com.lhwdev.ktui

import com.lhwdev.ktui.graphics.Canvas
import com.lhwdev.ktui.graphics.Rect


interface DrawScope : Canvas {
	val canvas: Canvas
	val parentSize: Rect
}

interface DrawConcatScope : DrawScope {
	fun drawNext()
}

interface DrawContainerScope : DrawScope {
	fun drawChild()
}

open class DrawScopeImpl(override val canvas: Canvas, override val parentSize: Rect) : DrawScope, Canvas by canvas


fun DrawScope(canvas: Canvas, parentSize: Rect): DrawScope = DrawScopeImpl(canvas, parentSize)

fun DrawContainerScope(scope: DrawConcatScope, element: Element): DrawContainerScope =
	object : DrawScopeImpl(scope.canvas, scope.parentSize), DrawContainerScope {
		override fun drawChild() {
//			element.draw(parentScope = scope) // TODO
		}
	}
