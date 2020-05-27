package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.util.referenceClassifier
import org.jetbrains.kotlin.ir.util.referenceFunction


val CallableMemberDescriptor.symbol get() = context.symbolTable.referenceFunction(this)
val SimpleFunctionDescriptor.symbol get() = context.symbolTable.referenceSimpleFunction(this)
val ClassConstructorDescriptor.symbol get() = context.symbolTable.referenceConstructor(this)
val PropertyDescriptor.symbol get() = context.symbolTable.referenceProperty(this)
val ClassDescriptor.symbol get() = context.symbolTable.referenceClass(this)
val TypeAliasDescriptor.symbol get() = context.symbolTable.referenceTypeAlias(this)
val ClassifierDescriptor.symbol get() = context.symbolTable.referenceClassifier(this)
val ValueDescriptor.symbol get() = context.symbolTable.referenceValue(this)
val VariableDescriptor.symbol get() = context.symbolTable.referenceVariable(this)
val ValueParameterDescriptor.symbol get() = context.symbolTable.referenceValueParameter(this)
