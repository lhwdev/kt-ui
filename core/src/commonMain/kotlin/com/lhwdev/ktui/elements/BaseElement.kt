package com.lhwdev.ktui.elements

import com.lhwdev.ktui.Element


abstract class BaseElement<T> : Element<T>() {
	abstract override val children: MutableList<Element<*>>
	
	
	override fun insertChild(index: Int, child: Element<*>) {
		children.add(index, child)
		child.attach()
	}
	
	override fun removeChild(index: Int) {
		children.removeAt(index).detach()
	}
}
