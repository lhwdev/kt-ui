package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.util.getPackageFragment
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.FqName


fun UiIntrinsicsTransformer() = uiIrPhase("UiIntrinsicsTransformer") { _ ->
	try {
		val currentScopePackage = FqName("com.lhwdev.ktui")
		val file = moduleFragment.files.find { it.getPackageFragment()?.fqName == currentScopePackage && it.name == "bridge.kt" }!!
		file.transformChildrenVoid(object : IrElementTransformerVoid() {
			override fun visitFunction(
				declaration: IrFunction
			): IrStatement = withBuilderScope(declaration) {
				declaration.body = when(declaration.name.asString()) {
					"currentScope" -> irExpressionBody(irGet(declaration.valueParameters.first()))
					"nextId" -> irExpressionBody(irError("inline intrinsic"))
					"bridgeCall" -> irBlockBody {
						val buildScope = declaration.extensionReceiverParameter!!
						val widget = declaration.valueParameters[0]
						val changed = declaration.valueParameters[1]
						+irInvoke(functionalTypeReceiver = widget.symbol, valueArguments = listOf(irGet(buildScope), irGet(changed)))
					}
					else -> error("Unexpected ui intrinsic ${declaration.name}")
				}
				
				super.visitFunction(declaration)
			}
		})
	} catch(e: Throwable) {
		// in the case of this, not compiling ui library, just client; ignore it
	}
}
