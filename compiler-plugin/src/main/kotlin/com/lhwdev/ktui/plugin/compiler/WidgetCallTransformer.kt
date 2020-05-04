package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.util.copyTypeAndValueArgumentsFrom
import org.jetbrains.kotlin.ir.util.irCall
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.psi2ir.findFirstFunction
import kotlin.math.exp


/*
 * Widget v2 implementation
 * WidgetDeclarationTransformer and WidgetCallTransformer should be separated to use transformedFunctions
 */


// TODO: lambda fun is not skippable

private val defaultValue = IrConstImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, context.irBuiltIns.intType, IrConstKind.Int, 0)


class WidgetCallTransformer : UiTransformerBase() {
	private val fNextId = context.symbolTable.referenceSimpleFunction(module.getPackage(PACKAGE).memberScope.findFirstFunction("nextId") { true })
	
	override fun visitFunction(declaration: IrFunction): IrStatement {
		val declarationInfo = transformedFunctions.values.find { it.function == declaration }
			?: return super.visitFunction(declaration)
		
		
		declaration.transformChildrenVoid(object : IrElementTransformerVoid() {
			val scopeParameter = declaration.valueParameters.let { it[it.size - if(declarationInfo.kind.isWidget) 2 else 1] }
			
			/*
			 * idState:
			 * _  _  _  _ | _  _  _  _
			 * state(32~63)  id(0~31)
			 *
			 * [known changed]
			 * 00 - unknown
			 * 10 - known; unchanged
			 * 11 - known; changed
			 *
			 *  [32]  [33] - p1
			 *  [34]  [35] - p2
			 * ...
			 */
			val idStateParameter = if(declarationInfo.kind == WidgetTransformationKind.widget) declaration.valueParameters.let { it[it.size - 1] } else null
//			var isNested = false
			
			
			fun getLocationId() = 123 // TODO
			
			
			override fun visitCall(expression: IrCall): IrExpression {
				val symbol = expression.symbol
				val descriptor = symbol.descriptor
				
				// special cases
				
				// #1. nextId()
				if(symbol == fNextId) return irBuilder(symbol).irConst(getLocationId())

//				if(isNested) return super.visitCall(expression)
				
				fun IrBuilderWithScope.irWidgetCall(expression: IrCall, info: WidgetTransformationInfo): IrCall {
					fun IrBuilderWithScope.stateForArgument(parameter: ValueParameterDescriptor/*, callDescriptor: FunctionDescriptor*/): IrExpression? {
						return if(parameter.containingDeclaration === descriptor)
							irOr(irShl(irAnd(irShr(irGet(declarationInfo.changes!!), irConst(descriptor.valueParameters.indexOf(parameter))), irConst(0x1)), irConst(1)), irConst(2))
						//	else if(parameter.containingDeclaration === callDescriptor) // default value: fun hi(a: Int, b: Int = a /* this */)
						//		irConst(0x) // TODO: care about this - `@Widget fun Abc(a: Int, b: Int = a)`: the state of b should be the state of a
						else null
					}
					
					fun IrBuilderWithScope.changesForArguments(arguments: List<IrExpression?>) =
						if(arguments.isEmpty()) irConst(0) else arguments.map {
							it ?: defaultValue
						}.map {
							if(it === defaultValue) irConst(0x0) // TODO: here, in Main(a: Int, b: Int = a), b is always unchanged
							else when(it) { // just returning acc means its state is unknown so need checking by BuildScope
								is IrConst<*> -> irConst(0x3)
								is IrGetValue -> (it.symbol.descriptor as? ValueParameterDescriptor)?.let { parameter -> stateForArgument(parameter) }
									?: irConst(0x0)
								else -> irConst(0x0)
							}
						}.reduceRight { acc, value -> irOr(irShl(acc, irConst(0x1)), value) }
					
					val newFunction = info.function as IrSimpleFunction
					
					return irCall(newFunction).apply { // TODO: support not simple ones
						copyTypeAndValueArgumentsFrom(expression)
						
						putValueArgument(WIDGET_PARAMETER_SCOPE, irGet(scopeParameter))
						
						if(info.kind == WidgetTransformationKind.widget) {
							if(!declarationInfo.kind.isWidget) error("@WidgetUtil fun calling @Widget fun: ${declaration.descriptor.dump()} -> ${expression.symbol.descriptor.dump()}")
							
							val id = getLocationId()
							val state = changesForArguments(expression.valueArguments)
							
							val idState = irOr(irConst(id), irShl(irPrimitiveCast(state, context.irBuiltIns.longType), irConst(32))) // state is already shifted
							putValueArgument(WIDGET_PARAMETER_ID_STATE, idState)
						} else if(info.kind == WidgetTransformationKind.inlineWidget) {
							val changes = changesForArguments(expression.valueArguments)
							
							putValueArgument(WIDGET_PARAMETER_CHANGES, changes)
						}
					}.also {
						super.visitCall(it)
					}
				}
				
				
				getTransformedFunctionFor(symbol)?.let {
					return irBuilder(symbol) { irWidgetCall(expression, it) }
				}
				
				descriptor.dispatchReceiverParameter?.type?.let {
					val kind = it.widgetTransformationKind
					if(kind != null) // TODO: safety?
						return irBuilder(symbol) { irWidgetCall(expression, transformedFunctions[(expression.dispatchReceiver!! as IrFunctionExpression).function]!!) }
				}
				
				transformedFunctions[expression]?.let {
					return irBuilder(symbol) { irWidgetCall(expression, it) }
				}
				
				return super.visitCall(expression)
			}
		})
		
		return super.visitFunction(declaration)
	}
}


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
//		is IrConst<*> -> if(expression.kind == IrConstKind.String) getParameterExpressionAsTemp(expression) // TODO: maybe string literals are better to just use irTemporary??
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
