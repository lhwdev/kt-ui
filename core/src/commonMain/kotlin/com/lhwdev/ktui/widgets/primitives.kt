package com.lhwdev.ktui.widgets

import com.lhwdev.ktui.InlineWidget
import com.lhwdev.ktui.Widget


/* TODO: this should be inlined properly
 *
 * widget { Hi() }
 *
 * into
 *
 * $buildScope.start(123)
 * { $buildScope -> Hi($buildScope, 123L) }.invoke($buildScope)
 * $buildScope.end()
 *
 *
 * (wrong transformation examples)
 *
 * 1.
 * { $buildScope -> Hi($buildScope, 123L) }.invoke($buildScope)
 *
 * 2.
 * $buildScope.start(123)
 * { $buildScope ->
 *     val changes = $buildScope.start(124, arrayOf(), -1)
 *     if(changes != 0) {
 *         Hi($buildScope, 123L)
 *     }
 *     $buildScope.end()
 * }.invoke($buildScope)
 * $buildScope.end()
 */

@Widget
@InlineWidget(doInline = false)
inline fun widget(child: @Widget () -> Unit) {
	child()
}
