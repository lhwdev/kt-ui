package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.*
import com.lhwdev.ktui.layout.Constraints
import com.lhwdev.ktui.layout.Measurable
import com.lhwdev.ktui.node.MeasureBlock
import com.lhwdev.ktui.node.Nodes
import com.lhwdev.ktui.node.nodeWidget


val Empty: @Widget () -> Unit = {}


@Widget
inline fun Draw(crossinline onDraw: DrawScope.() -> Unit) = nodeWidget {
	object : Nodes.Draw() {
		override fun DrawConcatScope.onDraw(element: Element) {
			onDraw()
			drawNext()
		}
	}
}

@Widget
inline fun DrawConcat(crossinline onDraw: DrawConcatScope.() -> Unit) = nodeWidget {
	object : Nodes.Draw() {
		override fun DrawConcatScope.onDraw(element: Element) {
			onDraw() // not handing Element?
		}
	}
}

@Widget
inline fun DrawContainer(
	crossinline onDraw: DrawContainerScope.() -> Unit,
	crossinline child: @Widget () -> Unit
) {
	child()
	
	nodeWidget {
		object : Nodes.Draw() {
			override fun DrawConcatScope.onDraw(element: Element) {
				DrawContainerScope(this, element).onDraw()
			}
		}
	}
}

@Widget
inline fun RepaintBoundary(crossinline child: @Widget () -> Unit) {
	child()
	
	nodeWidget {
		Nodes.RepaintBoundary()
	}
}


inline fun Layout(
	crossinline children: @Widget () -> Unit,
	crossinline measureBlock: MeasureBlock
) {
	children() // TODO
	
	nodeWidget {
		object : Nodes.Layout() {
			override fun measure(measurables: List<Measurable>, size: Constraints) =
				LayoutScope.measureBlock(measurables, size)
		}
	}
}
