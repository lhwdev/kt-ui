package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Immutable
import com.lhwdev.ktui.Mergeable
import com.lhwdev.ktui.Widget


// TODO: delete this file and move to material/other design module

@Immutable
data class ButtonStyle(val creator: Any? = null) : Mergeable<ButtonStyle> {
	override fun merge(other: ButtonStyle) = ButtonStyle(other.creator ?: creator)
}


@Widget
fun Button(style: ButtonStyle = TODO(), onClick: (() -> Unit)? = null, child: @Widget () -> Unit) {
	Clickable(onClick = onClick) {
		child()
	}
}

@Widget
fun Button(text: CharSequence, style: ButtonStyle = TODO(), onClick: (() -> Unit)? = null) {
	Button(style = style, onClick = onClick) {
		Text(text)
	}
}
