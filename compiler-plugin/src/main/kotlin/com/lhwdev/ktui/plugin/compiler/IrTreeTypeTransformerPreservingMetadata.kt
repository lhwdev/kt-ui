package com.lhwdev.ktui.plugin.compiler


import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.impl.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol
import org.jetbrains.kotlin.ir.symbols.IrReturnableBlockSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SymbolRemapper
import org.jetbrains.kotlin.ir.util.TypeRemapper
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.util.withinScope
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import java.util.HashMap


inline fun <T> MutableList<T>.transformList(transform: (T) -> T) {
	forEachIndexed { index, element ->
		set(index, transform(element))
	}
}

// TODO: update this on Kotlin v1.4-M1
open class IrTreeTypeTransformerPreservingMetadata(
	private val symbolRemapper: SymbolRemapper,
	private val typeRemapper: TypeRemapper
) : IrElementTransformerVoid() {
	
	//	init {
//		// TODO refactor
//		(typeRemapper as? DeepCopyTypeRemapper)?.let {
//			it.deepCopy = this
//		}
//	}
	private fun IrType.remapType() = typeRemapper.remapType(this)
	
	private inline fun <reified T : IrElement> T.transform() =
		transform(this@IrTreeTypeTransformerPreservingMetadata, null) as T
	
	private inline fun <reified T : IrElement> List<T>.transform() =
		map { it.transform() }
	
	private inline fun <reified T : IrElement> List<T>.transformTo(destination: MutableList<T>) =
		mapTo(destination) { it.transform() }
	
	private fun <T : IrDeclarationContainer> T.transformDeclarationsTo(destination: T) =
		declarations.transformTo(destination.declarations)
	
	
	override fun visitClass(declaration: IrClass) = with(declaration) {
		superTypes = superTypes.map { it.remapType() }
		
		super.visitClass(this)
	}
//		IrClassImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			symbolRemapper.getDeclaredClass(declaration.symbol),
//			symbolRenamer.getClassName(declaration.symbol),
//			declaration.kind,
//			declaration.visibility,
//			declaration.modality,
//			declaration.isCompanion,
//			declaration.isInner,
//			declaration.isData,
//			declaration.isExternal,
//			declaration.isInline,
//			declaration.isExpect // + isFun
//		).apply {
//			transformAnnotations(declaration)
//			copyTypeParametersFrom(declaration)
//			declaration.superTypes.mapTo(superTypes) {
//				it.remapType()
//			}
//			thisReceiver = declaration.thisReceiver?.transform()
//			declaration.transformDeclarationsTo(this)
//		}.copyAttributes(declaration)

//	override fun visitSimpleFunction(declaration: IrSimpleFunction): IrSimpleFunction =
//		IrFunctionImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			symbolRemapper.getDeclaredFunction(declaration.symbol),
//			symbolRenamer.getFunctionName(declaration.symbol),
//			declaration.visibility,
//			declaration.modality,
//			declaration.returnType,
//			isInline = declaration.isInline,
//			isExternal = declaration.isExternal,
//			isTailrec = declaration.isTailrec,
//			isSuspend = declaration.isSuspend,
//			isExpect = declaration.isExpect,
//			isFakeOverride = declaration.isFakeOverride,
//			isOperator = declaration.isOperator
//		).apply {
//			declaration.overriddenSymbols.mapTo(overriddenSymbols) {
//				symbolRemapper.getReferencedFunction(it) as IrSimpleFunctionSymbol
//			}
//			transformFunctionChildren(declaration)
//		}

//	override fun visitConstructor(declaration: IrConstructor): IrConstructor =
//		IrConstructorImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			symbolRemapper.getDeclaredConstructor(declaration.symbol),
//			declaration.name,
//			declaration.visibility,
//			declaration.returnType,
//			isInline = declaration.isInline,
//			isExternal = declaration.isExternal,
//			isPrimary = declaration.isPrimary,
//			isExpect = declaration.isExpect
//		).apply {
//			transformFunctionChildren(declaration)
//		}
	
	override fun visitFunction(declaration: IrFunction) = with(declaration) {
		returnType = typeRemapper.withinScope(this) { returnType.remapType() }
		
		super.visitFunction(this)
	}

//	private fun <T : IrFunction> T.transformFunctionChildren(declaration: T): T =
//		apply {
//			transformAnnotations(declaration)
//			copyTypeParametersFrom(declaration)
//			typeRemapper.withinScope(this) {
//				dispatchReceiverParameter = declaration.dispatchReceiverParameter?.transform()
//				extensionReceiverParameter = declaration.extensionReceiverParameter?.transform()
//				returnType = typeRemapper.remapType(declaration.returnType)
//				declaration.valueParameters.transformTo(valueParameters)
//				body = declaration.body?.transform()
//			}
//		}

//	private fun IrMutableAnnotationContainer.transformAnnotations(declaration: IrAnnotationContainer) {
//		declaration.annotations.transformTo(annotations)
//	}

//	override fun visitProperty(declaration: IrProperty): IrProperty =
//		IrPropertyImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			symbolRemapper.getDeclaredProperty(declaration.symbol),
//			declaration.name,
//			declaration.visibility,
//			declaration.modality,
//			isVar = declaration.isVar,
//			isConst = declaration.isConst,
//			isLateinit = declaration.isLateinit,
//			isDelegated = declaration.isDelegated,
//			isExpect = declaration.isExpect,
//			isExternal = declaration.isExternal
//		).apply {
//			transformAnnotations(declaration)
//			this.backingField = declaration.backingField?.transform()
//			this.getter = declaration.getter?.transform()
//			this.setter = declaration.setter?.transform()
//			this.backingField?.let {
//				it.correspondingPropertySymbol = symbol
//			}
//		}
	
	override fun visitField(declaration: IrField) = with(declaration) {
		IrFieldImpl(
			startOffset, endOffset,
			origin = origin,
			symbol = symbolRemapper.getDeclaredField(symbol),
			name = name,
			type = type.remapType(),
			visibility = visibility,
			isFinal = isFinal,
			isExternal = isExternal,
			isStatic = isStatic,
			isFakeOverride = isFakeOverride
		).let {
			it.annotations = annotations
			it.overriddenSymbols = overriddenSymbols
			it.initializer = initializer
			it.metadata = metadata
			
			super.visitField(it)
		}
	}
	
	override fun visitLocalDelegatedProperty(declaration: IrLocalDelegatedProperty) =
		with(declaration) {
			IrLocalDelegatedPropertyImpl(
				startOffset, endOffset,
				origin = origin,
				symbol = symbolRemapper.getDeclaredLocalDelegatedProperty(symbol),
				type = type.remapType()
			).let {
				it.annotations = annotations
				it.delegate = delegate
				it.getter = getter
				it.setter = setter
				
				super.visitLocalDelegatedProperty(it)
			}
		}


//	override fun visitEnumEntry(declaration: IrEnumEntry): IrEnumEntry =
//		IrEnumEntryImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			symbolRemapper.getDeclaredEnumEntry(declaration.symbol),
//			symbolRenamer.getEnumEntryName(declaration.symbol)
//		).apply {
//			transformAnnotations(declaration)
//			correspondingClass = declaration.correspondingClass?.transform()
//			initializerExpression = declaration.initializerExpression?.transform()
//		}
//
//	override fun visitAnonymousInitializer(declaration: IrAnonymousInitializer): IrAnonymousInitializer =
//		IrAnonymousInitializerImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			IrAnonymousInitializerSymbolImpl(declaration.descriptor)
//		).apply {
//			body = declaration.body.transform()
//		}
	
	override fun visitVariable(declaration: IrVariable) = with(declaration) {
		IrVariableImpl(
			startOffset, endOffset,
			origin = origin,
			symbol = symbolRemapper.getDeclaredVariable(symbol),
			name = name,
			type = type.remapType(),
			isVar = isVar,
			isConst = isConst,
			isLateinit = isLateinit
		).let {
			it.annotations = annotations
			it.initializer = initializer
			
			super.visitVariable(it)
		}
	}
	
	
	override fun visitTypeParameter(declaration: IrTypeParameter) = with(declaration) {
		superTypes.transformList { it.remapType() }
		
		super.visitTypeParameter(declaration)
	}
//		copyTypeParameter(declaration).apply {
//			// TODO type parameter scopes?
//			declaration.superTypes.mapTo(superTypes) { it.remapType() }
//		}

//	private fun copyTypeParameter(declaration: IrTypeParameter) =
//		IrTypeParameterImpl(
//			declaration.startOffset, declaration.endOffset,
//			mapDeclarationOrigin(declaration.origin),
//			symbolRemapper.getDeclaredTypeParameter(declaration.symbol),
//			symbolRenamer.getTypeParameterName(declaration.symbol),
//			declaration.index,
//			declaration.isReified,
//			declaration.variance
//		).apply {
//			transformAnnotations(declaration)
//		}
	
	private fun IrTypeParametersContainer.copyTypeParametersFrom(other: IrTypeParametersContainer) =
		typeRemapper.withinScope(this) {
			typeParameters = other.typeParameters
			
			typeParameters.forEach { param ->
				param.superTypes.transformList { it.remapType() }
			}
		}
	
	override fun visitValueParameter(declaration: IrValueParameter) = with(declaration) {
		IrValueParameterImpl(
			startOffset, endOffset,
			origin = origin,
			symbol = symbolRemapper.getDeclaredValueParameter(symbol),
			name = name,
			index = index,
			type = type.remapType(),
			varargElementType = varargElementType?.remapType(),
			isCrossinline = isCrossinline,
			isNoinline = isNoinline
		).let {
			it.annotations = annotations
			it.defaultValue = defaultValue
			
			super.visitValueParameter(it)
		}
	}
	
	override fun visitTypeAlias(declaration: IrTypeAlias) = with(declaration) {
		IrTypeAliasImpl(
			startOffset, endOffset,
			symbol = symbolRemapper.getDeclaredTypeAlias(symbol),
			name = name,
			visibility = visibility,
			expandedType = expandedType.remapType(),
			isActual = isActual,
			origin = declaration.origin
		).let {
			it.annotations = annotations
			it.copyTypeParametersFrom(this)
			
			super.visitTypeAlias(it)
		}
	}
	
	override fun visitVararg(expression: IrVararg) = with(expression) {
		IrVarargImpl(
			startOffset, endOffset,
			type = type.remapType(), varargElementType = varargElementType.remapType(),
			elements = elements
		).let {
			it.copyAttributes(this)
			
			super.visitVararg(it)
		}
	}
	
	override fun visitBlock(expression: IrBlock): IrExpression = with(expression) {
		if(this is IrReturnableBlock) IrReturnableBlockImpl(
			startOffset, endOffset,
			type = type.remapType(),
			symbol = symbolRemapper.getReferencedReturnableBlock(symbol),
			origin = origin,
			statements = statements,
			inlineFunctionSymbol = inlineFunctionSymbol
		)
		else IrBlockImpl(
			startOffset, endOffset,
			type = type.remapType(),
			origin = expression.origin,
			statements = statements
		)
	}.let {
		it.copyAttributes(expression)
		
		super.visitBlock(it)
	}
	
	override fun visitComposite(expression: IrComposite) = with(expression) {
		IrCompositeImpl(
			startOffset, endOffset,
			type = type.remapType(),
			origin = origin,
			statements = statements
		).let {
			it.copyAttributes(this)
			
			super.visitComposite(it)
		}
	}
	
	override fun visitStringConcatenation(expression: IrStringConcatenation) = with(expression) {
		IrStringConcatenationImpl(
			startOffset, endOffset,
			type = type.remapType(),
			arguments = arguments
		).let {
			it.copyAttributes(this)
			
			super.visitStringConcatenation(it)
		}
	}
	
	override fun visitGetObjectValue(expression: IrGetObjectValue) = with(expression) {
		IrGetObjectValueImpl(
			startOffset, endOffset,
			type = type.remapType(),
			symbol = symbolRemapper.getReferencedClass(symbol)
		).let {
			it.copyAttributes(this)
			
			super.visitGetObjectValue(it)
		}
	}
	
	override fun visitGetEnumValue(expression: IrGetEnumValue) = with(expression) {
		IrGetEnumValueImpl(
			startOffset, endOffset,
			type = type.remapType(),
			symbol = symbolRemapper.getReferencedEnumEntry(symbol)
		).let {
			it.copyAttributes(this)
			
			super.visitGetEnumValue(it)
		}
	}
	
	override fun visitGetValue(expression: IrGetValue) = with(expression) {
		IrGetValueImpl(
			startOffset, endOffset,
			type = type.remapType(),
			symbol = symbolRemapper.getReferencedValue(symbol),
			origin = origin
		).let {
			it.copyAttributes(this)
			
			super.visitGetValue(it)
		}
	}
	
	override fun visitSetVariable(expression: IrSetVariable) = with(expression) {
		IrSetVariableImpl(
			startOffset, endOffset,
			type = type.remapType(),
			symbol = symbolRemapper.getReferencedVariable(symbol),
			value = value,
			origin = origin
		).let {
			it.copyAttributes(this)
			
			super.visitSetVariable(it)
		}
	}
	
	override fun visitGetField(expression: IrGetField) = with(expression) {
		IrGetFieldImpl(
			startOffset, endOffset,
			symbol = symbolRemapper.getReferencedField(symbol),
			type = type.remapType(),
			receiver = receiver,
			origin = origin,
			superQualifierSymbol = symbolRemapper.getReferencedClassOrNull(expression.superQualifierSymbol)
		).let {
			it.copyAttributes(this)
			
			super.visitGetField(it)
		}
	}
	
	override fun visitSetField(expression: IrSetField) = with(expression) {
		IrSetFieldImpl(
			startOffset, endOffset,
			symbol = symbolRemapper.getReferencedField(symbol),
			receiver = receiver,
			value = value,
			type = type.remapType(),
			origin = origin,
			superQualifierSymbol = symbolRemapper.getReferencedClassOrNull(expression.superQualifierSymbol)
		).let {
			it.copyAttributes(this)
			
			super.visitSetField(it)
		}
	}
	
	override fun visitCall(expression: IrCall) = shallowCopyCall(expression).run {
		copyRemappedTypeArgumentsFrom(expression)
		transformValueArguments(expression)
		
		super.visitCall(this)
	}
	
	override fun visitConstructorCall(expression: IrConstructorCall) = IrConstructorCallImpl(
		expression.startOffset, expression.endOffset,
		type = expression.type.remapType(),
		symbol = symbolRemapper.getReferencedConstructor(expression.symbol),
		typeArgumentsCount = expression.typeArgumentsCount,
		constructorTypeArgumentsCount = expression.constructorTypeArgumentsCount,
		valueArgumentsCount = expression.valueArgumentsCount,
		origin = expression.origin
	).apply {
		copyRemappedTypeArgumentsFrom(expression)
		transformValueArguments(expression)
		copyAttributes(expression)
	}.let { super.visitConstructorCall(it) }
	
	private fun IrMemberAccessExpression.copyRemappedTypeArgumentsFrom(other: IrMemberAccessExpression) {
		assert(typeArgumentsCount == other.typeArgumentsCount) {
			"Mismatching type arguments: $typeArgumentsCount vs ${other.typeArgumentsCount} "
		}
		for(i in 0 until typeArgumentsCount) {
			putTypeArgument(i, other.getTypeArgument(i)?.remapType())
		}
	}
	
	private fun shallowCopyCall(expression: IrCall): IrCall {
		return IrCallImpl(
			expression.startOffset, expression.endOffset,
			type = expression.type.remapType(),
			symbol = symbolRemapper.getReferencedFunction(expression.symbol),
			typeArgumentsCount = expression.typeArgumentsCount,
			valueArgumentsCount = expression.valueArgumentsCount,
			origin = expression.origin,
			superQualifierSymbol = symbolRemapper.getReferencedClassOrNull(expression.superQualifierSymbol)
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
		}.copyAttributes(expression)
	}
	
	private fun <T : IrMemberAccessExpression> T.transformReceiverArguments(original: T) = apply {
		dispatchReceiver = original.dispatchReceiver
		extensionReceiver = original.extensionReceiver
	}
	
	private fun <T : IrMemberAccessExpression> T.transformValueArguments(original: T) {
		transformReceiverArguments(original)
		for(i in 0 until original.valueArgumentsCount) {
			putValueArgument(i, original.getValueArgument(i))
		}
	}
	
	override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall): IrExpression {
		val newConstructor = symbolRemapper.getReferencedConstructor(expression.symbol)
		return IrDelegatingConstructorCallImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			newConstructor,
			expression.typeArgumentsCount
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
			transformValueArguments(expression)
			copyAttributes(expression)
		}.let { super.visitDelegatingConstructorCall(it) }
	}
	
	override fun visitEnumConstructorCall(expression: IrEnumConstructorCall): IrExpression {
		val newConstructor = symbolRemapper.getReferencedConstructor(expression.symbol)
		return IrEnumConstructorCallImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			newConstructor,
			expression.typeArgumentsCount
		).apply {
			copyRemappedTypeArgumentsFrom(expression)
			transformValueArguments(expression)
			copyAttributes(expression)
		}.let { super.visitEnumConstructorCall(it) }
	}
	
	override fun visitGetClass(expression: IrGetClass) = IrGetClassImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.argument
	).let {
		it.copyAttributes(expression)
		super.visitGetClass(it)
	}
	
	override fun visitFunctionReference(expression: IrFunctionReference) = IrFunctionReferenceImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedFunction(expression.symbol),
		expression.typeArgumentsCount,
		expression.valueArgumentsCount,
		expression.reflectionTarget?.let { symbolRemapper.getReferencedFunction(it) },
		expression.origin
	).apply {
		copyRemappedTypeArgumentsFrom(expression)
		transformValueArguments(expression)
		copyAttributes(expression)
	}.let { super.visitFunctionReference(it) }
	
	
	override fun visitPropertyReference(expression: IrPropertyReference) = IrPropertyReferenceImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedProperty(expression.symbol),
		expression.typeArgumentsCount,
		expression.field?.let { symbolRemapper.getReferencedField(it) },
		expression.getter?.let { symbolRemapper.getReferencedSimpleFunction(it) },
		expression.setter?.let { symbolRemapper.getReferencedSimpleFunction(it) },
		expression.origin
	).apply {
		copyRemappedTypeArgumentsFrom(expression)
		transformReceiverArguments(expression)
		copyAttributes(expression)
	}.let { super.visitPropertyReference(it) }
	
	override fun visitLocalDelegatedPropertyReference(expression: IrLocalDelegatedPropertyReference) =
		IrLocalDelegatedPropertyReferenceImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			symbolRemapper.getReferencedLocalDelegatedProperty(expression.symbol),
			symbolRemapper.getReferencedVariable(expression.delegate),
			symbolRemapper.getReferencedSimpleFunction(expression.getter),
			expression.setter?.let { symbolRemapper.getReferencedSimpleFunction(it) },
			expression.origin
		).let {
			it.copyAttributes(expression)
			
			super.visitLocalDelegatedPropertyReference(it)
		}
	
	override fun visitFunctionExpression(expression: IrFunctionExpression) =
		IrFunctionExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.function,
			expression.origin
		).let {
			it.copyAttributes(expression)
			
			super.visitFunctionExpression(it)
		}
	
	override fun visitClassReference(expression: IrClassReference) = IrClassReferenceImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedClassifier(expression.symbol),
		expression.classType.remapType()
	).let {
		it.copyAttributes(expression)
		
		super.visitClassReference(it)
	}
	
	override fun visitInstanceInitializerCall(expression: IrInstanceInitializerCall) =
		IrInstanceInitializerCallImpl(
			expression.startOffset, expression.endOffset,
			symbolRemapper.getReferencedClass(expression.classSymbol),
			expression.type.remapType()
		).let {
			it.copyAttributes(expression)
			
			super.visitInstanceInitializerCall(it)
		}
	
	override fun visitTypeOperator(expression: IrTypeOperatorCall) = IrTypeOperatorCallImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.operator,
		expression.typeOperand.remapType(),
		expression.argument
	).let {
		it.copyAttributes(expression)
		
		super.visitTypeOperator(it)
	}
	
	override fun visitWhen(expression: IrWhen) = IrWhenImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.origin,
		expression.branches
	).let {
		it.copyAttributes(expression)
		
		super.visitWhen(it)
	}
	
	private val transformedLoops = HashMap<IrLoop, IrLoop>()
	
	private fun getTransformedLoop(irLoop: IrLoop): IrLoop =
		transformedLoops.getOrElse(irLoop) { getNonTransformedLoop(irLoop) }
	
	protected open fun getNonTransformedLoop(irLoop: IrLoop): IrLoop =
		throw AssertionError("Outer loop was not transformed: ${irLoop.render()}")
	
	override fun visitWhileLoop(loop: IrWhileLoop) =
		IrWhileLoopImpl(loop.startOffset, loop.endOffset, loop.type.remapType(), loop.origin).also { newLoop ->
			transformedLoops[loop] = newLoop
			newLoop.label = loop.label
			newLoop.condition = loop.condition
			newLoop.body = loop.body
		}.let {
			it.copyAttributes(loop)
			
			super.visitWhileLoop(it)
		}
	
	override fun visitDoWhileLoop(loop: IrDoWhileLoop) =
		IrDoWhileLoopImpl(loop.startOffset, loop.endOffset, loop.type.remapType(), loop.origin).also { newLoop ->
			transformedLoops[loop] = newLoop
			newLoop.label = loop.label
			newLoop.condition = loop.condition
			newLoop.body = loop.body
		}.let {
			it.copyAttributes(loop)
			
			super.visitDoWhileLoop(it)
		}
	
	override fun visitBreak(jump: IrBreak) = IrBreakImpl(
		jump.startOffset, jump.endOffset,
		jump.type.remapType(),
		getTransformedLoop(jump.loop)
	).apply { label = jump.label }.let {
		it.copyAttributes(jump)
		
		super.visitBreak(it)
	}
	
	override fun visitContinue(jump: IrContinue) = IrContinueImpl(
		jump.startOffset, jump.endOffset,
		jump.type.remapType(),
		getTransformedLoop(jump.loop)
	).apply { label = jump.label }.let {
		it.copyAttributes(jump)
		
		super.visitContinue(it)
	}
	
	override fun visitTry(aTry: IrTry) = IrTryImpl(
		aTry.startOffset, aTry.endOffset,
		aTry.type.remapType(),
		aTry.tryResult,
		aTry.catches,
		aTry.finallyExpression
	).let {
		it.copyAttributes(aTry)
		
		super.visitTry(it)
	}
	
	override fun visitReturn(expression: IrReturn) = IrReturnImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		symbolRemapper.getReferencedReturnTarget(expression.returnTargetSymbol),
		expression.value
	).let {
		it.copyAttributes(expression)
		
		super.visitReturn(it)
	}
	
	private fun SymbolRemapper.getReferencedReturnTarget(returnTarget: IrReturnTargetSymbol) =
		when(returnTarget) {
			is IrFunctionSymbol -> getReferencedFunction(returnTarget)
			is IrReturnableBlockSymbol -> getReferencedReturnableBlock(returnTarget)
			else -> throw AssertionError("Unexpected return target: ${returnTarget.javaClass} $returnTarget")
		}
	
	override fun visitThrow(expression: IrThrow) = IrThrowImpl(
		expression.startOffset, expression.endOffset,
		expression.type.remapType(),
		expression.value
	).let {
		it.copyAttributes(expression)
		
		super.visitThrow(it)
	}
	
	override fun visitDynamicOperatorExpression(expression: IrDynamicOperatorExpression) =
		IrDynamicOperatorExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.operator
		).apply {
			receiver = expression.receiver
			arguments += expression.arguments
		}.let {
			it.copyAttributes(expression)
			
			super.visitDynamicOperatorExpression(it)
		}
	
	
	override fun visitDynamicMemberExpression(expression: IrDynamicMemberExpression) =
		IrDynamicMemberExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.memberName,
			expression.receiver
		).let {
			it.copyAttributes(expression)
			
			super.visitDynamicMemberExpression(it)
		}
	
	
	override fun visitErrorExpression(expression: IrErrorExpression) =
		IrErrorExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.description
		).let {
			it.copyAttributes(expression)
			
			super.visitErrorExpression(it)
		}
	
	
	override fun visitErrorCallExpression(expression: IrErrorCallExpression) =
		IrErrorCallExpressionImpl(
			expression.startOffset, expression.endOffset,
			expression.type.remapType(),
			expression.description
		).apply {
			explicitReceiver = expression.explicitReceiver
			arguments += expression.arguments
		}.let {
			it.copyAttributes(expression)
			
			super.visitErrorCallExpression(it)
		}
}
