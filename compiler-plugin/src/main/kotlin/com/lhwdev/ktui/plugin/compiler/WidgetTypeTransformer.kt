package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.UiLibrary.WIDGET
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptorImpl
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.impl.IrTypeAbbreviationImpl
import org.jetbrains.kotlin.ir.types.impl.makeTypeProjection
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
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
		val typeRemapper = WidgetTypeRemapper(pluginContext, symbolRemapper, pluginContext.typeTranslator, UiLibraryDescriptors.buildScope, AnnotationDescriptorImpl(moduleFragment.descriptor.resolveTopLevelClass(WIDGET, NoLookupLocation.FROM_BACKEND)!!.defaultType, emptyMap(), SourceElement.NO_SOURCE))
		val transformer = IrTreeTypeTransformerPreservingMetadata(symbolRemapper, typeRemapper)
		typeRemapper.deepCopy = transformer
		target.transform(transformer, null)
		target.patchDeclarationParents()
	}

class WidgetTypeRemapper(
	private val context: IrPluginContext,
	private val symbolRemapper: SymbolRemapper,
	private val typeTranslator: TypeTranslator,
	private val buildScopeTypeDescriptor: ClassDescriptor,
	private val widgetAnnotation: AnnotationDescriptor
) : TypeRemapper {
	lateinit var deepCopy: IrElementTransformerVoid
	
	override fun enterScope(irTypeParametersContainer: IrTypeParametersContainer) {
	}
	
	override fun leaveScope() {
	}
	
	private fun KotlinType.toIrType(): IrType = typeTranslator.translateType(this)
	
	override fun remapType(type: IrType): IrType {
		if(type !is IrSimpleType) return type
		
		if(!type.isFunction()) return underlyingRemapType(type)
		
		val isWidget = type.isWidget()
		if(!isWidget) return underlyingRemapType(type)
		
		val oldArguments = type.toKotlinType().arguments
		val newArguments = mutableListOf<TypeProjection>().apply {
			addAll(oldArguments.dropLast(1))
			add(TypeProjectionImpl(buildScopeTypeDescriptor.defaultType))
			add(TypeProjectionImpl(context.builtIns.longType))
			add(oldArguments.last())
		}
		
		val transformedType = context.builtIns.getFunction(newArguments.size - 1).defaultType
			.replace(newArguments)
			.let { it.replaceAnnotations(Annotations.create(it.annotations.filter { annotation -> annotation.fqName != WIDGET })) }
			.toIrType()
			.withHasQuestionMark(type.hasQuestionMark) as IrSimpleType
		
		return underlyingRemapType(transformedType).withLog { "transformed: " + it.renderReadable() }
	}
	
	private fun underlyingRemapType(type: IrSimpleType): IrType = IrSimpleTypeImpl(
		null,
		symbolRemapper.getReferencedClassifier(context.symbolTable.referenceClassifier(type.classifier.descriptor)),
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
