package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.impl.makeTypeProjection
import org.jetbrains.kotlin.ir.types.typeWithArguments
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.types.Variance


data class IrFunctionLiteralType(
	val valueParameters: List<IrType>,
	val returnType: IrType,
	val lambdaClassSymbol: IrClassSymbol = irBuiltIns.function(valueParameters.size),
	val lambdaType: IrType = lambdaClassSymbol
		.typeWithArguments((valueParameters + returnType).map { makeTypeProjection(it, Variance.INVARIANT) }),
	val functionSymbol: IrSimpleFunctionSymbol = lambdaClassSymbol.getSimpleFunction("invoke")!!
)
