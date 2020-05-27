package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.types.KotlinType


@JvmName("toIrTypeNullable")
fun KotlinType?.toIrType() = this?.toIrType() ?: irBuiltIns.unitType

fun KotlinType.toIrType() = context.typeTranslator.translateType(this)
