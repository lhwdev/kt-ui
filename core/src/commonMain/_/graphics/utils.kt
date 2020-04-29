package com.lhwdev.ktui.graphics

import kotlin.math.PI


fun Float.toRadian(): Float = this * PI.toFloat() / 180f

fun Float.toDegree(): Float = this * 180f / PI.toFloat()
