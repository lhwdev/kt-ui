package com.lhwdev.ktui.flow

import com.lhwdev.ktui.AmbientSet
import com.lhwdev.ktui.Mergeable
import com.lhwdev.ktui.ambientOf
import com.lhwdev.ktui.merge
import com.lhwdev.ktui.text.TextStyle


val dialogStyleAmbient = ambientOf { DialogStyle() }


data class DialogStyle(val titleStyle: TextStyle? = null, val contentStyle: AmbientSet? = null) : Mergeable<DialogStyle> {
	override fun merge(other: DialogStyle) = DialogStyle(
		titleStyle = other.titleStyle.merge(titleStyle),
		contentStyle = other.contentStyle.merge(contentStyle)
	)
}
