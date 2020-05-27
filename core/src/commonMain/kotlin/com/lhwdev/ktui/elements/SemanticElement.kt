package com.lhwdev.ktui.elements

import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.currentScope
import com.lhwdev.ktui.element
import com.lhwdev.ktui.nextId
import com.lhwdev.ktui.widgets.widget


@Widget
fun Semantic.provide(child: @Widget () -> Unit) {
	currentScope().element(nextId(), state = this, elementCreator = { SemanticElement() }) {
		widget(child)
	}
}


// TODO
data class Semantic(val todo: Any? = null)


class SemanticElement : ProxyElement<Semantic>(), SpecialElement
