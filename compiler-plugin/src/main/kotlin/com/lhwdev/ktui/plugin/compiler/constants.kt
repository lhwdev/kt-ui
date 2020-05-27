@file:Suppress("ClassName")

package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi2ir.findFirstFunction
import org.jetbrains.kotlin.resolve.descriptorUtil.resolveTopLevelClass


const val PLUGIN_ID = "com.lhwdev.ktui.plugin"


object Widgets {
	const val buildScope = "\$buildScope"
	const val id = "\$id"
	const val changed = "\$changed"
	const val default = "\$default"
}

object UiLibrary {
	val PACKAGE = FqName("com.lhwdev.ktui")
	
	val WIDGET = PACKAGE.child(Name.identifier("Widget"))
	val INLINE_WIDGET = PACKAGE.child(Name.identifier("InlineWidget"))
	
	val BUILD_SCOPE = PACKAGE.child(Name.identifier("BuildScope"))
	
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
	
	val key = module.resolveTopLevelClass(UiLibrary.PACKAGE.child(Name.identifier("Key")), NoLookupLocation.FROM_BACKEND)
	
	object BuildScope {
		val start = buildScope.findFirstFunction("start") { it.valueParameters.size == 1 }
		val startExpr = buildScope.findFirstFunction("startExpr") { it.valueParameters.size == 1 }
		val end = buildScope.findFirstFunction("end") { it.valueParameters.size == 0 }
		val endRestartable = buildScope.findFirstFunction("end") { it.valueParameters.size == 1 }
		val endExpr = buildScope.findFirstFunction("endExpr") { it.valueParameters.size == 0 }
		
		val isChanged = buildScope.findFirstFunction("isChanged") { it.valueParameters.size == 1 }
	}
}
