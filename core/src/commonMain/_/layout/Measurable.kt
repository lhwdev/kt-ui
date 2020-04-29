package com.lhwdev.ktui.layout


interface Measurable {
	fun measure(constraints: Constraints): Placeable
}
