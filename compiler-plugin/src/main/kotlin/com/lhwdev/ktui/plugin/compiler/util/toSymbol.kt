package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.util.findFirstFunction
import org.jetbrains.kotlin.ir.util.referenceClassifier
import org.jetbrains.kotlin.ir.util.referenceFunction
import org.jetbrains.kotlin.name.FqName


val FunctionDescriptor.symbol get() = context.symbolTable.referenceFunction(this)
val FunctionDescriptor.simpleFunctionSymbol get() = context.symbolTable.referenceSimpleFunction(this)
val ClassConstructorDescriptor.symbol get() = context.symbolTable.referenceConstructor(this)
val PropertyDescriptor.symbol get() = context.symbolTable.referenceProperty(this)
val ClassDescriptor.symbol get() = context.symbolTable.referenceClass(this)
val TypeAliasDescriptor.symbol get() = context.symbolTable.referenceTypeAlias(this)
val ClassifierDescriptor.symbol get() = context.symbolTable.referenceClassifier(this)
val ValueDescriptor.symbol get() = context.symbolTable.referenceValue(this)
val VariableDescriptor.symbol get() = context.symbolTable.referenceVariable(this)
val ValueParameterDescriptor.symbol get() = context.symbolTable.referenceValueParameter(this)


fun IrElementScope.irClassSymbol(name: FqName) =
	context.moduleDescriptor.resolveClassByFqName(name, NoLookupLocation.FROM_BACKEND)!!.symbol

inline fun IrElementScope.irFunctionSymbol(packageName: FqName, name: String, predicate: (CallableMemberDescriptor) -> Boolean) =
	context.moduleDescriptor.getPackage(packageName).memberScope.findFirstFunction(name, predicate).symbol

fun IrElementScope.irFunctionSymbol(packageName: FqName, name: String, valueParametersCount: Int) =
	irFunctionSymbol(packageName, name) { it.valueParameters.size == valueParametersCount }
