// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
	repositories {
		jcenter()
		google()
		mavenCentral()
		maven("https://dl.bintray.com/kotlin/kotlin-eap")
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
		maven("https://dl.bintray.com/jetbrains/intellij-plugin-service")
	}
	
	dependencies {
		classpath("com.android.tools.build:gradle:${com.asm.build.androidGradleBuildTool}")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${com.asm.build.kotlinVersion}")
		classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:0.5.0-SNAPSHOT")
	}
}

allprojects {
	buildscript {
		repositories {
			jcenter()
			google()
			mavenCentral()
			maven("https://dl.bintray.com/kotlin/kotlin-eap")
			maven("https://oss.sonatype.org/content/repositories/snapshots/")
			maven("https://dl.bintray.com/jetbrains/intellij-plugin-service")
		}
	}
	
	repositories {
		jcenter()
		google()
		mavenCentral()
		maven("https://dl.bintray.com/kotlin/kotlin-eap")
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
		maven("https://dl.bintray.com/jetbrains/intellij-plugin-service")
		maven("https://plugins.gradle.org/m2/")
	}
}
