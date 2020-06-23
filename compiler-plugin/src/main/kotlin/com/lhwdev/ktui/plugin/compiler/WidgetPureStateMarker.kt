package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.irConstructorCall
import com.lhwdev.ktui.plugin.compiler.util.scope
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid


/**
 * Some optimization to remove unnecessary group start/end.
 * This task should be recursive, but need to prevent infinite recursion.
 * 1. finds a widget parameters only with call widget lambda like:
 *    ```kotlin
 *    @Widget
 *    fun RealPureWidget(child: @Widget () -> Unit) {
 *        child() // real-pure
 *    }
 *    ```
 * 2. recursively finds pure parameter until no additional findings exist
 */
fun WidgetPureStateMarker() = uiIrPhase(name = "WidgetPureStateMarker") {
	// 1. finds a widget parameters only with call widget lambda and mark
	target.transform(object : IrElementTransformerVoid() {
		inline val transformer get() = this
		inline fun <reified T : IrElement> T.transformVoid() = transform(transformer, null) as T
		
		val usagesToTrack = mutableListOf<Set<IrValueSymbol>>()
		val nonPureUsages = mutableListOf<MutableSet<IrValueSymbol>>()
		
		override fun visitFunction(declaration: IrFunction): IrStatement {
			val marker = declaration.widgetInfoMarker
			if(marker != null) {
				val trackIndexes = mutableListOf<Int>()
				
				val localUsagesToTrack = mutableSetOf<IrValueSymbol>()
				for(parameter in marker.realParameters) {
					if(!parameter.isWidget()) continue
					trackIndexes += parameter.index
					localUsagesToTrack += parameter.symbol
				}
				if(localUsagesToTrack.isEmpty()) return super.visitFunction(declaration)
				
				usagesToTrack += localUsagesToTrack
				nonPureUsages += mutableListOf()
				declaration.transformChildrenVoid()
				usagesToTrack.removeAt(usagesToTrack.lastIndex)
				val nonPureUsage = nonPureUsages.removeAt(nonPureUsages.lastIndex)
				val pureUsage = localUsagesToTrack - nonPureUsage
				
				for(index in trackIndexes) {
					val parameter = marker.realParameters[index]
					if(parameter.symbol in pureUsage)
						parameter.annotations += declaration.scope.irConstructorCall(UiLibraryDescriptors.widgetFunctionPureUsage.constructors.single())
				}
				
				return declaration
			}
			return super.visitFunction(declaration)
		}
		
		override fun visitGetValue(expression: IrGetValue): IrExpression {
			val symbol = expression.symbol
			usagesToTrack.forEachIndexed { index, usage ->
				if(symbol in usage) nonPureUsages[index].add(symbol)
			}
			return super.visitGetValue(expression)
		}
		
		override fun visitCall(expression: IrCall): IrExpression = expression.apply {
			if(symbol.descriptor.dispatchReceiverParameter?.isWidget() == true &&
				symbol.descriptor.isFunctionInvoke()) {
				// from IrCallImpl / IrCallWithIndexedArgumentsBase, only without dispatchReceiver = ...
				extensionReceiver = extensionReceiver?.transformVoid()
				valueArguments.forEachIndexed { index, argument ->
					putValueArgument(index, argument?.transformVoid())
				}
			} else super.visitCall(this)
		}
	}, null)
}
