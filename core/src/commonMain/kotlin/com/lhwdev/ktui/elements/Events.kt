package com.lhwdev.ktui.elements


abstract class EventReceiver<T> : ProxyElement(), SpecialElement {
	abstract fun onReceive(key: Any, event: T)
}
