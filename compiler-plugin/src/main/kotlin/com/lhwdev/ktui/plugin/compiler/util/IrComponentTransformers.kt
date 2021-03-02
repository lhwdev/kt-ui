package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid


interface IrComponentTransformer<D> : IrElementTransformer<D>, IrElementVisitor<IrElement, D> {
	fun visitComponent(component: IrComponent<*>, data: D, default: () -> IrElement): IrElement = default()
	
	fun visitComponentExpression(
		component: IrComponentExpression<*>,
		data: D,
		default: () -> IrExpression
	): IrExpression = default()
	
	fun visitComponentDeclaration(
		component: IrComponentDeclaration<*>,
		data: D,
		default: () -> IrStatement
	): IrStatement = default()
}

abstract class IrComponentTransformerVoid : IrComponentTransformer<Nothing?>, IrElementTransformerVoid() {
	final override fun visitComponent(component: IrComponent<*>, data: Nothing?, default: () -> IrElement): IrElement =
		visitComponent(component, default)
	
	final override fun visitComponentExpression(
		component: IrComponentExpression<*>,
		data: Nothing?,
		default: () -> IrExpression
	): IrExpression = visitComponentExpression(component, default)
	
	final override fun visitComponentDeclaration(
		component: IrComponentDeclaration<*>,
		data: Nothing?,
		default: () -> IrStatement
	): IrStatement = visitComponentDeclaration(component, default)
	
	
	open fun visitComponent(component: IrComponent<*>, default: () -> IrElement): IrElement = default()
	
	open fun visitComponentExpression(component: IrComponentExpression<*>, default: () -> IrExpression): IrExpression =
		default()
	
	open fun visitComponentDeclaration(component: IrComponentDeclaration<*>, default: () -> IrStatement): IrStatement =
		default()
}


interface IrComponentVisitor<R, D> : IrElementVisitor<R, D> {
	fun visitComponent(component: IrComponent<*>, data: D, default: () -> R): R = default()
	
	fun visitComponentExpression(component: IrComponentExpression<*>, data: D, default: () -> R): R =
		visitComponent(component, data, default)
	
	fun visitComponentDeclaration(component: IrComponentDeclaration<*>, data: D, default: () -> R): R =
		visitComponent(component, data, default)
}

interface IrComponentVisitorVoid : IrComponentVisitor<Unit, Nothing?>, IrElementVisitorVoid {
	override fun visitComponent(component: IrComponent<*>, data: Nothing?, default: () -> Unit) =
		visitComponent(component, default)
	
	override fun visitComponentExpression(component: IrComponentExpression<*>, data: Nothing?, default: () -> Unit) =
		visitComponentExpression(component, default)
	
	override fun visitComponentDeclaration(component: IrComponentDeclaration<*>, data: Nothing?, default: () -> Unit) =
		visitComponentDeclaration(component, default)
	
	
	fun visitComponent(component: IrComponent<*>, default: () -> Unit) = default()
	
	fun visitComponentExpression(component: IrComponentExpression<*>, default: () -> Unit) =
		visitComponent(component, default)
	
	fun visitComponentDeclaration(component: IrComponentDeclaration<*>, default: () -> Unit) =
		visitComponent(component, default)
}
