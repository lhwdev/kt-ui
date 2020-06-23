package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.irFunctionExpression
import com.lhwdev.ktui.plugin.compiler.util.scope
import com.lhwdev.ktui.plugin.compiler.util.symbol
import com.lhwdev.ktui.plugin.compiler.util.toIrType
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.impl.IrTypeAbbreviationImpl
import org.jetbrains.kotlin.ir.types.impl.makeTypeProjection
import org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.util.TypeRemapper
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.types.TypeProjection
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.replace
import kotlin.system.measureTimeMillis


fun WidgetTypeTransformer() = uiIrPhase("WidgetTypeTransformer") {
	val symbolRemapper = DeepCopySymbolRemapper()
	val typeRemapper = WidgetTypeRemapper(pluginContext, symbolRemapper)
	val transformer = DeepCopyIrTreeWithSymbolsPreservingMetadata(context, symbolRemapper, typeRemapper, context.typeTranslator)
	measureTimeMillis { target.acceptVoid(symbolRemapper) }.withLog { "remapping symbols took $it ms" }
	typeRemapper.deepCopy = transformer
	target.transformChildrenVoid(transformer)
	target.patchDeclarationParents() // necessary: if not exist, causes error like 'Parent not initialized: org.jetbrains.kotlin.ir.declarations.impl.IrConstructorImpl@34e0f4d3'
	
	target.transformChildrenVoid(object : IrElementTransformerVoid() {
		override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
			for((index, argument) in expression.valueArguments.withIndex()) {
				if(argument == null) continue
				val lambda = argument.asLambdaFunction() ?: continue
				if(lambda.widgetMarker != null)
					expression.putValueArgument(index, when(argument) {
						is IrFunctionExpression -> argument.scope.irFunctionExpression(argument.type.mapToWidget(), argument.function, argument.origin)
						is IrBlock -> IrBlockImpl(argument.startOffset, argument.endOffset, argument.type.mapToWidget(), argument.origin, argument.statements)
						else -> error("unknown type of argument $argument")
					})
			}
			return super.visitFunctionAccess(expression)
		}
	})
}

fun IrType.mapToWidget() = (replaceAnnotations(
	annotations + IrConstructorCallImpl(
		UNDEFINED_OFFSET, UNDEFINED_OFFSET,
		UiLibraryDescriptors.widget.defaultType.toIrType(), UiLibraryDescriptors.widget.constructors.single().symbol,
		0, 0, 0
	)
) as IrSimpleType).convertToWidgetType()


fun IrSimpleType.convertToWidgetType(): IrSimpleType {
	val oldArguments = toKotlinType().arguments
	val newArguments = mutableListOf<TypeProjection>().apply {
		val realParams = oldArguments.dropLast(1)
		addAll(realParams)
		add(TypeProjectionImpl(UiLibraryDescriptors.buildScope.defaultType))
		val changedCount = widgetChangedParamsCount(realParams.size + 1)
		for(i in 0 until changedCount) add(TypeProjectionImpl(context.builtIns.intType))
		add(oldArguments.last())
	}
	
	return context.builtIns.getFunction(newArguments.size - 1).defaultType
		.replace(newArguments)
		.toIrType()
		.replaceAnnotations(annotations)
		.withHasQuestionMark(hasQuestionMark) as IrSimpleType
}

class WidgetTypeRemapper(
	private val context: IrPluginContext,
	private val symbolRemapper: SymbolRemapper
) : TypeRemapper {
	lateinit var deepCopy: IrElementTransformerVoid
	
	override fun enterScope(irTypeParametersContainer: IrTypeParametersContainer) {
	}
	
	override fun leaveScope() {
	}
	
	override fun remapType(type: IrType): IrType {
		if(type !is IrSimpleType) return type
		
		if(!type.isFunction()) return underlyingRemapType(type)
		
		val isWidget = type.isWidget()
		if(!isWidget) return underlyingRemapType(type)
		
		val transformedType = type.convertToWidgetType()
		
		return underlyingRemapType(transformedType).withLog { "transformed: " + it.renderReadable() }
	}
	
	private fun underlyingRemapType(type: IrSimpleType): IrType = IrSimpleTypeImpl(
		null,
		symbolRemapper.getReferencedClassifier(type.classifier),
		type.hasQuestionMark,
		type.arguments.map { remapTypeArgument(it) },
		type.annotations.map { it.transform(deepCopy, null) as IrConstructorCall },
		type.abbreviation?.remapTypeAbbreviation()
	)
	
	private fun remapTypeArgument(typeArgument: IrTypeArgument): IrTypeArgument =
		if(typeArgument is IrTypeProjection)
			makeTypeProjection(this.remapType(typeArgument.type), typeArgument.variance)
		else
			typeArgument
	
	private fun IrTypeAbbreviation.remapTypeAbbreviation() = IrTypeAbbreviationImpl(
		symbolRemapper.getReferencedTypeAlias(typeAlias),
		hasQuestionMark,
		arguments.map { remapTypeArgument(it) },
		annotations
	)
}
