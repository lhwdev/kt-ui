package com.lhwdev.ktui

import com.lhwdev.ktui.text.TextStyle
import com.lhwdev.ktui.text.font.FontWeight
import com.lhwdev.ktui.widgets.Button
import com.lhwdev.ktui.widgets.Text


@Widget
fun Main() {
	var number by state { 1 }
	
	Text("Hello, $number!", style = TextStyle(fontWeight = FontWeight.bold))
	
	Button(onClick = { number++ }) {
		// Image(Icon.next)
		Text("Click me!")
	}
	
	Button(text = "Another text button")
}
