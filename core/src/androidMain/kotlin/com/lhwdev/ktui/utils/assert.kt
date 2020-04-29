package com.lhwdev.ktui.utils


@PublishedApi
internal object Dummy

@JvmField
val sAssertionEnabled = Dummy::class.java.desiredAssertionStatus()


actual inline fun assert(case: Boolean, lazyMessage: () -> String) {
	if(!case && sAssertionEnabled) throw AssertionError(lazyMessage())
}
