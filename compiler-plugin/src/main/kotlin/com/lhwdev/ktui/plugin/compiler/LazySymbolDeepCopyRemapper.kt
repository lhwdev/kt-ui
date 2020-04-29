package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.*
import org.jetbrains.kotlin.ir.util.SymbolRemapper


class LazySymbolDeepCopyRemapper : SymbolRemapper {
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
	
	
	private inline fun <T : IrSymbol> MutableMap<T, T>.getDeclared(symbol: T, create: () -> T): T {
		val item = getOrDefault(symbol, null)
		return if(item == null) {
//			val existing = entries.find { (k, _) -> k.descriptor == symbol.descriptor }
//			if(existing != null) {
//				put(symbol, existing.value)
//				log2("EXISTING! $symbol -> ${existing.value}")
//				return existing.value
//			}
			val to = create()
			put(symbol, to)
			to
		} else item.also {
			if(symbol === item) log5("Wow! conflict")
		}
	}
	
	fun dump() {
		val all = classes + constructors + enumEntries + externalPackageFragments + fields + files + functions + properties + returnableBlocks + typeParameters + valueParameters + variables + localDelegatedProperties + typeAliases
		log5(all.entries.joinToString(separator = ",\n") { (k, v) -> "$k ${k.descriptor} -> $v ${v.descriptor}" })
	}
	
	
	private fun <T : IrSymbol> MutableMap<T, T>.getReferenced(symbol: T) =
		getOrElse(symbol) {
			put(symbol, symbol)
			symbol
		}
	
	
	override fun getDeclaredClass(symbol: IrClassSymbol): IrClassSymbol =
		classes.getDeclared(symbol) { IrClassSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol =
		functions.getDeclared(symbol) { IrSimpleFunctionSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredProperty(symbol: IrPropertySymbol): IrPropertySymbol =
		properties.getDeclared(symbol) { IrPropertySymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredField(symbol: IrFieldSymbol): IrFieldSymbol =
		fields.getDeclared(symbol) { IrFieldSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredFile(symbol: IrFileSymbol): IrFileSymbol =
		files.getDeclared(symbol) { IrFileSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol =
		constructors.getDeclared(symbol) { IrConstructorSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol =
		enumEntries.getDeclared(symbol) { IrEnumEntrySymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredExternalPackageFragment(symbol: IrExternalPackageFragmentSymbol): IrExternalPackageFragmentSymbol =
		externalPackageFragments.getDeclared(symbol) { IrExternalPackageFragmentSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredVariable(symbol: IrVariableSymbol): IrVariableSymbol =
		variables.getDeclared(symbol) { IrVariableSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredTypeParameter(symbol: IrTypeParameterSymbol): IrTypeParameterSymbol =
		typeParameters.getDeclared(symbol) { IrTypeParameterSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredValueParameter(symbol: IrValueParameterSymbol): IrValueParameterSymbol =
		valueParameters.getDeclared(symbol) { IrValueParameterSymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol =
		localDelegatedProperties.getDeclared(symbol) { IrLocalDelegatedPropertySymbolImpl(symbol.descriptor) }
	
	override fun getDeclaredTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol =
		typeAliases.getDeclared(symbol) { IrTypeAliasSymbolImpl(symbol.descriptor) }
	
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
}
