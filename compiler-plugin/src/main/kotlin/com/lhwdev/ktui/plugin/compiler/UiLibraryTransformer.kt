//package com.lhwdev.ktui.plugin.compiler
//
//import org.jetbrains.kotlin.ir.IrStatement
//import org.jetbrains.kotlin.ir.builders.irBlockBody
//import org.jetbrains.kotlin.ir.builders.irCall
//import org.jetbrains.kotlin.ir.builders.irGet
//import org.jetbrains.kotlin.ir.builders.irReturn
//import org.jetbrains.kotlin.ir.declarations.IrFunction
//import org.jetbrains.kotlin.ir.declarations.name
//import org.jetbrains.kotlin.ir.util.getPackageFragment
//import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
//import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
//import org.jetbrains.kotlin.name.FqName
//import org.jetbrains.kotlin.psi2ir.findFirstFunction
//
//
//fun UiLibraryTransformer() =
//	uiIrPhase("UiLibraryTransformer") { _ ->
//		try {
//			val currentScopePackage = FqName("com.lhwdev.ktui")
//			val file = moduleFragment.files.find { it.getPackageFragment()?.fqName == currentScopePackage && it.name == "bridge.kt" }!!
//			file.transformChildrenVoid(object : IrElementTransformerVoid() {
//				override fun visitFunction(declaration: IrFunction): IrStatement {
//					declaration.body = irBuilder(declaration.symbol, declaration.startOffset, declaration.endOffset).irBlockBody {
//						when(declaration.name.asString()) {
//							"currentScope" -> +irReturn(irGet(declaration.valueParameters[0]))
//
//							"bridgeCall" -> +irCall(
//								pluginContext.symbolTable.referenceSimpleFunction(
//									pluginContext.builtIns.getFunction(2).findFirstFunction("invoke") { it.valueParameters.size == 2 }
//								)
//							).apply {
//								dispatchReceiver = irGet(declaration.valueParameters[0])
//								putValueArgument(0, irGet(declaration.extensionReceiverParameter!!))
//								putValueArgument(1, irConst(0L)) // calling this signifies that id doesn't matter: in ProxyWidget or Root etc.
//							}
//
//							"nextId" -> +irSimpleError("calling this must have inlined by compiler plugin") // any calls to this is inlined
//
//							else -> error("unexpected function ${declaration.name} in bridge.kt")
//						}
//					}
//
//					return super.visitFunction(declaration)
//				}
//			})
//		} catch(e: Throwable) {
//			// in the case of this, not compiling ui library, just client; ignore it
//		}
//	}
