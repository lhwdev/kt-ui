@file:Suppress("DEPRECATION")

package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.descriptors.*
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrReturnableBlock
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.IrBindableSymbolBase
import org.jetbrains.kotlin.ir.symbols.impl.IrExternalPackageFragmentSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrFileSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrReturnableBlockSymbolImpl
import org.jetbrains.kotlin.ir.util.DescriptorsRemapper
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.ReferenceSymbolTable
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid


open class DeepCopyWrappingSymbolRemapper(val symbolTable: ReferenceSymbolTable) : IrElementVisitorVoid, SymbolRemapper,
	DescriptorsRemapper, ReferenceSymbolTable {
	private val classes = hashMapOf<IrClassSymbol, IrClassSymbol>()
	private val constructors = hashMapOf<IrConstructorSymbol, IrConstructorSymbol>()
	private val enumEntries = hashMapOf<IrEnumEntrySymbol, IrEnumEntrySymbol>()
	private val externalPackageFragments = hashMapOf<IrExternalPackageFragmentSymbol, IrExternalPackageFragmentSymbol>()
	private val fields = hashMapOf<IrFieldSymbol, IrFieldSymbol>()
	private val files = hashMapOf<IrFileSymbol, IrFileSymbol>()
	private val functions = hashMapOf<IrSimpleFunctionSymbol, IrSimpleFunctionSymbol>()
	private val properties = hashMapOf<IrPropertySymbol, IrPropertySymbol>()
	private val returnableBlocks = hashMapOf<IrReturnableBlockSymbol, IrReturnableBlockSymbol>()
	private val typeParameters = hashMapOf<IrTypeParameterSymbol, IrTypeParameterSymbol>()
	private val valueParameters = hashMapOf<IrValueParameterSymbol, IrValueParameterSymbol>()
	private val variables = hashMapOf<IrVariableSymbol, IrVariableSymbol>()
	private val localDelegatedProperties = hashMapOf<IrLocalDelegatedPropertySymbol, IrLocalDelegatedPropertySymbol>()
	private val typeAliases = hashMapOf<IrTypeAliasSymbol, IrTypeAliasSymbol>()
	
	private val classesMapping = hashMapOf<ClassDescriptor, IrClassSymbol>()
	private val constructorsMapping = hashMapOf<ClassConstructorDescriptor, IrConstructorSymbol>()
	private val enumEntriesMapping = hashMapOf<ClassDescriptor, IrEnumEntrySymbol>()
	private val externalPackageFragmentsMapping =
		hashMapOf<PackageFragmentDescriptor, IrExternalPackageFragmentSymbol>()
	private val fieldsMapping = hashMapOf<PropertyDescriptor, IrFieldSymbol>()
	private val filesMapping = hashMapOf<PackageFragmentDescriptor, IrFileSymbol>()
	private val functionsMapping = hashMapOf<FunctionDescriptor, IrSimpleFunctionSymbol>()
	private val propertiesMapping = hashMapOf<PropertyDescriptor, IrPropertySymbol>()
	private val returnableBlocksMapping = hashMapOf<FunctionDescriptor, IrReturnableBlockSymbol>()
	private val typeParametersMapping = hashMapOf<TypeParameterDescriptor, IrTypeParameterSymbol>()
	private val valueParametersMapping = hashMapOf<ParameterDescriptor, IrValueParameterSymbol>()
	private val variablesMapping = hashMapOf<VariableDescriptor, IrVariableSymbol>()
	private val localDelegatedPropertiesMapping =
		hashMapOf<VariableDescriptorWithAccessors, IrLocalDelegatedPropertySymbol>()
	private val typeAliasesMapping = hashMapOf<TypeAliasDescriptor, IrTypeAliasSymbol>()
	
	private val classesDescriptor = hashMapOf<ClassDescriptor, WrappedClassDescriptor>()
	private val constructorsDescriptor = hashMapOf<ClassConstructorDescriptor, WrappedClassConstructorDescriptor>()
	private val enumEntriesDescriptor = hashMapOf<ClassDescriptor, WrappedEnumEntryDescriptor>()
	private val fieldsDescriptor = hashMapOf<PropertyDescriptor, WrappedFieldDescriptor>()
	private val functionsDescriptor = hashMapOf<FunctionDescriptor, WrappedSimpleFunctionDescriptor>()
	private val propertiesDescriptor = hashMapOf<PropertyDescriptor, WrappedPropertyDescriptor>()
	
	//	private val returnableBlocksDescriptor = hashMapOf<IrReturnableBlockSymbol, IrReturnableBlockSymbol>()
	private val typeParametersDescriptor = hashMapOf<TypeParameterDescriptor, WrappedTypeParameterDescriptor>()
	private val valueParametersDescriptor = hashMapOf<ParameterDescriptor, ParameterDescriptor>()
	private val variablesDescriptor = hashMapOf<VariableDescriptor, WrappedVariableDescriptor>()
	private val localDelegatedPropertiesDescriptor =
		hashMapOf<VariableDescriptorWithAccessors, WrappedVariableDescriptorWithAccessor>()
	private val typeAliasesDescriptor = hashMapOf<TypeAliasDescriptor, WrappedTypeAliasDescriptor>()
	
	override fun visitElement(element: IrElement) {
		element.acceptChildrenVoid(this)
	}
	
	protected inline fun <D : DeclarationDescriptor, B : IrSymbolOwner, reified S : IrBindableSymbol<D, B>>
		remapSymbol(map: MutableMap<S, S>, symbolTable: MutableMap<D, S>, owner: B, createNewSymbol: (S) -> S) {
		val symbol = owner.symbol as S
		val newSymbol = createNewSymbol(symbol)
		map[symbol] = newSymbol
		symbolTable[newSymbol.descriptor] = newSymbol
	}
	
	protected inline fun <D : WrappedDeclarationDescriptor<B>, B : IrSymbolOwner, S : IrBindableSymbol<D, B>>
		createBinding(descriptor: D, create: (D) -> S): S {
		val symbol = create(descriptor)
		return symbol
	}
	
	override fun visitClass(declaration: IrClass) {
		remapSymbol(classes, classesMapping, declaration) {
			val descriptor = WrappedClassDescriptor(it.descriptor.annotations, it.descriptor.source)
			classesDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<ClassDescriptor, IrClass>(descriptor), IrClassSymbol {
				override fun bind(owner: IrClass) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitConstructor(declaration: IrConstructor) {
		remapSymbol(constructors, constructorsMapping, declaration) {
			val descriptor = WrappedClassConstructorDescriptor(it.descriptor.annotations, it.descriptor.source)
			constructorsDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<ClassConstructorDescriptor, IrConstructor>(descriptor), IrConstructorSymbol {
				override fun bind(owner: IrConstructor) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitEnumEntry(declaration: IrEnumEntry) {
		remapSymbol(enumEntries, enumEntriesMapping, declaration) {
			val descriptor = WrappedEnumEntryDescriptor(it.descriptor.annotations, it.descriptor.source)
			enumEntriesDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<ClassDescriptor, IrEnumEntry>(descriptor), IrEnumEntrySymbol {
				override fun bind(owner: IrEnumEntry) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitExternalPackageFragment(declaration: IrExternalPackageFragment) {
		remapSymbol(externalPackageFragments, externalPackageFragmentsMapping, declaration) {
			IrExternalPackageFragmentSymbolImpl(it.descriptor)
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitField(declaration: IrField) {
		remapSymbol(fields, fieldsMapping, declaration) {
			val descriptor = WrappedFieldDescriptor(it.descriptor.annotations, it.descriptor.source)
			fieldsDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<PropertyDescriptor, IrField>(descriptor), IrFieldSymbol {
				override fun bind(owner: IrField) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitFile(declaration: IrFile) {
		remapSymbol(files, filesMapping, declaration) {
			IrFileSymbolImpl(it.descriptor)
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction) {
		remapSymbol(functions, functionsMapping, declaration) {
			val descriptor = WrappedSimpleFunctionDescriptor(it.descriptor.annotations, it.descriptor.source)
			functionsDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<FunctionDescriptor, IrSimpleFunction>(descriptor), IrSimpleFunctionSymbol {
				override fun bind(owner: IrSimpleFunction) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitProperty(declaration: IrProperty) {
		remapSymbol(properties, propertiesMapping, declaration) {
			val descriptor = WrappedPropertyDescriptor(it.descriptor.annotations, it.descriptor.source)
			propertiesDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<PropertyDescriptor, IrProperty>(descriptor), IrPropertySymbol {
				override fun bind(owner: IrProperty) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitTypeParameter(declaration: IrTypeParameter) {
		remapSymbol(typeParameters, typeParametersMapping, declaration) {
			val descriptor = WrappedTypeParameterDescriptor(it.descriptor.annotations, it.descriptor.source)
			typeParametersDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<TypeParameterDescriptor, IrTypeParameter>(descriptor), IrTypeParameterSymbol {
				override fun bind(owner: IrTypeParameter) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitValueParameter(declaration: IrValueParameter) {
		remapSymbol(valueParameters, valueParametersMapping, declaration) {
			val descriptor: ParameterDescriptor =
				if(it.descriptor is ValueParameterDescriptor) WrappedValueParameterDescriptor(
					it.descriptor.annotations,
					it.descriptor.source
				)
				else WrappedReceiverParameterDescriptor(it.descriptor.annotations, it.descriptor.source)
			valueParametersDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<ParameterDescriptor, IrValueParameter>(descriptor), IrValueParameterSymbol {
				override fun bind(owner: IrValueParameter) {
					super.bind(owner)
					
					@Suppress("UNCHECKED_CAST")
					(descriptor as WrappedDeclarationDescriptor<IrValueParameter>).bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitVariable(declaration: IrVariable) {
		remapSymbol(variables, variablesMapping, declaration) {
			val descriptor = WrappedVariableDescriptor(it.descriptor.annotations, it.descriptor.source)
			variablesDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<VariableDescriptor, IrVariable>(descriptor), IrVariableSymbol {
				override fun bind(owner: IrVariable) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitLocalDelegatedProperty(declaration: IrLocalDelegatedProperty) {
		remapSymbol(localDelegatedProperties, localDelegatedPropertiesMapping, declaration) {
			val descriptor = WrappedVariableDescriptorWithAccessor()
			localDelegatedPropertiesDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<VariableDescriptorWithAccessors, IrLocalDelegatedProperty>(descriptor),
				IrLocalDelegatedPropertySymbol {
				override fun bind(owner: IrLocalDelegatedProperty) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitTypeAlias(declaration: IrTypeAlias) {
		remapSymbol(typeAliases, typeAliasesMapping, declaration) {
			val descriptor = WrappedTypeAliasDescriptor()
			typeAliasesDescriptor[it.descriptor] = descriptor
			object : IrBindableSymbolBase<TypeAliasDescriptor, IrTypeAlias>(descriptor), IrTypeAliasSymbol {
				override fun bind(owner: IrTypeAlias) {
					super.bind(owner)
					descriptor.bind(owner)
				}
			}
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitBlock(expression: IrBlock) {
		if(expression is IrReturnableBlock) {
			remapSymbol(returnableBlocks, returnableBlocksMapping, expression) {
				IrReturnableBlockSymbolImpl(expression.descriptor)
			}
		}
		expression.acceptChildrenVoid(this)
	}
	
	// SymbolRemapper
	private fun <T : IrSymbol> Map<T, T>.getDeclared(symbol: T) =
		getOrElse(symbol) {
			throw IllegalArgumentException("Non-remapped symbol $symbol ${symbol.descriptor}")
		}
	
	private fun <T : IrSymbol> Map<T, T>.getReferenced(symbol: T) =
		getOrElse(symbol) { symbol }
	
	override fun getDeclaredClass(symbol: IrClassSymbol): IrClassSymbol =
		classes.getDeclared(symbol)
	
	override fun getDeclaredFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol =
		functions.getDeclared(symbol)
	
	override fun getDeclaredProperty(symbol: IrPropertySymbol): IrPropertySymbol =
		properties.getDeclared(symbol)
	
	override fun getDeclaredField(symbol: IrFieldSymbol): IrFieldSymbol = fields.getDeclared(symbol)
	override fun getDeclaredFile(symbol: IrFileSymbol): IrFileSymbol = files.getDeclared(symbol)
	override fun getDeclaredConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol =
		constructors.getDeclared(symbol)
	
	override fun getDeclaredEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol =
		enumEntries.getDeclared(symbol)
	
	override fun getDeclaredExternalPackageFragment(symbol: IrExternalPackageFragmentSymbol): IrExternalPackageFragmentSymbol =
		externalPackageFragments.getDeclared(symbol)
	
	override fun getDeclaredVariable(symbol: IrVariableSymbol): IrVariableSymbol =
		variables.getDeclared(symbol)
	
	override fun getDeclaredTypeParameter(symbol: IrTypeParameterSymbol): IrTypeParameterSymbol =
		typeParameters.getDeclared(symbol)
	
	override fun getDeclaredValueParameter(symbol: IrValueParameterSymbol): IrValueParameterSymbol =
		valueParameters.getDeclared(symbol)
	
	override fun getDeclaredLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol =
		localDelegatedProperties.getDeclared(symbol)
	
	override fun getDeclaredTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol =
		typeAliases.getDeclared(symbol)
	
	override fun getReferencedClass(symbol: IrClassSymbol): IrClassSymbol =
		classes.getReferenced(symbol)
	
	override fun getReferencedClassOrNull(symbol: IrClassSymbol?): IrClassSymbol? =
		symbol?.let { classes.getReferenced(it) }
	
	override fun getReferencedEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol =
		enumEntries.getReferenced(symbol)
	
	override fun getReferencedVariable(symbol: IrVariableSymbol): IrVariableSymbol =
		variables.getReferenced(symbol)
	
	override fun getReferencedLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol =
		localDelegatedProperties.getReferenced(symbol)
	
	override fun getReferencedField(symbol: IrFieldSymbol): IrFieldSymbol =
		fields.getReferenced(symbol)
	
	override fun getReferencedConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol =
		constructors.getReferenced(symbol)
	
	override fun getReferencedSimpleFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol =
		functions.getReferenced(symbol)
	
	override fun getReferencedProperty(symbol: IrPropertySymbol): IrPropertySymbol =
		properties.getReferenced(symbol)
	
	override fun getReferencedValue(symbol: IrValueSymbol): IrValueSymbol =
		when(symbol) {
			is IrValueParameterSymbol -> valueParameters.getReferenced(symbol)
			is IrVariableSymbol -> variables.getReferenced(symbol)
			else -> throw IllegalArgumentException("Unexpected symbol $symbol ${symbol.descriptor}")
		}
	
	override fun getReferencedFunction(symbol: IrFunctionSymbol): IrFunctionSymbol =
		when(symbol) {
			is IrSimpleFunctionSymbol -> functions.getReferenced(symbol)
			is IrConstructorSymbol -> constructors.getReferenced(symbol)
			else -> throw IllegalArgumentException("Unexpected symbol $symbol ${symbol.descriptor}")
		}
	
	override fun getReferencedReturnableBlock(symbol: IrReturnableBlockSymbol): IrReturnableBlockSymbol =
		returnableBlocks.getReferenced(symbol)
	
	override fun getReferencedClassifier(symbol: IrClassifierSymbol): IrClassifierSymbol =
		when(symbol) {
			is IrClassSymbol -> classes.getReferenced(symbol)
			is IrTypeParameterSymbol -> typeParameters.getReferenced(symbol)
			else -> throw IllegalArgumentException("Unexpected symbol $symbol ${symbol.descriptor}")
		}
	
	override fun getReferencedTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol =
		typeAliases.getReferenced(symbol)
	
	
	// DescriptorsRemapper
	private fun <T : DeclarationDescriptor> Map<T, T>.getDescriptor(descriptor: T) =
		getOrElse(descriptor) { descriptor }
	
	override fun remapDeclaredClass(descriptor: ClassDescriptor) =
		classesDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredConstructor(descriptor: ClassConstructorDescriptor) =
		constructorsDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredEnumEntry(descriptor: ClassDescriptor) =
		enumEntriesDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredField(descriptor: PropertyDescriptor) =
		fieldsDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredLocalDelegatedProperty(descriptor: VariableDescriptorWithAccessors) =
		localDelegatedPropertiesDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredProperty(descriptor: PropertyDescriptor) =
		propertiesDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredSimpleFunction(descriptor: FunctionDescriptor) =
		functionsDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredTypeAlias(descriptor: TypeAliasDescriptor) =
		typeAliasesDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredTypeParameter(descriptor: TypeParameterDescriptor) =
		typeParametersDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredValueParameter(descriptor: ParameterDescriptor) =
		valueParametersDescriptor.getDescriptor(descriptor)
	
	override fun remapDeclaredVariable(descriptor: VariableDescriptor) =
		variablesDescriptor.getDescriptor(descriptor)
	
	
	// SymbolTable
	override fun enterScope(owner: DeclarationDescriptor) {
		symbolTable.enterScope(owner)
	}
	
	override fun leaveScope(owner: DeclarationDescriptor) {
		symbolTable.leaveScope(owner)
	}
	
	override fun referenceClass(descriptor: ClassDescriptor): IrClassSymbol {
		return classesMapping[descriptor] ?: symbolTable.referenceClass(descriptor)
	}
	
	override fun referenceClassFromLinker(descriptor: ClassDescriptor, sig: IdSignature): IrClassSymbol {
		return classesMapping[descriptor] ?: symbolTable.referenceClassFromLinker(descriptor, sig)
	}
	
	override fun referenceConstructor(descriptor: ClassConstructorDescriptor): IrConstructorSymbol {
		return constructorsMapping[descriptor] ?: symbolTable.referenceConstructor(descriptor)
	}
	
	override fun referenceConstructorFromLinker(
		descriptor: ClassConstructorDescriptor,
		sig: IdSignature
	): IrConstructorSymbol {
		return constructorsMapping[descriptor] ?: symbolTable.referenceConstructorFromLinker(descriptor, sig)
	}
	
	override fun referenceDeclaredFunction(descriptor: FunctionDescriptor): IrSimpleFunctionSymbol {
		return functionsMapping[descriptor] ?: symbolTable.referenceDeclaredFunction(descriptor)
	}
	
	override fun referenceEnumEntry(descriptor: ClassDescriptor): IrEnumEntrySymbol {
		return enumEntriesMapping[descriptor] ?: symbolTable.referenceEnumEntry(descriptor)
	}
	
	override fun referenceEnumEntryFromLinker(descriptor: ClassDescriptor, sig: IdSignature): IrEnumEntrySymbol {
		return enumEntriesMapping[descriptor] ?: symbolTable.referenceEnumEntryFromLinker(descriptor, sig)
	}
	
	override fun referenceField(descriptor: PropertyDescriptor): IrFieldSymbol {
		return fieldsMapping[descriptor] ?: symbolTable.referenceField(descriptor)
	}
	
	override fun referenceFieldFromLinker(descriptor: PropertyDescriptor, sig: IdSignature): IrFieldSymbol {
		return fieldsMapping[descriptor] ?: symbolTable.referenceFieldFromLinker(descriptor, sig)
	}
	
	override fun referenceProperty(descriptor: PropertyDescriptor): IrPropertySymbol {
		return propertiesMapping[descriptor] ?: symbolTable.referenceProperty(descriptor)
	}
	
	override fun referenceProperty(descriptor: PropertyDescriptor, generate: () -> IrProperty): IrProperty {
		return symbolTable.referenceProperty(descriptor, generate) // TODO
	}
	
	override fun referencePropertyFromLinker(descriptor: PropertyDescriptor, sig: IdSignature): IrPropertySymbol {
		return propertiesMapping[descriptor] ?: symbolTable.referencePropertyFromLinker(descriptor, sig) // TODO
	}
	
	override fun referenceSimpleFunction(descriptor: FunctionDescriptor): IrSimpleFunctionSymbol {
		return functionsMapping[descriptor] ?: symbolTable.referenceSimpleFunction(descriptor)
	}
	
	override fun referenceSimpleFunctionFromLinker(
		descriptor: FunctionDescriptor,
		sig: IdSignature
	): IrSimpleFunctionSymbol {
		return functionsMapping[descriptor] ?: symbolTable.referenceSimpleFunctionFromLinker(descriptor, sig)
	}
	
	override fun referenceTypeAlias(descriptor: TypeAliasDescriptor): IrTypeAliasSymbol {
		return symbolTable.referenceTypeAlias(descriptor)
	}
	
	override fun referenceTypeAliasFromLinker(descriptor: TypeAliasDescriptor, sig: IdSignature): IrTypeAliasSymbol {
		return symbolTable.referenceTypeAliasFromLinker(descriptor, sig)
	}
	
	override fun referenceTypeParameter(classifier: TypeParameterDescriptor): IrTypeParameterSymbol {
		return symbolTable.referenceTypeParameter(classifier)
	}
	
	override fun referenceTypeParameterFromLinker(
		classifier: TypeParameterDescriptor,
		sig: IdSignature
	): IrTypeParameterSymbol {
		return symbolTable.referenceTypeParameterFromLinker(classifier, sig)
	}
	
	override fun referenceValueParameter(descriptor: ParameterDescriptor): IrValueParameterSymbol {
		return symbolTable.referenceValueParameter(descriptor)
	}
	
	override fun referenceVariable(descriptor: VariableDescriptor): IrVariableSymbol {
		return symbolTable.referenceVariable(descriptor)
	}
}
