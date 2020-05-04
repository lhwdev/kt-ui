package com.lhwdev.ktui.graphics


data class Size(val width: Float, val height: Float)


fun Size.toRect(position: Position): Rect = toRect(position.x, position.y)

fun Size.toRect(x: Float, y: Float): Rect = Rect.sized(x, y, width, height)

fun Size.toRectAtOrigin(): Rect = Rect(0f, 0f, width, height)
