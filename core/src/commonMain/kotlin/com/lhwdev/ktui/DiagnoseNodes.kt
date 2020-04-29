package com.lhwdev.ktui


object DiagnoseNodes {
	class Text(val text: String) : DiagnoseNode {
		override fun toString() = text
	}
}
