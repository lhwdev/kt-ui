package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Element
import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.node.ElementWidget


@Widget
fun onAttach(block: Element.() -> Unit) {
	ElementWidget {
		object : Element() {
			override fun onAttach() {
				super.onAttach()
				block()
			}
		}
	}
}

@Widget
fun onDispose(block: Element.() -> Unit) {
	ElementWidget {
		object : Element() {
			override fun onDispose() {
				super.onDispose()
				block()
			}
		}
	}
}
