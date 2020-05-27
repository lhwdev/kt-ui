package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.ambientOf
import com.lhwdev.ktui.merge
import com.lhwdev.ktui.text.TextStyle


val textStyleAmbient = ambientOf { TextStyle() }


// actual logic is much more complicated: this is just stub
@Widget
fun Text(text: CharSequence, style: TextStyle? = null) {
	val mergedStyle = textStyleAmbient().merge(style)
	
	Draw {
		drawText(text, 0f, 0f, mergedStyle)
	}
}
