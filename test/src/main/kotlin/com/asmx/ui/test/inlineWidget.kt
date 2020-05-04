package com.lhwdev.ktui.test

import com.lhwdev.ktui.InlineWidget
import com.lhwdev.ktui.Widget


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
@Widget
@InlineWidget
fun MyInlineWidget(arg: Int) {
	println(arg)
}

@Widget
fun CallInlineWidget() {
	MyInlineWidget(123)
}
