package com.asm.build

import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.kotlin


// com.asm.build.common-plugin
// When using this plugin, you should rename main and test into androidMain and androidTest if using kotlin multiplatform
// this should be applied after all the other plugins such as kotlin or android
class CommonPlugin : Plugin<Project> {
	override fun apply(target: Project): Unit = with(target) {
		if(isKotlin) configureKotlin()
		if(isAndroid) configureAndroid()
	}
}


private fun Project.configureKotlin(): Unit = kotlinProjectExtension {
	sourceSets {
		all {
			with(languageSettings) {
				useExperimentalAnnotation("kotlin.RequiresOptIn")
				useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
				useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
				enableLanguageFeature("InlineClasses")
			}
		}
		
	}
	
	if(isKotlinMultiplatform) configureKotlinMultiplatform()
	
	if(isKotlinJvm) configureKotlinJvm()
}

private fun Project.configureKotlinMultiplatform() {
	kotlinMutliplatformExtension {
		if(isAndroid) android()
	}
	
	kotlinDependencies {
		// all common dependencies
		commonMain {
			// kotlin
			implementation(kotlin("stdlib-common"))
			
			// coroutine - don't force users to use it
			compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutine")
		}
		
		if(isAndroid) {
			// TODO: split into jvmMain & androidMain after this is solved
			// https://youtrack.jetbrains.com/issue/KT-27801
			// jvmMain {
			androidMain {
				implementation(kotlin("stdlib-jdk8"))
				
				// coroutine
				compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine")
			}
			
			// jvmTest {
			androidTest {
				// include coroutine due to compilation errors
				implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine")
				
				implementation(kotlin("test-junit"))
				
				// mockk
				implementation("io.mockk:mockk:1.9.3")
			}
		}
	}
}

private fun Project.configureKotlinJvm() {
	dependencies {
			"implementation"(kotlin("stdlib-jdk8"))
			
			// coroutine
			"compileOnly"("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine")
		
		
			// include coroutine due to compilation errors
			"testImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine")
			
			"testImplementation"(kotlin("test-junit"))
			
			// mockk
			"testImplementation"("io.mockk:mockk:1.9.3")
		
	}
}


private fun Project.configureAndroid() {
	val isLibrary = isAndroidLibrary
	
	androidExtension {
		buildToolsVersion = "29.0.3"
		
		compileSdkVersion(COMPILE_SDK_VERSION)
		
		defaultConfig {
			minSdkVersion(MIN_SDK_VERSION)
			targetSdkVersion(TARGET_SDK_VERSION)
			versionCode = VERSION_CODE
			versionName = VERSION_NAME
			
			testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		}
		
		compileOptions {
			sourceCompatibility = JavaVersion.VERSION_1_8
			targetCompatibility = JavaVersion.VERSION_1_8
		}
		
		buildTypes {
			named("release") {
				if(isLibrary) {
//					consumerProguardFiles(asmProguardPath) // already brought by a non-library project
				} else {
					isMinifyEnabled = true
					proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), asmProguardPath)
				}
			}
		}
	}
	
	configurations.all {
		resolutionStrategy {
			force("org.objenesis:objenesis:2.6") // https://github.com/mockk/mockk/issues/281
		}
	}
	
	if(isKotlinMultiplatform) configureAndroidWithMultiplatform()
}

private fun Project.configureAndroidWithMultiplatform() {
	androidExtension {
		sourceSets {
			fun AndroidSourceSet.defaults(sourceType: String) {
				val root = "src/$sourceType"
				setRoot(root)
				java.setSrcDirs(setOf("$root/kotlin"))
			}
			
			// See notes #3
			named("main") {
				defaults("androidMain")
			}
			named("androidTest") {
				defaults("androidTest")
			}
		}
	}
	
	kotlinDependencies {
		androidTest {
			// androidx.test
			
			// core library
			implementation("androidx.test:core:1.0.0")
			
			// AndroidJUnitRunner and JUnit Rules
			implementation("androidx.test:runner:1.1.0")
		}
	}
}
