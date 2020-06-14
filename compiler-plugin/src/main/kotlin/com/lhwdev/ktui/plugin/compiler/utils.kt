package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.builtins.isFunctionTypeOrSubtype
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.builders.declarations.IrValueParameterBuilder
import org.jetbrains.kotlin.ir.builders.declarations.UNDEFINED_PARAMETER_INDEX
import org.jetbrains.kotlin.ir.builders.declarations.build
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner
import org.jetbrains.kotlin.ir.declarations.impl.IrFunctionImpl
import org.jetbrains.kotlin.ir.descriptors.WrappedFunctionDescriptorWithContainerSource
import org.jetbrains.kotlin.ir.descriptors.WrappedPropertyGetterDescriptor
import org.jetbrains.kotlin.ir.descriptors.WrappedPropertySetterDescriptor
import org.jetbrains.kotlin.ir.descriptors.WrappedSimpleFunctionDescriptor
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DescriptorWithContainerSource
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.Variance
import org.jetbrains.kotlin.types.replace
import org.jetbrains.kotlin.types.typeUtil.isUnit
import org.jetbrains.kotlin.util.OperatorNameConventions


object Global {
	val moduleFragment = currentTransformation().moduleFragment
	val pluginContext = currentTransformation().pluginContext
	val target = currentTransformation().target
	val module = moduleFragment.descriptor
	val scope = IrScope(target, pluginContext, moduleFragment)
}


inline val moduleFragment get() = Global.moduleFragment
inline val module get() = Global.module
inline val irScope get() = Global.scope
inline val context get() = Global.pluginContext
inline val pluginContext get() = Global.pluginContext


enum class WidgetTransformationKind(val isSkippable: Boolean, val isRestartable: Boolean, val isInline: Boolean) {
	inlineWidget(true, false, true),
	widget(true, true, false),
	nonSkippableWidget(false, false, false),
	nonSkippableWidgetWithReturn(false, false, false),
	innerWidget(false, false, false) // lambda, function literal, nested function etc.
}


fun IrType.isWidget() = hasAnnotation(UiLibrary.WIDGET)
fun Annotated.isWidget() = annotations.hasAnnotation(UiLibrary.WIDGET)
fun IrFunction.isWidget() = hasAnnotation(UiLibrary.WIDGET)
fun List<AnnotationDescriptor>.isWidget() = any { it.fqName == UiLibrary.WIDGET }

val IrFunction.widgetMarker get() = irScope.uiTrace[UiWritableSlices.WIDGET_KIND, this]
val IrFunction.widgetInfoMarker get() = irScope.uiTrace[UiWritableSlices.WIDGET_TRANSFORMATION_INFO, this]
val IrFunction.widgetInfoMarkerForCall get() = irScope.uiTrace[UiWritableSlices.WIDGET_TRANSFORMATION_INFO_FOR_CALL, this]


val IrSymbol.ownerOrNull get() = if(isBound) owner else null
val <B : IrSymbolOwner> IrBindableSymbol<*, B>.ownerOrNull
	get() = when {
		isBound -> owner
		tryBind().isBound -> owner
		else -> null
	}


val IrFunction.widgetTransformationKind
	get() = if(isWidget()) when { // there is annotation? [yes: there is valueParameter? [yes: that value, no: true], no: isInline]
		run {
			val annotation = annotations.findAnnotation(UiLibrary.INLINE_WIDGET)
			if(annotation == null) isInline
			else {
				val parameter = annotation.valueArguments.singleOrNull()
				parameter?.isTrueConst() ?: true
			}
		} -> WidgetTransformationKind.inlineWidget
		
		returnType.isUnit() -> WidgetTransformationKind.widget
		visibility == Visibilities.LOCAL -> WidgetTransformationKind.innerWidget
		else -> WidgetTransformationKind.nonSkippableWidget
	}
	else null

val CallableMemberDescriptor.widgetTransformationKind
	get() = if(isWidget()) when { // there is annotation? [yes: there is valueParameter? [yes: that value, no: true], no: isInline]
		this is FunctionDescriptor && run {
			val annotation = annotations.findAnnotation(UiLibrary.INLINE_WIDGET)
			if(annotation == null) isInline
			else {
				val parameter = annotation.allValueArguments.values.singleOrNull()
				parameter?.let { it.value == true } ?: true
			}
		} -> WidgetTransformationKind.inlineWidget
		
		returnType?.isUnit() ?: true -> WidgetTransformationKind.widget
		visibility == Visibilities.LOCAL -> WidgetTransformationKind.innerWidget
		else -> WidgetTransformationKind.nonSkippableWidget
	}
	else null


fun FunctionDescriptor.isFunctionInvoke() =
	dispatchReceiverParameter?.type?.let {
		it.isFunctionTypeOrSubtype &&
			isOperator &&
			name == OperatorNameConventions.INVOKE &&
			it.arguments.size == valueParameters.size + 1 // size: valueParameters + returnType
	} == true


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

fun IrElementScope.irSimpleError(message: String) =
	irCall(module.getPackage(kotlinPackageFqn).memberScope.findFirstFunction("error") { it.valueParameters.size == 1 }, valueArguments = listOf(irString(message)))

inline fun IrMemberAccessExpression.forEachValueArgument(block: (index: Int, valueArgument: IrExpression?) -> Unit) {
	for(i in 0 until valueArgumentsCount)
		block(i, getValueArgument(i))
}

val IrMemberAccessExpression.valueArguments: List<IrExpression?>
	get() = object : AbstractList<IrExpression?>() {
		override val size: Int get() = valueArgumentsCount
		override fun get(index: Int) = getValueArgument(index)
	}

val IrMemberAccessExpression.valueArgumentsMap: MutableMap<Int, IrExpression>
	get() = object : AbstractMutableMap<Int, IrExpression>() {
		override val entries = object : AbstractMutableSet<MutableMap.MutableEntry<Int, IrExpression>>() {
			override val size get() = valueArgumentsCount
			
			override fun add(element: MutableMap.MutableEntry<Int, IrExpression>): Boolean {
				putValueArgument(element.key, element.value)
				return true
			}
			
			override fun iterator() =
				object : MutableIterator<MutableMap.MutableEntry<Int, IrExpression>> {
					private var index = 0
					private var nextIndex = nextIndex()
					
					fun nextIndex(): Int {
						for(i in index until valueArgumentsCount)
							if(getValueArgument(i) != null) return i
						return -1
					}
					
					override fun hasNext() = nextIndex != -1
					
					override fun next() = object : MutableMap.MutableEntry<Int, IrExpression> {
						override val key = index
						override val value get() = getValueArgument(key)!!
						override fun setValue(newValue: IrExpression): IrExpression {
							val last = value
							putValueArgument(key, newValue)
							return last
						}
					}.also {
						index = nextIndex
						nextIndex = nextIndex()
					}
					
					override fun remove() {
						putValueArgument(index, null)
					}
				}
		}
		
		override fun put(key: Int, value: IrExpression): IrExpression? {
			val last = getValueArgument(key)
			putValueArgument(key, value)
			return last
		}
	}

val IrMemberAccessExpression.typeArguments: List<IrType?>
	get() = object : AbstractList<IrType?>() {
		override val size: Int get() = typeArgumentsCount
		override fun get(index: Int) = getTypeArgument(index)
	}

fun IrFunctionAccessExpression.putValueArgument(name: String, valueArgument: IrExpression?) {
	putValueArgument(symbol.descriptor.valueParameters.find { it.name.asString() == name }!!, valueArgument)
}

fun IrElementScope.irArrayOf(type: IrType, values: List<IrExpression>) =
	irCall(pluginContext.symbols.arrayOf, type).apply {
		putTypeArgument(0, type)
		putValueArgument(0, irVararg(type, values)) // >> attrs = arrayOf(abc)
	}


private val sRunSymbol by lazy {
	context.symbolTable.referenceSimpleFunction(
		context.moduleDescriptor.getPackage(kotlinPackageFqn).memberScope
			.findFirstFunction("run") { it.valueParameters.size == 1 && it.extensionReceiverParameter == null }
	)
}

fun IrBuilderScope.irRun(
	type: IrType,
	body: IrBlockBodyBuilder.() -> Unit
): IrExpression = irCall(callee = sRunSymbol, type = type).apply {
	putTypeArgument(0, type)
	putValueArgument(0,
		irLambdaExpression(
			returnType = type.toKotlinType(),
			type = context.builtIns.getFunction(0).defaultType.replace(listOf(TypeProjectionImpl(type.toKotlinType()))).toIrType()
		) { body() }
	)
}


fun IrExpression.asLambdaFunction(): IrSimpleFunction? = when {
	this is IrFunctionExpression -> function
	this is IrBlock && origin == IrStatementOrigin.LAMBDA -> statements.first() as IrSimpleFunction
	else -> null
}

fun IrType.replaceAnnotations(newAnnotations: List<IrConstructorCall>): IrType = when(this) {
	is IrSimpleType -> IrSimpleTypeImpl(originalKotlinType, classifier, hasQuestionMark, arguments, newAnnotations, abbreviation)
	is IrErrorType -> IrErrorTypeImpl(originalKotlinType, newAnnotations, (this as? IrTypeBase)?.variance
		?: Variance.INVARIANT)
	is IrDynamicType -> IrDynamicTypeImpl(originalKotlinType, newAnnotations, (this as? IrTypeBase)?.variance
		?: Variance.INVARIANT)
	else -> error("unexpected type")
}

fun IrFunction.toFunctionalType() =
	context.builtIns.getFunction(valueParameters.size.let { if(extensionReceiverParameter == null) it else it + 1 })
		.defaultType
		.replace(valueParameters.map { TypeProjectionImpl(it.type.toKotlinType()) } + TypeProjectionImpl(returnType.toKotlinType())) // just defined type?


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

fun ClassDescriptor.toValueParameter(parameterName: String, parameterIndex: Int = UNDEFINED_PARAMETER_INDEX) =
	IrValueParameterBuilder().apply {
		type = defaultType.toIrType()
		name = Name.guessByFirstCharacter(parameterName)
		index = parameterIndex
	}.build()

fun ClassDescriptor.toReceiverParameter() =
	toValueParameter("<this>")


private fun copyDescriptor(descriptor: FunctionDescriptor) = when(descriptor) {
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

fun IrFunction.copyLight(
	isInline: Boolean = this.isInline,
	modality: Modality = descriptor.modality
): IrSimpleFunction {
	// TODO(lmr): use deepCopy instead?
	val descriptor = descriptor
	val newDescriptor = copyDescriptor(descriptor)
	
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
