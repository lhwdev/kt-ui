package com.lhwdev.ktui.widgets


object LayoutScope {
	data class LayoutResult(val width: Float, val height: Float, val place: () -> Unit)
	
	
	fun layout(width: Float, height: Float, place: () -> Unit) = LayoutResult(width, height, place)
}
