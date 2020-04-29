package com.lhwdev.ktui

import com.lhwdev.ktui.node.AmbientData
import com.lhwdev.ktui.node.AmbientNode
import com.lhwdev.ktui.node.nodeWidget


class Ambient<T : Any>(defaultValueBlock: (() -> T)? = null) {
	val defaultValue by lazy {
		defaultValueBlock?.invoke()
	}
	
	
	operator fun invoke(value: T, child: @Widget () -> Unit) = nodeWidget {
		child()
		
		AmbientNode(AmbientData(this@Ambient, value))
	}
}

interface Mergeable<This : Any> {
	fun merge(other: This): This
}
