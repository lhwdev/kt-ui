package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid


abstract class IrElementVisitorRecursive : IrElementVisitorVoid {
	protected fun IrElement.acceptChildren() {
		acceptChildren(this@IrElementVisitorRecursive, null)
	}
	
	override fun visitElement(element: IrElement) {
		element.acceptChildren()
	}
}
