package com.lhwdev.ktui.plugin.compiler.util

import com.lhwdev.ktui.plugin.compiler.patchDeclarationParentsChildren
import org.jetbrains.kotlin.descriptors.DescriptorVisibility
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DeserializedContainerSource


abstract class IrSimpleFunctionWrapper(original: IrSimpleFunction) : IrSimpleFunction() {
	override val startOffset: Int = original.startOffset
	override val endOffset: Int = original.endOffset
	
	override val factory: IrFactory = original.factory
	
	@ObsoleteDescriptorBasedAPI
	override val descriptor: FunctionDescriptor
		get() = symbol.descriptor
	
	override val name: Name = original.name
	override val modality: Modality = original.modality
	override var visibility: DescriptorVisibility = original.visibility
	
	override var attributeOwnerId: IrAttributeContainer = original.attributeOwnerId
	override val containerSource: DeserializedContainerSource? = original.containerSource
	override var origin: IrDeclarationOrigin = original.origin
	override var parent: IrDeclarationParent = original.parent
	
	override var valueParameters: List<IrValueParameter> = original.valueParameters
	
	override var typeParameters: List<IrTypeParameter> = original.typeParameters
	
	override var dispatchReceiverParameter: IrValueParameter? = original.dispatchReceiverParameter
	
	override var extensionReceiverParameter: IrValueParameter? = original.extensionReceiverParameter
	
	
	override var annotations: List<IrConstructorCall> = original.annotations
	override var returnType: IrType = original.returnType
	
	override var body: IrBody? = original.body
	
	override val isExpect: Boolean = original.isExpect
	override val isExternal: Boolean = original.isExternal
	override val isInline: Boolean = original.isInline
	override val isFakeOverride: Boolean = original.isFakeOverride
	override val isInfix: Boolean = original.isInfix
	override val isOperator: Boolean = original.isOperator
	override val isSuspend: Boolean = original.isSuspend
	override val isTailrec: Boolean = original.isTailrec
	
	override var metadata: MetadataSource? = original.metadata
	
	override var overriddenSymbols: List<IrSimpleFunctionSymbol> = original.overriddenSymbols
	override var correspondingPropertySymbol: IrPropertySymbol? = original.correspondingPropertySymbol
	
	init {
		patchDeclarationParentsChildren()
	}
}

class IrSimpleFunctionWrapperImpl(original: IrSimpleFunction, override val symbol: IrSimpleFunctionSymbol) :
	IrSimpleFunctionWrapper(original)


open class IrCallWrapper(original: IrCall) : IrCall(original.typeArgumentsCount, original.valueArgumentsCount) {
	init {
		original.valueArguments.forEachIndexed { index, argument -> putValueArgument(index, argument) }
		original.typeArguments.forEachIndexed { index, argument -> putTypeArgument(index, argument) }
		dispatchReceiver = original.dispatchReceiver
		extensionReceiver = original.extensionReceiver
	}
	
	override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R = visitor.visitCall(this, data)
	
	override val startOffset: Int = original.startOffset
	override val endOffset: Int = original.endOffset
	override val symbol: IrSimpleFunctionSymbol = original.symbol
	override val superQualifierSymbol: IrClassSymbol? = original.superQualifierSymbol
	override var type: IrType = original.type
	override val origin: IrStatementOrigin? = original.origin
	override var attributeOwnerId: IrAttributeContainer = original.attributeOwnerId
}

