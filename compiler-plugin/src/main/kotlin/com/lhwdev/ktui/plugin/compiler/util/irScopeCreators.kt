@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner
import org.jetbrains.kotlin.ir.symbols.IrSymbol


class IrSimpleElementScope(override val startOffset: Int, override val endOffset: Int) : IrElementScope

class IrSimpleBuilderScope(override val startOffset: Int, override val endOffset: Int, override val scope: Scope) : IrBuilderScope


inline fun irElementScope(startOffset: Int, endOffset: Int): IrElementScope =
	IrSimpleElementScope(startOffset, endOffset)

inline fun <R> irElementScope(startOffset: Int, endOffset: Int, block: IrElementScope.() -> R): R =
	irElementScope(startOffset, endOffset).run(block)

inline fun irBuilderScope(startOffset: Int, endOffset: Int, scope: Scope): IrBuilderScope =
	IrSimpleBuilderScope(startOffset, endOffset, scope)

inline fun <R> irBuilderScope(startOffset: Int, endOffset: Int, scope: Scope, block: IrBuilderScope.() -> R): R =
	irBuilderScope(startOffset, endOffset, scope).run(block)

inline fun irBuilderScope(element: IrSymbolOwner): IrBuilderScope =
	IrSimpleBuilderScope(element.startOffset, element.endOffset, Scope(element.symbol))

inline fun irBuilderScope(symbol: IrSymbol): IrBuilderScope =
	IrSimpleBuilderScope(UNDEFINED_OFFSET, UNDEFINED_OFFSET, Scope(symbol))

inline fun <R> irBuilderScope(element: IrSymbolOwner, block: IrElementScope.() -> R): R =
	irBuilderScope(element).run(block)

inline fun <R> irBuilderScope(symbol: IrSymbol, block: IrElementScope.() -> R): R =
	irBuilderScope(symbol).run(block)


val IrElement.scope get() = IrSimpleElementScope(startOffset, endOffset)
val IrSymbolOwner.scope get() = IrSimpleBuilderScope(startOffset, endOffset, Scope(symbol))

private class IrBuilderScopeWithOffset(val delegate: IrBuilderScope, override val startOffset: Int, override val endOffset: Int) : IrBuilderScope by delegate

fun IrBuilderScope.offset(startOffset: Int, endOffset: Int): IrBuilderScope =
	if(this is IrBuilderScopeWithOffset) delegate.offset(startOffset, endOffset) else IrBuilderScopeWithOffset(this, startOffset, endOffset)
