package com.lhwdev.ktui.plugin.compiler.lower


inline fun <T> List<T>.mutate(block: MutableList<T>.() -> Unit): List<T> {
	val list = toMutableList()
	list.block()
	return list
}

fun <T> MutableList<T>.setAll(index: Int, new: List<T>) {
	if(this is RandomAccess) for(i in new.indices) {
		this[i + index] = new[i]
	} else {
		val iterator = listIterator(index)
		for(i in new.indices) {
			iterator.next()
			iterator.set(new[i])
		}
	}
}
