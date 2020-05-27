package com.lhwdev.ktui.graphics

import com.lhwdev.ktui.Immutable


expect class FrameworkPath

internal expect fun Path.toFrameworkPathInternal(): FrameworkPath


@Immutable // ???
class Path {
	private val frameworkPaintCache by lazy(LazyThreadSafetyMode.NONE) { toFrameworkPathInternal() }
	
	fun toFrameworkPath() = frameworkPaintCache
}
