package com.lhwdev.ktui.utils


fun lerp(start: Float, stop: Float, fraction: Float): Float =
	start * fraction + stop * (1f - fraction)

fun lerp(start: Int, stop: Int, fraction: Float): Int =
	(start * fraction + stop * (1f - fraction)).toInt()
