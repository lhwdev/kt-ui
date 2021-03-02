@file:Suppress("ClassName")

package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.util.referenceFunction
import com.lhwdev.ktui.plugin.compiler.util.referencePackage
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name


const val PLUGIN_ID = "com.lhwdev.ktui.plugin"


object UiNameConventions {
	const val buildScope = "\$buildScope"
	const val changed = "\$changed"
	const val default = "\$default"
}

object UiLibraryNames {
	val BuildScope = Name.identifier("BuildScope")
	
	val Widget = Name.identifier("Widget")
	val InlineWidget = Name.identifier("InlineWidget")
	val DefaultParameter = Name.identifier("DefaultParameter")
	val WidgetFunctionPureUsage = Name.identifier("WidgetFunctionPureUsage")
	
	val remember = Name.identifier("remember")
	
	
	object BuildScopeClass {
		object start {
			const val idState = "idState"
			const val attrs = "attrs"
			const val keyIndex = "keyIndex"
		}
		
		object startExpr {
			const val id = "id"
		}
	}
}

object UiLibraryFqNames {
	val uiPackage = FqName("com.lhwdev.ktui")
	
	val Widget = uiPackage.child(UiLibraryNames.Widget)
	val InlineWidget = uiPackage.child(UiLibraryNames.InlineWidget)
	val DefaultParameter = uiPackage.child(UiLibraryNames.DefaultParameter)
	val WidgetFunctionPureUsage = uiPackage.child(UiLibraryNames.WidgetFunctionPureUsage)
}

@Suppress("PropertyName")
class UiLibrary(pluginContext: IrPluginContext) {
	val uiPackage = pluginContext.referencePackage(UiLibraryFqNames.uiPackage)
	val buildScope = uiPackage.referenceClass(UiLibraryNames.BuildScope)
	val buildScopeType = buildScope.defaultType
	val buildScopeClass = buildScope.owner
	
	val widget = uiPackage.referenceClass(UiLibraryNames.Widget)
	val defaultParameter = uiPackage.referenceClass(UiLibraryNames.DefaultParameter)
	val widgetFunctionPureUsage = uiPackage.referenceClass(UiLibraryNames.WidgetFunctionPureUsage)
	
	val key = pluginContext.referenceClass(UiLibraryFqNames.uiPackage.child(Name.identifier("Key")))!!
	
	val BuildScope = BuildScopeClass()
	
	inner class BuildScopeClass {
		val start = buildScopeClass.referenceFunction("start", 1)
		val end = buildScopeClass.referenceFunction("end", 0)
		val endRestartable = buildScopeClass.referenceFunction("end", 1)
		
		val startExpr = buildScopeClass.referenceFunction("startExpr")
		val endExpr = buildScopeClass.referenceFunction("endExpr")
		
		val startReplaceableGroup = buildScopeClass.referenceFunction("startReplaceableGroup")
		val endReplaceableGroup = buildScopeClass.referenceFunction("endReplaceableGroup")
		
		val startRemovableGroup = buildScopeClass.referenceFunction("startRemovableGroup")
		val endRemovableGroup = buildScopeClass.referenceFunction("endRemovableGroup")
		
		val isChanged = buildScopeClass.referenceFunction("isChanged")
	}
}
