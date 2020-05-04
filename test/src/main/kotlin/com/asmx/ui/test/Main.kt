package com.lhwdev.ktui.test

import com.lhwdev.ktui.Element
import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.currentScope
import com.lhwdev.ktui.nextId


/* TODO: deprecate @InlineWidget and use inline keyword to detect? Maybe non-inline @InlineWidget fun?
 *  - no, its name is 'Inline' widget
 *  - So let's remove it
 */

/* To-Test:
 * - inline widget
 * - inline widget lambda
 * - nested widget & nested widget lambda
 * - diagnostics: X -> @W, X -> @WU, @WU -> @W, try..catch in widget
 * - normal inline lambda calling widget(like ??.let { MyWidget(it) }): should be allowed without bug
 * - widget in class
 */

@Suppress("MayBeConstant")
val aNum = 4

@Widget
fun Main() {
	Text("Hello, world!") // TO WATCH: state for the default value
	val a = aNum shl 3 shl 3 and 7
	
	@Suppress("ComplexRedundantLet")
	"Hi".let { Text(it) } // test for calling widget in normal inline lambda
}


@Widget
fun Text(text: String, size: Int = 30) {
	Draw {
		println("Text! $text at $size")
		
		val id = nextId()
	}
}

@Widget
fun WidgetWithReturn(): Int {
	if(aNum == 2) return 1
	EmptyWidget()
	Proxy {
		Text("Hell'o, world!")
	}
	return 123
}

@Widget
fun Proxy(child: @Widget () -> Unit) {
	child()
}

class DrawScope

@Widget
fun Draw(block: DrawScope.() -> Unit) {
	currentScope().assignNextElement { _, _ ->
		object : Element() {
			fun draw() {
				DrawScope().block()
			}
		}
	}
	EmptyWidget()
}


@Widget
fun EmptyWidget() {}
