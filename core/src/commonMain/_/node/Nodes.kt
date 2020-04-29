package com.lhwdev.ktui.node

import com.lhwdev.ktui.DrawConcatScope
import com.lhwdev.ktui.Element
import com.lhwdev.ktui.layout.Constraints
import com.lhwdev.ktui.layout.Measurable
import com.lhwdev.ktui.widgets.LayoutScope


/**
 * Node is a special class which the framework takes care of it.
 * There are several predefined nodes which takes a great part of the framework; see [Nodes],
 * [SpecialNode] and [ElementNode].
 * You can define your custom node and handle it.
 */
interface Node


typealias MeasureBlock = LayoutScope.(measurables: List<Measurable>, size: Constraints) -> LayoutScope.LayoutResult


object Nodes {
	abstract class Draw : Node {
		abstract fun DrawConcatScope.onDraw(element: Element)
	}
	
	class RepaintBoundary : Node
	
	abstract class Layout : Node {
		abstract fun measure(measurables: List<Measurable>, size: Constraints): LayoutScope.LayoutResult
	}
	
	object Focusable : Node
	
	class Semantics : Node
	
	class EventHandler : Node
}
