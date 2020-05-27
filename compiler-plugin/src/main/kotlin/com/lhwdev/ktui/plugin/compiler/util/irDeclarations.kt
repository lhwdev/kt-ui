package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.LocalVariableDescriptor
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.declarations.impl.IrVariableImpl
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrVariableSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.toKotlinType
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.KotlinType


// Variables

fun IrStatementsScope<*>.irVariable(
	name: String,
	type: IrType,
	isVar: Boolean = false,
	isDelegated: Boolean = false,
	isLateinit: Boolean = false,
	annotations: Annotations = Annotations.EMPTY,
	origin: IrDeclarationOrigin = IrDeclarationOrigin.DEFINED
) = IrVariableImpl(startOffset, endOffset, origin, IrVariableSymbolImpl(
	LocalVariableDescriptor(scope.scopeOwner, annotations, Name.guessByFirstCharacter(name),
		type.toKotlinType(), isVar, isDelegated, isLateinit, SourceElement.NO_SOURCE)), type)

fun IrStatementsScope<*>.irTemporary(
	value: IrExpression,
	nameHint: String? = null,
	typeHint: KotlinType? = null,
	irType: IrType? = null
): IrVariable {
	val temporary = scope.createTemporaryVariable(value, nameHint, type = typeHint, irType = irType)
	+temporary
	return temporary
}

fun IrStatementsScope<*>.irTemporaryVar(
	value: IrExpression,
	nameHint: String? = null,
	typeHint: KotlinType? = null
): IrVariable {
	val temporary = scope.createTemporaryVariable(value, nameHint, isMutable = true, type = typeHint)
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


