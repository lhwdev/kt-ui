@file:Suppress("ClassName")

package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name


const val PLUGIN_ID = "com.lhwdev.ktui.plugin"

val PACKAGE = FqName("com.lhwdev.ktui")

val WIDGET = PACKAGE.child(Name.identifier("Widget"))
val WIDGET_UTIL = PACKAGE.child(Name.identifier("WidgetUtil"))
val INLINE_WIDGET = PACKAGE.child(Name.identifier("InlineWidget"))


val BUILD_SCOPE = PACKAGE.child(Name.identifier("BuildScope"))

object kBuildScope {
	const val START = "start"
	
	object start {
		const val idState = "idState"
		const val attrs = "attrs"
		const val keyIndex = "keyIndex"
	}
	
	const val END = "end"
	const val END_SKIP = "endSkip"
	
	object end1 {
		const val returnValue = "returnValue"
	}
}


const val WIDGET_PARAMETER_SCOPE = "\$buildScope"
const val WIDGET_PARAMETER_ID_STATE = "\$idState"
const val WIDGET_PARAMETER_CHANGES = "\$changes"
