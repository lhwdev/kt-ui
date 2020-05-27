pluginManagement {
	repositories {
		maven("https://dl.bintray.com/kotlin/kotlin-eap")
		
		mavenCentral()
		
		maven("https://plugins.gradle.org/m2/")
	}
}
rootProject.apply {
	name = "kt-ui"
}


include(":compiler-plugin", ":idea-plugin", ":test")
include(":core")

@Suppress("UnstableApiUsage")
enableFeaturePreview("GRADLE_METADATA")
include("kotlin-compile-test")
