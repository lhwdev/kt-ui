package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.util.kotlinPackageFqn


private val kotlinPackage = pluginContext.referencePackage(kotlinPackageFqn)


fun IrElementScope.irError(message: String) = irCall(
	kotlinPackage.referenceFirstFunction("error", 1),
	valueArguments = listOf(irString(message))
)
