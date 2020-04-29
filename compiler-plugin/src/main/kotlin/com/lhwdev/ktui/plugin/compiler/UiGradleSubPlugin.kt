package com.lhwdev.ktui.plugin.compiler//package com.lhwdev.ktui.compilerPlugin
//
//import com.google.auto.service.AutoService
//import org.gradle.api.Project
//import org.gradle.api.tasks.compile.AbstractCompile
//import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
//import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
//import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
//import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
//import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
//
//
//@AutoService(KotlinGradleSubplugin::class) // don't forget!
//class UiGradleSubPlugin : KotlinGradleSubplugin<AbstractCompile> {
//	override fun apply(project: Project, kotlinCompile: AbstractCompile, javaCompile: AbstractCompile?, variantData: Any?, androidProjectHandler: Any?, kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?): List<SubpluginOption> {
//		val extension = project.extensions.findByType(UiGradleExtension::class.java)
//			?: UiGradleExtension()
//		return emptyList()
//	}
//
//	override fun getCompilerPluginId() = PLUGIN_ID
//
//	override fun getPluginArtifact() = SubpluginArtifact(
//		groupId = "com.kt", artifactId = "ui-compiler-plugin", version = "0.0.1"
//	)
//
//	override fun isApplicable(project: Project, task: AbstractCompile) =
//		project.plugins.hasPlugin(UiGradlePlugin::class.java)
//}
