package com.lhwdev.ktui

import kotlin.jvm.JvmName


interface Mergeable<This : Any> {
	fun merge(other: This): This
}


fun <T : Mergeable<T>> T.merge(other: T?) =
	if(other == null) this else merge(other)

@JvmName("mergeOnNullable")
fun <T : Mergeable<T>> T?.merge(other: T?): T? = when {
	this == null -> other
	other == null -> this
	else -> merge(other)
}

@JvmName("mergeOnNullable2")
fun <T : Mergeable<T>> T?.mergeOnNullable(other: T?): T? = when {
	this == null -> other
	other == null -> this
	else -> merge(other)
}

@Suppress("UNCHECKED_CAST")
fun <T> T.mergeIfMergeable(other: T): T =
	if(this is Mergeable<*>) other?.let { (this as Mergeable<Any>).merge(it) as T } ?: this
	else other

@Suppress("UNCHECKED_CAST")
@JvmName("mergeIfMergeableNullable")
fun <T : Any> T.mergeIfMergeable(other: T?): T = when {
	this is Mergeable<*> -> other?.let { (this as Mergeable<Any>).merge(it) as T } ?: this
	other is Mergeable<*> -> other
	else -> other ?: this
}
