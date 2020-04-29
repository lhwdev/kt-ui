package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.types.isUnit
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


val transformedFunctions = mutableMapOf<Any, WidgetTransformationInfo>()

fun getTransformedFunctionFor(symbol: IrFunctionSymbol) =
//	if(symbol.descriptor.isFunctionInvoke())
//		context.symbolTable.referenceSimpleFunction(
//			context.builtIns.getFunction(symbol.descriptor.valueParameters.size)
//				.unsubstitutedMemberScope
//				.findFirstFunction("invoke") { it.valueParameters.size == symbol.descriptor.valueParameters.size }
//		).getBoundIfPossible().owner
//	else
	transformedFunctions[symbol.tryBind().owner]


data class WidgetTransformationInfo(val function: IrFunction, val kind: WidgetTransformationKind, val changes: IrValueDeclaration?)


@Suppress("MemberVisibilityCanBePrivate")
class WidgetDeclarationTransformer : UiTransformerBase() {
//	private val declarationStack = mutableListOf<IrSymbolOwner>()
	
	override fun visitDeclaration(declaration: IrDeclaration): IrStatement {
		log6("visitDeclaration: ${declaration.descriptor.dump()}")
		return super.visitDeclaration(declaration)
	}

//	override fun visitDeclaration(declaration: IrDeclaration): IrStatement {
//		if(declaration !is IrSymbolOwner) return super.visitDeclaration(declaration)
//		try {
//			declarationStack.push(declaration)
//			return super.visitDeclaration(declaration)
//		} finally {
//			declarationStack.pop()
//		}
//	}

//	fun nearestScope(): IrValueParameter {
//		declarationStack.reversed().forEach {
//			if(it is IrFunction) {
//				val param = it.valueParameters.lastOrNull()
//				if(param != null && param.isScopeParam()) {
//					return param
//				}
//			}
//		}
//
//		error("couldn't find BuildScope")
//	}
	
	override fun visitFunction(declaration: IrFunction) =
		super.visitFunction(declaration.widgetTransformationKind?.let {
			val info = declaration.withWidgetCall(it)
			transformedFunctions[declaration] = info
			info.function
		} ?: declaration)
	
	override fun visitCall(expression: IrCall): IrExpression { // to transform lambda arguments
		expression.symbol.tryBind().owner.valueParameters.forEach { parameter ->
			val argument = expression.getValueArgument(parameter.index)
			
			if(argument is IrFunctionExpression)
				expression.putValueArgument(0, expression.transformWidgetLambdaCallIfNecessary(parameter, argument))
		}
		return super.visitCall(expression)
	}
	
	/*
	 * @Widget
	 * fun Hello(abc: Int): Float = Main(abc)
	 *
	 * into
	 *
	 * @Widget
	 * fun Hello(abc: Int, $buildScope: BuildScope, $idState: Int): Float {
	 *     val changes = $buildScope.start(idState = $idState, attrs = arrayOf(abc))
	 *     return if(changes == 0) endSkip() as Float else
	 *         Main(abc, scope, 123 or ((changes shr 0 and 0x1 or 0x2) shr 32).also { end(it) } // ... 0 0 1 (0 / changed)
	 *
	 * }
	 */
	fun IrFunction.withWidgetCall(kind: WidgetTransformationKind): WidgetTransformationInfo {
		val fn = copyLight()
//		if(fn.descriptor.isFunctionInvoke())
		
		log("IrFunction.withWidgetCallIfNeeded(): $name")
		
		// #1. parameters
//			uiTrace.record(UiWritableSlices.IS_WIDGET_CALL, )
		val originalParameters = fn.valueParameters // >> (abc: Int,
		
		val scopeParameter = // >> $buildScope: BuildScope,
			fn.addValueParameter(WIDGET_PARAMETER_SCOPE, UiDeclarations.buildScope.defaultType.toIrType())
		
		var changesInfo: IrValueDeclaration? = null
		
		// @Widget
		@Suppress("SuspiciousCollectionReassignment")
		if(kind == WidgetTransformationKind.widget) {
			val idState = fn.addValueParameter(WIDGET_PARAMETER_ID_STATE, context.irBuiltIns.longType) // >> $idState: Int)
			
			val builder = irBuilder(fn)
			
			// #2. body
			
			var attrs = originalParameters.map { builder.irGet(it) }
			fn.dispatchReceiverParameter?.let { attrs += builder.irGet(it) }
			fn.extensionReceiverParameter?.let { attrs += builder.irGet(it) }
			
			fn.body = builder.irBlockBody { // >> ... {
				val changes = irTemporary( // val changes =
					irCall(UiDeclarations.BuildScope.start).apply { // >> $buildScope.start(
						dispatchReceiver = irGet(scopeParameter) // ($buildScope.)
						
						putValueArgument(kBuildScope.start.idState, irGet(idState)) // >> idState = $idState,
						putValueArgument(kBuildScope.start.attrs, irArrayOf(context.irBuiltIns.anyNType, attrs)) // >> attrs = arrayOf(abc),
						
						// TODO: >> keyIndex = ???
						putValueArgument(kBuildScope.start.keyIndex, irConst(-1)) // >> keyIndex = -1
						
					}, // >> )
					"scope"
				)
				
				changesInfo = changes
				
				
				fun IrBuilderWithScope.mapReturns(statement: IrStatement) =
					statement.transform(object : IrElementTransformerVoid() {
						override fun visitReturn(expression: IrReturn) =
							if(expression.returnTargetSymbol == symbol)
								irReturn(expression.value)
							else expression
					}, null)
				
				
				val body = body
				val condition = irEquals(irGet(changes), irConst(0))
				
				if(returnType.isUnit()) { // if return type is Unit,
					+irIfThen( // >> if(changes == 0) {
						type = returnType,
						condition = condition,
						thenPart = irRun(type = returnType) { // >> run { Main(...) }
							body!!.statements.forEach { +mapReturns(it) }
						},
						origin = IrStatementOrigin.IF
					) // >> }
					
					+irCall(UiDeclarations.BuildScope.end0).apply { // $buildScope.end()
						dispatchReceiver = irGet(scopeParameter)
					}
				} else {
					+irReturn(
						irIfThenElse( // >> if(changes == 0) {
							type = returnType,
							condition = condition,
							thenPart = irBlock { // >> run { Main(...) }
								val originalStatementsAsExpression = builder.irRun(type = returnType) {
									body!!
									
									when(body) {
										is IrBlockBody -> body.statements.forEach { +mapReturns(it) }
										is IrExpressionBody -> +irReturn(mapReturns(body.expression) as IrExpression)
										else -> error("Unsupported widget type ${body::class.java.name}")
									}
								}
								
								val originalReturn = irTemporary(originalStatementsAsExpression, "returnValue")
								
								+irCall(UiDeclarations.BuildScope.end1).apply { // .also { $buildScope.end(it) }
									dispatchReceiver = irGet(scopeParameter)
									
									putValueArgument(kBuildScope.end1.returnValue, irGet(originalReturn))
								}
								+irGet(originalReturn)
							},
							elsePart = irAs( // >> } else $buildScope.endSkip() as Float
								argument = irCall(UiDeclarations.BuildScope.endSkip).apply {
									dispatchReceiver = irGet(scopeParameter)
								},
								type = returnType
							),
							origin = IrStatementOrigin.IF
						)
					)
				}
				
			}
		} else if(kind == WidgetTransformationKind.inlineWidget) {
			changesInfo = fn.addValueParameter(WIDGET_PARAMETER_CHANGES, context.irBuiltIns.intType)

//			val builder = irBuilder(fn)
			
			// #2. body
			
			// mapping returns is not needed
			// skipping of a inline widget depends on caller, not skipping individually
			// so, no need to transform body
		}
		
		// @WidgetUtil: don't touch anymore
		
		return WidgetTransformationInfo(fn, kind, changesInfo)
	}
	
	
	fun IrCall.transformWidgetLambdaCallIfNecessary(valueParameter: IrValueParameter, valueArgument: IrFunctionExpression): IrExpression {
		val kind = valueParameter.type.tryBind().widgetTransformationKind
		if(kind != null) {
			val info = valueArgument.function.withWidgetCall(kind)
			transformedFunctions[this] = info
			valueArgument.function = info.function as IrSimpleFunction
		}
		return valueArgument
	}
}
