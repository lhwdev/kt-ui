package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass
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
		
		UiIrContext(pluginContext, moduleFragment, target).transformations {
			initUnboundSymbolUtils(pluginContext)
			
			log4(pluginContext.symbolTable.referenceClass(pluginContext.moduleDescriptor.resolveTopLevelClass(FqName("kotlin.Function0"), NoLookupLocation.FROM_BACKEND)!!).tryBind().defaultType.isFunction())
//			measureTime {
//				pluginContext.symbolTable.allUnbound().forEach {
//					it.getBoundIfPossible()
//				}
//			}.inMilliseconds.withLog()

//			log2(target.dumpSrc())
//		val target = moduleFragment.files.withLog { it.joinToString { files -> files.name } }.find { it.name == "Main.kt" }!!
			+WidgetDeclarationTransformer()
//			target.logDumpColored()
//			log(target.dumpSrc())
			+WidgetTypeTransformer()
			+WidgetCallTransformer()
			+UiLibraryTransformer()
		}
		
		log("\nDUMP OVERALL TRANSFORMED IR TREE")
		log(target.dumpColored())
		log(target.dumpSrc())
	}
}
