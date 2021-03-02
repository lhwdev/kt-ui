package com.lhwdev.ktui.plugin.compiler.util

import com.lhwdev.ktui.plugin.compiler.dumpSrcHeadColored
import com.lhwdev.ktui.plugin.compiler.log4
import com.lhwdev.ktui.plugin.compiler.patchDeclarationParentsChildren
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrFactoryImpl
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.util.DeepCopyTypeRemapper
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.SymbolRenamer
import org.jetbrains.kotlin.ir.util.TypeRemapper
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.name.Name


fun <D, R> IrComponent<*>.handleAccept(visitor: IrElementVisitor<R, D>, data: D, default: () -> R): R =
	if(visitor is IrComponentVisitor) visitor.visitComponent(this, data, default)
	else default()

fun <D, R> IrComponentExpression<*>.handleAccept(visitor: IrElementVisitor<R, D>, data: D, default: () -> R): R =
	if(visitor is IrComponentVisitor) visitor.visitComponentExpression(this, data, default)
	else default()

fun <D, R> IrComponentDeclaration<*>.handleAccept(visitor: IrElementVisitor<R, D>, data: D, default: () -> R): R =
	if(visitor is IrComponentVisitor) visitor.visitComponentDeclaration(this, data, default)
	else default()


interface IrComponentBuildScope {
	val deepCopyRequired: Boolean
	val componentSymbolRemapper: DeepCopyComponentSymbolRemapper
	val componentTypeRemapper: TypeRemapper
	val componentSymbolRenamer: SymbolRenamer
}

interface IrComponent<out T : IrElement> : IrElement {
	fun deepCopyWithSymbols(transformer: IrDeepCopyComponentTransformer): IrComponent<T>
	
	fun IrComponentBuildScope.build(): T = doBuild()
	fun IrComponentBuildScope.doBuild(): T
}

fun <T : IrElement> IrComponent<T>.callBuild(scope: IrComponentBuildScope): T = scope.build()

private val sStubScope = object : IrComponentBuildScope {
	override val deepCopyRequired: Boolean get() = false
	override val componentSymbolRemapper = DeepCopyComponentSymbolRemapper()
	override val componentTypeRemapper = DeepCopyTypeRemapper(componentSymbolRemapper)
	override val componentSymbolRenamer = SymbolRenamer.DEFAULT
}

fun <T : IrElement> IrComponent<T>.dumpPreview(): T = sStubScope.build()

interface IrComponentBase<out T : IrElement> : IrComponent<T> {
	override val startOffset get() = UNDEFINED_OFFSET
	override val endOffset get() = UNDEFINED_OFFSET
	
	override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D) = handleAccept(visitor, data) {
		visitor.visitElement(this, data)
	}
}

interface IrComponentStatement<out T : IrStatement> : IrComponent<T>, IrStatement

// any class that implements this should extend IrExpression
interface IrComponentExpression<out T : IrExpression> : IrComponentStatement<T>, IrVarargElement, IrAttributeContainer

interface IrComponentExpressionBase<out T : IrExpression> : IrComponentExpression<T>, IrComponentBase<T> {
	override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D) = handleAccept(visitor, data) {
		visitor.visitExpression(this as IrExpression, data)
	}
}

interface IrSymbolOwnerComponent<out T : IrSymbolOwner, out S : IrSymbol> : IrComponent<T>, IrSymbolOwner {
	override val symbol: S
	
	fun remapSymbol(symbolsRemapper: DeepCopyComponentSymbolRemapper)
}

interface IrComponentDeclaration<out T : IrStatement> : IrComponentStatement<T>, IrDeclaration

interface IrComponentDeclarationBase<out T : IrStatement> : IrComponentDeclaration<T>, IrComponentBase<T> {
	@ObsoleteDescriptorBasedAPI
	override val descriptor: DeclarationDescriptor
		get() = IrComponentDescriptorStub
	override val factory: IrFactory get() = IrFactoryImpl
	override var annotations: List<IrConstructorCall>
		get() = emptyList()
		set(_) {}
	override var origin: IrDeclarationOrigin
		get() =
			IrDeclarationOrigin.DEFINED // generally, origin of IrComponentDeclaration does not matter a lot
		set(_) {}
	
	override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D) = handleAccept(visitor, data) {
		visitor.visitDeclaration(this as IrDeclarationBase, data)
	}
}

interface IrComponentSymbolDeclaration<out T, out S : IrSymbol> :
	IrComponentDeclaration<T>, IrSymbolOwnerComponent<T, S>, IrSymbolDeclaration<S>
	where T : IrSymbolOwner, T : IrDeclaration

// note that [original] can be no longer used by itself outside.
interface IrLightComponentDeclaration<out T : IrStatement> : IrComponentDeclaration<T> {
	val original: T
	
	
	override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
		original.acceptChildren(visitor, data)
	}
	
	override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
		original.transformChildren(transformer, data)
	}
}

object IrComponentDescriptorStub : DeclarationDescriptor {
	override fun getName() = Name.identifier("stub")
	override fun getOriginal() = this
	override fun getContainingDeclaration(): DeclarationDescriptor? = null
	override fun <R, D> accept(visitor: DeclarationDescriptorVisitor<R, D>, data: D): R = error("not supported")
	override fun acceptVoid(visitor: DeclarationDescriptorVisitor<Void, Void>) {}
	override val annotations = Annotations.EMPTY
}


class BuildIrComponentsTransformer(
	val target: IrElement,
	val createCopier: (DeepCopyComponentSymbolRemapper, TypeRemapper) -> IrDeepCopyComponentTransformer =
		::IrDeepCopyComponentTransformerImpl,
	buildAllComponents: Boolean = true
) {
	var hasComponent: Boolean = false
		private set
	var hasComponentDeclarations: Boolean = false
		private set
	
	fun transform(): IrElement {
		val result = target.transform(transformer, null)
		return when {
			deepCopy != null ->
				target.transform(deepCopy!!, null)
			hasComponent -> {
				target.patchDeclarationParentsChildren()
				target
			}
			else -> result
		}
	}
	
	
	private var deepCopy: IrDeepCopyComponentTransformer? = null
	
	private fun deepCopy() = deepCopy ?: run {
		val symbolRemapper = DeepCopyComponentSymbolRemapper()
		target.acceptVoid(symbolRemapper)
		val new = createCopier(symbolRemapper, DeepCopyTypeRemapper(symbolRemapper))
		deepCopy = new
		new
	}
	
	private val buildScope: IrComponentBuildScope = object : IrComponentBuildScope {
		override val deepCopyRequired get() = deepCopy != null
		
		// lazy
		override val componentSymbolRemapper get() = deepCopy().symbolRemapper
		override val componentTypeRemapper: TypeRemapper get() = deepCopy().typeRemapper
		override val componentSymbolRenamer: SymbolRenamer get() = deepCopy().symbolRenamer
	}
	
	private fun <T : IrElement> IrComponent<T>.callBuild(): T {
		log4("build: ${dumpSrcHeadColored()}")
		hasComponent = true
		return buildScope.build()
	}
	
	private val transformer = if(buildAllComponents) object : IrElementTransformerVoid() {
		override fun visitElement(element: IrElement): IrElement = (
			if(element is IrComponent<*>) element.callBuild().transform(this, null) else element
			).also { it.transformChildren(this, null) }
		
		override fun visitDeclaration(declaration: IrDeclarationBase): IrStatement = (
			if(declaration is IrComponentDeclaration<*>) {
				hasComponentDeclarations = true
				declaration.callBuild().transform(this, null) as IrStatement
			} else declaration
			).also { it.transformChildren(this, null) }
		
		override fun visitExpression(expression: IrExpression): IrExpression = (
			if(expression is IrComponentExpression<*>) expression.callBuild().transform(this, null)
			else expression
			).also { it.transformChildren(this, null) }
	} else object : IrComponentTransformerVoid() {
		override fun visitComponent(component: IrComponent<*>, default: () -> IrElement): IrElement =
			component.callBuild().transform(this, null)
		
		override fun visitComponentExpression(
			component: IrComponentExpression<*>,
			default: () -> IrExpression
		): IrExpression = component.callBuild().transform(this, null)
		
		override fun visitComponentDeclaration(
			component: IrComponentDeclaration<*>,
			default: () -> IrStatement
		): IrStatement {
			hasComponentDeclarations = true
			return component.callBuild().transform(this, null) as IrStatement
		}
	}
}


// symbol stubs
abstract class IrComponentSymbol<out D : DeclarationDescriptor, B>(
	val symbol: IrBindableSymbol<D, B>
) : IrBindableSymbol<D, B> where B : IrSymbolOwner, B : IrDeclaration {
	lateinit var component: IrComponentSymbolDeclaration<B, *>
		private set
	override val isBound get() = component.symbol.isBound
	override val signature get() = component.symbol.signature
	
	
	fun bindComponent(component: IrComponentSymbolDeclaration<B, *>) {
		this.component = component
	}
	
	
	override fun hashCode() = symbol.hashCode()
	override fun equals(other: Any?) = symbol == (other as? IrComponentSymbol<*, *>)?.symbol
	
	
	abstract class IrBindableComponentSymbol<out D : DeclarationDescriptor, B : IrSymbolDeclaration<IrBindableSymbol<D, B>>>(
		symbol: IrBindableSymbol<D, B>
	) : IrComponentSymbol<D, B>(symbol) {
		@ObsoleteDescriptorBasedAPI
		override val descriptor: D
			get() = symbol.descriptor
		
		override val owner get(): B = symbol.owner
		
		override fun bind(owner: B) {
			symbol.bind(owner)
		}
	}
	
	
	class SimpleFunction(symbol: IrBindableSymbol<FunctionDescriptor, IrSimpleFunction>) :
		IrBindableComponentSymbol<FunctionDescriptor, IrSimpleFunction>(symbol), IrSimpleFunctionSymbol
	
	class Constructor(symbol: IrBindableSymbol<ClassConstructorDescriptor, IrConstructor>) :
		IrBindableComponentSymbol<ClassConstructorDescriptor, IrConstructor>(symbol), IrConstructorSymbol
	
	class Class(symbol: IrBindableSymbol<ClassDescriptor, IrClass>) :
		IrBindableComponentSymbol<ClassDescriptor, IrClass>(symbol), IrClassSymbol
	
	class Field(symbol: IrBindableSymbol<PropertyDescriptor, IrField>) :
		IrBindableComponentSymbol<PropertyDescriptor, IrField>(symbol), IrFieldSymbol
	
	class Property(symbol: IrBindableSymbol<PropertyDescriptor, IrProperty>) :
		IrComponentSymbol<PropertyDescriptor, IrProperty>(symbol), IrPropertySymbol {
		@ObsoleteDescriptorBasedAPI
		override val descriptor
			get() = symbol.descriptor
		
		override val owner get() = symbol.owner
		
		override fun bind(owner: IrProperty) {
			symbol.bind(owner)
		}
	}
	
	class Variable(symbol: IrBindableSymbol<VariableDescriptor, IrVariable>) :
		IrBindableComponentSymbol<VariableDescriptor, IrVariable>(symbol), IrVariableSymbol
	
	class TypeParameter(symbol: IrBindableSymbol<TypeParameterDescriptor, IrTypeParameter>) :
		IrBindableComponentSymbol<TypeParameterDescriptor, IrTypeParameter>(symbol), IrTypeParameterSymbol
	
	class ValueParameter(symbol: IrBindableSymbol<ParameterDescriptor, IrValueParameter>) :
		IrBindableComponentSymbol<ParameterDescriptor, IrValueParameter>(symbol), IrValueParameterSymbol
	
	class TypeAlias(symbol: IrBindableSymbol<TypeAliasDescriptor, IrTypeAlias>) :
		IrBindableComponentSymbol<TypeAliasDescriptor, IrTypeAlias>(symbol), IrTypeAliasSymbol
	
	class EnumEntry(symbol: IrBindableSymbol<ClassDescriptor, IrEnumEntry>) :
		IrBindableComponentSymbol<ClassDescriptor, IrEnumEntry>(symbol), IrEnumEntrySymbol
}

abstract class IrStubSymbol<out D : DeclarationDescriptor, B : IrSymbolOwner> : IrBindableSymbol<D, B> {
	protected fun no(): Nothing = error("stub symbol")
	
	@ObsoleteDescriptorBasedAPI
	override val descriptor: D
		get() = no()
	override val owner: B get() = no()
	
	override fun bind(owner: B) {
		no()
	}
	
	override val isBound: Boolean get() = no()
	override val signature: IdSignature? get() = no()
	
	
	class SimpleFunction : IrStubSymbol<FunctionDescriptor, IrSimpleFunction>(), IrSimpleFunctionSymbol
	
	class Constructor : IrStubSymbol<ClassConstructorDescriptor, IrConstructor>(), IrConstructorSymbol
	
	class Class : IrStubSymbol<ClassDescriptor, IrClass>(), IrClassSymbol
	
	class Field : IrStubSymbol<PropertyDescriptor, IrField>(), IrFieldSymbol
	
	class Property : IrStubSymbol<PropertyDescriptor, IrProperty>(), IrPropertySymbol
	
	class Variable : IrStubSymbol<VariableDescriptor, IrVariable>(), IrVariableSymbol
	
	class TypeParameter : IrStubSymbol<TypeParameterDescriptor, IrTypeParameter>(), IrTypeParameterSymbol
	
	class ValueParameter : IrStubSymbol<ParameterDescriptor, IrValueParameter>(), IrValueParameterSymbol
	
	class TypeAlias : IrStubSymbol<TypeAliasDescriptor, IrTypeAlias>(), IrTypeAliasSymbol
	
	class EnumEntry : IrStubSymbol<ClassDescriptor, IrEnumEntry>(), IrEnumEntrySymbol
}


val <S : IrSymbol> S.component get() = (this as? IrComponentSymbol<*, *>)?.component
