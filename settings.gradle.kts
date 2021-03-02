rootProject.apply {
	name = "kt-ui"
}


includeBuild("includeBuild")

include(":compiler-plugin", ":idea-plugin", ":test")
include(":core")

include("kotlin-compile-test")


pluginManagement {
	repositories {
		gradlePluginPortal()
		google()
	}
	
	resolutionStrategy.eachPlugin {
		if(requested.id.id == "com.android.application") {
			useModule("com.android.tools.build:gradle:4.0.2") // sync with includeBuild/build.gradle.kts
		}
	}
}

