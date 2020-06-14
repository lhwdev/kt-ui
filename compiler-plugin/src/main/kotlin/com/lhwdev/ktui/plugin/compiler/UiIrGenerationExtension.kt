package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime


/*
* @Widget
* fun Main(num: Int) {}
*
* into (compilation phase - after the semantic analysis phase)
*
* fun Main($scope: BuildScope, num: Int) {}
*
*
* Main(3)
*
* into
*
* val _temp_num = 3
* call(13924, _temp_num) { // 13924: unique location hash
*     Main(_temp_num)
* }
*/

class UiIrGenerationExtension : IrGenerationExtension {
	@OptIn(ExperimentalTime::class)
	override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
		@Suppress("NAME_SHADOWING")
		val pluginContext = IrPluginContext(
			pluginContext.moduleDescriptor,
			pluginContext.bindingContext,
			pluginContext.languageVersionSettings,
			pluginContext.symbolTable,
//			AutoResolveUnboundSymbolTable(pluginContext.symbolTable),
			pluginContext.typeTranslator,
			pluginContext.irBuiltIns,
			pluginContext.symbols)
		
		logColor("Hello from UiIrGenerationExtension", ConsoleColors.GREEN)
		log("")
		
		val target = moduleFragment
		
		UiIrContext(pluginContext, moduleFragment, target).transformations("main") {
			initUnboundSymbolUtils(pluginContext)
			
			measureTimeMillis {
				bindAll()
			}.withLog { "bind took $it" }

//			log2(target.dumpSrc())
//		val target = moduleFragment.files.withLog { it.joinToString { files -> files.name } }.find { it.name == "Main.kt" }!!
			target.deepCopyWithSymbols().logDumpColored()
			
			+WidgetMarkerResolver()
			log4(uiTrace.getKeys(UiWritableSlices.WIDGET_KIND).joinToString { it.name.toString() })
			+WidgetFunctionParamTransformer()
			+WidgetCallTransformer()
			+WidgetFunctionBodyTransformer()
//			target.logSrcColored(debug = true)
			+UiIntrinsicsTransformer()
			log("\nDUMP OVERALL TRANSFORMED IR TREE")
			// this causes compilation to fail(I don't know why dump causes it but removing it worked;
			// dump() in kotlin compiler library has the same error); so copy it then dump
			target.deepCopyWithSymbols().logDumpColored()
			log("")
			target.logSrcColored(debug = true)
			bindAll() // because the exception 'Unbound type parameters are forbidden'
		}
	}
}
