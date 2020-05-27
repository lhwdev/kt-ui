package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.BindingTraceContext


// TODO
private val temporaryGlobalBindingTrace = BindingTraceContext()

@Suppress("unused")
val IrScope.uiTrace: BindingTrace
	get() = temporaryGlobalBindingTrace
