package com.asm.build

import org.gradle.api.Project


// up-to-date: to settings.gradle.kts of buildSrc

// android build variants
const val COMPILE_SDK_VERSION = 29
const val MIN_SDK_VERSION = 15
const val TARGET_SDK_VERSION = 29
const val VERSION_CODE = 1
const val VERSION_NAME = "0.0.1" // we use semantic versioning

// versions

// kotlin
const val kotlinVersion = "1.4-M1" // up-to-date-kotlinVersion
const val coroutine = "1.3.5"

// android build tool
const val androidGradleBuildTool = "3.5.3" // up-to-date-androidGradleBuildTool


val Project.asmProguardPath get() = "$rootDir/asm/asm-proguard-files.pro"
