package com.lhwdev.ktui.utils


@PublishedApi
internal object Dummy

@JvmField
actual val sAssertionEnabled = Dummy::class.java.desiredAssertionStatus()

