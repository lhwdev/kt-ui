package com.lhwdev.ktui.graphics

import kotlin.math.PI


fun Float.toRadian() = this * PI.toFloat() / 180f

fun Float.toDegree() = this * 180f / PI.toFloat()


fun pow(number: Float) = number * number
