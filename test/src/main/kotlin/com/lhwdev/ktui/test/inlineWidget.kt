package com.lhwdev.ktui.test

import com.lhwdev.ktui.InlineWidget
import com.lhwdev.ktui.Widget
import kotlin.system.measureNanoTime


/**
 * Widget inlining
 * - We can't touch the inline lowering
 *
 * ```
 * @Widget
 * fun A(a: Int) {
 *     B(a)
 * }
 *
 * @Widget
 * @InlineWidget
 * inline fun B(data: Int) {
 *     Text(data)
 * }
 * ```
 *
 *
 * into IrGenerationExtension
 *
 * ```
 * fun A(a: Int, $buildScope: BuildScope, $idState: Int) {
 *     val changes = $buildScope.start($idState, arrayOf(a))
 *     if(changes != 0) {
 *         B(a, $buildScope, (changes shr 0 and 1))
 *     }
 *     $buildScope.end()
 * }
 *
 * inline fun B(data: Int, $buildScope: BuildScope, $changes: Int) {
 *     Text(data, $buildScope, unique id in the inline widget in advance or (changes shr 0 and 1 shl 32))
 * }
 *
 * ```
 *
 * Finally
 *
 * ```
 * fun A(a: Int, $buildScope: BuildScope, $idState: Int) {
 *     val changes = $buildScope.start($idState, arrayOf(a))
 *     if(changes != 0) {
 *         val `$$inline$changes` = changes shr 0 and 1
 *         Text(a, $buildScope, 123 or ((`$$inline$changes` shr 0 and 1) shl 32))
 *     }
 *     $buildScope.end()
 * }
 *
 * ```
 */

fun rec() {
	1.run {
		this@run.and(0)
	}
}


fun hi22() {
	a@ for(i in 0..5) {
		for(ii in 0..1) {
			break@a
		}
	}
	
	val list = listOf("a", "b")
	for((i, n) in list.withIndex()) {
		print("$i to $n")
	}
}

@Widget
@InlineWidget
fun MyInlineWidget(arg: Int) {
	println(arg)
}

@Widget
fun CallInlineWidget() {
	MyInlineWidget(123)
	InlineProxy { Text("Hi") }
}

@Widget
@InlineWidget
fun InlineProxy(child: @Widget () -> Unit) {
	child()
}

fun hoi() {
	val a = 3
	when(a) {
		1 -> Unit
		2 -> Unit
	}
	
	when(val a = measureNanoTime { }) {
		1L, 2L, 3L -> Unit
	}
}
