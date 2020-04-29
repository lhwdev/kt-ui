package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.types.TypeSubstitutor


interface WidgetCallableDescriptor : CallableDescriptor {
	val underlyingDescriptor: CallableDescriptor
}


interface WidgetFunctionDescriptor : FunctionDescriptor, WidgetCallableDescriptor {
	override val underlyingDescriptor: FunctionDescriptor
	
}

interface WidgetPropertyDescriptor : PropertyDescriptor, WidgetCallableDescriptor {
	override val underlyingDescriptor: PropertyDescriptor
}

class WidgetPropertyDescriptorImpl(
	override val underlyingDescriptor: PropertyDescriptor
) : PropertyDescriptor by underlyingDescriptor, WidgetPropertyDescriptor {
	override fun substitute(substitutor: TypeSubstitutor): PropertyDescriptor? {
		return underlyingDescriptor.substitute(substitutor)?.let {
			WidgetPropertyDescriptorImpl(it)
		}
	}
}

fun WidgetFunctionDescriptor(
	underlyingDescriptor: FunctionDescriptor
): WidgetFunctionDescriptor {
	return if(underlyingDescriptor is SimpleFunctionDescriptor) {
		WidgetSimpleFunctionDescriptorImpl(underlyingDescriptor)
	} else {
		WidgetFunctionDescriptorImpl(underlyingDescriptor)
	}
}

class WidgetFunctionDescriptorImpl(
	override val underlyingDescriptor: FunctionDescriptor
) : FunctionDescriptor by underlyingDescriptor, WidgetFunctionDescriptor {
	override fun substitute(substitutor: TypeSubstitutor): FunctionDescriptor? {
		return underlyingDescriptor.substitute(substitutor)?.let {
			WidgetFunctionDescriptor(it)
		}
	}
}

class WidgetSimpleFunctionDescriptorImpl(
	override val underlyingDescriptor: SimpleFunctionDescriptor
) : SimpleFunctionDescriptor by underlyingDescriptor, WidgetFunctionDescriptor {
	override fun substitute(substitutor: TypeSubstitutor): FunctionDescriptor? {
		return underlyingDescriptor.substitute(substitutor)?.let {
			WidgetFunctionDescriptor(it)
		}
	}
}
