@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui.utils

import kotlin.contracts.contract


expect val sAssertionEnabled: Boolean

inline fun assert(case: Boolean) {
	contract {
		returns() implies case
	}
	
	if(!case && sAssertionEnabled) throw AssertionError()
}

inline fun assert(case: Boolean, lazyMessage: () -> String) {
	contract {
		returns() implies case
	}
	
	if(!case && sAssertionEnabled) throw AssertionError(lazyMessage())
}
