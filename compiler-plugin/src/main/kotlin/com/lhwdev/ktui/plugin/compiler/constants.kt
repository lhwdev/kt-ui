@file:Suppress("ClassName")

package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.util.findFirstFunction
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass


const val PLUGIN_ID = "com.lhwdev.ktui.plugin"


object Widgets {
	const val buildScope = "\$buildScope"
	const val changed = "\$changed"
	const val default = "\$default"
}

object UiLibrary {
	val PACKAGE = FqName("com.lhwdev.ktui")
	
	val WIDGET = PACKAGE.child(Name.identifier("Widget"))
	val INLINE_WIDGET = PACKAGE.child(Name.identifier("InlineWidget"))
	
	val BUILD_SCOPE = PACKAGE.child(Name.identifier("BuildScope"))
	
	val DEFAULT_PARAMETER = PACKAGE.child(Name.identifier("DefaultParameter"))
	
	val REMEMBER = PACKAGE.child(Name.identifier("remember"))
	
	object BuildScope {
		object Start {
			const val idState = "idState"
			const val attrs = "attrs"
			const val keyIndex = "keyIndex"
		}
		
		object StartExpr {
			const val id = "id"
		}
	}
}

object UiLibraryDescriptors {
	val buildScope = module.resolveTopLevelClass(UiLibrary.BUILD_SCOPE, NoLookupLocation.FROM_BACKEND)!!
	
	val widget = module.resolveTopLevelClass(UiLibrary.WIDGET, NoLookupLocation.FROM_BACKEND)!!
	
	val defaultParameter = module.resolveTopLevelClass(UiLibrary.DEFAULT_PARAMETER, NoLookupLocation.FROM_BACKEND)!!
	
	val key = module.resolveTopLevelClass(UiLibrary.PACKAGE.child(Name.identifier("Key")), NoLookupLocation.FROM_BACKEND)!!
	
	object BuildScope {
		private fun function(name: String, valueParametersCount: Int) =
			buildScope.findFirstFunction(name) { it.valueParameters.size == valueParametersCount }
		
		val start = function("start", 1)
		val end = function("end", 0)
		val endRestartable = function("end", 1)
		
		val startExpr = function("startExpr", 1)
		val endExpr = function("endExpr", 0)
		
		val startReplaceableGroup = function("startReplaceableGroup", 2)
		val endReplaceableGroup = function("endReplaceableGroup", 0)
		
		val startRemovableGroup = function("startRemovableGroup", 1)
		val endRemovableGroup = function("endRemovableGroup", 0)
		
		val isChanged = function("isChanged", 1)
	}
}
