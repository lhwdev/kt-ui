package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.types.KotlinType


@ObsoleteDescriptorBasedAPI
@JvmName("toIrTypeNullable")
fun KotlinType?.toIrType() = this?.toIrType() ?: irBuiltIns.unitType

@ObsoleteDescriptorBasedAPI
fun KotlinType.toIrType() = pluginContext.typeTranslator.translateType(this)
