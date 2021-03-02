@file:OptIn(ObsoleteDescriptorBasedAPI::class)

package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.builtins.extractParameterNameFromFunctionTypeArgument
import org.jetbrains.kotlin.builtins.getReceiverTypeFromFunctionType
import org.jetbrains.kotlin.builtins.getReturnTypeFromFunctionType
import org.jetbrains.kotlin.builtins.getValueParameterTypesFromFunctionType
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.AnonymousFunctionDescriptor
import org.jetbrains.kotlin.descriptors.impl.ValueParameterDescriptorImpl
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorFactory
import org.jetbrains.kotlin.types.KotlinType


fun IrElementScope.createAnonymousFunctionDescriptor(
	valueParameters: List<ValueParameterDescriptor> = emptyList(),
	extensionReceiverParameter: ReceiverParameterDescriptor? = null,
	dispatchReceiverParameter: ReceiverParameterDescriptor? = null,
	typeParameters: List<TypeParameterDescriptor> = emptyList(),
	returnType: KotlinType = context.builtIns.unitType,
	modality: Modality = Modality.FINAL,
	visibility: DescriptorVisibility = DescriptorVisibilities.LOCAL,
	owner: DeclarationDescriptor,
	annotations: Annotations = Annotations.EMPTY,
	kind: CallableMemberDescriptor.Kind = CallableMemberDescriptor.Kind.SYNTHESIZED,
	sourceElement: SourceElement = SourceElement.NO_SOURCE,
	isSuspend: Boolean = false
) = AnonymousFunctionDescriptor(owner, annotations, kind, sourceElement, isSuspend).apply {
	initialize(extensionReceiverParameter, dispatchReceiverParameter, typeParameters, valueParameters, returnType, modality, visibility)
}

fun IrBuilderScope.createAnonymousFunctionDescriptor(
	valueParameters: List<ValueParameterDescriptor> = emptyList(),
	extensionReceiverParameter: ReceiverParameterDescriptor? = null,
	dispatchReceiverParameter: ReceiverParameterDescriptor? = null,
	typeParameters: List<TypeParameterDescriptor> = emptyList(),
	returnType: KotlinType = context.builtIns.unitType,
	modality: Modality = Modality.FINAL,
	visibility: DescriptorVisibility = DescriptorVisibilities.LOCAL,
	owner: DeclarationDescriptor = scope.scopeOwnerSymbol.descriptor,
	annotations: Annotations = Annotations.EMPTY,
	kind: CallableMemberDescriptor.Kind = CallableMemberDescriptor.Kind.SYNTHESIZED,
	sourceElement: SourceElement = SourceElement.NO_SOURCE,
	isSuspend: Boolean = false
) = (this as IrElementScope).createAnonymousFunctionDescriptor(
	valueParameters, extensionReceiverParameter, dispatchReceiverParameter, typeParameters, returnType,
	modality, visibility, owner, annotations, kind, sourceElement, isSuspend
)

fun IrBuilderScope.createFunctionDescriptorFromFunctionalType(
	type: KotlinType,
	owner: DeclarationDescriptor = scope.scopeOwnerSymbol.descriptor
) = AnonymousFunctionDescriptor(
	owner,
	Annotations.EMPTY,
	CallableMemberDescriptor.Kind.SYNTHESIZED,
	SourceElement.NO_SOURCE,
	false
).apply {
	initialize(
		type.getReceiverTypeFromFunctionType()?.let {
			DescriptorFactory.createExtensionReceiverParameterForCallable(
				this,
				it,
				Annotations.EMPTY
			)
		},
		null,
		emptyList(),
		type.getValueParameterTypesFromFunctionType().mapIndexed { i, t ->
			ValueParameterDescriptorImpl(
				containingDeclaration = this,
				original = null,
				index = i,
				annotations = Annotations.EMPTY,
				name = t.type.extractParameterNameFromFunctionTypeArgument()
					?: Name.identifier("p$i"),
				outType = t.type,
				declaresDefaultValue = false,
				isCrossinline = false,
				isNoinline = false,
				varargElementType = null,
				source = SourceElement.NO_SOURCE
			)
		},
		type.getReturnTypeFromFunctionType(),
		Modality.FINAL,
		DescriptorVisibilities.LOCAL,
		null
	)
	
	isOperator = false
	isInfix = false
	isExternal = false
	isInline = false
	isTailrec = false
	isSuspend = false
	isExpect = false
	isActual = false
}


fun IrMemberAccessExpression<*>.putValueArgument(parameter: IrValueParameter, value: IrExpression?) {
	putValueArgument(parameter.index, value)
}

fun IrMemberAccessExpression<*>.putValueArgument(parameter: IrValueParameterSymbol, value: IrExpression?) {
	putValueArgument(parameter.owner, value)
}
