package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.util.getPackageFragment
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid


fun UiIntrinsicsTransformer() = uiIrPhase("UiIntrinsicsTransformer") {
	val libraryPackage = UiLibraryNames.PACKAGE
	val file = moduleFragment.files.find { it.getPackageFragment()?.fqName == libraryPackage && it.name == "bridge.kt" }
	if(file != null) +uiIrPhase("transform bridge.kt of ktui core library") {
		file.transformChildrenVoid(object : IrElementTransformerVoid() {
			override fun visitFunction(
				declaration: IrFunction
			): IrStatement = withBuilderScope(declaration) {
				declaration.body = when(declaration.name.asString()) {
					"currentScope" -> irExpressionBody(irGet(declaration.valueParameters.first()))
					"nextId" -> irExpressionBody(irError("compiler intrinsic"))
//					"nextId" -> irBlockBody {
//						+irReturn(irReturnableBlock(irBuiltIns.intType) {
//							+irReturn(irInt(0)).withLog { it.dumpColored() }
//							+irReturn(irInt(1))
//						})
//					}
					"bridgeCall" -> irBlockBody {
						val buildScope = declaration.extensionReceiverParameter!!
						val widget = declaration.valueParameters[0]
						val changed = declaration.valueParameters[1]
						+irInvoke(
							functionalTypeReceiver = widget.symbol,
							valueArguments = listOf(irGet(buildScope), irGet(changed))
						)
					}
					else -> error("Unexpected ui intrinsic ${declaration.name}")
				}
				
				super.visitFunction(declaration)
			}
		})
	}


//	// transform bridge function calls // nextId() is transformed in WidgetFunctionBodyTransformer
//	target.transformChildrenVoid(object : IrWidgetElementTransformerVoidScoped() {
//		override fun visitCall(expression: IrCall): IrExpression {
//			val descriptor = expression.symbol.descriptor
//			if(descriptor.containingDeclaration.fqNameSafe == libraryPackage) {
//				when(descriptor.name.asString()) {
//					"currentScope" -> return irGet(widgetScope.info.buildScopeParameter) // in fact, this is not needed
//				}
//			}
//			return super.visitCall(expression)
//		}
//	})
}
