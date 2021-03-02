package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.backend.common.ScopeWithIr
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrScriptSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull



class SimpleElementScope(override val irElement: IrSymbolOwner, override val scope: Scope) : IrBuilderScope


abstract class IrSimpleComponentTransformerVoidScoped : IrComponentTransformerVoidScoped<SimpleElementScope>() {
	override fun createScope(declaration: IrSymbolOwner) =
		SimpleElementScope(declaration, Scope(declaration.symbol))
}

abstract class IrSimpleComponentVisitorVoidScoped : IrComponentTransformerVoidScoped<SimpleElementScope>() {
	override fun createScope(declaration: IrSymbolOwner) =
		SimpleElementScope(declaration, Scope(declaration.symbol))
}

abstract class IrComponentTransformerVoidScoped<T : IrBuilderScope> :
	IrComponentTransformerVoidWithContext<T>(), IrBuilderScope {
	
	override val startOffset: Int
		get() = currentScope.irElement.startOffset
	
	override val endOffset: Int
		get() = currentScope.irElement.endOffset
	
	override val scope: Scope
		get() = currentScope.scope
}

abstract class IrComponentTransformerVoidWithContext<T : IrBuilderScope> : IrComponentTransformerVoid() {
	val scopeStack = mutableListOf<T>()
	
	abstract fun createScope(declaration: IrSymbolOwner): T
	
	inline fun <R> withScope(scope: T, block: () -> R): R {
		scopeStack.add(scope)
		return try {
			block()
		} finally {
			scopeStack.removeAt(scopeStack.lastIndex)
		}
	}
	
	inline fun <R> withScope(declaration: IrSymbolOwner, block: () -> R) =
		withScope(createScope(declaration), block)
	
	final override fun visitFile(declaration: IrFile) = withScope(createScope(declaration)) {
		visitFileNew(declaration)
	}
	
	final override fun visitClass(declaration: IrClass) = withScope(createScope(declaration)) {
		visitClassNew(declaration)
	}
	
	final override fun visitProperty(declaration: IrProperty) =
		withScope(createScope(declaration)) {
			visitPropertyNew(declaration)
		}
	
	final override fun visitField(declaration: IrField) = withScope(createScope(declaration)) {
		visitFieldNew(declaration)
	}
	
	final override fun visitFunction(declaration: IrFunction) =
		withScope(createScope(declaration)) {
			visitFunctionNew(declaration)
		}
	
	final override fun visitAnonymousInitializer(declaration: IrAnonymousInitializer) =
		withScope(createScope(declaration)) {
			visitAnonymousInitializerNew(declaration)
		}
	
	final override fun visitValueParameter(declaration: IrValueParameter) =
		withScope(createScope(declaration)) {
			visitValueParameterNew(declaration)
		}
	
	final override fun visitScript(declaration: IrScript) = withScope(createScope(declaration)) {
		visitScriptNew(declaration)
	}
	
	final override fun visitComponentDeclaration(
		component: IrComponentDeclaration<*>,
		default: () -> IrStatement
	): IrStatement {
		if(component !is IrSymbolOwner) return visitComponentDeclarationNew(component, default)
		return withScope(createScope(component)) {
			visitComponentDeclarationNew(component, default)
		}
	}
	
	val currentScope get() = scopeStack.last()
	val currentDeclarationParent get() = scopeStack.last { it.irElement is IrDeclarationParent }.irElement as IrDeclarationParent
	
	inline fun <reified T : IrBuilderScope> topScope() =
		scopeStack.asReversed().firstIsInstance<T>()
	
	inline fun <reified T : IrBuilderScope> topScopeOrNull() =
		scopeStack.asReversed().firstIsInstanceOrNull<T>()
	
	val parentScope: T get() = scopeStack[scopeStack.size - 2]
	val currentFile get() = scopeStack.last { it.irElement is IrFile }.irElement as IrFile
	val currentScript get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrScriptSymbol }
	val currentClass get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrClassSymbol }
	val currentFunction get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrFunctionSymbol }
	val currentProperty get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrPropertySymbol }
	val currentAnonymousInitializer get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrAnonymousInitializer }
	val currentValueParameter get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrValueParameter }
	
	val parentScopeOrNull get() = if(scopeStack.size < 2) null else scopeStack[scopeStack.size - 2]
	val currentScriptOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrScriptSymbol }
	val currentClassOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrClassSymbol }
	val currentFunctionOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrFunctionSymbol }
	val currentPropertyOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrPropertySymbol }
	val currentAnonymousInitializerOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrAnonymousInitializer }
	val currentValueParameterOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrValueParameter }
	
	fun printScopeStack() {
		scopeStack.forEach { println(it.scope.scopeOwnerSymbol) }
	}
	
	open fun visitFileNew(declaration: IrFile): IrFile {
		return super.visitFile(declaration)
	}
	
	open fun visitClassNew(declaration: IrClass): IrStatement {
		return super.visitClass(declaration)
	}
	
	open fun visitFunctionNew(declaration: IrFunction): IrStatement {
		return super.visitFunction(declaration)
	}
	
	open fun visitPropertyNew(declaration: IrProperty): IrStatement {
		return super.visitProperty(declaration)
	}
	
	open fun visitFieldNew(declaration: IrField): IrStatement {
		return super.visitField(declaration)
	}
	
	open fun visitAnonymousInitializerNew(declaration: IrAnonymousInitializer): IrStatement {
		return super.visitAnonymousInitializer(declaration)
	}
	
	open fun visitValueParameterNew(declaration: IrValueParameter): IrStatement {
		return super.visitValueParameter(declaration)
	}
	
	open fun visitScriptNew(declaration: IrScript): IrStatement {
		return super.visitScript(declaration)
	}
	
	open fun visitComponentDeclarationNew(
		component: IrComponentDeclaration<*>,
		default: () -> IrStatement
	): IrStatement {
		return super.visitComponentDeclaration(component, default)
	}
}

abstract class IrComponentVisitorVoidScoped<T : ScopeWithIr> : IrComponentVisitorVoidWithContext<T>(), IrBuilderScope {
	override val startOffset: Int
		get() = currentScope.irElement.startOffset
	
	override val endOffset: Int
		get() = currentScope.irElement.endOffset
	
	override val scope: Scope
		get() = currentScope.scope
}

abstract class IrComponentVisitorVoidWithContext<T : ScopeWithIr> : IrElementVisitorVoid {
	val scopeStack = mutableListOf<T>()
	
	abstract fun createScope(declaration: IrSymbolOwner): T
	
	inline fun <R> withScope(scope: T, block: () -> R): R {
		scopeStack.add(scope)
		return try {
			block()
		} finally {
			scopeStack.removeAt(scopeStack.lastIndex)
		}
	}
	
	inline fun <R> withScope(declaration: IrSymbolOwner, block: () -> R) =
		withScope(createScope(declaration), block)
	
	
	final override fun visitFile(declaration: IrFile) {
		scopeStack.push(createScope(declaration))
		visitFileNew(declaration)
		scopeStack.pop()
	}
	
	final override fun visitClass(declaration: IrClass) {
		scopeStack.push(createScope(declaration))
		visitClassNew(declaration)
		scopeStack.pop()
	}
	
	final override fun visitProperty(declaration: IrProperty) {
		scopeStack.push(createScope(declaration))
		visitPropertyNew(declaration)
		scopeStack.pop()
	}
	
	final override fun visitField(declaration: IrField) {
		@OptIn(ObsoleteDescriptorBasedAPI::class)
		val isDelegated = declaration.descriptor.isDelegated
		if(isDelegated) scopeStack.push(createScope(declaration))
		visitFieldNew(declaration)
		if(isDelegated) scopeStack.pop()
	}
	
	final override fun visitFunction(declaration: IrFunction) {
		scopeStack.push(createScope(declaration))
		visitFunctionNew(declaration)
		scopeStack.pop()
	}
	
	final override fun visitAnonymousInitializer(declaration: IrAnonymousInitializer) {
		scopeStack.push(createScope(declaration))
		visitAnonymousInitializerNew(declaration)
		scopeStack.pop()
	}
	
	final override fun visitValueParameter(declaration: IrValueParameter) {
		scopeStack.push(createScope(declaration))
		visitValueParameterNew(declaration)
		scopeStack.pop()
	}
	
	val currentScope get() = scopeStack.last()
	val currentDeclarationParent get() = scopeStack.last { it.irElement is IrDeclarationParent }.irElement as IrDeclarationParent
	
	val parentScope get() = scopeStack[scopeStack.size - 2]
	val currentFile get() = scopeStack.last { it.irElement is IrFile }.irElement as IrFile
	val currentScript get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrScriptSymbol }
	val currentClass get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrClassSymbol }
	val currentFunction get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrFunctionSymbol }
	val currentProperty get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrPropertySymbol }
	val currentAnonymousInitializer get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrAnonymousInitializer }
	val currentValueParameter get() = scopeStack.last { it.scope.scopeOwnerSymbol is IrValueParameter }
	
	val parentScopeOrNull get() = if(scopeStack.size < 2) null else scopeStack[scopeStack.size - 2]
	val currentScriptOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrScriptSymbol }
	val currentClassOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrClassSymbol }
	val currentFunctionOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrFunctionSymbol }
	val currentPropertyOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrPropertySymbol }
	val currentAnonymousInitializerOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrAnonymousInitializer }
	val currentValueParameterOrNull get() = scopeStack.lastOrNull { it.scope.scopeOwnerSymbol is IrValueParameter }
	
	fun printScopeStack() {
		scopeStack.forEach { println(it.scope.scopeOwnerSymbol) }
	}
	
	open fun visitFileNew(declaration: IrFile) {
		super.visitFile(declaration)
	}
	
	open fun visitClassNew(declaration: IrClass) {
		super.visitClass(declaration)
	}
	
	open fun visitFunctionNew(declaration: IrFunction) {
		super.visitFunction(declaration)
	}
	
	open fun visitPropertyNew(declaration: IrProperty) {
		super.visitProperty(declaration)
	}
	
	open fun visitFieldNew(declaration: IrField) {
		super.visitField(declaration)
	}
	
	open fun visitAnonymousInitializerNew(declaration: IrAnonymousInitializer) {
		super.visitAnonymousInitializer(declaration)
	}
	
	open fun visitValueParameterNew(declaration: IrValueParameter) {
		super.visitValueParameter(declaration)
	}
}
