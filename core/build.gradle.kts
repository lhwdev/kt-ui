import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	kotlin("multiplatform")
	
	id("com.android.application")
	id("com.asm.build.common-plugin")
}


//evaluationDependsOn(":compiler-plugin")


tasks.withType<KotlinCompile> {
	kotlinOptions {
//		useIR = true
	}
}


dependencies {
//	kotlinCompilerPluginClasspath(project(":compiler-plugin"))
}
