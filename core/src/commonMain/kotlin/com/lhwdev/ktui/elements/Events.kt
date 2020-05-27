package com.lhwdev.ktui.elements

import com.lhwdev.ktui.*


@Widget
inline fun <reified T : Any> EventReceiver(key: Any, crossinline onEvent: (T) -> Boolean, child: @Widget () -> Unit) {
	currentScope().element(nextId(), state = sInternalEmptyAttrs, elementCreator = {
		object : WidgetElement() {
			val keyParam = key
			
			override fun onEvent(key: Any?, event: Any) =
				if(event is T && keyParam == key && onEvent(event)) true
				else super.onEvent(key, event)
		}
	}) {
		child()
	}
}

@Widget
inline fun <reified T : Any> EventReceiver(crossinline onEvent: (T) -> Boolean, child: @Widget () -> Unit) {
	currentScope().element(nextId(), state = sInternalEmptyAttrs, elementCreator = {
		object : WidgetElement() {
			override fun onEvent(key: Any?, event: Any) =
				if(event is T && onEvent(event)) true
				else super.onEvent(key, event)
		}
	}) {
		child()
	}
}
