package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.IdSignatureComposer
import org.jetbrains.kotlin.ir.util.SymbolTable


class AutoResolveUnboundSymbolTable(symbolTable: SymbolTable) : SymbolTable(symbolTable::class.java.getDeclaredField("signaturer").run {
	isAccessible = true
	get(symbolTable) as IdSignatureComposer
}/*symbolTable.signaturer*/ /* made public in v1.7-M1-eap-2 */) {
	@PublishedApi
	internal var isNested = false
	
	@Suppress("NOTHING_TO_INLINE")
	inline fun <S : IrSymbol> bind(referencedValue: S): S {
		if(!isNested) try {
			isNested = true
			if(!referencedValue.isBound) referencedValue.tryBind()
//			log4("get bound ${referencedValue.descriptor.dump()}")
		} finally {
			isNested = false
		}
		return referencedValue
	}
	
	override fun referenceClass(descriptor: ClassDescriptor) =
		bind(super.referenceClass(descriptor))
	
	override fun referenceClassFromLinker(descriptor: ClassDescriptor, sig: IdSignature) =
		bind(super.referenceClassFromLinker(descriptor, sig))
	
	override fun referenceConstructor(descriptor: ClassConstructorDescriptor) =
		bind(super.referenceConstructor(descriptor))
	
	override fun referenceConstructorFromLinker(descriptor: ClassConstructorDescriptor, sig: IdSignature) =
		bind(super.referenceConstructorFromLinker(descriptor, sig))
	
	override fun referenceDeclaredFunction(descriptor: FunctionDescriptor) =
		bind(super.referenceDeclaredFunction(descriptor))
	
	override fun referenceEnumEntry(descriptor: ClassDescriptor) =
		bind(super.referenceEnumEntry(descriptor))
	
	override fun referenceEnumEntryFromLinker(descriptor: ClassDescriptor, sig: IdSignature) =
		bind(super.referenceEnumEntryFromLinker(descriptor, sig))
	
	override fun referenceField(descriptor: PropertyDescriptor) =
		bind(super.referenceField(descriptor))
	
	override fun referenceFieldFromLinker(descriptor: PropertyDescriptor, sig: IdSignature) =
		bind(super.referenceFieldFromLinker(descriptor, sig))
	
	override fun referencePropertyFromLinker(descriptor: PropertyDescriptor, sig: IdSignature) =
		bind(super.referencePropertyFromLinker(descriptor, sig))
	
	override fun referenceSimpleFunction(descriptor: FunctionDescriptor) =
		bind(super.referenceSimpleFunction(descriptor))
	
	override fun referenceSimpleFunctionFromLinker(descriptor: FunctionDescriptor, sig: IdSignature) =
		bind(super.referenceSimpleFunctionFromLinker(descriptor, sig))
	
	override fun referenceTypeAlias(descriptor: TypeAliasDescriptor) =
		bind(super.referenceTypeAlias(descriptor))
	
	override fun referenceTypeAliasFromLinker(descriptor: TypeAliasDescriptor, sig: IdSignature) =
		bind(super.referenceTypeAliasFromLinker(descriptor, sig))
	
	override fun referenceTypeParameter(classifier: TypeParameterDescriptor) =
		bind(super.referenceTypeParameter(classifier))
	
	override fun referenceTypeParameterFromLinker(classifier: TypeParameterDescriptor, sig: IdSignature) =
		bind(super.referenceTypeParameterFromLinker(classifier, sig))
	
	override fun referenceValueParameter(descriptor: ParameterDescriptor) =
		bind(super.referenceValueParameter(descriptor))
	
	override fun referenceVariable(descriptor: VariableDescriptor) =
		bind(super.referenceVariable(descriptor))
}
