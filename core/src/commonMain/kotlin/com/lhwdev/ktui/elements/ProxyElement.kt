package com.lhwdev.ktui.elements

import com.lhwdev.ktui.Element
import com.lhwdev.ktui.utils.assert


open class ProxyElement<T> : BaseElement<T>() {
	var child: Element<*>? = null
	
	override val children = object : AbstractMutableList<Element<*>>() {
		override val size = if(child == null) 0 else 1
		
		override fun get(index: Int): Element<*> {
			assert(index == 0) { "getting child at $index on ProxyElement" }
			return child!!
		}
		
		override fun add(index: Int, element: Element<*>) {
			assert(index == 0) { "adding child at $index on ProxyElement" }
			assert(child == null) { "adding child on ProxyElement which already has a child" }
			child = element
		}
		
		override fun removeAt(index: Int): Element<*> {
			assert(index == 0) { "removing child at $index on ProxyElement" }
			val element = child
			assert(element != null) { "removing child on ProxyElement which does not have a child" }
			child = null
			return element
		}
		
		override fun set(index: Int, element: Element<*>): Element<*> {
			assert(index == 0) { "setting child at $index on ProxyElement" }
			assert(child != null) { "setting child on ProxyElement which does not have a child" }
			child = element
			return element
		}
	}
}
