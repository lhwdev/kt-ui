package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptorImpl
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.makeTypeProjection
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.util.TypeRemapper
import org.jetbrains.kotlin.ir.util.TypeTranslator
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeProjection
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.replace


fun WidgetTypeTransformer() =
	uiIrPhase("WidgetTypeTransformer") { target ->
		// should we deepcopy these all things?
//	val symbolRemapper = DeepCopySymbolRemapper()
//	moduleFragment.acceptChildren(symbolRemapper, null)
//	log3((pluginContext.symbolTable.unboundClasses + pluginContext.symbolTable.unboundConstructors + pluginContext.symbolTable.unboundEnumEntries + pluginContext.symbolTable.unboundFields + pluginContext.symbolTable.unboundProperties + pluginContext.symbolTable.unboundSimpleFunctions + pluginContext.symbolTable.unboundTypeAliases + pluginContext.symbolTable.unboundTypeParameters).joinToString { it.descriptor.name.toString() })
//	val remapper = WidgetTypeRemapper(pluginContext, symbolRemapper, pluginContext.typeTranslator, moduleFragment.descriptor.resolveTopLevelClass(BUILD_SCOPE, NoLookupLocation.FROM_BACKEND)!!)
//	val realTransformer = DeepCopyIrTreeWithSymbolsPreservingMetadata(pluginContext, symbolRemapper, remapper, pluginContext.typeTranslator)
//	val transformer = object : IrElementTransformerVoid() {
//		override fun visitFile(declaration: IrFile): IrFile {
//			declaration.transformChildrenVoid(realTransformer) // temp patch
//			return super.visitFile(declaration)
//		}
//	}
////	val transformer = DeepCopyIrTreeWithSymbols(symbolRemapper, remapper)
//	remapper.deepCopy = transformer
//
//	target.transform(transformer, null)
//	target.patchDeclarationParents()
		
		val symbolRemapper = LazySymbolDeepCopyRemapper()
		val typeRemapper = WidgetTypeRemapper(pluginContext, symbolRemapper, pluginContext.typeTranslator, moduleFragment.descriptor.resolveTopLevelClass(BUILD_SCOPE, NoLookupLocation.FROM_BACKEND)!!, AnnotationDescriptorImpl(moduleFragment.descriptor.resolveTopLevelClass(WIDGET, NoLookupLocation.FROM_BACKEND)!!.defaultType, emptyMap(), SourceElement.NO_SOURCE))
		val transformer = IrTreeTypeTransformerPreservingMetadata(symbolRemapper, typeRemapper)
		target.transform(transformer, null)
//		target.patchDeclarationParents()
	}

class WidgetTypeRemapper(
	private val context: IrPluginContext,
	private val symbolRemapper: SymbolRemapper,
	private val typeTranslator: TypeTranslator,
	private val buildScopeTypeDescriptor: ClassDescriptor,
	private val widgetAnnotation: AnnotationDescriptor
) : TypeRemapper {
//	lateinit var deepCopy: IrElementTransformerVoid
	
	override fun enterScope(irTypeParametersContainer: IrTypeParametersContainer) {
	}
	
	override fun leaveScope() {
	}
	
	private fun KotlinType.toIrType(): IrType = typeTranslator.translateType(this)
	
	override fun remapType(type: IrType): IrType {
		if(type !is IrSimpleType) return type
		type.classifierOrNull?.tryBind()
		type.annotations.forEach { it.symbol.tryBind() }
		if(!type.isFunction()) return underlyingRemapType(type)
		log5("do transform for ${type.renderReadable()}")
//		symbolRemapper.dump()
//		if(!type.classifier.isBound)
//			return underlyingRemapType(type).withLog { "UNBOUNDED" }
		val isWidget = type.isWidget()
		val isWidgetUtil = type.isWidgetUtil()
		if(!isWidget && !isWidgetUtil) return underlyingRemapType(type)
//		if (!shouldTransform) return underlyingRemapType(type)
		val oldArguments = type.toKotlinType().arguments
		val newArguments = mutableListOf<TypeProjection>().apply {
			addAll(oldArguments.subList(0, oldArguments.size - 1))
			add(TypeProjectionImpl(buildScopeTypeDescriptor.defaultType))
			if(isWidget) add(TypeProjectionImpl(context.builtIns.longType))
			add(oldArguments.last())
		}
		val transformedType = context
			.builtIns
			.getFunction(oldArguments.size + 1) // return type is an argument, so this is n + 1
			.defaultType
			.replace(newArguments).withLog { it.toString() }
			.replaceAnnotations(Annotations.create(listOf(widgetAnnotation))) // TODO
			.toIrType()
			.withHasQuestionMark(type.hasQuestionMark) as IrSimpleType
		
		return underlyingRemapType(transformedType).withLog { "transformed: " + it.renderReadable() }
	}
	
	private fun underlyingRemapType(type: IrSimpleType): IrType = type
//	{
//		return IrSimpleTypeImpl(
//			null,
//			symbolRemapper.getReferencedClassifier(context.symbolTable.referenceClassifier(type.classifier.descriptor)).also { log5("URT: ${type.classifier.isBound} ${it.isBound}") },
//			type.hasQuestionMark,
//			type.arguments.map { remapTypeArgument(it) },
//			type.annotations.map { it.transform(deepCopy, null) as IrConstructorCall },
//			type.abbreviation?.remapTypeAbbreviation()
//		)
//	}
	
	private fun remapTypeArgument(typeArgument: IrTypeArgument): IrTypeArgument =
		if(typeArgument is IrTypeProjection)
			makeTypeProjection(this.remapType(typeArgument.type), typeArgument.variance)
		else
			typeArgument
	
	private fun IrTypeAbbreviation.remapTypeAbbreviation() = this
//		IrTypeAbbreviationImpl(
//			symbolRemapper.getReferencedTypeAlias(typeAlias),
//			hasQuestionMark,
//			arguments.map { remapTypeArgument(it) },
//			annotations
//		)
}
