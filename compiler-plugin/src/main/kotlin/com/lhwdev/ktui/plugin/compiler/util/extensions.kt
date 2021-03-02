package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.declarations.IrTypeParameter
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.types.IrType


operator fun List<IrExpression?>.get(parameter: IrValueParameter): IrExpression? = get(parameter.index)
operator fun List<IrType?>.get(parameter: IrTypeParameter): IrType? = get(parameter.index)

val IrMemberAccessExpression<*>.valueArguments: MutableList<IrExpression?>
	get() = object : AbstractMutableList<IrExpression?>() {
		private fun fixedSize(): Nothing = error("cannot change value parameters count")
		
		override val size get() = valueArgumentsCount
		override fun removeAt(index: Int) = fixedSize()
		override fun add(index: Int, element: IrExpression?) = fixedSize()
		
		override fun set(index: Int, element: IrExpression?): IrExpression? {
			val last = getValueArgument(index)
			putValueArgument(index, element)
			return last
		}
		
		override fun get(index: Int) = getValueArgument(index)
	}

val IrMemberAccessExpression<*>.typeArguments: MutableList<IrType?>
	get() = object : AbstractMutableList<IrType?>() {
		private fun fixedSize(): Nothing = error("cannot change type parameters count")
		
		override val size get() = typeArgumentsCount
		override fun removeAt(index: Int) = fixedSize()
		override fun add(index: Int, element: IrType?) = fixedSize()
		
		override fun set(index: Int, element: IrType?): IrType? {
			val last = getTypeArgument(index)
			putTypeArgument(index, element)
			return last
		}
		
		override fun get(index: Int) = getTypeArgument(index)
	}

