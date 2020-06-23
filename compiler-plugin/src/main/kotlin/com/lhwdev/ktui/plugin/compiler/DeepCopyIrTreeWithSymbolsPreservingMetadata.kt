package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
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

