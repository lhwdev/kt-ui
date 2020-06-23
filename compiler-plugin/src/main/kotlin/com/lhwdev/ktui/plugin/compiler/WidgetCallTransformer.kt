package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.backend.jvm.lower.inlineclasses.InlineClassAbi
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.findFirstFunction
import org.jetbrains.kotlin.resolve.calls.components.isVararg


// TODO: lambda fun is not skippable


class WidgetCallTransformer : IrWidgetElementTransformerVoidScoped(), UiIrPhase {
	override fun lower() {
		target.transformChildrenVoid()
	}
	
	override fun visitFunctionAccess(
		expression: IrFunctionAccessExpression
	): IrExpression = with(expression) {
		when(this) {
			is IrCall, is IrConstructorCall -> Unit
			else -> return super.visitFunctionAccess(expression)
		}
		
		// is widget call or widget lambda call
		val isWidget = symbol.descriptor.isWidget()
		val isWidgetLambdaInvocation = dispatchReceiver?.type?.isWidget() ?: false && symbol.descriptor.isFunctionInvoke()
		
		when {
			isWidget -> {
				val info = expression.symbol.getOrPopulateInfo()
				
				irWidgetCall(info)
			}
			
			isWidgetLambdaInvocation -> {
				val realCount = valueArgumentsCount
				val changedCount = widgetChangedParamsCount(realCount + 1)
				var allCount = valueArgumentsCount + 1 + changedCount
				if(symbol.descriptor.extensionReceiverParameter != null) allCount++
				val functionClass = builtIns.getFunction(allCount + if(symbol.descriptor.extensionReceiverParameter == null) 0 else 1)
				val newInvocation = functionClass.findFirstFunction("invoke") { it.valueParameters.size == allCount }.symbol.tryBind().owner as IrSimpleFunction
				val buildScopeIndex = realCount
				val changedIndex = buildScopeIndex + 1
				val parameters = newInvocation.valueParameters
				val info = WidgetTransformationInfoForCall(
					WidgetTransformationKind.innerWidget,
					newInvocation.symbol, parameters.subList(0, realCount).map { it.symbol },
					parameters[buildScopeIndex].symbol,
					parameters.drop(changedIndex).map { it.symbol },
					null // lambda do not have any default values
				)
				
				irInvoke(
					functionSymbol = newInvocation.symbol,
					functionalTypeReceiver = dispatchReceiver!!,
					valueArguments = listOfNotNull(extensionReceiver) + valueArguments.requireNoNulls()
				).irWidgetCall(info)
			}
			
			else -> super.visitFunctionAccess(this)
		}
	}
	
	private fun IrFunctionAccessExpression.irWidgetCall(info: WidgetTransformationInfoForCall): IrExpression {
		val caller = widgetScope
		
		return irCall(
			info.functionSymbol,
			valueArguments = valueArguments, typeArguments = typeArguments,
			dispatchReceiver = dispatchReceiver, extensionReceiver = extensionReceiver
		).apply {
			putValueArgument(info.buildScopeParameter, irGet(caller.info.buildScopeParameter))
			
			val paramState = ParamStateList(info.changedParameters)
			
			val realParameters = info.realParameters
			var default = 0
			
			var defaultIndex = 0
			
			info.changedParameters.forEachIndexed { changedIndex, changedParam ->
				var stateInt = 0
				val stateExprs = mutableListOf<IrExpression>()
				
				for(realIndex in changedIndex * PARAM_COUNT_PER_INT until minOf(realParameters.size, (changedIndex + 1) * PARAM_COUNT_PER_INT)) {
					val realParam = realParameters[realIndex]
					val (_, bitIndex) = indexesForParameter(realIndex + 1)
					val argument = getValueArgument(realIndex) // it is not transformed yet, so can be null
					if(argument == null) {
						default = default or (1 shl defaultIndex)
						putValueArgument(realIndex, if(realParam.descriptor.isVararg) irNull() else realParam.descriptor.type.toIrType().defaultValue())
					} else {
						val visitor = WidgetStaticObserver()
						// TODO: handle default arguments
						val dependType = argument.accept(visitor, null)
						
						if(dependType == DependType.static) stateInt = stateInt or (0b11 shl bitIndex)
						else if(dependType == DependType.stateless) (visitor.dependencies.singleOrNull() as? IrValueParameterSymbol)?.let {
							val dependencyIndex = info.realParameters.indexOf(it)
							if(dependencyIndex != -1)
								stateExprs += paramState.run { shiftBitAt(dependencyIndex, bitIndex) }
						}
					}
					
					// before WidgetBodyTransformer which removes all defaultValue attribute
					if((realParam.descriptor as ValueParameterDescriptor).declaresDefaultValue())
						defaultIndex++
				}
				
				val changedArgument = when(stateExprs.size) {
					0 -> irInt(stateInt)
					else -> irOr(irInt(stateInt), stateExprs.reduce { acc, state -> irOr(acc, state) })
				}
				
				putValueArgument(changedParam, changedArgument)
			}
			
			info.defaultParameter?.let { putValueArgument(it, irInt(default)) }
		}.let { super.visitFunctionAccess(it) }
	}
	
	private fun IrFunctionSymbol.getOrPopulateInfo() =
		(if(tryBind().isBound) owner.widgetInfoMarkerForCall!! else TODO()) /*?: with(descriptor) {
			log2("wow get! ${if(isBound) owner.toString() else "no owner"} / ")
			// cannot judge by the name
			val scopeIndex = valueParameters.indexOfLast { it.type.constructor.declarationDescriptor == UiLibraryDescriptors.buildScope }
			assert(scopeIndex != -1)
			
			val real = valueParameters.subList(0, scopeIndex)
			val idIndex = scopeIndex + 1
			val changedFirstIndex = idIndex + 1
			
			val hasDefault = real.any { param -> param.annotations.any { it.fqName == UiLibrary.DEFAULT_PARAMETER } }
			
			WidgetTransformationInfoForCall(
				kind = widgetTransformationKind!!,
				functionSymbol = this@getOrPopulateInfo,
				realParameters = real.map { it.symbol },
				buildScopeParameter = valueParameters[scopeIndex].symbol,
				idParameter = valueParameters[idIndex].symbol,
				changedParameters = valueParameters.drop(changedFirstIndex).let { if(hasDefault) it.dropLast(1) else it }.map { it.symbol },
				defaultParameter = if(hasDefault) valueParameters.last().symbol else null,
			).also {
				it.dirty = TODO()
			}
		}*/
	
	private fun IrType.defaultValue(): IrExpression {
		val classSymbol = classOrNull?.tryBind()
		if(this !is IrSimpleType || hasQuestionMark || classSymbol?.owner?.isInline != true)
			return IrConstImpl.defaultValueForType(startOffset, endOffset, this)
		
		val klass = classSymbol.owner
		val ctor = classSymbol.constructors.first()
		val underlyingType = InlineClassAbi.getUnderlyingType(klass)
		
		// TODO(lmr): We should not be calling the constructor here, but this seems like a
		//  reasonable interim solution. We should figure out how to get access to the unsafe
		//  coerce and use that instead if possible.
		return IrConstructorCallImpl(
			startOffset,
			endOffset,
			this,
			ctor,
			typeArgumentsCount = 0,
			constructorTypeArgumentsCount = 0,
			valueArgumentsCount = 1,
			origin = null
		).also {
			it.putValueArgument(0, underlyingType.defaultValue())
		}
	}
}


//private val defaultValue = IrConstImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, context.irBuiltIns.intType, IrConstKind.Int, 0)
//
//
//class WidgetCallTransformerOld22 : IrSimpleElementTransformerVoidScoped() {
//	private val fNextId = context.symbolTable.referenceSimpleFunction(module.getPackage(UiLibrary.PACKAGE).memberScope.findFirstFunction("nextId") { true })
//
//	override fun visitFunctionNew(declaration: IrFunction): IrStatement {
//		val info = declaration.widgetInfoMarker
//			?: return super.visitFunctionNew(declaration)
//
//		declaration.transformChildrenVoid(object : IrElementTransformerVoid() {
//			val scopeParameter = info.buildScopeParameter
//
//			/*
//			 * idState:
//			 * _  _  _  _ | _  _  _  _
//			 * state(32~63)  id(0~31)
//			 *
//			 * [known changed]
//			 * 00 - unknown
//			 * 10 - known; unchanged
//			 * 11 - known; changed
//			 *
//			 *  [32]  [33] - p1
//			 *  [34]  [35] - p2
//			 * ...
//			 */
////			var isNested = false
//
//
//			fun getLocationId() = 123 // TODO
//
//
//			override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
//				val symbol = expression.symbol
//
//				// special cases
//
//				// #1. nextId()
//				if(symbol == fNextId) return irInt(getLocationId())
//
////				if(isNested) return super.visitCall(expression)
//
//				fun IrBuilderWithScope.irWidgetCall(expression: IrFunctionAccessExpression, info: WidgetTransformationInfo): IrFunctionAccessExpression {
//					// fun From(from: Int) = To(from)
//					// fun To(to: Int) = ...
//
//					fun IrBuilderWithScope.stateForArgument(fromParameter: ValueParameterDescriptor /* from */): IrExpression? {
//						return if(transformedFunctions.entries.find { (k, _) -> k is IrFunction && k.descriptor == fromParameter.containingDeclaration }/* From */?.value?.functionSymbol == declaration.symbol)
//						// >> changes shr <paramIndex> and 0b1 or 0b10
//							irOr(
//								irAnd(
//									irUshr(
//										irGet(info.changes!!), // changes
//										irInt(declaration.descriptor.valueParameters.indexOf(fromParameter)) // index of from
//									),
//									irInt(0b1)
//								),
//								irInt(0b10)
//							)
//						//	else if(parameter.containingDeclaration === callDescriptor) // default value: fun hi(a: Int, b: Int = a /* this */)
//						//		irInt(0x) // TODO: care about this - `@Widget fun Abc(a: Int, b: Int = a)`: the state of b should be the state of a
//						else null
//					}
//
//					fun IrBuilderWithScope.changesForArguments(arguments: List<IrExpression?>) =
//						if(arguments.isEmpty()) irInt(0) else arguments.map {
//							it ?: defaultValue
//						}.map { argument ->
//							if(argument === defaultValue) irInt(0b0) // TODO: here, in Main(a: Int, b: Int = a), b is always unchanged
//							else when(argument) { // just returning acc means its state is unknown so need checking by BuildScope
//								is irInt<*> -> irInt(0b11)
//								is IrGetValue -> (argument.symbol.descriptor as? ValueParameterDescriptor)?.let { stateForArgument(it) }
//									?: irInt(0b0)
//								else -> irInt(0b0)
//							}
//						}.reduceRight { acc, value -> irOr(irShl(acc, irInt(2)), value) }
//
//					val newFunctionSymbol = info.functionSymbol.tryBind()
//
//					return irCall(newFunctionSymbol, expression.type).apply { // >> ...(...,
//						copyTypeAndValueArgumentsFrom(expression)
//
//						val extraParametersCount = info.kind.extraParametersCount
//
//						// use index in case invoking lambda fun
//						putValueArgument(/* WIDGET_PARAMETER_SCOPE */ valueArgumentsCount - extraParametersCount, irGet(scopeParameter)) // $buildScope = $buildScope,
//
//						log5("widgetCall ${expression.symbol.descriptor.name} | info = $info")
//
//						when(info.kind) {
//							WidgetTransformationKind.skippableWidget -> {
//								val id = getLocationId()
//								val state = changesForArguments(expression.valueArguments)
//
//								val idState = irOr(irInt(id), irShl(irPrimitiveCast(state, context.irBuiltIns.longType), irInt(32))) // state is already shifted
//								putValueArgument(/* WIDGET_PARAMETER_ID_STATE */ valueArgumentsCount - 1, idState) // >> $idState = <locationId> or (<state> shl 32))
//							}
//
//							WidgetTransformationKind.nonSkippableWidget -> {
//								putValueArgument(/* WIDGET_PARAMETER_ID */ valueArgumentsCount - 1, irInt(getLocationId().toLong())) // >> $id = <locationId>)
//							}
//
//							WidgetTransformationKind.inlineWidget -> {
//								val changes = irInt(12345) // TODO
//
//								putValueArgument(/* WIDGET_PARAMETER_CHANGES */ valueArgumentsCount - 1, changes) // >> $changes = ...)
//							}
//							WidgetTransformationKind.widgetUtil -> {
//								// no-op; $buildScope argument is already put
//							}
//						}
//					}.also {
//						super.visitFunctionAccess(it)
//					}
//				}
//
//				getTransformedFunctionFor(symbol)?.let {
//					return irBuilder(symbol) { irWidgetCall(expression, it) }
//				}
//
//				// widget lambda invocation
////				descriptor.dispatchReceiverParameter?.type?.let {
////					log6(it.annotations)
////					val kind = it.widgetTransformationKind
//				// TODO: inlineWidget = widget?
//				expression.dispatchReceiver?.let {
//					val kind = it.type.toKotlinType().getWidgetTransformationKind(true) // TODO: true?
//					if(kind != null && expression.symbol.descriptor.isFunctionInvoke()) // TODO: safety?
//						return irBuilder(symbol) {
//							log2(symbol.descriptor.dump())
//							val lambdaKind = kind.toLambdaKind()
//							val info = WidgetTransformationInfoStub(expression.symbol.convertToWidgetLambdaFunctionInvocation(lambdaKind), lambdaKind, null)
//							irWidgetCall(expression, info)
//						}
//				}
//
//				transformedFunctions[expression]?.let {
//					return irBuilder(symbol) { irWidgetCall(expression, it) }
//				}
//
//				return super.visitFunctionAccess(expression)
//			}
//		})
//
//		return super.visitFunctionNew(declaration)
//	}
//}


//class WidgetCallTransformerOld : UiTransformerBase() {
//	override fun visitFunction(declaration: IrFunction): IrStatement {
//		if(declaration.isWidget() || declaration.isWidgetUtil()) {
//			declaration.transform(object : IrElementTransformerVoid() {
//				val scopeParameter = declaration.valueParameters.last()
//				var isNested = false
//				override fun visitCall(expression: IrCall): IrExpression {
//					val descriptor = expression.symbol.descriptor
//					val isWidget = descriptor.isWidget()
//
//					if(isNested) return super.visitCall(expression).withLog { "nested" }
//
//					if(isWidget || descriptor.isWidgetUtil()) try {
//						log2("GO! >> " + expression.symbol.descriptor.dump())
//						isNested = true
//
//						return irBuilder(expression.symbol) { convertToWidgetCall(isWidget, expression, irGet(scopeParameter)) }.withLog { "TRANSFORMED\n" + it.dumpColored() }
//					} finally {
//						isNested = false
//					}
//
//					if(expression.dispatchReceiver?.type?.run { isWidget() && isFunction() } == true &&
//						expression.symbol.descriptor.name.asString() == "invoke") { // via invoke() for @Widget (...) -> ...
//						log5("TRANSFORM LAMBDA FUN! ${expression.symbol.descriptor.dump()}")
//
//						// here, WidgetDeclarationTransformer and WidgetCallTransformer didn't change the call target signature
//
//
//						return irBuilder(expression.symbol) {
//							val newValueParametersCount = expression.valueArgumentsCount + 1
//
//							val functionDescriptor = context.builtIns.getFunction(newValueParametersCount).unsubstitutedMemberScope.findFirstFunction("invoke") { it.valueParameters.size == newValueParametersCount }
//							val transformedOriginal = irCall(
//								this@WidgetCallTransformer.pluginContext.symbolTable.referenceFunction(functionDescriptor),
//								functionDescriptor, functionDescriptor.returnType!!.toIrType() // TODO: !! ?.
//							).apply {
//								// last
//								copyTypeArgumentsFrom(expression) // ??????
//
//								dispatchReceiver = expression.dispatchReceiver
//								extensionReceiver = expression.extensionReceiver // just in case?
//
//								// TODO: is something missed?
//
//								for(i in 0 until expression.valueArgumentsCount)
//									putValueArgument(i, expression.getValueArgument(i))
//
//								symbol.getBoundIfPossible()
//							}.withLog { it.dumpColored() }
//
//							log5("ORIGINAL: ${transformedOriginal.symbol.descriptor.dump()}")
//
//							convertToWidgetCall(
//								isWidget,
//								transformedOriginal,
//								irGet(scopeParameter),
//								false
//							)
//						}.withLog { "TRANSFORMED\n" + it.dumpColored() }
//					}
//					return super.visitCall(expression)
//				}
//			}, null)
//		}
//
//		return super.visitFunction(declaration)
//	}
//
//	private fun IrBuilderWithScope.convertToWidgetCall(isWidget: Boolean, original: IrCall, scope: IrExpression, modifyParametersCount: Boolean = true) =
//		irBlock {
//			+irWidgetCall(isWidget, original, scope, modifyParametersCount)
//		}.transform(this@WidgetCallTransformer, null) // all usage of this function won't be able to call super (as parameter type IrCall), so this
//
//	private fun IrBlockBuilder.irWidgetCall(isWidget: Boolean, original: IrCall, scope: IrExpression, modifyParametersCount: Boolean = true): IrCall =
//		/*
//		* Foo(text = "bar")
//		*
//		* into
//		*
//		* val attr_text = "bar"
//		* $buildScope.call(id = 123, attrs = arrayOf(attr_text)) { Foo(attr_text, this) }
//		*
//		* (detailed spec:)
//		*
//		* val attr_text = "bar"
//		* $buildScope(arg/last).call(id = 123, attrs = arrayOf(vararg[attr_text]), block = extension_block@ { return@extension_block Foo(1 = attr_text, 2 = this@extension_block) })
//		*/
//		irCall(callee = UiDeclarations.buildScopeCallSymbol, type = original.type).apply { // > $buildScope.call(
//			val newFunction = transformedFunctions[original.symbol.owner]!!
//
//			log("irWidgetCall original = " + newFunction.descriptor.dump())
//
//			// type arguments
//			putTypeArgument(0, original.type)
//
//
//			// value arguments
//			dispatchReceiver = scope // (> $buildScope.)
//
//			val params = UiDeclarations.buildScopeCall.valueParameters.map { it.name to it }.toMap()
//
//			// - id
//			putValueArgument(params.getValue(Call.argId), irInt(123)) // > id = 123,
//
//			// - attrs
//			val attrs = original.symbol.descriptor.valueParameters.let { if(modifyParametersCount) it else it.subList(0, it.size - 1) }.map { // the last parameter won't be put if !modifyParametersCount
//				// >> create temporary
//				getParameterExpression(original.getValueArgument(it)!!)
//			}
//
//			val tmpDispatchReceiver = original.dispatchReceiver?.let { getParameterExpression(it) }
//			val tmpExtensionReceiver = original.extensionReceiver?.let { getParameterExpression(it) }
//
//			val allAttrs = attrs.toMutableList()
//			tmpDispatchReceiver?.let { allAttrs += it }
//			tmpExtensionReceiver?.let { allAttrs += it }
//
//			val anyN = context.irBuiltIns.anyNType
//			val arrayType = pluginContext.symbols.array.typeWith(anyN)
//			val array = irCall(callee = pluginContext.symbols.arrayOf, type = arrayType).apply { // > arrayOf
//				putTypeArgument(0, anyN) // > <Any?>
//				putValueArgument(0, IrVarargImpl(startOffset, endOffset, arrayType, anyN, allAttrs.map { it() })) // (...(attrs))
//			}
//
//			putValueArgument(params.getValue(Call.argAttrs), array) // > (attrs = ~)
//
//			// - key (optional)
//			// TODO: later
//
//			// - block
//			val blockParameter = params.getValue(Call.argBlock)
//
//			putValueArgument(blockParameter, // > block = {
//				irLambdaExpression(
//					startOffset = startOffset,
//					endOffset = endOffset,
//					descriptor = createFunctionDescriptor(
//						type = blockParameter.type,
//						owner = symbol.descriptor.containingDeclaration // TODO: ???
//					),
//					type = blockParameter.type.toIrType()
//				) { fn ->
//					+irReturn(IrCallImpl( // > MyWidget(
//						startOffset = startOffset,
//						endOffset = endOffset,
//						type = original.type,
//						symbol = newFunction.symbol,
//						typeArgumentsCount = original.typeArgumentsCount,
//						valueArgumentsCount = original.valueArgumentsCount + if(modifyParametersCount) 1 else 0,
//						origin = original.origin,
//						superQualifierSymbol = original.superQualifierSymbol
//					).apply {
//						copyTypeArgumentsFrom(original)
//
//						dispatchReceiver = tmpDispatchReceiver?.let { it() } // TODO: support these things
//						extensionReceiver = tmpExtensionReceiver?.let { it() }
//
//						attrs.forEachIndexed { index, expr ->
//							putValueArgument(index, expr()) // > ...(attrs)
//						}
//
//						putValueArgument(valueArgumentsCount - 1, irGet(fn.extensionReceiverParameter!!)) // > , $buildScope = it)
//						logDumpColored(baseColor = ConsoleColors.BLACK_BRIGHT)
//					})
//				}
//			)
//		}
//
//	//	private fun isChildrenParameter(desc: ValueParameterDescriptor, expr: IrExpression): Boolean {
////		return expr is IrFunctionExpression &&
////			expr.origin == IrStatementOrigin.LAMBDA &&
////			desc is EmitChildrenValueParameterDescriptor
////	}
////
//	private fun IrBlockBuilder.getParameterExpression(
////		desc: ValueParameterDescriptor,
//		expression: IrExpression
//	): () -> IrExpression = when(expression) {
//		is irInt<*> -> if(expression.kind == irIntKind.String) getParameterExpressionAsTemp(expression) // TODO: maybe string literals are better to just use irTemporary??
//		else ({ expression.copy() })
//		is IrGetValue -> ({ expression.copy() })
////			isChildrenParameter(desc, expr) ->
////				({ expr })
//		else -> getParameterExpressionAsTemp(expression)
//	}
//
//	private fun IrBlockBuilder.getParameterExpressionAsTemp(expression: IrExpression): () -> IrExpression {
//		val temp = irTemporary(
////					covertLambdaIfNecessary(expression),
//			expression,
//			irType = expression.type
//		)
//
//		return { irGet(temp) }
//	}
//
////	private fun IrBlockBuilder.convertLambdaIfNecessary(expression: IrExpression): IrExpression {
////		val functionExpression = expression as? IrFunctionExpression ?: return expression
////		val function = functionExpression.function
////		if (!function.hasAnnotation(WIDGET)) return expression
////
////
////	}
//}
