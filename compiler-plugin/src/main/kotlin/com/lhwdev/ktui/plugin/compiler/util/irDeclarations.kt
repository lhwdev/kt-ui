package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.declarations.impl.IrVariableImpl
import org.jetbrains.kotlin.ir.descriptors.WrappedVariableDescriptor
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrExpressionBodyImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrVariableSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.name.Name


// Variables

fun IrBuilderScope.irVariable(
	name: String,
	type: IrType,
	isVar: Boolean = false,
	isConst: Boolean = false,
	isLateinit: Boolean = false,
	annotations: List<IrConstructorCall> = emptyList(),
	origin: IrDeclarationOrigin = IrDeclarationOrigin.DEFINED
): IrVariable {
	val name2 = Name.guessByFirstCharacter(name)
	val descriptor = WrappedVariableDescriptor()
	val variable = IrVariableImpl(
		startOffset, endOffset, origin, IrVariableSymbolImpl(descriptor), name2, type, isVar, isConst, isLateinit
	)
	variable.annotations = annotations
	return variable
}

fun IrBuilderScope.irVariable(
	name: String,
	type: IrType,
	isVar: Boolean = false,
	isConst: Boolean = false,
	isLateinit: Boolean = false,
	annotations: List<IrConstructorCall> = emptyList(),
	origin: IrDeclarationOrigin = IrDeclarationOrigin.DEFINED,
	initializer: IrExpression
): IrVariable = irVariable(name, type, isVar, isConst, isLateinit, annotations, origin).also {
	it.initializer = initializer
}

fun IrStatementsScope<*>.irTemporary(
	value: IrExpression,
	nameHint: String? = null,
	type: IrType? = null
): IrVariable {
	val temporary = scope.createTemporaryVariable(value, nameHint, irType = type)
	+temporary
	return temporary
}

fun IrStatementsScope<*>.irTemporaryVar(
	value: IrExpression,
	nameHint: String? = null,
	type: IrType? = null
): IrVariable {
	val temporary = scope.createTemporaryVariable(value, nameHint, isMutable = true, irType = type)
	+temporary
	return temporary
}

fun IrStatementsScope<*>.irTemporaryVariable(
	type: IrType,
	nameHint: String? = null,
	isMutable: Boolean = true
): IrVariable {
	val temporary = scope.createTemporaryVariableDeclaration(type, nameHint, isMutable = isMutable)
	+temporary
	return temporary
}

fun IrBuilderScope.irCreateTemporary(
	value: IrExpression,
	nameHint: String? = null,
	type: IrType? = null
): IrVariable = scope.createTemporaryVariable(value, nameHint, irType = type)

fun IrBuilderScope.irCreateTemporaryVar(
	value: IrExpression,
	nameHint: String? = null,
	type: IrType? = null
): IrVariable = scope.createTemporaryVariable(value, nameHint, isMutable = true, irType = type)

fun IrBuilderScope.irCreateTemporaryVariable(
	type: IrType,
	nameHint: String? = null,
	isMutable: Boolean = true
): IrVariable = scope.createTemporaryVariableDeclaration(type, nameHint, isMutable = isMutable)


// Body

class IrBlockBodyBuilder(override val startOffset: Int, override val endOffset: Int, override val scope: Scope) : IrStatementsScope<IrBlockBody> {
	val statements = mutableListOf<IrStatement>()
	
	override fun IrStatement.unaryPlus() {
		statements += this
	}
	
	override fun build(): IrBlockBody = IrBlockBodyImpl(startOffset, endOffset, statements)
}

inline fun IrBuilderScope.irBlockBody(
	body: IrBlockBodyBuilder.() -> Unit
) = IrBlockBodyBuilder(startOffset, endOffset, scope).apply(body).build()

fun IrBuilderScope.irExpressionBody(
	body: IrExpression
) = IrExpressionBodyImpl(startOffset, endOffset, body)


