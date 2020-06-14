package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


abstract class IrComponent<T : IrElement> : IrElement {
	override val startOffset = UNDEFINED_OFFSET
	override val endOffset = UNDEFINED_OFFSET
	
	protected abstract fun build(): T
	internal fun doBuild() = build()
}

abstract class IrComponentExpression<T : IrExpression>(override val type: IrType) : IrComponent<T>(), IrExpression {
	@Suppress("LeakingThis")
	override var attributeOwnerId: IrAttributeContainer = this
	
	
}

abstract class IrComponentDeclaration<T : IrDeclaration> : IrComponent<T>(), IrDeclaration {
	override val metadata: MetadataSource? = null
	override lateinit var parent: IrDeclarationParent
	
	// stubs
	override var annotations: List<IrConstructorCall>
		get() = emptyList()
		set(_) {}
	override var origin: IrDeclarationOrigin
		get() = IrDeclarationOrigin.DEFINED
		set(_) {}
}


object ReplaceIrComponentsTransformer : IrElementTransformerVoid() {
	override fun visitElement(element: IrElement): IrElement {
		if(element is IrComponent<*>) return super.visitElement(element.doBuild())
		return super.visitElement(element)
	}
}
