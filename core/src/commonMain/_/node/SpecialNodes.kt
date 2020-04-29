package com.lhwdev.ktui.node

import com.lhwdev.ktui.Element


sealed class SpecialNode : Node

abstract class ElementNode : SpecialNode() {
	abstract fun createElement(): Element
}

inline fun ElementNode(crossinline onCreate: () -> Element) =
	object : ElementNode() {
		override fun createElement() = onCreate()
	}

inline fun ElementWidget(crossinline onCreate: () -> Element) = nodeWidget {
	ElementNode(onCreate)
}
