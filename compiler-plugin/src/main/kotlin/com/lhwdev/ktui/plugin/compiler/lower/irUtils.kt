package com.lhwdev.ktui.plugin.compiler.lower

import org.jetbrains.kotlin.backend.common.ir.copyParameterDeclarationsFrom
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.copyAttributes
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.constructedClass


fun <T> List<T?>.checkNotNull(): List<T> {
	forEach {
		if(it == null) throw NullPointerException("null element found")
	}
	
	@Suppress("UNCHECKED_CAST")
	return this as List<T>
}


val IrConstructorCall.constructedClass: IrClass get() = symbol.owner.constructedClass


fun IrSimpleFunction.copyShallow(symbol: IrSimpleFunctionSymbol) = factory.createFunction(
	startOffset = startOffset, endOffset = endOffset,
	origin = origin,
	symbol = symbol,
	name = name,
	visibility = visibility, modality = modality,
	returnType = returnType,
	isInline = isInline, isExternal = isExternal, isTailrec = isTailrec,
	isSuspend = isSuspend, isOperator = isOperator, isInfix = isInfix,
	isExpect = isExpect, isFakeOverride = isFakeOverride,
	containerSource = containerSource
).also {
	it.parent = parent
	it.overriddenSymbols = overriddenSymbols
	it.annotations = annotations
	it.returnType = returnType
	it.body = body
	it.correspondingPropertySymbol = correspondingPropertySymbol
	it.metadata = metadata
	it.copyParameterDeclarationsFrom(this)
	copyAttributes(it)
}
