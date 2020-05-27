package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrSyntheticBody
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.symbols.IrVariableSymbol
import org.jetbrains.kotlin.ir.util.statements

const val sSameOnlyFlagsOnFirst = 0b0101_0101_0101_0101_0101_0101_0101_0111
const val sSameOnlyFlags = 0b0101_0101_0101_0101_0101_0101_0101_0101

/**
 * ```kotlin
 * @Widget
 * fun MyWidget(param: Int, paramWithDefault: Int = 3) {
 *     Text("Hi", param)
 * }
 * ```
 *
 * into
 *
 * ```kotlin
 * @Widget
 * fun MyWidget(param: Int, paramWithDefault: Int = 3 /* default 0b1(first) */, $buildScope: BuildScope, $id: Int, $changed: Int) {
 *     $buildScope.start($id)
 *     val dirty = $changed // whether it is var or val doesn't matter; if it is var, it will be wrapped with IntRef
 *     if($changed and 0b1100 == 0)
 *         dirty = dirty or if($buildScope.isChanged(param)) 0b1000 else 0b0100
 *     val paramWithDefault = if(default and 0b1 == 0) {
 *         if($changed and 0b110000 == 0)
 *             dirty = dirty or if($buildScope.isChanged(paramWithDefault)) 0b100000 else 0b010000
 *         paramWithDefault
 *     } else {
 *         dirty = dirty or 0b110000 // state of `3` (defaultValue)
 *         3
 *     }
 *
 *     if(dirty and 0b0111 != 0) { // skippable
 *         Text(
 *             "Hi", param, $buildScope, 1234 /* location id */,
 *             0x110000 /* 1st parameter is static */ or
 *             (dirty and 0x1100 shl 2)
 *         )
 *     }
 *     $buildScope.end { scope -> MyWidget(param, scope, $id, $changed or 0b1) } // restartable
 * }
 * ```
 */
class WidgetFunctionBodyTransformer : IrElementTransformerVoidScoped<WidgetFunctionBodyTransformer.MyScope<out IrElement>>(), UiIrPhase {
	sealed class MyScope<T : IrElement>(override val irElement: T, override val scope: Scope) : ElementScope<T> {
		class Widget(val info: WidgetTransformationInfo, irElement: IrFunction, scope: Scope) : MyScope<IrFunction>(irElement, scope) {
			var paramsMapping: Map<IrValueParameterSymbol, IrVariableSymbol>? = null
		}
		
		class Other(irElement: IrElement, scope: Scope) : MyScope<IrElement>(irElement, scope)
	}
	
	override fun createScope(declaration: IrSymbolOwner) =
		(declaration as? IrFunction)?.widgetInfoMarker?.let { MyScope.Widget(it, declaration, Scope(declaration.symbol)) }
			?: MyScope.Other(declaration, Scope(declaration.symbol))
	
	override fun lower() {
		log4(uiTrace.getKeys(UiWritableSlices.WIDGET_TRANSFORMATION_INFO).joinToString { it.name.toString() })
		target.transform(this, null)
	}
	
	override fun visitFunctionNew(declaration: IrFunction) = with(declaration) {
		(currentScope as? MyScope.Widget)?.let {
			return irWidgetDeclaration(it)
		}
		super.visitFunctionNew(this)
	}
	
	private fun IrFunction.irWidgetDeclaration(scope: MyScope.Widget): IrStatement {
		val info = scope.info
		val previousBody = body
		if(previousBody == null || previousBody is IrSyntheticBody) return this
		
		var defaultIndex = 0
		val defaultParamsMapping = mutableMapOf<IrValueParameterSymbol, IrVariableSymbol>()
		
		body = irBlockBody {
			val kind = info.kind
			val buildScope = info.buildScopeParameter
			
			// $buildScope.start(id) / $buildScope.startExpr(id)
			+irCall(if(kind.isSkippable) UiLibraryDescriptors.BuildScope.start else UiLibraryDescriptors.BuildScope.startExpr, dispatchReceiver = irGet(buildScope), valueArguments = listOf(irGet(info.idParameter)))
			
			// val dirty = $changed / val dirtyN = $changedN
			val dirtyList = info.changedParameters.mapIndexed { i, changed -> irTemporary(irGet(changed), nameHint = "dirty$i") }
			info.infoForCall.dirty = dirtyList
			val changedState = ParamStateList(info.changedParameters.map { it.symbol })
			
			log5("transform widget fun: $info")
			logIndent {
				log4("dirty list: ${dirtyList.joinToString { it.name.toString() }}")
			}
			
			info.realParameters.forEachIndexed { i, parameter ->
				val index = i + 1 // special 2 bit: force updating
				val (varIndex, bitIndex) = indexesForParameter(index)
				val dirty = dirtyList[varIndex]
				
				fun addThisStateToDirty() = irSet(dirty, irOr(irGet(dirty), // dirty = dirty or
					// if($buildScope.isChanged(param)) 0b1000
					irIf(type = irBuiltIns.intType) {
						irCall(
							UiLibraryDescriptors.BuildScope.isChanged,
							dispatchReceiver = irGet(buildScope),
							valueArguments = listOf(irGet(parameter))
						) then irInt(0b10 shl bitIndex)
						
						// else 0b0100
						orElse(irInt(0b01 shl bitIndex))
					}
				))
				
				val defaultValue = parameter.defaultValue
				if(defaultValue == null) {
					// a. valueParameter without defaultValue
					
					// if($changed and 0x1100 == 0)
					
					+irIfThen(
						irBuiltIns.unitType,
						condition = irEquals(changedState.run { bitAtUnshifted(index) }, irInt(0)),
						then = addThisStateToDirty()
					)
					
				} else {
					// b. valueParameter with defaultValue
					
					/* val paramWithDefault = if(default and 0b1 == 0) {
					 *     if($changed and 0b110000 == 0)
					 *         dirty = dirty or if($buildScope.isChanged(paramWithDefault)) 0b100000 else 0b010000
					 *     paramWithDefault
					 * } else {
					 *     dirty = dirty or 0b110000 // state of `3` (defaultValue)
					 *     3
					 * }
					 */
					val type = parameter.type
					val actualValue = irVariable(name = "the_" + parameter.name.asString(), type = type)
					actualValue.initializer = irIfWithScope(type) {
						// if(default and 0b1 == 0) {
						irEquals(irAnd(irGet(info.defaultParameter!!), irInt(0b1 shl defaultIndex)), irInt(0)) then {
							
							// if($changed and 0b110000 == 0)
							+irIfThen(
								irBuiltIns.unitType,
								condition = irEquals(changedState.run { bitAtUnshifted(index) }, irInt(0)),
								then = addThisStateToDirty() // dirty = dirty or if($buildScope.isChanged(paramWithDefault)) 0b100000 else 0b010000
							)
							
							// paramWithDefault
							+irGet(parameter)
						}
						
						// } else {
						
						orElse {
							// dirty = dirty or 0b110000 // state of `3` (defaultValue)
							// track dependencies / isStatic state
							stateOf(defaultValue.expression, info.realParameters.map { it.symbol }, dirtyList.map { it.symbol }, bitIndex)
								?.let { +irSet(dirty, irOr(irGet(dirty), it)) }
							
							// 3
							+defaultValue.expression
							// }
						}
					}
					+actualValue
					
					parameter.defaultValue = null // don't let kotlin compiler handle defaultValue; we already handled
					defaultIndex++
					defaultParamsMapping[parameter.symbol] = actualValue.symbol
				}
			}
			
			scope.paramsMapping = defaultParamsMapping
			
			previousBody.transformChildrenVoid() // after assigning paramsMapping to scope
			
			val content = irBlock(returnType) {
				statements += previousBody.statements
			}
			
			// if(dirty and 0b0101~0111 != 0) { // skippable
			if(kind.isSkippable) +irIfThen(
				irBuiltIns.unitType,
				condition = dirtyList.mapIndexed { i, dirty ->
					irNotEquals(irAnd(irGet(dirty), irInt(if(i == 0) sSameOnlyFlagsOnFirst else sSameOnlyFlags)), irInt(0))
				}.reduce { acc, isDirty -> irLogicAnd(acc, isDirty) },
				then = content
			) else +content
			
			// }
			
			when {
				kind.isRestartable -> // $buildScope.end { scope -> MyWidget(param, scope, $id, $changed or 0b1) } // restartable
					+irCall(
						UiLibraryDescriptors.BuildScope.endRestartable,
						dispatchReceiver = irGet(buildScope),
						valueArguments = listOf(irLambdaExpression(
							valueParameters = listOf { valueParameterOf(it, index = 0, name = "scope", type = UiLibraryDescriptors.buildScope.defaultType) },
							returnType = builtIns.unitType
						) {
							+irCall(
								symbol,
								dispatchReceiver = dispatchReceiverParameter?.let { irGet(it) },
								extensionReceiver = extensionReceiverParameter?.let { irGet(it) },
								valueArguments = info.realParameters.map { irGet(it) }
								// TODO: here, need to put typeArguments? usually it doesn't matter but in case of <reified T>, it does matter or not?
							).apply {
								putValueArgument(info.buildScopeParameter, irGet(it.valueParameters[0]))
								info.changedParameters.forEachIndexed { i, changed ->
									putValueArgument(changed, irGet(changed)
										.let { if(i == 0) irOr(it, irInt(0b11)) else it })
								}
								info.defaultParameter?.let { putValueArgument(it, irGet(it)) }
							}
						})
					)
				
				kind.isSkippable -> +irCall(UiLibraryDescriptors.BuildScope.end, dispatchReceiver = irGet(buildScope))
				
				else -> +irCall(UiLibraryDescriptors.BuildScope.endExpr, dispatchReceiver = irGet(buildScope))
			}
		}
		
		return this
	}
	
	override fun visitGetValue(expression: IrGetValue): IrExpression {
		scopeStack.reversed().forEach {
			val mapping = (it as? MyScope.Widget)?.paramsMapping
			if(mapping != null) {
				val mapped = mapping[expression.symbol]
				if(mapped != null) return super.visitGetValue(IrGetValueImpl(expression.startOffset, expression.endOffset, mapped, expression.origin))
			}
		}
		
		return super.visitGetValue(expression)
	}
}
