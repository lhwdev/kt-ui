package com.lhwdev.ktui.plugin.compiler

import org.gradle.api.Plugin
import org.gradle.api.Project


class UiGradlePlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.extensions.create("ktUi", UiGradleExtension::class.java)
	}
}


open class UiGradleExtension
