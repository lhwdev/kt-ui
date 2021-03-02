package com.lhwdev.ktui.plugin.compiler.util

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.symbols.impl.*
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import java.util.HashMap


inline fun <reified T : IrElement> T.deepCopyComponentWithSymbol(
	initialParent: IrDeclarationParent? = null,
	createCopier: (DeepCopyComponentSymbolRemapper, TypeRemapper) -> IrDeepCopyComponentTransformer =
		::IrDeepCopyComponentTransformerImpl
): T {
	val symbolRemapper = DeepCopyComponentSymbolRemapper()
	acceptVoid(symbolRemapper)
	val typeRemapper = DeepCopyTypeRemapper(symbolRemapper)
	return transform(createCopier(symbolRemapper, typeRemapper), null).patchDeclarationParents(initialParent) as T
}

interface IrDeepCopyComponentTransformer : IrComponentTransformer<Nothing?> {
	val symbolRemapper: DeepCopyComponentSymbolRemapper
	val typeRemapper: TypeRemapper
	val symbolRenamer: SymbolRenamer
	
	fun mapDeclarationOrigin(origin: IrDeclarationOrigin) = origin
	fun mapStatementOrigin(origin: IrStatementOrigin?) = origin
}


@OptIn(ObsoleteDescriptorBasedAPI::class)
open class IrDeepCopyComponentTransformerImpl(
	override val symbolRemapper: DeepCopyComponentSymbolRemapper,
	override val typeRemapper: TypeRemapper,
	override val symbolRenamer: SymbolRenamer = SymbolRenamer.DEFAULT,
	private val copyMetadata: Boolean = false
) : IrDeepCopyComponentTransformer, IrElementTransformerVoid() {
	constructor(symbolRemapper: DeepCopyComponentSymbolRemapper, typeRemapper: TypeRemapper) : this(
		symbolRemapper,
		typeRemapper,
		SymbolRenamer.DEFAULT
	)
	
	override fun mapDeclarationOrigin(origin: IrDeclarationOrigin) = origin
	override fun mapStatementOrigin(origin: IrStatementOrigin?) = origin
	
	protected inline fun <reified T : IrElement> T.transform() =
		transform(this@IrDeepCopyComponentTransformerImpl, null) as T
	
	protected inline fun <reified T : IrElement> List<T>.transform() =
		map { it.transform() }
	
	protected inline fun <reified T : IrElement> List<T>.transformTo(destination: MutableList<T>) =
		mapTo(destination) { it.transform() }
	
	protected fun <T : IrDeclarationContainer> T.transformDeclarationsTo(destination: T) =
		declarations.transformTo(destination.declarations)
	
	protected fun IrType.remapType() = typeRemapper.remapType(this)
	
	override fun visitElement(element: IrElement): IrElement =
		throw IllegalArgumentException("Unsupported element type: $element")
	
	override fun visitModuleFragment(declaration: IrModuleFragment): IrModuleFragment = IrModuleFragmentImpl(
		declaration.descriptor,
		declaration.irBuiltins,
		declaration.files.transform()
	)
	
	override fun visitExternalPackageFragment(declaration: IrExternalPackageFragment): IrExternalPackageFragment =
		IrExternalPackageFragmentImpl(
			symbolRemapper.getDeclaredExternalPackageFragment(declaration.symbol),
			symbolRenamer.getExternalPackageFragmentName(declaration.symbol)
		).apply {
			declaration.transformDeclarationsTo(this)
		}
	
	override fun visitFile(declaration: IrFile): IrFile = IrFileImpl(
		declaration.fileEntry,
		symbolRemapper.getDeclaredFile(declaration.symbol),
		symbolRenamer.getFileName(declaration.symbol)
	).apply {
		transformAnnotations(declaration)
		declaration.transformDeclarationsTo(this)
		if(copyMetadata) metadata = declaration.metadata
	}
	
	override fun visitDeclaration(declaration: IrDeclarationBase): IrStatement =
		throw IllegalArgumentException("Unsupported declaration type: $declaration")
	
	override fun visitScript(declaration: IrScript): IrScript = IrScriptImpl(
		symbolRemapper.getDeclaredScript(declaration.symbol),
		declaration.name
	).also { scriptCopy ->
		scriptCopy.thisReceiver = declaration.thisReceiver.transform()
		declaration.statements.mapTo(scriptCopy.statements) { it.transform() }
		scriptCopy.explicitCallParameters = declaration.explicitCallParameters.map { it.transform() }
		scriptCopy.implicitReceiversParameters = declaration.implicitReceiversParameters.map { it.transform() }
		scriptCopy.providedProperties = declaration.providedProperties.map { it.first.transform() to it.second }
	}
	
	override fun visitClass(declaration: IrClass) = declaration.factory.createClass(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredClass(declaration.symbol),
		symbolRenamer.getClassName(declaration.symbol),
		declaration.kind,
		declaration.visibility,
		declaration.modality,
		isCompanion = declaration.isCompanion,
		isInner = declaration.isInner,
		isData = declaration.isData,
		isExternal = declaration.isExternal,
		isInline = declaration.isInline,
		isExpect = declaration.isExpect,
		isFun = declaration.isFun
	).apply {
		transformAnnotations(declaration)
		copyTypeParametersFrom(declaration)
		superTypes = declaration.superTypes.map {
			it.remapType()
		}
		thisReceiver = declaration.thisReceiver?.transform()
		declaration.transformDeclarationsTo(this)
		if(copyMetadata) metadata = declaration.metadata
	}.copyAttributes(declaration)
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction) = declaration.factory.createFunction(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredFunction(declaration.symbol),
		symbolRenamer.getFunctionName(declaration.symbol),
		declaration.visibility,
		declaration.modality,
		declaration.returnType,
		isInline = declaration.isInline,
		isExternal = declaration.isExternal,
		isTailrec = declaration.isTailrec,
		isSuspend = declaration.isSuspend,
		isOperator = declaration.isOperator,
		isInfix = declaration.isInfix,
		isExpect = declaration.isExpect,
		isFakeOverride = declaration.isFakeOverride,
		containerSource = declaration.containerSource,
	).apply {
		overriddenSymbols = declaration.overriddenSymbols.map {
			symbolRemapper.getReferencedFunction(it) as IrSimpleFunctionSymbol
		}
		copyAttributes(declaration)
		transformFunctionChildren(declaration)
		if(copyMetadata) {
			correspondingPropertySymbol = declaration.correspondingPropertySymbol
			metadata = declaration.metadata
		}
	}
	
	override fun visitConstructor(declaration: IrConstructor) = declaration.factory.createConstructor(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredConstructor(declaration.symbol),
		declaration.name,
		declaration.visibility,
		declaration.returnType,
		isInline = declaration.isInline,
		isExternal = declaration.isExternal,
		isPrimary = declaration.isPrimary,
		isExpect = declaration.isExpect,
		containerSource = declaration.containerSource,
	).apply {
		transformFunctionChildren(declaration)
		if(copyMetadata) metadata = declaration.metadata
	}
	
	private fun <T : IrFunction> T.transformFunctionChildren(declaration: T): T = apply {
		transformAnnotations(declaration)
		copyTypeParametersFrom(declaration)
		typeRemapper.withinScope(this) {
			dispatchReceiverParameter = declaration.dispatchReceiverParameter?.transform()
			extensionReceiverParameter = declaration.extensionReceiverParameter?.transform()
			returnType = typeRemapper.remapType(declaration.returnType)
			valueParameters = declaration.valueParameters.transform()
			body = declaration.body?.transform()
		}
	}
	
	protected fun IrMutableAnnotationContainer.transformAnnotations(declaration: IrAnnotationContainer) {
		annotations = declaration.annotations.transform()
	}
	
	override fun visitProperty(declaration: IrProperty) = declaration.factory.createProperty(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredProperty(declaration.symbol),
		declaration.name,
		declaration.visibility,
		declaration.modality,
		isVar = declaration.isVar,
		isConst = declaration.isConst,
		isLateinit = declaration.isLateinit,
		isDelegated = declaration.isDelegated,
		isExternal = declaration.isExternal,
		isExpect = declaration.isExpect,
		containerSource = declaration.containerSource,
	).apply {
		transformAnnotations(declaration)
		copyAttributes(declaration)
		this.backingField = declaration.backingField?.transform()?.also {
			it.correspondingPropertySymbol = symbol
		}
		this.getter = declaration.getter?.transform()?.also {
			it.correspondingPropertySymbol = symbol
		}
		this.setter = declaration.setter?.transform()?.also {
			it.correspondingPropertySymbol = symbol
		}
		if(copyMetadata) metadata = declaration.metadata
	}
	
	override fun visitField(declaration: IrField) = declaration.factory.createField(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredField(declaration.symbol),
		symbolRenamer.getFieldName(declaration.symbol),
		declaration.type.remapType(),
		declaration.visibility,
		isFinal = declaration.isFinal,
		isExternal = declaration.isExternal,
		isStatic = declaration.isStatic,
	).apply {
		transformAnnotations(declaration)
		initializer = declaration.initializer?.transform()
		if(copyMetadata) metadata = declaration.metadata
	}
	
	override fun visitLocalDelegatedProperty(declaration: IrLocalDelegatedProperty) =
		declaration.factory.createLocalDelegatedProperty(
			declaration.startOffset, declaration.endOffset,
			mapDeclarationOrigin(declaration.origin),
			symbolRemapper.getDeclaredLocalDelegatedProperty(declaration.symbol),
			declaration.name,
			declaration.type.remapType(),
			declaration.isVar
		).apply {
			transformAnnotations(declaration)
			delegate = declaration.delegate.transform()
			getter = declaration.getter.transform()
			setter = declaration.setter?.transform()
		}
	
	override fun visitEnumEntry(declaration: IrEnumEntry) = declaration.factory.createEnumEntry(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredEnumEntry(declaration.symbol),
		symbolRenamer.getEnumEntryName(declaration.symbol)
	).apply {
		transformAnnotations(declaration)
		correspondingClass = declaration.correspondingClass?.transform()
		initializerExpression = declaration.initializerExpression?.transform()
	}
	
	override fun visitAnonymousInitializer(declaration: IrAnonymousInitializer) =
		declaration.factory.createAnonymousInitializer(
			declaration.startOffset, declaration.endOffset,
			mapDeclarationOrigin(declaration.origin),
			IrAnonymousInitializerSymbolImpl(declaration.descriptor)
		).apply {
			body = declaration.body.transform()
		}
	
	override fun visitVariable(declaration: IrVariable): IrVariable = IrVariableImpl(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredVariable(declaration.symbol),
		symbolRenamer.getVariableName(declaration.symbol),
		declaration.type.remapType(),
		declaration.isVar,
		declaration.isConst,
		declaration.isLateinit
	).apply {
		transformAnnotations(declaration)
		initializer = declaration.initializer?.transform()
	}
	
	override fun visitTypeParameter(declaration: IrTypeParameter): IrTypeParameter =
		copyTypeParameter(declaration).apply {
			// TODO type parameter scopes?
			superTypes = declaration.superTypes.map { it.remapType() }
		}
	
	private fun copyTypeParameter(declaration: IrTypeParameter) = declaration.factory.createTypeParameter(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredTypeParameter(declaration.symbol),
		symbolRenamer.getTypeParameterName(declaration.symbol),
		declaration.index,
		declaration.isReified,
		declaration.variance
	).apply {
		transformAnnotations(declaration)
	}
	
	protected fun IrTypeParametersContainer.copyTypeParametersFrom(other: IrTypeParametersContainer) {
		this.typeParameters = other.typeParameters.map {
			copyTypeParameter(it)
		}
		
		typeRemapper.withinScope(this) {
			for((thisTypeParameter, otherTypeParameter) in this.typeParameters.zip(other.typeParameters)) {
				thisTypeParameter.superTypes = otherTypeParameter.superTypes.map {
					typeRemapper.remapType(it)
				}
			}
		}
	}
	
	override fun visitValueParameter(declaration: IrValueParameter) = declaration.factory.createValueParameter(
		declaration.startOffset, declaration.endOffset,
		mapDeclarationOrigin(declaration.origin),
		symbolRemapper.getDeclaredValueParameter(declaration.symbol),
		symbolRenamer.getValueParameterName(declaration.symbol),
		declaration.index,
		declaration.type.remapType(),
		declaration.varargElementType?.remapType(),
		declaration.isCrossinline,
		declaration.isNoinline,
		declaration.isHidden,
		declaration.isAssignable
	).apply {
		transformAnnotations(declaration)
		defaultValue = declaration.defaultValue?.transform()
	}
	
	override fun visitTypeAlias(declaration: IrTypeAlias) = declaration.factory.createTypeAlias(
		declaration.startOffset, declaration.endOffset,
		symbolRemapper.getDeclaredTypeAlias(declaration.symbol),
		symbolRenamer.getTypeAliasName(declaration.symbol),
		declaration.visibility,
		declaration.expandedType.remapType(),
		declaration.isActual,
		mapDeclarationOrigin(declaration.origin)
	).apply {
		transformAnnotations(declaration)
		copyTypeParametersFrom(declaration)
	}
	
	override fun visitBody(body: IrBody): IrBody =
		throw IllegalArgumentException("Unsupported body type: $body")
	
	override fun visitExpressionBody(body: IrExpressionBody): IrExpressionBody =
		body.factory.createExpressionBody(body.expression.transform())
	
	override fun visitBlockBody(body: IrBlockBody) = body.factory.createBlockBody(
		body.startOffset, body.endOffset,
		body.statements.map { it.transform() }
	)
	
	override fun visitSyntheticBody(body: IrSyntheticBody): IrSyntheticBody =
		IrSyntheticBodyImpl(body.startOffset, body.endOffset, body.kind)
	
	override fun visitExpression(expression: IrExpression): IrExpression =
		throw IllegalArgumentException("Unsupported expression type: $expression")
	
	override fun <T> visitConst(expression: IrConst<T>): IrConst<T> =
		expression.copy().copyAttributes(expression)
	
	override fun visitVararg(expression: IrVararg): IrVararg = IrVarargImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(), expression.varargElementType.remapType(),
		expression.elements.transform()
	).copyAttributes(expression)
	
	override fun visitSpreadElement(spread: IrSpreadElement): IrSpreadElement = IrSpreadElementImpl(
		spread.startOffset, spread.endOffset,
		spread.expression.transform()
	)
	
	override fun visitBlock(expression: IrBlock): IrBlock = if(expression is IrReturnableBlock)
		IrReturnableBlockImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			symbolRemapper.getReferencedReturnableBlock(expression.symbol),
			mapStatementOrigin(expression.origin),
			expression.statements.map { it.transform() },
			expression.inlineFunctionSymbol
		).copyAttributes(expression) else IrBlockImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		mapStatementOrigin(expression.origin),
		expression.statements.map { it.transform() }
	).copyAttributes(expression)
	
	override fun visitComposite(expression: IrComposite): IrComposite = IrCompositeImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		mapStatementOrigin(expression.origin),
		expression.statements.map { it.transform() }
	).copyAttributes(expression)
	
	override fun visitStringConcatenation(expression: IrStringConcatenation): IrStringConcatenation =
		IrStringConcatenationImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.arguments.map { it.transform() }
		).copyAttributes(expression)
	
	override fun visitGetObjectValue(expression: IrGetObjectValue): IrGetObjectValue = IrGetObjectValueImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedClass(expression.symbol)
	).copyAttributes(expression)
	
	override fun visitGetEnumValue(expression: IrGetEnumValue): IrGetEnumValue = IrGetEnumValueImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedEnumEntry(expression.symbol)
	).copyAttributes(expression)
	
	override fun visitGetValue(expression: IrGetValue): IrGetValue = IrGetValueImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedValue(expression.symbol),
		mapStatementOrigin(expression.origin)
	).copyAttributes(expression)
	
	override fun visitSetValue(expression: IrSetValue): IrSetValue = IrSetValueImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedValue(expression.symbol),
		expression.value.transform(),
		mapStatementOrigin(expression.origin)
	).copyAttributes(expression)
	
	override fun visitGetField(expression: IrGetField): IrGetField = IrGetFieldImpl(
		expression.startOffset, expression.endOffset,
		symbolRemapper.getReferencedField(expression.symbol),
		expression.type.remapType(),
		expression.receiver?.transform(),
		mapStatementOrigin(expression.origin),
		symbolRemapper.getReferencedClassOrNull(expression.superQualifierSymbol)
	).copyAttributes(expression)
	
	override fun visitSetField(expression: IrSetField): IrSetField = IrSetFieldImpl(
		expression.startOffset, expression.endOffset,
		symbolRemapper.getReferencedField(expression.symbol),
		expression.receiver?.transform(),
		expression.value.transform(),
		expression.type.remapType(),
		mapStatementOrigin(expression.origin),
		symbolRemapper.getReferencedClassOrNull(expression.superQualifierSymbol)
	).copyAttributes(expression)
	
	override fun visitCall(expression: IrCall): IrCall = shallowCopyCall(expression).apply {
		copyRemappedTypeArgumentsFrom(expression)
		transformValueArguments(expression)
	}
	
	override fun visitConstructorCall(expression: IrConstructorCall): IrConstructorCall {
		val constructorSymbol = symbolRemapper.getReferencedConstructor(expression.symbol)
		return IrConstructorCallImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			constructorSymbol,
			expression.typeArgumentsCount,
			expression.constructorTypeArgumentsCount,
			expression.valueArgumentsCount,
			mapStatementOrigin(expression.origin)
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
			transformValueArguments(expression)
		}.copyAttributes(expression)
	}
	
	private fun IrMemberAccessExpression<*>.copyRemappedTypeArgumentsFrom(other: IrMemberAccessExpression<*>) {
		assert(typeArgumentsCount == other.typeArgumentsCount) {
			"Mismatching type arguments: $typeArgumentsCount vs ${other.typeArgumentsCount} "
		}
		for(i in 0 until typeArgumentsCount) {
			putTypeArgument(i, other.getTypeArgument(i)?.remapType())
		}
	}
	
	private fun shallowCopyCall(expression: IrCall): IrCall {
		val newCallee = symbolRemapper.getReferencedSimpleFunction(expression.symbol)
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
	
	private fun <T : IrMemberAccessExpression<*>> T.transformReceiverArguments(original: T): T = apply {
		dispatchReceiver = original.dispatchReceiver?.transform()
		extensionReceiver = original.extensionReceiver?.transform()
	}
	
	private fun <T : IrMemberAccessExpression<*>> T.transformValueArguments(original: T) {
		transformReceiverArguments(original)
		for(i in 0 until original.valueArgumentsCount) {
			putValueArgument(i, original.getValueArgument(i)?.transform())
		}
	}
	
	override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall): IrDelegatingConstructorCall {
		val newConstructor = symbolRemapper.getReferencedConstructor(expression.symbol)
		return IrDelegatingConstructorCallImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			newConstructor,
			expression.typeArgumentsCount,
			expression.valueArgumentsCount
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
			transformValueArguments(expression)
		}.copyAttributes(expression)
	}
	
	override fun visitEnumConstructorCall(expression: IrEnumConstructorCall): IrEnumConstructorCall {
		val newConstructor = symbolRemapper.getReferencedConstructor(expression.symbol)
		return IrEnumConstructorCallImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			newConstructor,
			expression.typeArgumentsCount,
			expression.valueArgumentsCount
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
			transformValueArguments(expression)
		}.copyAttributes(expression)
	}
	
	override fun visitGetClass(expression: IrGetClass): IrGetClass = IrGetClassImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.argument.transform()
	).copyAttributes(expression)
	
	override fun visitFunctionReference(expression: IrFunctionReference): IrFunctionReference {
		val symbol = symbolRemapper.getReferencedFunction(expression.symbol)
		val reflectionTarget = expression.reflectionTarget?.let { symbolRemapper.getReferencedFunction(it) }
		return IrFunctionReferenceImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			symbol,
			expression.typeArgumentsCount,
			expression.valueArgumentsCount,
			reflectionTarget,
			mapStatementOrigin(expression.origin)
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
			transformValueArguments(expression)
		}.copyAttributes(expression)
	}
	
	override fun visitPropertyReference(expression: IrPropertyReference): IrPropertyReference = IrPropertyReferenceImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedProperty(expression.symbol),
		expression.typeArgumentsCount,
		expression.field?.let { symbolRemapper.getReferencedField(it) },
		expression.getter?.let { symbolRemapper.getReferencedSimpleFunction(it) },
		expression.setter?.let { symbolRemapper.getReferencedSimpleFunction(it) },
		mapStatementOrigin(expression.origin)
	).apply {
		copyRemappedTypeArgumentsFrom(expression)
		transformReceiverArguments(expression)
	}.copyAttributes(expression)
	
	override fun visitLocalDelegatedPropertyReference(expression: IrLocalDelegatedPropertyReference): IrLocalDelegatedPropertyReference =
		IrLocalDelegatedPropertyReferenceImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			symbolRemapper.getReferencedLocalDelegatedProperty(expression.symbol),
			symbolRemapper.getReferencedVariable(expression.delegate),
			symbolRemapper.getReferencedSimpleFunction(expression.getter),
			expression.setter?.let { symbolRemapper.getReferencedSimpleFunction(it) },
			mapStatementOrigin(expression.origin)
		).copyAttributes(expression)
	
	override fun visitFunctionExpression(expression: IrFunctionExpression): IrFunctionExpression =
		IrFunctionExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.function.transform(),
			mapStatementOrigin(expression.origin)!!
		).copyAttributes(expression)
	
	override fun visitClassReference(expression: IrClassReference): IrClassReference = IrClassReferenceImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedClassifier(expression.symbol),
		expression.classType.remapType()
	).copyAttributes(expression)
	
	override fun visitInstanceInitializerCall(expression: IrInstanceInitializerCall): IrInstanceInitializerCall =
		IrInstanceInitializerCallImpl(
			expression.startOffset, expression.endOffset,
			symbolRemapper.getReferencedClass(expression.classSymbol),
			expression.type.remapType()
		).copyAttributes(expression)
	
	override fun visitTypeOperator(expression: IrTypeOperatorCall): IrTypeOperatorCall = IrTypeOperatorCallImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.operator,
		expression.typeOperand.remapType(),
		expression.argument.transform()
	).copyAttributes(expression)
	
	override fun visitWhen(expression: IrWhen): IrWhen = IrWhenImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		mapStatementOrigin(expression.origin),
		expression.branches.map { it.transform() }
	).copyAttributes(expression)
	
	override fun visitBranch(branch: IrBranch): IrBranch = IrBranchImpl(
		branch.startOffset, branch.endOffset,
		branch.condition.transform(),
		branch.result.transform()
	)
	
	override fun visitElseBranch(branch: IrElseBranch): IrElseBranch = IrElseBranchImpl(
		branch.startOffset, branch.endOffset,
		branch.condition.transform(),
		branch.result.transform()
	)
	
	private val transformedLoops = HashMap<IrLoop, IrLoop>()
	
	private fun getTransformedLoop(irLoop: IrLoop): IrLoop =
		transformedLoops.getOrElse(irLoop) { getNonTransformedLoop(irLoop) }
	
	protected open fun getNonTransformedLoop(irLoop: IrLoop): IrLoop =
		throw AssertionError("Outer loop was not transformed: ${irLoop.render()}")
	
	override fun visitWhileLoop(loop: IrWhileLoop): IrWhileLoop = IrWhileLoopImpl(
		loop.startOffset,
		loop.endOffset,
		loop.type.remapType(),
		mapStatementOrigin(loop.origin)
	).also { newLoop ->
		transformedLoops[loop] = newLoop
		newLoop.label = loop.label
		newLoop.condition = loop.condition.transform()
		newLoop.body = loop.body?.transform()
	}.copyAttributes(loop)
	
	override fun visitDoWhileLoop(loop: IrDoWhileLoop): IrDoWhileLoop = IrDoWhileLoopImpl(
		loop.startOffset,
		loop.endOffset,
		loop.type.remapType(),
		mapStatementOrigin(loop.origin)
	).also { newLoop ->
		transformedLoops[loop] = newLoop
		newLoop.label = loop.label
		newLoop.condition = loop.condition.transform()
		newLoop.body = loop.body?.transform()
	}.copyAttributes(loop)
	
	override fun visitBreak(jump: IrBreak): IrBreak = IrBreakImpl(
		jump.startOffset, jump.endOffset,
		jump.type.remapType(),
		getTransformedLoop(jump.loop)
	).apply { label = jump.label }.copyAttributes(jump)
	
	override fun visitContinue(jump: IrContinue): IrContinue = IrContinueImpl(
		jump.startOffset, jump.endOffset,
		jump.type.remapType(),
		getTransformedLoop(jump.loop)
	).apply { label = jump.label }.copyAttributes(jump)
	
	override fun visitTry(aTry: IrTry): IrTry = IrTryImpl(
		aTry.startOffset, aTry.endOffset,
		aTry.type.remapType(),
		aTry.tryResult.transform(),
		aTry.catches.map { it.transform() },
		aTry.finallyExpression?.transform()
	).copyAttributes(aTry)
	
	override fun visitCatch(aCatch: IrCatch): IrCatch = IrCatchImpl(
		aCatch.startOffset, aCatch.endOffset,
		aCatch.catchParameter.transform(),
		aCatch.result.transform()
	)
	
	override fun visitReturn(expression: IrReturn): IrReturn = IrReturnImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedReturnTarget(expression.returnTargetSymbol),
		expression.value.transform()
	).copyAttributes(expression)
	
	private fun SymbolRemapper.getReferencedReturnTarget(returnTarget: IrReturnTargetSymbol) = when(returnTarget) {
		is IrFunctionSymbol -> getReferencedFunction(returnTarget)
		is IrReturnableBlockSymbol -> getReferencedReturnableBlock(returnTarget)
		else -> throw AssertionError("Unexpected return target: ${returnTarget.javaClass} $returnTarget")
	}
	
	override fun visitThrow(expression: IrThrow): IrThrow = IrThrowImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.value.transform()
	).copyAttributes(expression)
	
	override fun visitDynamicOperatorExpression(expression: IrDynamicOperatorExpression): IrDynamicOperatorExpression =
		IrDynamicOperatorExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.operator
		).apply {
			receiver = expression.receiver.transform()
			expression.arguments.mapTo(arguments) { it.transform() }
		}.copyAttributes(expression)
	
	override fun visitDynamicMemberExpression(expression: IrDynamicMemberExpression): IrDynamicMemberExpression =
		IrDynamicMemberExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.memberName,
			expression.receiver.transform()
		).copyAttributes(expression)
	
	override fun visitErrorDeclaration(declaration: IrErrorDeclaration): IrErrorDeclaration =
		declaration.factory.createErrorDeclaration(
			declaration.startOffset,
			declaration.endOffset,
			declaration.descriptor
		)
	
	override fun visitErrorExpression(expression: IrErrorExpression): IrErrorExpression = IrErrorExpressionImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.description
	).copyAttributes(expression)
	
	override fun visitErrorCallExpression(expression: IrErrorCallExpression): IrErrorCallExpression =
		IrErrorCallExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.description
		).apply {
			explicitReceiver = expression.explicitReceiver?.transform()
			expression.arguments.transformTo(arguments)
		}.copyAttributes(expression)
}


@OptIn(ObsoleteDescriptorBasedAPI::class)
open class DeepCopyComponentSymbolRemapper(
	val descriptorsRemapper: DescriptorsRemapper = DescriptorsRemapper.Default
) : IrComponentVisitorVoid, SymbolRemapper {
	
	val classes = hashMapOf<IrClassSymbol, IrClassSymbol>()
	val scripts = hashMapOf<IrScriptSymbol, IrScriptSymbol>()
	val constructors = hashMapOf<IrConstructorSymbol, IrConstructorSymbol>()
	val enumEntries = hashMapOf<IrEnumEntrySymbol, IrEnumEntrySymbol>()
	val externalPackageFragments = hashMapOf<IrExternalPackageFragmentSymbol, IrExternalPackageFragmentSymbol>()
	val fields = hashMapOf<IrFieldSymbol, IrFieldSymbol>()
	val files = hashMapOf<IrFileSymbol, IrFileSymbol>()
	val functions = hashMapOf<IrSimpleFunctionSymbol, IrSimpleFunctionSymbol>()
	val properties = hashMapOf<IrPropertySymbol, IrPropertySymbol>()
	val returnableBlocks = hashMapOf<IrReturnableBlockSymbol, IrReturnableBlockSymbol>()
	val typeParameters = hashMapOf<IrTypeParameterSymbol, IrTypeParameterSymbol>()
	val valueParameters = hashMapOf<IrValueParameterSymbol, IrValueParameterSymbol>()
	val variables = hashMapOf<IrVariableSymbol, IrVariableSymbol>()
	val localDelegatedProperties = hashMapOf<IrLocalDelegatedPropertySymbol, IrLocalDelegatedPropertySymbol>()
	val typeAliases = hashMapOf<IrTypeAliasSymbol, IrTypeAliasSymbol>()
	
	override fun visitElement(element: IrElement) {
		element.acceptChildrenVoid(this)
	}
	
	inline fun <D : DeclarationDescriptor, B : IrSymbolOwner, reified S : IrBindableSymbol<D, B>>
		remapSymbol(map: MutableMap<S, S>, owner: B, createNewSymbol: (S) -> S): S {
		val symbol = owner.symbol as S
		val newSymbol = createNewSymbol(symbol)
		map[symbol] = newSymbol
		return newSymbol
	}
	
	inline fun <D : DeclarationDescriptor, B : IrSymbolOwner, reified S : IrBindableSymbol<D, B>>
		addRemappedSymbol(map: MutableMap<S, S>, owner: B, newSymbol: S) {
		val symbol = owner.symbol as S
		map[symbol] = newSymbol
	}
	
	override fun visitComponent(component: IrComponent<*>, default: () -> Unit) {
		if(component is IrSymbolOwnerComponent<*, *>) component.remapSymbol(this)
		component.acceptChildrenVoid(this)
	}
	
	override fun visitClass(declaration: IrClass) {
		remapSymbol(classes, declaration) {
			IrClassSymbolImpl(descriptorsRemapper.remapDeclaredClass(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitScript(declaration: IrScript) {
		remapSymbol(scripts, declaration) {
			IrScriptSymbolImpl(descriptorsRemapper.remapDeclaredScript(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitConstructor(declaration: IrConstructor) {
		remapSymbol(constructors, declaration) {
			IrConstructorSymbolImpl(descriptorsRemapper.remapDeclaredConstructor(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitEnumEntry(declaration: IrEnumEntry) {
		remapSymbol(enumEntries, declaration) {
			IrEnumEntrySymbolImpl(descriptorsRemapper.remapDeclaredEnumEntry(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitExternalPackageFragment(declaration: IrExternalPackageFragment) {
		remapSymbol(externalPackageFragments, declaration) {
			IrExternalPackageFragmentSymbolImpl(descriptorsRemapper.remapDeclaredExternalPackageFragment(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitField(declaration: IrField) {
		remapSymbol(fields, declaration) {
			IrFieldSymbolImpl(descriptorsRemapper.remapDeclaredField(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitFile(declaration: IrFile) {
		remapSymbol(files, declaration) {
			IrFileSymbolImpl(descriptorsRemapper.remapDeclaredFilePackageFragment(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitSimpleFunction(declaration: IrSimpleFunction) {
		remapSymbol(functions, declaration) {
			IrSimpleFunctionSymbolImpl(descriptorsRemapper.remapDeclaredSimpleFunction(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitProperty(declaration: IrProperty) {
		remapSymbol(properties, declaration) {
			IrPropertySymbolImpl(descriptorsRemapper.remapDeclaredProperty(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitTypeParameter(declaration: IrTypeParameter) {
		remapSymbol(typeParameters, declaration) {
			IrTypeParameterSymbolImpl(descriptorsRemapper.remapDeclaredTypeParameter(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitValueParameter(declaration: IrValueParameter) {
		remapSymbol(valueParameters, declaration) {
			IrValueParameterSymbolImpl(descriptorsRemapper.remapDeclaredValueParameter(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitVariable(declaration: IrVariable) {
		remapSymbol(variables, declaration) {
			IrVariableSymbolImpl(descriptorsRemapper.remapDeclaredVariable(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitLocalDelegatedProperty(declaration: IrLocalDelegatedProperty) {
		remapSymbol(localDelegatedProperties, declaration) {
			IrLocalDelegatedPropertySymbolImpl(descriptorsRemapper.remapDeclaredLocalDelegatedProperty(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitTypeAlias(declaration: IrTypeAlias) {
		remapSymbol(typeAliases, declaration) {
			IrTypeAliasSymbolImpl(descriptorsRemapper.remapDeclaredTypeAlias(it.descriptor))
		}
		declaration.acceptChildrenVoid(this)
	}
	
	override fun visitBlock(expression: IrBlock) {
		if(expression is IrReturnableBlock) {
			remapSymbol(returnableBlocks, expression) {
				IrReturnableBlockSymbolImpl(expression.descriptor)
			}
		}
		expression.acceptChildrenVoid(this)
	}
	
	private fun <T : IrSymbol> Map<T, T>.getDeclared(symbol: T) =
		getOrElse(symbol) {
			throw IllegalArgumentException("Non-remapped symbol $symbol")
		}
	
	private fun <T : IrSymbol> Map<T, T>.getReferenced(symbol: T) =
		getOrElse(symbol) { symbol }
	
	override fun getDeclaredClass(symbol: IrClassSymbol): IrClassSymbol = classes.getDeclared(symbol)
	override fun getDeclaredScript(symbol: IrScriptSymbol): IrScriptSymbol = scripts.getDeclared(symbol)
	override fun getDeclaredFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol =
		functions.getDeclared(symbol)
	
	override fun getDeclaredProperty(symbol: IrPropertySymbol): IrPropertySymbol = properties.getDeclared(symbol)
	override fun getDeclaredField(symbol: IrFieldSymbol): IrFieldSymbol = fields.getDeclared(symbol)
	override fun getDeclaredFile(symbol: IrFileSymbol): IrFileSymbol = files.getDeclared(symbol)
	override fun getDeclaredConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol =
		constructors.getDeclared(symbol)
	
	override fun getDeclaredEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol = enumEntries.getDeclared(symbol)
	override fun getDeclaredExternalPackageFragment(symbol: IrExternalPackageFragmentSymbol): IrExternalPackageFragmentSymbol =
		externalPackageFragments.getDeclared(symbol)
	
	override fun getDeclaredVariable(symbol: IrVariableSymbol): IrVariableSymbol = variables.getDeclared(symbol)
	override fun getDeclaredTypeParameter(symbol: IrTypeParameterSymbol): IrTypeParameterSymbol =
		typeParameters.getDeclared(symbol)
	
	override fun getDeclaredValueParameter(symbol: IrValueParameterSymbol): IrValueParameterSymbol =
		valueParameters.getDeclared(symbol)
	
	override fun getDeclaredLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol =
		localDelegatedProperties.getDeclared(symbol)
	
	override fun getDeclaredTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol = typeAliases.getDeclared(symbol)
	
	override fun getReferencedClass(symbol: IrClassSymbol): IrClassSymbol = classes.getReferenced(symbol)
	override fun getReferencedScript(symbol: IrScriptSymbol): IrScriptSymbol = scripts.getReferenced(symbol)
	override fun getReferencedClassOrNull(symbol: IrClassSymbol?): IrClassSymbol? =
		symbol?.let { classes.getReferenced(it) }
	
	override fun getReferencedEnumEntry(symbol: IrEnumEntrySymbol): IrEnumEntrySymbol =
		enumEntries.getReferenced(symbol)
	
	override fun getReferencedVariable(symbol: IrVariableSymbol): IrVariableSymbol = variables.getReferenced(symbol)
	override fun getReferencedLocalDelegatedProperty(symbol: IrLocalDelegatedPropertySymbol): IrLocalDelegatedPropertySymbol =
		localDelegatedProperties.getReferenced(symbol)
	
	override fun getReferencedField(symbol: IrFieldSymbol): IrFieldSymbol = fields.getReferenced(symbol)
	override fun getReferencedConstructor(symbol: IrConstructorSymbol): IrConstructorSymbol =
		constructors.getReferenced(symbol)
	
	override fun getReferencedSimpleFunction(symbol: IrSimpleFunctionSymbol): IrSimpleFunctionSymbol =
		functions.getReferenced(symbol)
	
	override fun getReferencedProperty(symbol: IrPropertySymbol): IrPropertySymbol = properties.getReferenced(symbol)
	override fun getReferencedValue(symbol: IrValueSymbol): IrValueSymbol =
		when(symbol) {
			is IrValueParameterSymbol -> valueParameters.getReferenced(symbol)
			is IrVariableSymbol -> variables.getReferenced(symbol)
			else -> throw IllegalArgumentException("Unexpected symbol $symbol")
		}
	
	override fun getReferencedFunction(symbol: IrFunctionSymbol): IrFunctionSymbol =
		when(symbol) {
			is IrSimpleFunctionSymbol -> functions.getReferenced(symbol)
			is IrConstructorSymbol -> constructors.getReferenced(symbol)
			else -> throw IllegalArgumentException("Unexpected symbol $symbol")
		}
	
	override fun getReferencedReturnableBlock(symbol: IrReturnableBlockSymbol): IrReturnableBlockSymbol =
		returnableBlocks.getReferenced(symbol)
	
	override fun getReferencedClassifier(symbol: IrClassifierSymbol): IrClassifierSymbol =
		when(symbol) {
			is IrClassSymbol -> classes.getReferenced(symbol)
			is IrScriptSymbol -> scripts.getReferenced(symbol)
			is IrTypeParameterSymbol -> typeParameters.getReferenced(symbol)
			else -> throw IllegalArgumentException("Unexpected symbol $symbol")
		}
	
	override fun getReferencedTypeAlias(symbol: IrTypeAliasSymbol): IrTypeAliasSymbol =
		typeAliases.getReferenced(symbol)
}
