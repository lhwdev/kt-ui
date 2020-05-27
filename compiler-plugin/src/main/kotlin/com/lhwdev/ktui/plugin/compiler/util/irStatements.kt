@file:Suppress("NOTING_TO_INLINE", "NOTHING_TO_INLINE")

package com.lhwdev.ktui.plugin.compiler.util

import com.lhwdev.ktui.plugin.compiler.tryBind
import com.lhwdev.ktui.plugin.compiler.withLog
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptorImpl
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.AnonymousFunctionDescriptor
import org.jetbrains.kotlin.descriptors.impl.ValueParameterDescriptorImpl
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.backend.js.utils.OperatorNames
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.builders.primitiveOp1
import org.jetbrains.kotlin.ir.builders.primitiveOp2
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.IrFunctionImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrTypeParameterImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.IrReturnableBlockSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.endOffset
import org.jetbrains.kotlin.ir.util.kotlinPackageFqn
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.startOffset
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi2ir.findFirstFunction
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.replace
import kotlin.math.abs


// all implementation will be try not to get the owner of the symbol, which can cause errors so far.

inline val IrElementScope.builtIns get() = context.builtIns
inline val IrElementScope.irBuiltIns get() = context.irBuiltIns


// Function related

fun IrElementScope.irVararg(type: IrType, elements: List<IrVarargElement>) =
	IrVarargImpl(startOffset, endOffset, context.symbols.array.typeWith(type), type, elements)

private val sExtensionFunctionType by lazy {
	AnnotationDescriptorImpl(
		context.moduleDescriptor.resolveTopLevelClass(kotlinPackageFqn.child(Name.identifier("ExtensionFunctionType")), NoLookupLocation.FROM_BACKEND)!!.defaultType,
		emptyMap(), SourceElement.NO_SOURCE
	)
}

fun toFunctionType(
	valueParameters: List<ValueParameterDescriptor>, extensionReceiverParameter: ReceiverParameterDescriptor? = null,
	returnType: KotlinType, annotations: Annotations
) =
	builtIns.getFunction(valueParameters.size + extensionReceiverParameter.let { if(it == null) 0 else 1 })
		.defaultType
		.replace(
			(listOfNotNull(extensionReceiverParameter) + valueParameters).map { TypeProjectionImpl(it.type) } + TypeProjectionImpl(returnType),
			if(extensionReceiverParameter == null) annotations else Annotations.create(listOf(sExtensionFunctionType) + annotations))
		.toIrType()

//class FunctionDescriptorBuilder(val descriptor: FunctionDescriptor) {
//	private var valueParameters: ValueParametersBuilder.() -> Unit = {}
//	fun valueParameters(block: ValueParametersBuilder.() -> Unit) {
//		valueParameters = block
//	}
//
////	private var dispatchReceiverParameter: ((FunctionDescriptor) -> ReceiverParameterDescriptor)? = null
////	fun dispatchReceiverParameter(type: KotlinType, annotations: Annotations = Annotations.EMPTY) {
////		dispatchReceiverParameter = {
////			ReceiverParameterDescriptorImpl(it, ClassValueReceiver(type.clas, type, null), annotations)
////		}
////	} // TODO
//
//	private var extensionReceiverParameter: ((FunctionDescriptor) -> ReceiverParameterDescriptor)? = null
//	fun extensionReceiverParameter(type: KotlinType, annotations: Annotations = Annotations.EMPTY) {
//		extensionReceiverParameter = {
//			ReceiverParameterDescriptorImpl(it, ExtensionReceiver(it, type, null), annotations)
//		}
//	}
//}
//
//class ValueParametersBuilder(val descriptor: CallableDescriptor, val list: MutableList<ValueParameterDescriptor>) {
//	private var index = 0
//
//	fun add(name: String, type: KotlinType, annotations: Annotations = Annotations.EMPTY, isCrossinline: Boolean = false, isNoinline: Boolean = false, varargElementType: KotlinType? = null) {
//		list += ValueParameterDescriptorImpl(descriptor, null, index++, annotations, Name.guessByFirstCharacter(name), type, false, isCrossinline, isNoinline, varargElementType, SourceElement.NO_SOURCE)
//	}
//}

inline fun valueParameterOf(descriptor: CallableDescriptor, index: Int, name: String, type: KotlinType, declaredDefaultValue: Boolean = false, annotations: Annotations = Annotations.EMPTY, isCrossinline: Boolean = false, isNoinline: Boolean = false, varargElementType: KotlinType? = null) =
	ValueParameterDescriptorImpl(descriptor, null, index, annotations, Name.guessByFirstCharacter(name), type, declaredDefaultValue, isCrossinline, isNoinline, varargElementType, SourceElement.NO_SOURCE)


fun IrBuilderScope.irLambdaExpression(
	valueParameters: List<(FunctionDescriptor) -> ValueParameterDescriptor> = emptyList(),
	extensionReceiverParameter: ((FunctionDescriptor) -> ReceiverParameterDescriptor)? = null,
	typeParameters: List<(FunctionDescriptor) -> TypeParameterDescriptor> = emptyList(),
	returnType: KotlinType,
	owner: DeclarationDescriptor = scope.scopeOwner,
	annotations: Annotations = Annotations.EMPTY,
	sourceElement: SourceElement = SourceElement.NO_SOURCE,
	isSuspend: Boolean = false,
	type: IrType? = null,
	body: IrBlockBodyBuilder.(IrFunction) -> Unit
): IrExpression {
	val descriptor = AnonymousFunctionDescriptor(owner, annotations, CallableMemberDescriptor.Kind.SYNTHESIZED, sourceElement, isSuspend)
	val createdValueParameters = valueParameters.map { it(descriptor) }
	val createdExtensionReceiverParameters = extensionReceiverParameter?.invoke(descriptor)
	val createdTypeParameters = typeParameters.map { it(descriptor) }
	
	descriptor.initialize(createdExtensionReceiverParameters, null, createdTypeParameters, createdValueParameters, returnType, Modality.FINAL, Visibilities.LOCAL)
	
	return irLambdaExpression(descriptor, type
		?: toFunctionType(createdValueParameters, createdExtensionReceiverParameters, returnType, annotations), body)
}

fun IrBuilderScope.irLambdaExpression(
	descriptor: FunctionDescriptor,
	type: IrType,
	body: IrBlockBodyBuilder.(IrFunction) -> Unit
): IrExpression {
	val symbol = IrSimpleFunctionSymbolImpl(descriptor)
	
	val returnType = descriptor.returnType.toIrType()
//
	val lambda = IrFunctionImpl(
		startOffset, endOffset,
		IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA,
		symbol,
		returnType
	).also {
		it.parent = scope.getLocalDeclarationParent()
		it.createParameterDeclarationsFromDescriptor()
		it.body = irBuilderScope(it).irBlockBody { body(it) }
	}
	
	return IrFunctionExpressionImpl(startOffset, endOffset, type, lambda, IrStatementOrigin.LAMBDA)
}

fun IrFunction.createParameterDeclarationsFromDescriptor() {
	fun ParameterDescriptor.irValueParameter() = IrValueParameterImpl(
		this.startOffset ?: UNDEFINED_OFFSET,
		this.endOffset ?: UNDEFINED_OFFSET,
		IrDeclarationOrigin.DEFINED,
		this,
		type.toIrType(),
		(this as? ValueParameterDescriptor)?.varargElementType?.toIrType()
	).also {
		it.parent = this@createParameterDeclarationsFromDescriptor
	}
	
	fun TypeParameterDescriptor.irTypeParameter() = IrTypeParameterImpl(
		this.startOffset ?: UNDEFINED_OFFSET,
		this.endOffset ?: UNDEFINED_OFFSET,
		IrDeclarationOrigin.DEFINED,
		IrTypeParameterSymbolImpl(this)
	).also {
		it.parent = this@createParameterDeclarationsFromDescriptor
	}
	
	dispatchReceiverParameter = descriptor.dispatchReceiverParameter?.irValueParameter()
	extensionReceiverParameter = descriptor.extensionReceiverParameter?.irValueParameter()
	
	assert(valueParameters.isEmpty())
	valueParameters = descriptor.valueParameters.map { it.irValueParameter() }
	
	assert(typeParameters.isEmpty())
	typeParameters = descriptor.typeParameters.map { it.irTypeParameter() }
}


// Calls

fun IrElementScope.irCall(
	callee: IrSimpleFunctionSymbol,
	valueArguments: List<IrExpression?> = emptyList(),
	typeArguments: List<IrType?> = emptyList(),
	dispatchReceiver: IrExpression? = null,
	extensionReceiver: IrExpression? = null,
	type: IrType = callee.returnType,
	origin: IrStatementOrigin? = null
): IrCall = irCall(callee, type, origin).also {
	valueArguments.forEachIndexed { index, argument -> it.putValueArgument(index, argument) }
	typeArguments.forEachIndexed { index, argument -> it.putTypeArgument(index, argument) }
	it.dispatchReceiver = dispatchReceiver
	it.extensionReceiver = extensionReceiver
}

fun IrElementScope.irCall(
	callee: FunctionDescriptor,
	valueArguments: List<IrExpression?> = emptyList(),
	typeArguments: List<IrType?> = emptyList(),
	dispatchReceiver: IrExpression? = null,
	extensionReceiver: IrExpression? = null,
	type: IrType = callee.returnType.toIrType(),
	origin: IrStatementOrigin? = null
) =
	irCall(callee.symbol, valueArguments, typeArguments, dispatchReceiver, extensionReceiver, type, origin)

fun IrElementScope.irConstructorCall(
	callee: IrConstructorSymbol,
	valueArguments: List<IrExpression?> = emptyList(),
	typeArguments: List<IrType?> = emptyList(),
	type: IrType = callee.returnType,
	origin: IrStatementOrigin? = null
): IrConstructorCall = irConstructorCall(callee, type, origin).also {
	valueArguments.forEachIndexed { index, argument -> it.putValueArgument(index, argument) }
	typeArguments.forEachIndexed { index, argument -> it.putTypeArgument(index, argument) }
}

fun IrElementScope.irConstructorCall(
	callee: ClassConstructorDescriptor,
	valueArguments: List<IrExpression?> = emptyList(),
	typeArguments: List<IrType?> = emptyList(),
	type: IrType = callee.returnType.toIrType(),
	origin: IrStatementOrigin? = null
) = irConstructorCall(callee.symbol, valueArguments, typeArguments, type, origin)

inline fun IrElementScope.irCall(callee: IrFunctionSymbol, type: IrType = callee.returnType, block: IrFunctionAccessExpression.() -> Unit): IrFunctionAccessExpression =
	irCall(callee, type).apply(block)

inline fun IrElementScope.irCall(callee: CallableMemberDescriptor, type: IrType = callee.returnType.toIrType(), block: IrFunctionAccessExpression.() -> Unit): IrFunctionAccessExpression =
	irCall(callee.symbol, type).apply(block)

fun IrElementScope.irCall(callee: CallableMemberDescriptor, type: IrType = callee.returnType.toIrType(), origin: IrStatementOrigin? = null): IrFunctionAccessExpression =
	irCall(callee.symbol, type, origin)

fun IrElementScope.irCall(
	callee: CallableMemberDescriptor,
	valueArguments: List<IrExpression?> = emptyList(),
	typeArguments: List<IrType?> = emptyList(),
	dispatchReceiver: IrExpression? = null,
	extensionReceiver: IrExpression? = null,
	type: IrType = callee.returnType.toIrType(),
	origin: IrStatementOrigin? = null
) =
	irCall(callee.symbol, valueArguments, typeArguments, dispatchReceiver, extensionReceiver, type, origin)

fun IrElementScope.irCall(
	callee: IrFunctionSymbol,
	valueArguments: List<IrExpression?> = emptyList(),
	typeArguments: List<IrType?> = emptyList(),
	dispatchReceiver: IrExpression? = null,
	extensionReceiver: IrExpression? = null,
	type: IrType = callee.returnType,
	origin: IrStatementOrigin? = null
) = irCall(callee, type, origin).also {
	valueArguments.forEachIndexed { index, argument -> it.putValueArgument(index, argument) }
	typeArguments.forEachIndexed { index, argument -> it.putTypeArgument(index, argument) }
	it.dispatchReceiver = dispatchReceiver
	it.extensionReceiver = extensionReceiver
}

fun IrElementScope.irCall(callee: IrFunctionSymbol, type: IrType = callee.returnType, origin: IrStatementOrigin? = null): IrFunctionAccessExpression =
	when(callee) {
		is IrSimpleFunctionSymbol -> irCall(callee, type, origin = origin)
		is IrConstructorSymbol -> irConstructorCall(callee, type, origin = origin)
		else -> error("unexpected function symbol $callee")
	}

@PublishedApi
internal inline val IrFunctionSymbol.returnType
	get() = if(isBound) owner.returnType else descriptor.returnType.toIrType()

inline fun IrElementScope.irConstructorCall(callee: ClassConstructorDescriptor, type: IrType = callee.returnType.toIrType(), block: IrConstructorCall.() -> Unit): IrConstructorCall =
	irConstructorCall(callee.symbol, type, block)

inline fun IrElementScope.irConstructorCall(callee: IrConstructorSymbol, type: IrType = callee.returnType, block: IrConstructorCall.() -> Unit): IrConstructorCall =
	irConstructorCall(callee, type).apply(block)

fun IrElementScope.irConstructorCall(callee: ClassConstructorDescriptor, type: IrType = callee.returnType.toIrType(), origin: IrStatementOrigin? = null): IrConstructorCall =
	irConstructorCall(callee.symbol, type, origin)

fun IrElementScope.irConstructorCall(callee: IrConstructorSymbol, type: IrType = callee.returnType, origin: IrStatementOrigin? = null): IrConstructorCall =
	IrConstructorCallImpl.fromSymbolDescriptor(startOffset, endOffset, type, callee, origin)

inline fun IrElementScope.irCall(callee: IrSimpleFunctionSymbol, type: IrType = callee.returnType, block: IrCall.() -> Unit): IrCall =
	irCall(callee, type).apply(block)

fun IrElementScope.irCall(
	callee: IrSimpleFunctionSymbol,
	type: IrType = callee.returnType,
	origin: IrStatementOrigin? = null,
	superQualifierSymbol: IrClassSymbol? = null
): IrCall = IrCallImpl(startOffset, endOffset, type, callee, origin, superQualifierSymbol)

fun IrElementScope.irDelegatingConstructorCall(callee: IrConstructor): IrDelegatingConstructorCall =
	IrDelegatingConstructorCallImpl(
		startOffset, endOffset, irBuiltIns.unitType, callee.symbol,
		callee.parentAsClass.typeParameters.size, callee.valueParameters.size
	)

fun IrElementScope.irInvoke(
	functionalTypeReceiver: IrExpression,
	valueArguments: List<IrExpression?>,
	typeArguments: List<IrType?>
) = irCall(
	builtIns.getFunction(valueArguments.size).findFirstFunction("invoke") { it.valueParameters.size == valueArguments.size },
	dispatchReceiver = functionalTypeReceiver,
	valueArguments = valueArguments,
	typeArguments = typeArguments,
	origin = IrStatementOrigin.VARIABLE_AS_FUNCTION
)


// Reference

fun IrElementScope.irReference(target: IrSymbol, type: IrType, origin: IrStatementOrigin? = null): IrDeclarationReference =
	when(target) {
		is IrClassSymbol ->
			IrClassReferenceImpl(startOffset, endOffset, type, target, target.descriptor.defaultType.toIrType())
		is IrFunctionSymbol ->
			target.descriptor.let { descriptor -> IrFunctionReferenceImpl(startOffset, endOffset, type, target, descriptor.typeParametersCount, descriptor.valueParameters.size, origin = origin) }
		is IrPropertySymbol ->
			target.tryBind().owner.let { owner -> IrPropertyReferenceImpl(startOffset, endOffset, type, target, target.descriptor.typeParametersCount, owner.backingField?.symbol, owner.getter?.symbol, owner.setter?.symbol, origin) }
		is IrLocalDelegatedPropertySymbol ->
			target.tryBind().owner.let { owner -> IrLocalDelegatedPropertyReferenceImpl(startOffset, endOffset, type, target, owner.delegate.symbol, owner.getter.symbol as IrSimpleFunctionSymbol, owner.setter?.symbol as IrSimpleFunctionSymbol, origin) }
		else -> error("unexpected symbol $target")
	}


// Get / Set

fun IrElementScope.irGet(variable: IrValueDeclaration): IrGetValue =
	IrGetValueImpl(startOffset, endOffset, variable.type, variable.symbol)

fun IrElementScope.irGet(
	symbol: IrValueSymbol,
	type: IrType = if(symbol.isBound) symbol.owner.type else symbol.descriptor.type.toIrType()
): IrGetValue = IrGetValueImpl(startOffset, endOffset, type, symbol)

fun IrElementScope.irGet(receiver: IrExpression?, field: IrField) =
	IrGetFieldImpl(startOffset, endOffset, field.symbol, field.type, receiver)

fun IrElementScope.irGet(type: IrType, receiver: IrExpression?, getterSymbol: IrFunctionSymbol): IrCall =
	IrCallImpl(
		startOffset, endOffset,
		type,
		getterSymbol as IrSimpleFunctionSymbol,
		typeArgumentsCount = getterSymbol.descriptor.typeParameters.size,
		valueArgumentsCount = 0,
		origin = IrStatementOrigin.GET_PROPERTY
	).apply {
		dispatchReceiver = receiver
	}

fun IrElementScope.irGetObject(
	classSymbol: IrClassSymbol,
	type: IrType = if(classSymbol.isBound) classSymbol.defaultType else classSymbol.descriptor.defaultType.toIrType()
): IrGetObjectValue = IrGetObjectValueImpl(startOffset, endOffset, type, classSymbol)


fun IrElementScope.irSet(variable: IrVariable, value: IrExpression) = irSet(variable.symbol, value)

fun IrElementScope.irSet(variable: IrVariableSymbol, value: IrExpression) =
	IrSetVariableImpl(startOffset, endOffset, irBuiltIns.unitType, variable, value, IrStatementOrigin.EQ)

fun IrElementScope.irSet(receiver: IrExpression?, field: IrField, value: IrExpression) =
	IrSetFieldImpl(startOffset, endOffset, field.symbol, receiver, value, irBuiltIns.unitType)

fun IrElementScope.irSet(
	receiver: IrExpression?,
	setterSymbol: IrFunctionSymbol,
	value: IrExpression,
	type: IrType = irBuiltIns.unitType
): IrCall = IrCallImpl(
	startOffset, endOffset,
	type,
	setterSymbol as IrSimpleFunctionSymbol,
	typeArgumentsCount = setterSymbol.owner.typeParameters.size,
	valueArgumentsCount = 1,
	origin = IrStatementOrigin.EQ
).apply {
	dispatchReceiver = receiver
	putValueArgument(0, value)
}


// Branches

inline class IrIfBuilder(val expression: IrIfThenElseImpl) {
	infix fun IrExpression.then(then: IrExpression) {
		expression.branches += IrBranchImpl(expression.startOffset, expression.endOffset, this, then)
	}
	
	fun orElse(then: IrExpression) {
		expression.branches += IrElseBranchImpl(expression.startOffset, expression.endOffset, expression.scope.irTrue(), then)
	}
}


inline fun IrElementScope.irIf(type: IrType, block: IrIfBuilder.() -> Unit): IrIfThenElseImpl =
	IrIfBuilder(IrIfThenElseImpl(startOffset, endOffset, type, IrStatementOrigin.IF)).apply(block).expression

fun IrElementScope.irIfThen(type: IrType, condition: IrExpression, then: IrExpression) =
	IrIfThenElseImpl(startOffset, endOffset, type, IrStatementOrigin.IF).apply {
		branches += IrBranchImpl(startOffset, endOffset, condition, then)
	}


class IrIfBuilderWithScope(val scope: IrBuilderScope, val expression: IrIfThenElseImpl) {
	infix fun IrExpression.then(then: IrExpression) {
		expression.branches += IrBranchImpl(expression.startOffset, expression.endOffset, this, then)
	}
	
	inline infix fun IrExpression.then(then: IrBlockBuilder.() -> Unit) {
		then(this@IrIfBuilderWithScope.scope.irBlock(type = expression.type, body = then))
	}
	
	fun orElse(then: IrExpression) {
		expression.branches += IrElseBranchImpl(expression.startOffset, expression.endOffset, expression.scope.irTrue(), then)
	}
	
	inline fun orElse(then: IrBlockBuilder.() -> Unit) {
		orElse(this@IrIfBuilderWithScope.scope.irBlock(type = expression.type, body = then))
	}
}


inline fun IrBuilderScope.irIfWithScope(type: IrType, block: IrIfBuilderWithScope.() -> Unit): IrIfThenElseImpl =
	IrIfBuilderWithScope(this, IrIfThenElseImpl(startOffset, endOffset, type, IrStatementOrigin.IF)).apply(block).expression


// Expressions

fun IrElementScope.primitiveOp1(
	primitiveOpSymbol: IrSimpleFunctionSymbol,
	primitiveOpReturnType: IrType,
	origin: IrStatementOrigin,
	dispatchReceiver: IrExpression
) =
	primitiveOp1(startOffset, endOffset, primitiveOpSymbol, primitiveOpReturnType, origin, dispatchReceiver)

fun IrElementScope.primitiveOp2(
	primitiveOpSymbol: IrSimpleFunctionSymbol, primitiveOpReturnType: IrType,
	origin: IrStatementOrigin,
	argument1: IrExpression, argument2: IrExpression
) =
	primitiveOp2(startOffset, endOffset, primitiveOpSymbol, primitiveOpReturnType, origin, argument1, argument2)

inline fun IrElementScope.irStringTemplate(block: IrExpressionsBuilder.() -> Unit) =
	IrStringConcatenationImpl(startOffset, endOffset, irBuiltIns.stringType, buildExpressions(block))

// logic
fun IrElementScope.irNot(argument: IrExpression, origin: IrStatementOrigin = IrStatementOrigin.EXCL) =
	primitiveOp1(irBuiltIns.booleanNotSymbol, irBuiltIns.booleanType, origin, argument)

fun IrElementScope.irLogicAnd(left: IrExpression, right: IrExpression) =
	primitiveOp2(irBuiltIns.andandSymbol, irBuiltIns.booleanType, IrStatementOrigin.ANDAND, left, right)

fun IrElementScope.irLogicOr(left: IrExpression, right: IrExpression) =
	primitiveOp2(irBuiltIns.ororSymbol, irBuiltIns.booleanType, IrStatementOrigin.OROR, left, right)


// primitive operations
inline fun Number.isZero() = toInt() == 0

fun IrElementScope.irBinaryOperator(name: Name, left: IrExpression, right: IrExpression, typeLeft: KotlinType = left.type.toKotlinType(), typeRight: KotlinType = right.type.toKotlinType()) =
	irCall(context.symbols.getBinaryOperator(name, typeLeft, typeRight), dispatchReceiver = left, valueArguments = listOf(right), type = typeLeft.toIrType())

@Suppress("UNCHECKED_CAST")
fun IrElementScope.irOr(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.OR, left, right)

@Suppress("UNCHECKED_CAST")
fun IrElementScope.irAnd(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.AND, left, right)

fun IrElementScope.irShl(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.SHL, left, right, typeRight = builtIns.intType)

fun IrElementScope.irShr(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.SHR, left, right, typeRight = builtIns.intType)

fun IrElementScope.irUshr(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.SHRU, left, right, typeRight = builtIns.intType)

fun IrElementScope.irXor(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.XOR, left, right)

fun IrElementScope.irShiftBits(value: IrExpression, bitsToShiftLeft: Int) =
	if(bitsToShiftLeft == 0) value else irBinaryOperator(if(bitsToShiftLeft > 0) OperatorNames.SHL else OperatorNames.SHR, value, irInt(abs(bitsToShiftLeft)), typeRight = builtIns.intType)

// equals
fun IrElementScope.irEquals(argument1: IrExpression, argument2: IrExpression, origin: IrStatementOrigin = IrStatementOrigin.EQEQ) =
	primitiveOp2(irBuiltIns.eqeqSymbol, irBuiltIns.booleanType, origin, argument1, argument2)

fun IrElementScope.irNotEquals(argument1: IrExpression, argument2: IrExpression) =
	irNot(irEquals(argument1, argument2, origin = IrStatementOrigin.EXCLEQ), IrStatementOrigin.EXCLEQ)

fun IrElementScope.irEqualsNull(argument: IrExpression) = irEquals(argument, irNull())

fun IrElementScope.isEqualsReferential(argument1: IrExpression, argument2: IrExpression) =
	primitiveOp2(irBuiltIns.eqeqeqSymbol, irBuiltIns.booleanType, IrStatementOrigin.EQEQEQ, argument1, argument2)

// cast
fun IrElementScope.typeOperator(
	resultType: IrType,
	argument: IrExpression,
	typeOperator: IrTypeOperator,
	typeOperand: IrType
) =
	IrTypeOperatorCallImpl(startOffset, endOffset, resultType, typeOperator, typeOperand, argument)

fun IrElementScope.irIs(argument: IrExpression, type: IrType) =
	typeOperator(context.irBuiltIns.booleanType, argument, IrTypeOperator.INSTANCEOF, type)

fun IrElementScope.irNotIs(argument: IrExpression, type: IrType) =
	typeOperator(context.irBuiltIns.booleanType, argument, IrTypeOperator.NOT_INSTANCEOF, type)

fun IrElementScope.irAs(argument: IrExpression, type: IrType) =
	typeOperator(type, argument, IrTypeOperator.CAST, type)

fun IrElementScope.irPrimitiveCast(expression: IrExpression, type: IrType): IrExpression {
	val toName = type.classifierOrFail.descriptor.name.identifier.withLog().substringAfter('.')
	val castFunction = expression.type.classOrNull!!.descriptor.unsubstitutedMemberScope.findFirstFunction("to$toName") { true }
	return irCall(castFunction, dispatchReceiver = expression)
}

fun IrElementScope.irImplicitCast(argument: IrExpression, type: IrType) =
	typeOperator(type, argument, IrTypeOperator.IMPLICIT_CAST, type)

fun IrElementScope.irReinterpretCast(argument: IrExpression, type: IrType) =
	typeOperator(type, argument, IrTypeOperator.REINTERPRET_CAST, type)


// Block

open class IrBlockBuilder(override val startOffset: Int, override val endOffset: Int, val resultType: IrType? = null, val origin: IrStatementOrigin? = null, val isTransparentScope: Boolean, override val scope: Scope) : IrStatementsScope<IrContainerExpression> {
	val statements = mutableListOf<IrStatement>()
	
	override fun IrStatement.unaryPlus() {
		statements += this
	}
	
	private fun inferReturnType() =
		resultType ?: (statements.lastOrNull() as? IrExpression)?.type ?: irBuiltIns.unitType
	
	override fun build(): IrContainerExpression =
		if(isTransparentScope) IrCompositeImpl(startOffset, endOffset, inferReturnType(), origin, statements)
		else IrBlockImpl(startOffset, endOffset, inferReturnType(), origin, statements)
}

class IrReturnableBlockBuilder(startOffset: Int, endOffset: Int, resultType: IrType, origin: IrStatementOrigin? = null, scope: Scope)
	: IrBlockBuilder(startOffset, endOffset, resultType, origin, false, scope) {
	val returnTargetSymbol = IrReturnableBlockSymbolImpl(createAnonymousFunctionDescriptor(returnType = resultType.toKotlinType()))
	
	override fun build(): IrReturnableBlock =
		IrReturnableBlockImpl(startOffset, endOffset, resultType!!, returnTargetSymbol, origin, statements)
}

inline fun IrBuilderScope.irBlock(type: IrType? = null, origin: IrStatementOrigin? = null, body: IrBlockBuilder.() -> Unit) =
	IrBlockBuilder(startOffset, endOffset, resultType = type, origin = origin, isTransparentScope = false, scope = scope).apply(body).build()

inline fun IrBuilderScope.irComposite(type: IrType? = null, origin: IrStatementOrigin? = null, body: IrBlockBuilder.() -> Unit) =
	IrBlockBuilder(startOffset, endOffset, resultType = type, origin = origin, isTransparentScope = true, scope = scope).apply(body).build()

inline fun IrBuilderScope.irReturnableBlock(type: IrType, origin: IrStatementOrigin? = null, body: IrBlockBuilder.() -> Unit) =
	IrReturnableBlockBuilder(startOffset, endOffset, resultType = type, origin = origin, scope = scope).apply(body).build()

// Flows

// return
val IrBuilderScope.returnTargetSymbol
	get() = scope.scopeOwnerSymbol as? IrReturnTargetSymbol ?: error("returnTargetSymbol not found")

fun IrBuilderScope.irReturn(value: IrExpression) = irReturn(value, returnTargetSymbol)
fun IrElementScope.irReturn(value: IrExpression, returnTargetSymbol: IrReturnTargetSymbol): IrReturn =
	IrReturnImpl(startOffset, endOffset, irBuiltIns.nothingType, returnTargetSymbol, value)

fun IrBuilderScope.irReturnUnit() = irReturn(irUnit())
fun IrElementScope.irReturnUnit(returnTargetSymbol: IrReturnTargetSymbol) =
	irReturn(irUnit(), returnTargetSymbol)

fun IrBuilderScope.irReturnTrue() = irReturn(irFalse())
fun IrBuilderScope.irReturnTrue(returnTargetSymbol: IrReturnTargetSymbol) =
	irReturn(irFalse(), returnTargetSymbol)

fun IrBuilderScope.irReturnFalse() = irReturn(irFalse())
fun IrBuilderScope.irReturnFalse(returnTargetSymbol: IrReturnTargetSymbol) =
	irReturn(irFalse(), returnTargetSymbol)


// Constants

fun IrElementScope.irUnit() =
	irGetObject(irBuiltIns.unitClass, irBuiltIns.unitType)

fun IrElementScope.irBoolean(value: Boolean): IrConst<Boolean> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.booleanType, IrConstKind.Boolean, value)

fun IrElementScope.irTrue() = irBoolean(true)
fun IrElementScope.irFalse() = irBoolean(false)

fun IrElementScope.irByte(value: Byte): IrConst<Byte> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.byteType, IrConstKind.Byte, value)

fun IrElementScope.irShort(value: Short): IrConst<Short> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.shortType, IrConstKind.Short, value)

fun IrElementScope.irInt(value: Int): IrConst<Int> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.intType, IrConstKind.Int, value)

fun IrElementScope.irLong(value: Long): IrConst<Long> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.longType, IrConstKind.Long, value)

fun IrElementScope.irFloat(value: Float): IrConst<Float> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.floatType, IrConstKind.Float, value)

fun IrElementScope.irDouble(value: Double): IrConst<Double> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.doubleType, IrConstKind.Double, value)

fun IrElementScope.irChar(value: Char): IrConst<Char> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.charType, IrConstKind.Char, value)

fun IrElementScope.irString(value: String): IrConst<String> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.stringType, IrConstKind.String, value)

fun IrElementScope.irNull(): IrConst<Nothing?> =
	IrConstImpl(startOffset, endOffset, irBuiltIns.nothingNType, IrConstKind.Null, null)

fun IrElementScope.irNull(type: IrType): IrConst<Nothing?> =
	IrConstImpl.constNull(startOffset, endOffset, type)



