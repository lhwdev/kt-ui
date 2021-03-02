@file:OptIn(ObsoleteDescriptorBasedAPI::class) // for descriptor-related

package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.util.findFirstFunction
import org.jetbrains.kotlin.ir.util.referenceClassifier
import org.jetbrains.kotlin.ir.util.referenceFunction
import org.jetbrains.kotlin.name.FqName


val CallableMemberDescriptor.symbol get() = pluginContext.symbolTable.referenceFunction(this)
val FunctionDescriptor.simpleFunctionSymbol get() = pluginContext.symbolTable.referenceSimpleFunction(this)
val ClassConstructorDescriptor.symbol get() = pluginContext.symbolTable.referenceConstructor(this)
val PropertyDescriptor.symbol get() = pluginContext.symbolTable.referenceProperty(this)
val ClassDescriptor.symbol get() = pluginContext.symbolTable.referenceClass(this)
val TypeAliasDescriptor.symbol get() = pluginContext.symbolTable.referenceTypeAlias(this)
val ClassifierDescriptor.symbol get() = pluginContext.symbolTable.referenceClassifier(this)
val VariableDescriptor.symbol get() = pluginContext.symbolTable.referenceVariable(this)
val ValueParameterDescriptor.symbol get() = pluginContext.symbolTable.referenceValueParameter(this)


fun IrElementScope.irClassSymbol(name: FqName) =
	context.moduleDescriptor.resolveClassByFqName(name, NoLookupLocation.FROM_BACKEND)!!.symbol

inline fun IrElementScope.irFunctionSymbol(
	packageName: FqName,
	name: String,
	predicate: (CallableMemberDescriptor) -> Boolean
) = context.moduleDescriptor.getPackage(packageName).memberScope.findFirstFunction(name, predicate).symbol

fun IrElementScope.irFunctionSymbol(packageName: FqName, name: String, valueParametersCount: Int) =
	irFunctionSymbol(packageName, name) { it.valueParameters.size == valueParametersCount }
