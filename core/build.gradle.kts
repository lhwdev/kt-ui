plugins {
	kotlin("multiplatform")
	
	id("com.android.application")
	id("com.asm.build.common-plugin")
}


evaluationDependsOn(":compiler-plugin")


dependencies {
	kotlinCompilerPluginClasspath(project(":compiler-plugin"))
}
