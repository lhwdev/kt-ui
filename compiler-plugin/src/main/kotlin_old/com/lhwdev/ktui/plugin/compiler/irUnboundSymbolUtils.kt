package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContextImpl
import org.jetbrains.kotlin.ir.linkage.IrDeserializer
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrNull


// from Kotlin 1.4-M3(?), all symbols provided becomes bound
// but Ir library still internally uses IrProvider in some cases like IrPluginContext.referenceClass


private lateinit var linker: IrDeserializer


fun initUnboundSymbolUtils(context: IrPluginContextImpl) {
	linker = context.linker
//	val stubGenerator = DeclarationStubGenerator(
//		context.moduleDescriptor, context.symbolTable, context.irBuiltIns.languageVersionSettings, JvmGeneratorExtensions()
//	)
//	val deserializer = JvmIrLinker(
//		EmptyLoggingContext, context.irBuiltIns, context.symbolTable
//	)
//	context.moduleDescriptor.allDependencyModules.filter { it.getCapability(KlibModuleOrigin.CAPABILITY) != null }.forEach {
//		deserializer.deserializeIrModuleHeader(it)
//	}
//	providers = listOf(deserializer, stubGenerator)
//	stubGenerator.setIrProviders(providers)
}

fun bindAll() {
	var unbound: Set<IrSymbol>
	val visited = mutableSetOf<IrSymbol>()
	do {
		unbound = symbolTable.allUnbound()
		
		for(symbol in unbound) {
			if(visited.contains(symbol))
				continue
			
			// Symbol could get bound as a side effect of deserializing other symbols.
			if(!symbol.isBound)
				linker.getDeclaration(symbol)
			
			if(!symbol.isBound) visited.add(symbol)
		}
	} while((unbound - visited).isNotEmpty())
}


fun IrType.tryBind(): IrType {
	classifierOrNull?.tryBind()
	annotations.forEach { it.symbol.tryBind() }
	return this
}


// will deprecate
fun <T : IrSymbol> T.tryBind(): T {
//	tryBindNotRecursive()
//	val table = context.symbolTable
//	getBoundIfPossibleNotRecursive()
//	val d = descriptor
//	when(d) {
//		is CallableDescriptor -> {
//			d.typeParameters.forEach { table.referenceTypeParameter(it).getBoundIfPossibleNotRecursive() }
//			d.valueParameters.forEach { table.referenceValueParameter(it).getBoundIfPossibleNotRecursive() }
//			d.dispatchReceiverParameter?.let { table.referenceValueParameter(it).getBoundIfPossibleNotRecursive() }
//			d.dispatchReceiverParameter?.let { table.referenceValueParameter(it).getBoundIfPossibleNotRecursive() }
//			d.overriddenDescriptors.forEach { table.referenceFunction(it).getBoundIfPossible() }
//		}
//		is ClassDescriptor -> {
//			d.declaredTypeParameters.forEach { table.referenceTypeParameter(it).getBoundIfPossibleNotRecursive() }
//			d.unsubstitutedMemberScope.run {
//				this.
//			}
//		}
//	}
//
	return bound
}

val <T : IrSymbol> T.bound // in kotlin 1.4.0, this is not needed often
	get() = this.also {
		if(!isBound) linker.getDeclaration(it)
	}

//private fun IrSymbol.tryBindNotRecursive(): IrSymbol {
//	if(!isBound) {
//		for(provider in providers) {
//			if(isBound) break
//			else {
//				val declaration = provider.getDeclaration(this)
//				if(declaration != null) {
////			log4("bind ${this.descriptor.dump()} $this")
//					if(!isBound) {
////				@Suppress("UNCHECKED_CAST")
////				if(this is IrBindableSymbol<*, *> && declaration is IrSymbolOwner) (this as IrBindableSymbol<*, IrSymbolOwner>).bind(declaration)
//						log4("bound but not bound?? $this ${context.symbolTable.referenceMember(declaration.descriptor)}")
////				return context.symbolTable.referenceMember(declaration.descriptor)
//					}
//					break
//				}
//			}
//		}
//	}
//	return this
//}

