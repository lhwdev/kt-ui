package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.util.DescriptorsRemapper
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.ReferenceSymbolTable
import org.jetbrains.kotlin.ir.util.SymbolRemapper


class RemappedSymbolTable(
	val original: ReferenceSymbolTable,
	val symbolRemapper: SymbolRemapper,
	val descriptorsRemapper: DescriptorsRemapper
) : ReferenceSymbolTable {
	override fun enterScope(owner: DeclarationDescriptor) {
		original.enterScope(owner)
	}
	
	override fun leaveScope(owner: DeclarationDescriptor) {
		original.leaveScope(owner)
	}
	
	override fun referenceClass(descriptor: ClassDescriptor): IrClassSymbol {
		return symbolRemapper.getReferencedClass(
			original.referenceClass(
				descriptorsRemapper.remapDeclaredClass(
					descriptor
				)
			)
		)
	}
	
	override fun referenceClassFromLinker(descriptor: ClassDescriptor, sig: IdSignature): IrClassSymbol {
		return symbolRemapper.getReferencedClass(
			original.referenceClassFromLinker(
				descriptorsRemapper.remapDeclaredClass(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceConstructor(descriptor: ClassConstructorDescriptor): IrConstructorSymbol {
		return symbolRemapper.getReferencedConstructor(
			original.referenceConstructor(
				descriptorsRemapper.remapDeclaredConstructor(
					descriptor
				)
			)
		)
	}
	
	override fun referenceConstructorFromLinker(
		descriptor: ClassConstructorDescriptor,
		sig: IdSignature
	): IrConstructorSymbol {
		return symbolRemapper.getReferencedConstructor(
			original.referenceConstructorFromLinker(
				descriptorsRemapper.remapDeclaredConstructor(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceDeclaredFunction(descriptor: FunctionDescriptor): IrSimpleFunctionSymbol {
		return symbolRemapper.getDeclaredFunction(
			original.referenceDeclaredFunction(
				descriptorsRemapper.remapDeclaredSimpleFunction(
					descriptor
				)
			)
		)
	}
	
	override fun referenceEnumEntry(descriptor: ClassDescriptor): IrEnumEntrySymbol {
		return symbolRemapper.getReferencedEnumEntry(
			original.referenceEnumEntry(
				descriptorsRemapper.remapDeclaredEnumEntry(
					descriptor
				)
			)
		)
	}
	
	override fun referenceEnumEntryFromLinker(descriptor: ClassDescriptor, sig: IdSignature): IrEnumEntrySymbol {
		return symbolRemapper.getReferencedEnumEntry(
			original.referenceEnumEntryFromLinker(
				descriptorsRemapper.remapDeclaredEnumEntry(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceField(descriptor: PropertyDescriptor): IrFieldSymbol {
		return symbolRemapper.getReferencedField(
			original.referenceField(
				descriptorsRemapper.remapDeclaredField(
					descriptor
				)
			)
		)
	}
	
	override fun referenceFieldFromLinker(descriptor: PropertyDescriptor, sig: IdSignature): IrFieldSymbol {
		return symbolRemapper.getReferencedField(
			original.referenceFieldFromLinker(
				descriptorsRemapper.remapDeclaredField(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceProperty(descriptor: PropertyDescriptor): IrPropertySymbol {
		return symbolRemapper.getReferencedProperty(
			original.referenceProperty(
				descriptorsRemapper.remapDeclaredProperty(
					descriptor
				)
			)
		)
	}
	
	override fun referenceProperty(descriptor: PropertyDescriptor, generate: () -> IrProperty): IrProperty {
		return original.referenceProperty(descriptor, generate) // ???
	}
	
	override fun referencePropertyFromLinker(descriptor: PropertyDescriptor, sig: IdSignature): IrPropertySymbol {
		return symbolRemapper.getReferencedProperty(
			original.referencePropertyFromLinker(
				descriptorsRemapper.remapDeclaredProperty(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceSimpleFunction(descriptor: FunctionDescriptor): IrSimpleFunctionSymbol {
		return symbolRemapper.getReferencedSimpleFunction(
			original.referenceSimpleFunction(
				descriptorsRemapper.remapDeclaredSimpleFunction(
					descriptor
				)
			)
		)
	}
	
	override fun referenceSimpleFunctionFromLinker(
		descriptor: FunctionDescriptor,
		sig: IdSignature
	): IrSimpleFunctionSymbol {
		return symbolRemapper.getReferencedSimpleFunction(
			original.referenceSimpleFunctionFromLinker(
				descriptorsRemapper.remapDeclaredSimpleFunction(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceTypeAlias(descriptor: TypeAliasDescriptor): IrTypeAliasSymbol {
		return symbolRemapper.getReferencedTypeAlias(
			original.referenceTypeAlias(
				descriptorsRemapper.remapDeclaredTypeAlias(
					descriptor
				)
			)
		)
	}
	
	override fun referenceTypeAliasFromLinker(descriptor: TypeAliasDescriptor, sig: IdSignature): IrTypeAliasSymbol {
		return symbolRemapper.getReferencedTypeAlias(
			original.referenceTypeAliasFromLinker(
				descriptorsRemapper.remapDeclaredTypeAlias(
					descriptor
				), sig
			)
		)
	}
	
	override fun referenceTypeParameter(classifier: TypeParameterDescriptor): IrTypeParameterSymbol {
		return original.referenceTypeParameter(descriptorsRemapper.remapDeclaredTypeParameter(classifier)).let {
			try {
				symbolRemapper.getDeclaredTypeParameter(it)
			} catch(e: Throwable) {
				it
			}
		}
	}
	
	override fun referenceTypeParameterFromLinker(
		classifier: TypeParameterDescriptor,
		sig: IdSignature
	): IrTypeParameterSymbol {
		return original.referenceTypeParameterFromLinker(
			descriptorsRemapper.remapDeclaredTypeParameter(classifier),
			sig
		).let {
			try {
				symbolRemapper.getDeclaredTypeParameter(it)
			} catch(e: Throwable) {
				it
			}
		}
	}
	
	override fun referenceValueParameter(descriptor: ParameterDescriptor): IrValueParameterSymbol {
		return original.referenceValueParameter(descriptorsRemapper.remapDeclaredValueParameter(descriptor)).let {
			try {
				symbolRemapper.getDeclaredValueParameter(it)
			} catch(e: Throwable) {
				it
			}
		}
	}
	
	override fun referenceVariable(descriptor: VariableDescriptor): IrVariableSymbol {
		return symbolRemapper.getReferencedVariable(
			original.referenceVariable(
				descriptorsRemapper.remapDeclaredVariable(
					descriptor
				)
			)
		)
	}
}
