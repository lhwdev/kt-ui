package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.elements.EventReceiver
import com.lhwdev.ktui.elements.Semantic
import com.lhwdev.ktui.elements.provide
import com.lhwdev.ktui.event.ClickEventKey
import com.lhwdev.ktui.event.PointerEvent


/*
 * TODO:
 *  - accessibility
 *  - focus
 *  - in callbacks like [onClick], needs some context like [Element]
 */

@Widget
fun Clickable(onClick: (() -> Unit)? = null, child: @Widget () -> Unit) {
	val content = @Widget {
		Semantic().provide { // TODO
			child()
		}
	}
	
	if(onClick != null) EventReceiver<PointerEvent>(key = ClickEventKey, onEvent = {
		onClick()
		true
	}) { content() }
	else content()
}

@Widget
fun ClickableSuspend(onClick: suspend () -> Unit, child: @Widget () -> Unit) {
	TODO()
}
