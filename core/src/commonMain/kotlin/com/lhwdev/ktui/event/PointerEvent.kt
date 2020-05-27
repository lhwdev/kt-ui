package com.lhwdev.ktui.event

import com.lhwdev.ktui.graphics.Position


data class PointerEvent(val pointers: Array<Position>) : UiEvent {
	val x get() = first.x
	val y get() = first.y
	
	inline val first get() = pointers[0]
	
	
	override fun equals(other: Any?) = when {
		this === other -> true
		other == null || this::class != other::class -> false
		else -> pointers.contentEquals((other as PointerEvent).pointers)
	}
	
	override fun hashCode() = pointers.contentHashCode()
}
