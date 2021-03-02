package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.PackageViewDescriptor
import org.jetbrains.kotlin.descriptors.TypeAliasDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.util.findDeclaration
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name


// IrPackageFragment

fun IrDeclarationContainer.referenceClass(name: String) = findDeclaration<IrClass> { it.name.asString() == name }

inline fun IrDeclarationContainer.referenceFunction(name: String, predicate: (IrFunction) -> Boolean) =
	findDeclaration<IrFunction> { it.name.asString() == name && predicate(it) }!!

fun IrDeclarationContainer.referenceFunction(name: String) = referenceFunction(name) { true }
fun IrDeclarationContainer.referenceFunction(name: String, valueParametersCount: Int) =
	referenceFunction(name) { it.valueParameters.size == valueParametersCount }


inline fun IrDeclarationContainer.referenceVariable(name: String, predicate: (IrProperty) -> Boolean) =
	findDeclaration<IrProperty> { it.name.asString() == name && predicate(it) }!!

fun IrDeclarationContainer.referenceVariable(name: String) = referenceVariable(name) { true }


@OptIn(ObsoleteDescriptorBasedAPI::class)
fun IrPluginContext.referencePackage(fqName: FqName): IrPackage =
	IrPackageDescriptorImpl(this, fqName, moduleDescriptor.getPackage(fqName))


interface IrPackage {
	fun referenceClassOrNull(name: Name): IrClassSymbol?
	fun referenceClass(name: Name): IrClassSymbol
	
	fun referenceTypeAliasOrNull(name: Name): IrTypeAliasSymbol?
	fun referenceTypeAlias(name: Name): IrTypeAliasSymbol
	
	fun referenceFunctions(name: Name): Sequence<IrSimpleFunctionSymbol>
	fun referenceFirstFunction(name: Name, valueParametersCount: Int): IrSimpleFunctionSymbol =
		referenceFunctions(name).first { it.owner.valueParameters.size == valueParametersCount }
	
	fun referenceConstructors(name: Name): Sequence<IrConstructorSymbol>
	
	fun referenceProperties(name: Name): Sequence<IrPropertySymbol>
	
	fun child(name: Name): IrPackage
}


// extensions with `name: String`

fun IrPackage.referenceClassOrNull(name: String): IrClassSymbol? =
	referenceClassOrNull(Name.guessByFirstCharacter(name))

fun IrPackage.referenceClass(name: String): IrClassSymbol = referenceClass(Name.guessByFirstCharacter(name))

fun IrPackage.referenceTypeAliasOrNull(name: String): IrTypeAliasSymbol? =
	referenceTypeAliasOrNull(Name.guessByFirstCharacter(name))

fun IrPackage.referenceTypeAlias(name: String): IrTypeAliasSymbol = referenceTypeAlias(Name.guessByFirstCharacter(name))

fun IrPackage.referenceFunctions(name: String): Sequence<IrSimpleFunctionSymbol> =
	referenceFunctions(Name.guessByFirstCharacter(name))

fun IrPackage.referenceFirstFunction(name: String, valueParametersCount: Int): IrSimpleFunctionSymbol =
	referenceFirstFunction(Name.guessByFirstCharacter(name), valueParametersCount)

fun IrPackage.referenceConstructors(name: String): Sequence<IrConstructorSymbol> =
	referenceConstructors(Name.guessByFirstCharacter(name))

fun IrPackage.referenceProperties(name: String): Sequence<IrPropertySymbol> =
	referenceProperties(Name.guessByFirstCharacter(name))

fun IrPackage.child(name: String): IrPackage =
	child(Name.guessByFirstCharacter(name))


fun IrPackage.referenceFirstFunction(name: String) = referenceFunctions(name).first()


// TODO: do not depend on descriptor, or at least make using it affected by altering IR tree
@OptIn(ObsoleteDescriptorBasedAPI::class)
class IrPackageDescriptorImpl(
	val pluginContext: IrPluginContext,
	val fqName: FqName,
	val descriptor: PackageViewDescriptor
) : IrPackage {
	
	override fun referenceClassOrNull(name: Name): IrClassSymbol? {
		val classifier = descriptor.memberScope.getContributedClassifier(name, NoLookupLocation.FROM_BACKEND)
		
		if(classifier is ClassDescriptor)
			return pluginContext.symbolTable.referenceClass(classifier)
		
		return null
	}
	
	override fun referenceClass(name: Name): IrClassSymbol = referenceClassOrNull(name)!!
	
	override fun referenceTypeAliasOrNull(name: Name): IrTypeAliasSymbol? {
		val classifier = descriptor.memberScope.getContributedClassifier(name, NoLookupLocation.FROM_BACKEND)
		
		if(classifier is TypeAliasDescriptor)
			return pluginContext.symbolTable.referenceTypeAlias(classifier)
		
		return null
	}
	
	override fun referenceTypeAlias(name: Name): IrTypeAliasSymbol = referenceTypeAliasOrNull(name)!!
	
	override fun referenceFunctions(name: Name): Sequence<IrSimpleFunctionSymbol> =
		descriptor.memberScope.getContributedFunctions(name, NoLookupLocation.FROM_BACKEND).asSequence().map {
			pluginContext.symbolTable.referenceSimpleFunction(it)
		}
	
	override fun referenceFirstFunction(name: Name, valueParametersCount: Int): IrSimpleFunctionSymbol =
		descriptor.memberScope.getContributedFunctions(name, NoLookupLocation.FROM_BACKEND).asSequence()
			.first { it.valueParameters.size == valueParametersCount }
			.let {
				pluginContext.symbolTable.referenceSimpleFunction(it)
			}
	
	override fun referenceConstructors(name: Name): Sequence<IrConstructorSymbol> =
		referenceClass(name).owner.declarations.filterIsInstance<IrConstructor>().map { it.symbol }.asSequence()
	
	override fun referenceProperties(name: Name): Sequence<IrPropertySymbol> =
		descriptor.memberScope.getContributedVariables(name, NoLookupLocation.FROM_BACKEND).asSequence().map {
			pluginContext.symbolTable.referenceProperty(it)
		}
	
	override fun child(name: Name): IrPackageDescriptorImpl {
		val newPackage = fqName.child(name)
		return IrPackageDescriptorImpl(pluginContext, newPackage, descriptor.module.getPackage(newPackage))
	}
}
