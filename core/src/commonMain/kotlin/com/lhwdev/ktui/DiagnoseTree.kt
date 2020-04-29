package com.lhwdev.ktui


interface DiagnoseTree : DiagnoseNode {
	fun diagnose(): List<DiagnoseNode>
}
