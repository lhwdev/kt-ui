package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.graphics.Path


@Widget // TODO
fun Clip(path: Path) = DrawConcat {
	clipPath(path)
}

//fun Clip(shape: Shape)

@Widget
fun Clip(path: Path, child: @Widget () -> Unit) {
	Clip(path)
	child()
}
