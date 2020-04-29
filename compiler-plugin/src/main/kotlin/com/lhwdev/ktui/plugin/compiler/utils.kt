package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.builtins.*
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.AnonymousFunctionDescriptor
import org.jetbrains.kotlin.descriptors.impl.ValueParameterDescriptorImpl
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.backend.js.utils.OperatorNames
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.IrValueParameterBuilder
import org.jetbrains.kotlin.ir.builders.declarations.build
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrSymbolDeclaration
import org.jetbrains.kotlin.ir.declarations.impl.IrFunctionImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrTypeParameterImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.descriptors.WrappedFunctionDescriptorWithContainerSource
import org.jetbrains.kotlin.ir.descriptors.WrappedPropertyGetterDescriptor
import org.jetbrains.kotlin.ir.descriptors.WrappedPropertySetterDescriptor
import org.jetbrains.kotlin.ir.descriptors.WrappedSimpleFunctionDescriptor
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi2ir.findFirstFunction
import org.jetbrains.kotlin.resolve.DescriptorFactory
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DescriptorWithContainerSource
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.replace
import kotlin.math.abs


object Global {
	val moduleFragment = currentLowering().moduleFragment
	val pluginContext = currentLowering().pluginContext
	val module = moduleFragment.descriptor
	val scope = IrScope(pluginContext, moduleFragment)
}

object UiDeclarations {
	val buildScope = module.resolveTopLevelClass(BUILD_SCOPE, NoLookupLocation.FROM_BACKEND)!!
	
	object BuildScope {
		val start = buildScope.findFirstFunction(kBuildScope.START) { it.valueParameters.size == 3 }
		val end1 = buildScope.findFirstFunction(kBuildScope.END) { it.valueParameters.size == 1 }
		val end0 = buildScope.findFirstFunction(kBuildScope.END) { it.valueParameters.size == 0 }
		val endSkip = buildScope.findFirstFunction(kBuildScope.END_SKIP) { it.valueParameters.size == 0 }
	}
}

inline val module get() = Global.module
inline val scope get() = Global.scope
inline val context get() = Global.pluginContext
inline val pluginContext get() = Global.pluginContext


enum class WidgetKind { none, widgetUtil, widget }


enum class WidgetTransformationKind {
	widgetUtil, inlineWidget, widget;
	
	val isWidget get() = this == inlineWidget || this == widget
}


fun IrType.isWidget() = hasAnnotation(WIDGET)
fun IrType.isWidgetUtil() = hasAnnotation(WIDGET_UTIL)
fun Annotated.isWidget() = annotations.hasAnnotation(WIDGET)
fun Annotated.isWidgetUtil() = annotations.hasAnnotation(WIDGET_UTIL)
fun IrFunction.isWidget() = hasAnnotation(WIDGET)
fun IrFunction.isWidgetUtil() = hasAnnotation(WIDGET_UTIL)


val IrSymbol.ownerOrNull get() = if(isBound) owner else null

val List<AnnotationDescriptor>.widgetKind: WidgetKind
	get() {
		var isWidget = false
		var isWidgetUtil = false
		
		forEach {
			when(it.fqName) {
				WIDGET -> isWidget = true
				WIDGET_UTIL -> isWidgetUtil = true
			}
		}
		return when {
			isWidget -> WidgetKind.widget
			isWidgetUtil -> WidgetKind.widgetUtil
			else -> WidgetKind.none
		}
	}

val IrType.widgetTransformationKind
	get() = when {
		isWidget() -> if(hasAnnotation(INLINE_WIDGET)) WidgetTransformationKind.inlineWidget else WidgetTransformationKind.widget
		isWidgetUtil() -> WidgetTransformationKind.widgetUtil
		else -> null
	}
val Annotated.widgetKind
	get() = when {
		isWidget() -> WidgetKind.widget
		isWidgetUtil() -> WidgetKind.widgetUtil
		else -> WidgetKind.none
	}
val Annotated.anyWidgetKind
	get() = when {
		isWidget() -> WidgetKind.widget
		isWidgetUtil() -> WidgetKind.widgetUtil
		else -> null
	}

val Annotated.widgetTransformationKind
	get() = when {
		isWidget() -> if(annotations.hasAnnotation(INLINE_WIDGET)) WidgetTransformationKind.inlineWidget else WidgetTransformationKind.widget
		isWidgetUtil() -> WidgetTransformationKind.widgetUtil
		else -> null
	}

val IrFunction.widgetTransformationKind
	get() = when {
		isWidget() -> if(annotations.hasAnnotation(INLINE_WIDGET)) WidgetTransformationKind.inlineWidget else WidgetTransformationKind.widget
		isWidgetUtil() -> WidgetTransformationKind.widgetUtil
		else -> null
	}


fun FunctionDescriptor.isFunctionInvoke() =
	dispatchReceiverParameter?.type?.let {
		it.isFunctionTypeOrSubtype &&
			isOperator &&
			name.asString() == "invoke" &&
			it.arguments.size == valueParameters.size + 1 // size: valueParameters + returnType
	} == true

fun IrBuilderWithScope.irCall(callee: SimpleFunctionDescriptor) =
	irCall(pluginContext.symbolTable.referenceSimpleFunction(callee), callee.returnType?.toIrType()
		?: context.irBuiltIns.unitType)

fun IrBuilderWithScope.irCall(callee: IrSimpleFunction) = irCall(callee.symbol)

fun IrBuilderWithScope.irCallDefault(callee: IrFunctionSymbol) =
	irCall(callee, callee.descriptor, callee.descriptor.returnType?.toIrType()
		?: context.irBuiltIns.unitType)

fun IrBuilderWithScope.irCallDefault(callee: IrFunction) =
	irCall(callee.symbol, callee.descriptor, callee.returnType)


fun SymbolTable.allUnbound(): Set<IrSymbol> = mutableSetOf<IrSymbol>().apply {
	addAll(unboundClasses)
	addAll(unboundConstructors)
	addAll(unboundEnumEntries)
	addAll(unboundFields)
	addAll(unboundProperties)
	addAll(unboundSimpleFunctions)
	addAll(unboundTypeAliases)
	addAll(unboundTypeParameters)
}

fun IrScope.irBuilder(declaration: IrSymbolDeclaration<*>) =
	DeclarationIrBuilder(pluginContext, declaration.symbol, declaration.startOffset, declaration.endOffset)

fun <R> IrScope.irBuilder(declaration: IrSymbolDeclaration<*>, block: DeclarationIrBuilder.() -> R) =
	irBuilder(declaration).block()

fun IrScope.irBuilder(symbol: IrSymbol, startOffset: Int = UNDEFINED_OFFSET, endOffset: Int = UNDEFINED_OFFSET) =
	DeclarationIrBuilder(pluginContext, symbol, startOffset, endOffset)

fun <R> IrScope.irBuilder(symbol: IrSymbol, startOffset: Int = UNDEFINED_OFFSET, endOffset: Int = UNDEFINED_OFFSET, block: DeclarationIrBuilder.() -> R) =
	irBuilder(symbol, startOffset, endOffset).block()


inline fun IrMemberAccessExpression.forEachValueArgument(block: (index: Int, valueArgument: IrExpression?) -> Unit) {
	for(i in 0 until valueArgumentsCount)
		block(i, getValueArgument(i))
}

val IrMemberAccessExpression.valueArguments: List<IrExpression?>
	get() = object : AbstractList<IrExpression?>() {
		override val size: Int get() = valueArgumentsCount
		override fun get(index: Int) = getValueArgument(index)
	}

val IrFunctionAccessExpression.valueArgumentsMap get() = symbol.descriptor.valueParameters.map { it.name to getValueArgument(it) }.toMap()

fun IrFunctionAccessExpression.putValueArgument(name: String, valueArgument: IrExpression?) {
	putValueArgument(symbol.descriptor.valueParameters.find { it.name.asString() == name }!!, valueArgument)
}

fun IrBuilderWithScope.irArrayOf(type: IrType, values: List<IrExpression>) =
	irCall(pluginContext.symbols.arrayOf, type).apply {
		putTypeArgument(0, type)
		putValueArgument(0, irVararg(type, values)) // >> attrs = arrayOf(abc)
	}

fun IrBuilderWithScope.irPrimitiveCast(expression: IrExpression, type: IrType): IrExpression {
	val toName = type.classifierOrFail.descriptor.name.identifier.withLog().substringAfter('.')
	val castFunction = expression.type.classOrNull!!.descriptor.unsubstitutedMemberScope.findFirstFunction("to$toName") { true }
	return irCall(castFunction).apply {
		dispatchReceiver = expression
	}
}

private val sRunSymbol by lazy {
	context.symbolTable.referenceSimpleFunction(
		context.moduleDescriptor.getPackage(kotlinPackageFqn).memberScope
			.findFirstFunction("run") { it.valueParameters.size == 1 && it.extensionReceiverParameter == null }
	)
}

fun IrBuilderWithScope.irRun(
	startOffset: Int = this.startOffset,
	endOffset: Int = this.endOffset,
	type: IrType,
	body: IrBlockBodyBuilder.() -> Unit
): IrExpression = irCall(callee = sRunSymbol, type = type).apply {
	putTypeArgument(0, type)
	putValueArgument(0,
		irLambdaExpression(
			startOffset, endOffset,
			descriptor = createFunctionDescriptor(returnType = type.toKotlinType()),
			type = context.builtIns.getFunction(0).defaultType.replace(listOf(TypeProjectionImpl(type.toKotlinType()))).toIrType()
		) { body() }
	)
}

val IrBuilderWithScope.returnTargetSymbol get() = scope.scopeOwnerSymbol as IrReturnTargetSymbol


fun IrBuilderWithScope.irLambdaExpression(
	startOffset: Int = UNDEFINED_OFFSET,
	endOffset: Int = UNDEFINED_OFFSET,
	descriptor: FunctionDescriptor,
	type: IrType,
	body: IrBlockBodyBuilder.(IrFunction) -> Unit
): IrExpression {
	val symbol = IrSimpleFunctionSymbolImpl(descriptor)
	
	val returnType = descriptor.returnType!!.toIrType() // ?: context.irBuiltIns.unitType
//
	val lambda = IrFunctionImpl(
		startOffset, endOffset,
		IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA,
		symbol,
		returnType
	).also {
		it.parent = scope.getLocalDeclarationParent()
		it.createParameterDeclarations()
		it.body = DeclarationIrBuilder(context, symbol)
			.irBlockBody { body(it) }
	}
	
	return IrFunctionExpressionImpl(startOffset, endOffset, type, lambda, IrStatementOrigin.LAMBDA)
}

fun IrFunction.createParameterDeclarations() {
	fun ParameterDescriptor.irValueParameter() = IrValueParameterImpl(
		this.startOffset ?: UNDEFINED_OFFSET,
		this.endOffset ?: UNDEFINED_OFFSET,
		IrDeclarationOrigin.DEFINED,
		this,
		type.toIrType(),
		(this as? ValueParameterDescriptor)?.varargElementType?.toIrType()
	).also {
		it.parent = this@createParameterDeclarations
	}
	
	fun TypeParameterDescriptor.irTypeParameter() = IrTypeParameterImpl(
		this.startOffset ?: UNDEFINED_OFFSET,
		this.endOffset ?: UNDEFINED_OFFSET,
		IrDeclarationOrigin.DEFINED,
		IrTypeParameterSymbolImpl(this)
	).also {
		it.parent = this@createParameterDeclarations
	}
	
	dispatchReceiverParameter = descriptor.dispatchReceiverParameter?.irValueParameter()
	extensionReceiverParameter = descriptor.extensionReceiverParameter?.irValueParameter()
	
	assert(valueParameters.isEmpty())
	valueParameters = descriptor.valueParameters.map { it.irValueParameter() }
	
	assert(typeParameters.isEmpty())
	typeParameters = descriptor.typeParameters.map { it.irTypeParameter() }
}


fun IrBuilderWithScope.createFunctionDescriptor(
	valueParameters: List<ValueParameterDescriptor> = emptyList(),
	extensionReceiverParameter: ReceiverParameterDescriptor? = null,
	dispatchReceiverParameter: ReceiverParameterDescriptor? = null,
	typeParameters: List<TypeParameterDescriptor> = emptyList(),
	returnType: KotlinType = context.builtIns.unitType,
	modality: Modality = Modality.FINAL,
	visibility: Visibility = Visibilities.LOCAL,
	owner: DeclarationDescriptor = scope.scopeOwner,
	annotations: Annotations = Annotations.EMPTY,
	kind: CallableMemberDescriptor.Kind = CallableMemberDescriptor.Kind.SYNTHESIZED,
	sourceElement: SourceElement = SourceElement.NO_SOURCE,
	isSuspend: Boolean = false
) = AnonymousFunctionDescriptor(owner, annotations, kind, sourceElement, isSuspend).apply {
	initialize(extensionReceiverParameter, dispatchReceiverParameter, typeParameters, valueParameters, returnType, modality, visibility)
}

fun IrBuilderWithScope.createFunctionDescriptorFromFunctionType(
	type: KotlinType,
	owner: DeclarationDescriptor = scope.scopeOwner
) = AnonymousFunctionDescriptor(
	owner,
	Annotations.EMPTY,
	CallableMemberDescriptor.Kind.SYNTHESIZED,
	SourceElement.NO_SOURCE,
	false
).apply {
	initialize(
		type.getReceiverTypeFromFunctionType()?.let {
			DescriptorFactory.createExtensionReceiverParameterForCallable(
				this,
				it,
				Annotations.EMPTY
			)
		},
		null,
		emptyList(),
		type.getValueParameterTypesFromFunctionType().mapIndexed { i, t ->
			ValueParameterDescriptorImpl(
				containingDeclaration = this,
				original = null,
				index = i,
				annotations = Annotations.EMPTY,
				name = t.type.extractParameterNameFromFunctionTypeArgument()
					?: Name.identifier("p$i"),
				outType = t.type,
				declaresDefaultValue = false,
				isCrossinline = false,
				isNoinline = false,
				varargElementType = null,
				source = SourceElement.NO_SOURCE
			)
		},
		type.getReturnTypeFromFunctionType(),
		Modality.FINAL,
		Visibilities.LOCAL,
		null
	)
	
	isOperator = false
	isInfix = false
	isExternal = false
	isInline = false
	isTailrec = false
	isSuspend = false
	isExpect = false
	isActual = false
}

fun IrBuilder.irVararg(type: IrType, elements: List<IrVarargElement>) =
	IrVarargImpl(startOffset, endOffset, pluginContext.symbols.array.typeWith(type), type, elements)

fun IrBuilderWithScope.irBinaryOperator(name: Name, left: IrExpression, right: IrExpression, typeLeft: KotlinType, typeRight: KotlinType = typeLeft): IrExpression {
//	if(left is IrConst<*> && right is IrConst<*>) // TODO: is this needed?
//		when {
//			typeLeft.isInt() -> {
//				val l = left.value as Int
//				val r = right.value as Int
//
//				when(name) {
//					OperatorNames.AND -> l and r
//					OperatorNames.OR -> l or r
//					OperatorNames.SHL -> l shl r
//					OperatorNames.SHR -> l shr r
//					OperatorNames.SHRU -> l ushr r
//					OperatorNames.XOR -> l xor r
//					else -> null
//				}?.let { return irConst(it) }
//			}
//			typeLeft.isLong() -> {
//				val l = left.value as Long
//
//				when(name) {
//					OperatorNames.AND -> l and right.value as Long
//					OperatorNames.OR -> l or right.value as Long
//					OperatorNames.SHL -> l shl right.value as Int
//					OperatorNames.SHR -> l shr right.value as Int
//					OperatorNames.SHRU -> l ushr right.value as Int
//					OperatorNames.XOR -> l xor right.value as Long
//					else -> null
//				}?.let { return irConst(it) }
//			}
//		}
	
	return irCall(pluginContext.symbols.getBinaryOperator(name, typeLeft, typeRight), typeLeft.toIrType()).apply {
		dispatchReceiver = left
		putValueArgument(0, right)
	}
}


fun Number.isZero() = toInt() == 0


@Suppress("UNCHECKED_CAST")
fun IrBuilderWithScope.irOr(left: IrExpression, right: IrExpression) = when {
	left is IrConst<*> && (left.value as Number).isZero() -> right
	right is IrConst<*> && (right.value as Number).isZero() -> left
	else -> irBinaryOperator(OperatorNames.OR, left, right, left.type.toKotlinType())
}

@Suppress("UNCHECKED_CAST")
fun IrBuilderWithScope.irAnd(left: IrExpression, right: IrExpression) = when {
	left is IrConst<*> && (left.value as Number).isZero() -> IrConstImpl(startOffset, endOffset, left.type, left.kind as IrConstKind<Number>, 0x0)
	right is IrConst<*> && (right.value as Number).isZero() -> IrConstImpl(startOffset, endOffset, left.type, right.kind as IrConstKind<Number>, 0x0)
	else -> irBinaryOperator(OperatorNames.AND, left, right, left.type.toKotlinType())
}

fun IrBuilderWithScope.irShl(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.SHL, left, right, left.type.toKotlinType(), context.builtIns.intType)

fun IrBuilderWithScope.irShr(left: IrExpression, right: IrExpression) =
	irBinaryOperator(OperatorNames.SHR, left, right, left.type.toKotlinType(), context.builtIns.intType)

fun IrBuilderWithScope.irShiftBits(value: IrExpression, bitsToShiftLeft: Int) =
	if(bitsToShiftLeft == 0) value else irBinaryOperator(if(bitsToShiftLeft > 0) OperatorNames.SHL else OperatorNames.SHR, value, irConst(abs(bitsToShiftLeft)), value.type.toKotlinType(), context.builtIns.intType)

fun IrBuilder.irConst(value: Int) =
	IrConstImpl(startOffset, endOffset, context.irBuiltIns.intType, IrConstKind.Int, value)

fun IrBuilder.irConst(value: Long) =
	IrConstImpl(startOffset, endOffset, context.irBuiltIns.longType, IrConstKind.Long, value)

fun IrBuilder.irConst(value: Boolean) =
	IrConstImpl(startOffset, endOffset, context.irBuiltIns.booleanType, IrConstKind.Boolean, value)


fun IrClassifierSymbol.superTypes() = when(this) {
	is IrClassSymbol -> owner.superTypes
	is IrTypeParameterSymbol -> owner.superTypes
	else -> emptyList()
}

fun IrClassifierSymbol.isSubtypeOfClass(superClass: IrClassSymbol): Boolean {
	if(FqNameEqualityChecker.areEqual(this, superClass)) return true
	return superTypes().any { it.isSubtypeOfClass(superClass) }
}

fun IrType.isSubtypeOfClass(parent: IrClassSymbol) =
	classifierOrNull?.let { if(it.tryBind().isBound) it.isSubtypeOfClass(parent) else false }
		?: false

fun IrType.isFunction() =
	isSubtypeOfClass(context.symbolTable.referenceClass(context.moduleDescriptor.resolveTopLevelClass(kotlinPackageFqn.child(Name.identifier("Function")), NoLookupLocation.FROM_BACKEND)!!))


fun KotlinType.toIrType() = Global.pluginContext.typeTranslator.translateType(this)
//fun a() = IrTypeParameterBuilder().apply {
//	superTypes +=
//}.build()
//fun IrClassSymbol.asTypeParameter(origin: IrDeclarationOrigin) = IrTypeParameterImpl(startOffset = UNDEFINED_OFFSET, endOffset = UNDEFINED_OFFSET, origin = origin, symbol = IrTypeParameterSymbolImpl())
//fun IrType.asTypeParameter() = IrTypeParameterBuilder().apply {
//	superTypes += this@asTypeParameter
//}.build()
//
//fun IrType.asValueParameter() = IrValueParameterBuilder().apply {
//	type = this@asValueParameter
//	name = Name.identifier("<this>")
//}.build()

fun ClassDescriptor.toValueParameter(parameterName: String, parameterIndex: Int = -1) =
	IrValueParameterBuilder().apply {
		type = defaultType.toIrType()
		name = Name.guessByFirstCharacter(parameterName)
		index = parameterIndex
	}.build()

fun ClassDescriptor.toReceiverParameter() =
	toValueParameter("<this>")


private fun wrapDescriptor(descriptor: FunctionDescriptor): WrappedSimpleFunctionDescriptor {
	return when(descriptor) {
		is PropertyGetterDescriptor ->
			WrappedPropertyGetterDescriptor(
				descriptor.annotations,
				descriptor.source
			)
		is PropertySetterDescriptor ->
			WrappedPropertySetterDescriptor(
				descriptor.annotations,
				descriptor.source
			)
		is DescriptorWithContainerSource ->
			WrappedFunctionDescriptorWithContainerSource(descriptor.containerSource)
		else ->
			WrappedSimpleFunctionDescriptor(sourceElement = descriptor.source)
	}
}

fun IrFunction.copyLight(
	isInline: Boolean = this.isInline,
	modality: Modality = descriptor.modality
): IrSimpleFunction {
	// TODO(lmr): use deepCopy instead?
	val descriptor = descriptor
	val newDescriptor = wrapDescriptor(descriptor)
	
	return IrFunctionImpl(
		startOffset = startOffset,
		endOffset = endOffset,
		origin = origin,
		symbol = IrSimpleFunctionSymbolImpl(newDescriptor),
		name = name,
		visibility = visibility,
		modality = modality,
		returnType = returnType,
		isInline = isInline,
		isExternal = isExternal,
		isTailrec = descriptor.isTailrec,
		isSuspend = descriptor.isSuspend,
		isOperator = descriptor.isOperator,
		isExpect = descriptor.isExpect,
		isFakeOverride = isFakeOverride
	).also { fn ->
		newDescriptor.bind(fn)
		if(this is IrSimpleFunction) {
			fn.correspondingPropertySymbol = correspondingPropertySymbol
		}
		fn.parent = parent
		fn.valueParameters = valueParameters
		fn.typeParameters = typeParameters
//		fn.copyParameterDeclarationsFrom(this)
		fn.dispatchReceiverParameter = dispatchReceiverParameter
		fn.extensionReceiverParameter = extensionReceiverParameter
//		valueParameters.mapTo(fn.valueParameters) { p ->
//			// Composable lambdas will always have `IrGet`s of all of their parameters
//			// generated, since they are passed into the restart lambda. This causes an
//			// interesting corner case with "anonymous parameters" of composable functions.
//			// If a parameter is anonymous (using the name `_`) in user code, you can usually
//			// make the assumption that it is never used, but this is technically not the
//			// case in composable lambdas. The synthetic name that kotlin generates for
//			// anonymous parameters has an issue where it is not safe to dex, so we sanitize
//			// the names here to ensure that dex is always safe.
////			p.copyTo(fn, name = dexSafeName(p.name))
////			p.copyTo(fn)
//		}
		fn.annotations = annotations
		fn.body = body
	}
}

//private fun dexSafeName(name: Name): Name {
//	return if(name.isSpecial && name.asString().contains(' ')) {
//		val sanitized = name
//			.asString()
//			.replace(' ', '$')
//			.replace('<', '$')
//			.replace('>', '$')
//		Name.identifier(sanitized)
//	} else name
//}
