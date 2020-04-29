package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.JvmGeneratorExtensions
import org.jetbrains.kotlin.descriptors.konan.KlibModuleOrigin
import org.jetbrains.kotlin.ir.backend.jvm.serialization.EmptyLoggingContext
import org.jetbrains.kotlin.ir.backend.jvm.serialization.JvmIrLinker
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.util.DeclarationStubGenerator
import org.jetbrains.kotlin.ir.util.IrProvider
import org.jetbrains.kotlin.ir.util.referenceMember


private lateinit var providers: List<IrProvider>


fun initUnboundSymbolUtils(context: IrPluginContext) {
	val stubGenerator = DeclarationStubGenerator(
		context.moduleDescriptor, context.symbolTable, context.irBuiltIns.languageVersionSettings, JvmGeneratorExtensions()
	)
	val deserializer = JvmIrLinker(
		EmptyLoggingContext, context.irBuiltIns, context.symbolTable
	)
	context.moduleDescriptor.allDependencyModules.filter { it.getCapability(KlibModuleOrigin.CAPABILITY) != null }.forEach {
		deserializer.deserializeIrModuleHeader(it)
	}
	
	providers = listOf(deserializer, stubGenerator)
}


fun IrType.tryBind(): IrType {
	classifierOrNull?.tryBind()
	annotations.forEach { it.symbol.tryBind() }
	return this
}


fun <T : IrSymbol> T.tryBind(): T {
	tryBindNotRecursive()
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
	return this
}

private fun IrSymbol.tryBindNotRecursive(): IrSymbol {
	if(!isBound) for(provider in providers) {
		val declaration = provider.getDeclaration(this)
		if(declaration != null) {
//			log4("bind ${this.descriptor.dump()} $this")
			if(!isBound) {
//				@Suppress("UNCHECKED_CAST")
//				if(this is IrBindableSymbol<*, *> && declaration is IrSymbolOwner) (this as IrBindableSymbol<*, IrSymbolOwner>).bind(declaration)
				log4("bound but not bound?? $this ${context.symbolTable.referenceMember(declaration.descriptor)}")
//				return context.symbolTable.referenceMember(declaration.descriptor)
			}
			break
		}
	}
	return this
}

