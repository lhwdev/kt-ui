package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Ambient
import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.text.TextStyle


val TextStyleAmbient = Ambient<TextStyle>()


@Widget
fun Text(data: String) {
	val style = ambient(TextStyleAmbient)
	
	Draw {
		drawText(data)
	}
}
