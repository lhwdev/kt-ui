package com.asm.build

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


private const val sPluginKotlinMultiplatform = "org.jetbrains.kotlin.multiplatform"
private const val sPluginKotlinJvm = "org.jetbrains.kotlin.jvm"


inline fun <reified T : Any> Project.hasExtension() = try {
	extensions.getByType<T>()
	true
} catch(_: UnknownDomainObjectException) {
	false
}

val Project.isAndroid get() = hasExtension<BaseExtension>()

val Project.isAndroidLibrary get() = hasExtension<LibraryExtension>()

fun <R> Project.androidExtension(block: BaseExtension.() -> R) =
		extensions.getByType<BaseExtension>().run(block)

val Project.isKotlin get() = hasExtension<KotlinProjectExtension>()

fun <R> Project.kotlinProjectExtension(block: KotlinProjectExtension.() -> R) =
		extensions.getByType<KotlinProjectExtension>().run(block)

val Project.isKotlinMultiplatform get() = pluginManager.hasPlugin(sPluginKotlinMultiplatform)

fun <R> Project.kotlinMutliplatformExtension(block: KotlinMultiplatformExtension.() -> R) =
		extensions.getByType<KotlinMultiplatformExtension>().run(block)

val Project.isKotlinJvm get() = pluginManager.hasPlugin(sPluginKotlinJvm) // when applied `kotlin("jvm")`: ie. false for a project with the android plugin


// Project utils: shorten codes

class KotlinDependenciesScope(val project: Project)

fun Project.kotlinDependencies(block: KotlinDependenciesScope.() -> Unit) {
	kotlinDependencies.apply(block)
}

val Project.kotlinDependencies get() = KotlinDependenciesScope(this)

fun KotlinDependenciesScope.commonMain(block: KotlinDependencyHandler.() -> Unit): Unit =
		project.kotlinProjectExtension { sourceSets.named("commonMain") { dependencies(block) } }

fun KotlinDependenciesScope.commonTest(block: KotlinDependencyHandler.() -> Unit): Unit =
		project.kotlinProjectExtension { sourceSets.named("commonTest") { dependencies(block) } }

fun KotlinDependenciesScope.androidMain(block: KotlinDependencyHandler.() -> Unit): Unit =
		project.kotlinProjectExtension { sourceSets.named("androidMain") { dependencies(block) } }

fun KotlinDependenciesScope.androidTest(block: KotlinDependencyHandler.() -> Unit): Unit =
		project.kotlinProjectExtension { sourceSets.named("androidTest") { dependencies(block) } }

fun KotlinDependenciesScope.jvmMain(block: KotlinDependencyHandler.() -> Unit): Unit =
		project.kotlinProjectExtension { sourceSets.named("jvmMain") { dependencies(block) } }

fun KotlinDependenciesScope.jvmTest(block: KotlinDependencyHandler.() -> Unit): Unit =
		project.kotlinProjectExtension { sourceSets.named("jvmTest") { dependencies(block) } }
