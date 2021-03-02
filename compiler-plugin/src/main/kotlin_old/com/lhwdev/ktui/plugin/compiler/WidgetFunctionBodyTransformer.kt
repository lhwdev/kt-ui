package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptorVisitor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSymbolOwner
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol
import org.jetbrains.kotlin.ir.symbols.IrVariableSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance

const val sSameOnlyFlags = 0b1010_1010_1010_1010_1010_1010_1010_1010.toInt()


fun IrElementScope.irAssertParameterNotNull(expression: IrExpression): IrExpression =
	irAssertNonNull(expression) // TODO: changed to something like Intrinsics.checkParameterIsNotNull


private val sStubScope = Scope(object : IrSymbol {
	override val descriptor = object : DeclarationDescriptor {
		override fun getContainingDeclaration(): DeclarationDescriptor? = null
		override fun getOriginal() = this
		override fun getName() = Name.special("<stub>")
		override fun <R, D> accept(visitor: DeclarationDescriptorVisitor<R, D>, data: D) =
			error("stub")
		
		override fun acceptVoid(visitor: DeclarationDescriptorVisitor<Void, Void>) {}
		override val annotations = Annotations.EMPTY
	}
	override val isBound get() = false
	override val isPublicApi get() = false
	override val owner: IrSymbolOwner get() = error("stub")
	override val signature = IdSignature.ScopeLocalDeclaration(Int.MIN_VALUE)
	override fun <D, R> accept(visitor: IrSymbolVisitor<R, D>, data: D) =
		visitor.visitSymbol(this, data)
})

fun <T> List<T>.filterUntil(predicate: (T) -> Boolean) = subList(0, indexOfFirst(predicate))


/**
 * ```kotlin
 * @Widget
 * fun MyWidget(param: Int, paramWithDefault: Any = 3) {
 *     Text("Hi", param)
 * }
 * ```
 *
 * into
 *
 * ```kotlin
 * @Widget
 * fun MyWidget(param: Int, @DefaultParameter paramWithDefault: Any? = 3 /* nullable: this is only used by kotlin, it doesn't matter */, $buildScope: BuildScope, $changed: Int) {
 *     $buildScope.start(123)
 *     val dirty = $changed // whether it is var or val doesn't matter; if it is var, it will be wrapped with IntRef
 *     if($changed and 0b1100 == 0)
 *         dirty = dirty or if($buildScope.isChanged(param)) 0b1000 else 0b0100
 *     val paramWithDefault = if(default and 0b1 == 0) {
 *         if($changed and 0b110000 == 0)
 *             dirty = dirty or if($buildScope.isChanged(paramWithDefault)) 0b100000 else 0b010000
 *         paramWithDefault!! // here, null assertion is added
 *     } else {
 *         dirty = dirty or 0b110000 // state of `3` (defaultValue)
 *         3
 *     }
 *
 *     if(dirty and 0b0111 != 0) { // skippable
 *         Text(
 *             "Hi", param, $buildScope,
 *             0x110000 /* 1st parameter is static */ or
 *             (dirty and 0x1100 shl 2)
 *         )
 *     }
 *     $buildScope.end { scope -> MyWidget(param, scope, $changed or 0b1) } // restartable
 * }
 * ```
 */
class WidgetFunctionBodyTransformer :
	IrElementTransformerVoidScoped<WidgetFunctionBodyTransformer.MyScope<out IrElement>>(), UiIrPhase {
	sealed class MyScope<T : IrElement>(override val irElement: T, override val scope: Scope) : ElementScope<T> {
		class Widget(val info: WidgetTransformationInfo, irElement: IrFunction, scope: Scope) :
			MyScope<IrFunction>(irElement, scope) {
			var paramsMapping: Map<IrValueParameterSymbol, IrVariableSymbol>? = null
			lateinit var mapReturnTo: IrReturnTargetSymbol
			var locationIdCounter = 0
		}
		
		class Loop(loop: IrLoop) : MyScope<IrLoop>(loop, sStubScope)
		class Other(irElement: IrElement, scope: Scope) : MyScope<IrElement>(irElement, scope)
	}
	
	override fun createScope(declaration: IrSymbolOwner) =
		(declaration as? IrFunction)?.widgetInfoMarker?.let {
			MyScope.Widget(
				it,
				declaration,
				Scope(declaration.symbol)
			)
		}
			?: MyScope.Other(declaration, Scope(declaration.symbol))
	
	
	private val widgetScopeOrNull: MyScope.Widget?
		get() {
			for(scope in scopeStack.reversed()) {
				val element = scope.irElement
				if(element !is IrFunction) continue
				if(scope is MyScope.Widget) return scope
				if(element.symbol.isEffectiveInline) continue // TODO: BUG: in case of lambda, crossinline fun should not be passed
				else return null
			}
			return null
		}
	
	val widgetScope get() = widgetScopeOrNull!!
	
	override fun lower() {
		target.transformChildrenVoid()
	}
	
	override fun visitFunctionNew(declaration: IrFunction) = with(declaration) {
		(currentScope as? MyScope.Widget)?.let {
			return irWidgetDeclaration(it)
		}
		super.visitFunctionNew(this)
	}
	
	private fun IrFunction.irWidgetDeclaration(scope: MyScope.Widget): IrStatement {
		val info = scope.info
		val hasReturn = !returnType.isUnit()
		val previousBody = body
		if(previousBody == null || previousBody is IrSyntheticBody) return this
		
		var defaultIndex = 0
		val defaultParamsMapping = mutableMapOf<IrValueParameterSymbol, IrVariableSymbol>()
		
		body = irBlockBody {
			val kind = info.kind
			val buildScope = info.buildScopeParameter
			
			// $buildScope.start(id) / $buildScope.startExpr(id)
			+irCall(
				if(hasReturn) UiLibrary.BuildScope.startExpr else UiLibrary.BuildScope.start,
				dispatchReceiver = irGet(buildScope),
				valueArguments = listOf(irInt(getLocationId(info.function)))
			)
			
			// val dirty = $changed / val dirtyN = $changedN
			// TODO: is it okay to reassign on val in JS or Native? It is okay in JVM so far
			val dirtyList =
				info.changedParameters.mapIndexed { i, changed -> irTemporary(irGet(changed), nameHint = "dirty$i") }
			info.infoForCall.dirty = dirtyList
			val changedState = ParamStateList(info.changedParameters.map { it.symbol })
			
			log5("transform widget fun: $info")
			logIndent {
				log4("dirty list: ${dirtyList.joinToString { it.name.toString() }}")
			}
			
			info.realParameters.forEachIndexed { i, parameter ->
				val original = info.originalParameters[i]
				val index = i + 1 // special 2 bit: force updating
				val (varIndex, bitIndex) = indexesForParameter(index)
				val dirty = dirtyList[varIndex]
				
				fun addThisStateToDirty() = irSet(dirty, irOr(irGet(dirty), // dirty = dirty or
					// if($buildScope.isChanged(param)) 0b1000
					irIf(type = irBuiltIns.intType) {
						irCall(
							UiLibrary.BuildScope.isChanged,
							dispatchReceiver = irGet(buildScope),
							valueArguments = listOf(irGet(parameter))
						) then irInt(0b10 shl bitIndex)
						
						// else 0b0100
						orElse(irInt(0b01 shl bitIndex))
					}
				))
				
				val defaultValue = original.defaultValue
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
					 *     paramWithDefault!!
					 * } else {
					 *     dirty = dirty or 0b110000 // state of `3` (defaultValue)
					 *     3
					 * }
					 */
					val type = parameter.type
					val originalType = original.type
					val needsNullCheck = when {
						originalType.isPrimitiveType() -> false
						originalType.isNullable() -> false
						else -> true
					}
					val actualValue = irVariable(
						name = parameter.name.asString(),
						type = if(needsNullCheck) type.withHasQuestionMark(false) else type
					)
					actualValue.initializer = irIfWithScope(type) {
						// if(default and 0b1 == 0) {
						irEquals(irAnd(irGet(info.defaultParameter!!), irInt(0b1 shl defaultIndex)), irInt(0)) then {
							
							// if($changed and 0b110000 == 0)
							+irIfThen(
								irBuiltIns.unitType,
								condition = irEquals(changedState.run { bitAtUnshifted(index) }, irInt(0)),
								then = addThisStateToDirty() // dirty = dirty or if($buildScope.isChanged(paramWithDefault)) 0b100000 else 0b010000
							)
							
							// paramWithDefault!!
							if(needsNullCheck) +irAssertParameterNotNull(irGet(parameter))
							else +irGet(parameter)
						}
						
						// } else {
						orElse {
							// dirty = dirty or 0b110000 // state of `3` (defaultValue)
							// track dependencies / isStatic state
							stateOf(
								defaultValue.expression,
								info.realParameters.map { it.symbol },
								dirtyList.map { it.symbol },
								bitIndex
							)
								?.let { +irSet(dirty, irOr(irGet(dirty), it)) }
							
							// 3
							+defaultValue.expression
						}
						// }
					}
					+actualValue
					
					defaultIndex++
					defaultParamsMapping[parameter.symbol] = actualValue.symbol
				}
			}
			
			scope.paramsMapping = defaultParamsMapping
			
			val content = irReturnableBlock(returnType) {}
			scope.mapReturnTo = content.symbol
			
			previousBody.transformChildrenVoid() // after assigning data to scope
			content.statements += previousBody.statements
			
			// if(dirty and 0b1010~1010 != 0) { // skippable
			val returnValue = when {
				kind.isSkippable -> {
					+irIfThen( // skippable widgets so far do not have return value
						irBuiltIns.unitType,
						condition = dirtyList.map { dirty ->
							irNotEquals(
								irAnd(irXor(irGet(dirty), irInt(sSameOnlyFlags)), irInt(sSameOnlyFlags)),
								irInt(0)
							)
						}.reduce { acc, isDirty -> irLogicAnd(acc, isDirty) },
						then = content
					)
					
					null
				}
				hasReturn -> irTemporary(content, "returnValue")
				else -> {
					+content
					null
				}
			}
			
			// }
			
			when {
				kind.isRestartable -> // $buildScope.end { scope -> MyWidget(param, scope, $id, $changed or 0b1) } // restartable
					+irCall(
						UiLibrary.BuildScope.endRestartable,
						dispatchReceiver = irGet(buildScope),
						valueArguments = listOf(irLambdaExpression(
							valueParameters = {
								listOf(
									valueParameterOf(
										it,
										index = 0,
										name = "scope",
										type = UiLibrary.buildScope.defaultType.toKotlinType()
									)
								)
							},
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
										.let { if(i == 0) irOr(it, irInt(0b10)) else it })
								}
								info.defaultParameter?.let { putValueArgument(it, irGet(it)) }
							}
						})
					)
				
				hasReturn -> +irCall(UiLibrary.BuildScope.endExpr, dispatchReceiver = irGet(buildScope))
				else -> +irCall(UiLibrary.BuildScope.end, dispatchReceiver = irGet(buildScope))
			}
			
			if(returnValue != null)
				+irReturn(irGet(returnValue))
		}
		
		return this
	}
	
	private fun getLocationId(function: IrFunction) =
		(function.startOffset shl 32) + function.fqNameForIrSerialization.asString().hashCode()
	
	private fun MyScope.Widget.getLocationId(statement: IrStatement) =
		statement.startOffset + (locationIdCounter++ shl 32)
	
	
	private val nextIdIntrinsic =
		moduleFragment.files.find { it.getPackageFragment()?.fqName == UiLibraryNames.PACKAGE && it.name == "bridge.kt" }!!
			.findDeclaration<IrFunction> { it.name.asString() == "nextId" }!!.symbol
	
	override fun visitCall(expression: IrCall): IrExpression {
		if(expression.symbol == nextIdIntrinsic)
			return irInt(widgetScope.getLocationId(expression))
		return super.visitCall(expression)
	}
	
	override fun visitGetValue(expression: IrGetValue): IrExpression {
		scopeStack.asReversed().forEach {
			val mapping = (it as? MyScope.Widget)?.paramsMapping
			if(mapping != null) {
				val mapped = mapping[expression.symbol]
				if(mapped != null) return super.visitGetValue(
					IrGetValueImpl(
						expression.startOffset,
						expression.endOffset,
						mapped,
						expression.origin
					)
				)
			}
		}
		
		return super.visitGetValue(expression)
	}
	
	
	override fun visitReturn(expression: IrReturn): IrExpression {
		widgetScopeOrNull?.let { scope -> // these symbols are still not mapped
			if((expression.returnTargetSymbol.ownerOrNull as? IrFunction)?.widgetInfoMarkerForCall?.functionSymbol == scope.scope.scopeOwnerSymbol)
				return expression.scope.irReturn(expression.value, scope.mapReturnTo)
		}
		return super.visitReturn(expression)
	}
	
	override fun visitWhen(expression: IrWhen): IrExpression = with(expression) {
		val scope = widgetScopeOrNull ?: return super.visitWhen(this)
		
		fun locationId() = scope.getLocationId(expression)
		
		val hasWidgetCalls = branches.map {
			it.condition.hasWidgetCall() to it.result.hasWidgetCall()
		}
		
		val conditionHasWidgetCalls = hasWidgetCalls.any { it.first }
		val resultHasWidgetCalls = hasWidgetCalls.any { it.second }
		
		if(conditionHasWidgetCalls || resultHasWidgetCalls) irBlock(type = type) {
			val whenLocationId = locationId()
			if(conditionHasWidgetCalls) +irCall(
				UiLibrary.BuildScope.start,
				dispatchReceiver = irGet(scope.info.buildScopeParameter),
				valueArguments = listOf(irInt(locationId()))
			)
			val value = irWhen(type = type, origin = expression.origin) {
				branches.forEachIndexed { index, branch ->
					branch.condition.transformChildren() then irBlock {
						if(conditionHasWidgetCalls)
							+irCall(
								UiLibrary.BuildScope.end,
								dispatchReceiver = irGet(scope.info.buildScopeParameter)
							)
						+irCall(
							UiLibrary.BuildScope.startReplaceableGroup,
							dispatchReceiver = irGet(scope.info.buildScopeParameter),
							valueArguments = listOf(irInt(whenLocationId), irInt(index))
						)
						+branch.result
					}
				}
				
				if(branches.none { it.isElse }) orElse(irBlock {
					if(conditionHasWidgetCalls) +irCall(
						UiLibrary.BuildScope.end,
						dispatchReceiver = irGet(scope.info.buildScopeParameter)
					)
					
					+irCall(
						UiLibrary.BuildScope.startReplaceableGroup,
						dispatchReceiver = irGet(scope.info.buildScopeParameter),
						valueArguments = listOf(irInt(whenLocationId), irInt(branches.size))
					)
				})
			}
			
			val end = irCall(
				UiLibrary.BuildScope.endReplaceableGroup,
				dispatchReceiver = irGet(scope.info.buildScopeParameter)
			)
			
			if(type.isUnit()) {
				+value
				+end
			} else {
				val valueTemp = irTemporary(value, "when_returnValue")
				+end
				+irGet(valueTemp)
			}
			
		} else expression.transformChildren()
	}
	
	override fun visitBlock(expression: IrBlock): IrExpression {
		if(expression.origin == IrStatementOrigin.FOR_LOOP) {
			// without this, kotlin lowering for the for loop is broken
			// so insert grouping before and after the whole for loop(which is like irBlock { val tmp0_iterable = ..; while(tmp0_iterable.hasNext()) { val item = tmp0_iterable.next(); .. } })
			widgetScopeOrNull?.apply {
				val transformer = this@WidgetFunctionBodyTransformer
				val loop = expression.statements[1] as IrLoop
				return withScope(MyScope.Loop(loop)) {
					expression.statements[0] = expression.statements[0].transform(transformer, null)
					loop.transformChildrenVoid()
					transformLoop(expression)
				}
			}
		}
		return super.visitBlock(expression)
	}
	
	private fun MyScope.Widget.transformLoop(loop: IrExpression) = irBlock(loop.type) {
		+irCall(
			UiLibrary.BuildScope.start,
			dispatchReceiver = irGet(info.buildScopeParameter),
			valueArguments = listOf(irInt(getLocationId(loop)))
		)
		val end = irCall(
			UiLibrary.BuildScope.end,
			dispatchReceiver = irGet(info.buildScopeParameter)
		)
		if(loop.type.isUnit()) {
			+loop
			+end
		} else {
			val variable = irTemporary(loop)
			+end
			+irGet(variable)
		}
	}
	
	override fun visitLoop(loop: IrLoop) = widgetScopeOrNull?.run {
		withScope(MyScope.Loop(loop)) {
			loop.transformChildrenVoid()
			transformLoop(loop)
		}
	} ?: super.visitLoop(loop)
	
	override fun visitBreakContinue(jump: IrBreakContinue) = widgetScopeOrNull?.let {
		val nearestLoop = scopeStack.asReversed().firstIsInstance<MyScope.Loop>()
		if(jump.loop != nearestLoop.irElement) {// long break/continue
			val loops = scopeStack.asReversed()
				.filterUntil { scope -> !scope.irElement.let { it is IrFunction && !it.symbol.isEffectiveInline /* TODO */ } /* do not pass the function boundary */ }
				.filterIsInstance<MyScope.Loop>()
			val loopsToExtraJump = loops.indexOfFirst { it.irElement == jump.loop }
			irBlock {
				for(i in 0 until loopsToExtraJump) +irCall(
					UiLibrary.BuildScope.end,
					dispatchReceiver = irGet(it.info.buildScopeParameter)
				)
				+super.visitBreakContinue(jump)
			}
		} else super.visitBreakContinue(jump)
	} ?: super.visitBreakContinue(jump)
}

private val IrBranch.isElse get() = this is IrElseBranch || condition.let { it is IrConst<*> && it.value == true }

fun IrElement.hasWidgetCall() = WidgetCallFinder().let {
	acceptVoid(it)
	it.hasWidgetCall
}

class WidgetCallFinder : IrElementVisitorRecursive() {
	var hasWidgetCall = false
	
	override fun visitCall(expression: IrCall) {
		hasWidgetCall = hasWidgetCall || expression.symbol.descriptor.isWidget()
		super.visitCall(expression)
	}
}
