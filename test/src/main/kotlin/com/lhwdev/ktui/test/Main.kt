@file:Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE", "unused", "UNREACHABLE_CODE")

package com.lhwdev.ktui.test

import com.lhwdev.ktui.Widget
import com.lhwdev.ktui.nextId

class DelegationTest(a: String) : CharSequence by a


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

@Widget
fun MyWidget(param: Int, paramWithDefault: Int = param + 3, defaultClass: Float = 3f, def: TheEnum? = null) {
	Text("Hi", paramWithDefault)
}

enum class TheEnum(val a: Int = 3) {
	abc(1), hi, ho, laLa {
		fun hi() {}
	}
}

val aNum
	get() = 4

class Hooray {
	var test: Int = 3
		get() = field + 3
		internal set(value) {
			field = value * 7
		}
	
	var test2 = 3
		get() = 123
		private set
}

val Any.a get() = ""

annotation class A(val ar: Array<String>)

@A(["a"])
open class Hi<T>(val ho: Int) {
	val a: List<T> = TODO()
}

class C2() : Hi<Any>(3) {
	constructor(a: Int) : this() {
		print(a)
		val n: Byte = 1
	}
	
	init {
		println()
	}
}

@Widget
fun WidgetWithLongParameters(
	p1: Int, p2: Int, p3: Int, p4: Int, p5: Int,
	p6: Int, p7: Int, p8: Int, p9: Int, p10: Int,
	p11: Int, p12: Int, p13: Int, p14: Int, p15: Int,
	p16: Int, p17: Int, p18: Int, p19: Int, p20: Int,
	p21: Int, p22: Int, p23: Int, p24: Int, p25: Int,
	p26: Int, p27: Int, p28: Int, p29: Int, p30: Int,
	p31: Int, p32: Int, p33: Int, p34: Int, p35: Int,
	p36: Int, p37: Int, p38: Int, p39: Int, p40: Int,
	p41: String
) {
	Text(p41, p5)
}

@Widget
fun Hello(a: Int) {
	Text("$a")
	Hello(a)
}

@Widget
fun Main() {
	Text("Hello, world!") // TO WATCH: state for the default value
	val a = nextId()
	
	val b = run { 123 }
	
	MyWidget(123)
	
	@Suppress("ComplexRedundantLet")
	"Hi".let { Text(it) } // test for calling widget in normal inline lambda
	
	val returned = WidgetWithReturn()
//	val aha = (::Text)("11", 123)
}

class Ho<T> {
	fun hi(a: T) {}
}

fun HT() {
	Ho<Int>().hi(123)
}

@Widget
fun Main2() {
	outer@ for(i in 0 until 10) {
		Text("$i")
		if(aNum == 3) break@outer
		
		for(ii in 0..2) {
			Abc(WidgetWithReturn())
		}
	}
}

@Widget
fun Main3(a: Int, b: Int = a) {
	
	if(a == 3) Text("Ho")
	if(a == 2) {
		println("hi")
	}
	when(b) {
		1 -> Text("hello")
		2 -> Text("hi")
	}
	
	if(WidgetWithReturn() > 5) {
		Text("WOW")
	}
}


@Widget
fun Text(text: String, size: Int = 30, otherParam: Float = 1f) {
	val id = nextId()
	
	Text(text, size)
	
	Draw {
		println("Text! $text at $size")
	}
	
	if(size == 0) Text("ERROR")
}

@Widget
fun Abc(arg: Int) {

}

@Widget
fun WidgetWithReturnAnother() = 123.also { return 88 }

@Widget
fun WidgetWithReturn(): Int {
//	if(aNum == 2)
//		EmptyWidget()
	if(aNum == 3) return -1
//	Proxy {
//		Text("Hell'o, world!")
//	}
	return 123
}

val Empty: @Widget () -> Unit = @Widget {}

@Widget
fun NonPureProxy(child: @Widget () -> Unit) {
	val childValue = if(aNum != 2) child else Empty
	childValue()
}

@Widget
fun Proxy(child: @Widget () -> Unit) {
	child()
	run { return }
	val num = when(aNum) {
		1 -> WidgetWithReturn()
		WidgetWithReturn() -> 3
		else -> error("hah")
	}
}

class DrawScope

@Widget
fun Draw(block: DrawScope.() -> Unit) {
//	currentScope().assignNextElement { _, _ ->
//		object : Element() {
//			fun draw() {
//				DrawScope().block()
//			}
//		}
//	}
	EmptyWidget()
}


@Widget
fun EmptyWidget() {
}
