package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Draw
import com.lhwdev.ktui.Widget


@Widget
fun Text(text: CharSequence) {
	val style = remember { TextPaint() }
	
	Draw {
		drawText(text, 0f, 0f, style)
	}
}
