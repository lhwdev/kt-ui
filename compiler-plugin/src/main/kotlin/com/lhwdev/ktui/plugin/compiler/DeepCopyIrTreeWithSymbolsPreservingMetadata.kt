package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.builtins.isFunctionType
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.types.KotlinType


class DeepCopyIrTreeWithSymbolsPreservingMetadata(
	private val context: IrPluginContext,
	private val symbolRemapper: DeepCopySymbolRemapper,
	private val typeRemapper: TypeRemapper,
	private val typeTranslator: TypeTranslator,
	symbolRenamer: SymbolRenamer = SymbolRenamer.DEFAULT
) : DeepCopyIrTreeWithSymbols(symbolRemapper, typeRemapper, symbolRenamer) {
	override fun visitClass(declaration: IrClass): IrClass {
		return super.visitClass(declaration).also { it.copyMetadataFrom(declaration) }
	}
	
	override fun visitFunction(declaration: IrFunction): IrStatement {
		return super.visitFunction(declaration).also { it.copyMetadataFrom(declaration) }
	}
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction): IrSimpleFunction {
		return super.visitSimpleFunction(declaration).also {
			it.correspondingPropertySymbol = declaration.correspondingPropertySymbol
			it.copyMetadataFrom(declaration)
		}
	}
	
	override fun visitField(declaration: IrField): IrField {
		return super.visitField(declaration).also {
			(it as IrFieldImpl).metadata = declaration.metadata
		}
	}
	
	override fun visitProperty(declaration: IrProperty): IrProperty {
		return super.visitProperty(declaration).also { it.copyMetadataFrom(declaration) }
	}
	
	override fun visitFile(declaration: IrFile): IrFile {
		return super.visitFile(declaration).also {
			if(it is IrFileImpl) {
				it.metadata = declaration.metadata
			}
		}
	}
	
	override fun visitCall(expression: IrCall): IrCall {
		expression.symbol.tryBind()
		val ownerFn = expression.symbol.owner as? IrSimpleFunction
		val containingClass = expression.symbol.descriptor.containingDeclaration as? ClassDescriptor
		// Any virtual calls on widget functions we want to make sure we update the call to
		// the right function base class (of n+1 arity). The most often virtual call to make on
		// a function instance is `invoke`, which we *already* do in the ComposeParamTransformer.
		// There are others that can happen though as well, such as `equals` and `hashCode`. In this
		// case, we want to update those calls as well.
		if(
			ownerFn != null &&
			ownerFn.origin == IrDeclarationOrigin.FAKE_OVERRIDE &&
			containingClass != null &&
			containingClass.defaultType.isFunctionType &&
			expression.dispatchReceiver?.type?.isWidget() == true
		) {
			val typeArguments = containingClass.defaultType.arguments
			val newFnClass = context.symbolTable.referenceClass(context.builtIns
				.getFunction(typeArguments.size))
			val newDescriptor = newFnClass
				.descriptor
				.unsubstitutedMemberScope
				.findFirstFunction(ownerFn.name.identifier) { true }
			var newFn: IrSimpleFunction = IrFunctionImpl(
				ownerFn.startOffset,
				ownerFn.endOffset,
				ownerFn.origin,
				newDescriptor,
				expression.type
			)
			symbolRemapper.visitSimpleFunction(newFn)
			newFn = super.visitSimpleFunction(newFn).also { fn ->
				newFnClass.tryBind()
				fn.parent = newFnClass.owner
				fn.overriddenSymbols = ownerFn.overriddenSymbols
				fn.dispatchReceiverParameter = ownerFn.dispatchReceiverParameter
				fn.extensionReceiverParameter = ownerFn.extensionReceiverParameter
				newDescriptor.valueParameters.forEach { p ->
					fn.addValueParameter(p.name.identifier, p.type.toIrType())
				}
				fn.patchDeclarationParents(fn.parent)
				assert(fn.body == null) { "expected body to be null" }
			}
			val newCallee = symbolRemapper.getReferencedSimpleFunction(newFn.symbol)
			return shallowCopyCall(expression, newCallee).apply {
				copyRemappedTypeArgumentsFrom(expression)
				transformValueArguments(expression)
			}
		}
		/*// If we are calling an external function, we want to "remap" the types of its signature
		// as well, since if it is @Widget it will have its unmodified signature. These
		// functions won't be traversed by default by the DeepCopyIrTreeWithSymbols so we have to
		// do it here.
		//
		// When an external declaration for a property getter/setter is transformed, we need to
		// also transform the corresponding property so that we maintain the relationship
		// `getterFun.correspondingPropertySymbol.owner.getter == getterFun`. If we do not
		// maintain this relationship inline class getters will be incorrectly compiled.
		if(
			ownerFn != null &&
			ownerFn.origin == IrDeclarationOrigin.IR_EXTERNAL_DECLARATION_STUB
		) {
			if(ownerFn.correspondingPropertySymbol != null) {
				val property = ownerFn.correspondingPropertySymbol!!.owner
				symbolRemapper.visitProperty(property)
				super.visitProperty(property).also {
					it.getter?.correspondingPropertySymbol = it.symbol
					it.setter?.correspondingPropertySymbol = it.symbol
					it.parent = ownerFn.parent
					it.patchDeclarationParents(it.parent)
				}
			} else {
				symbolRemapper.visitSimpleFunction(ownerFn)
				super.visitSimpleFunction(ownerFn).also {
					it.parent = ownerFn.parent
					it.correspondingPropertySymbol = ownerFn.correspondingPropertySymbol
					it.patchDeclarationParents(it.parent)
				}
			}
			val newCallee = symbolRemapper.getReferencedSimpleFunction(ownerFn.symbol)
			return shallowCopyCall(expression, newCallee).apply {
				copyRemappedTypeArgumentsFrom(expression)
				transformValueArguments(expression)
			}
		}*/
		return super.visitCall(expression)
	}
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols, except with newCallee as a parameter */
	private fun shallowCopyCall(expression: IrCall, newCallee: IrSimpleFunctionSymbol): IrCall {
		return IrCallImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			newCallee,
			expression.typeArgumentsCount,
			expression.valueArgumentsCount,
			mapStatementOrigin(expression.origin),
			symbolRemapper.getReferencedClassOrNull(expression.superQualifierSymbol)
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
		}.copyAttributes(expression)
	}
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols */
	private fun IrMemberAccessExpression.copyRemappedTypeArgumentsFrom(
		other: IrMemberAccessExpression
	) {
		assert(typeArgumentsCount == other.typeArgumentsCount) {
			"Mismatching type arguments: $typeArgumentsCount vs ${other.typeArgumentsCount} "
		}
		for(i in 0 until typeArgumentsCount) {
			putTypeArgument(i, other.getTypeArgument(i)?.remapType())
		}
	}
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols */
	private fun <T : IrMemberAccessExpression> T.transformValueArguments(original: T) {
		transformReceiverArguments(original)
		for(i in 0 until original.valueArgumentsCount) {
			putValueArgument(i, original.getValueArgument(i)?.transform())
		}
	}
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols */
	private fun <T : IrMemberAccessExpression> T.transformReceiverArguments(original: T): T =
		apply {
			dispatchReceiver = original.dispatchReceiver?.transform()
			extensionReceiver = original.extensionReceiver?.transform()
		}
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols */
	private inline fun <reified T : IrElement> T.transform() =
		transform(this@DeepCopyIrTreeWithSymbolsPreservingMetadata, null) as T
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols */
	private fun IrType.remapType() = typeRemapper.remapType(this)
	
	/* copied verbatim from DeepCopyIrTreeWithSymbols */
	private fun mapStatementOrigin(origin: IrStatementOrigin?) = origin
	private fun IrElement.copyMetadataFrom(owner: IrMetadataSourceOwner) {
		when(this) {
			is IrPropertyImpl -> metadata = owner.metadata
			is IrFunctionBase<*> -> metadata = owner.metadata
			is IrClassImpl -> metadata = owner.metadata
		}
	}
	
	private fun IrType.isWidget(): Boolean {
		return annotations.hasAnnotation(UiLibrary.WIDGET)
	}
	
	private fun KotlinType.toIrType(): IrType = typeTranslator.translateType(this)
}

