package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.util.kotlinPackageFqn


fun IrElementScope.irError(message: String) =
	irCall(irFunctionSymbol(kotlinPackageFqn, "error", 1), valueArguments = listOf(irString(message)))
