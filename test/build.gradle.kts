import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	kotlin("jvm")
	id("com.asm.build.common-plugin")
}

evaluationDependsOn(":compiler-plugin")


tasks.withType<KotlinCompile> {
	kotlinOptions.useIR = true
}

//mainClassName = "com.lhwdev.ktui.test.TestKt"

dependencies {
//	implementation(project(":core"))
	
	kotlinCompilerPluginClasspath(project(":compiler-plugin"))
//	
//	// kotlin
//	implementation(kotlin("stdlib-jdk8"))
//
//	// test
//	testImplementation(kotlin("test"))
//	testImplementation(kotlin("test-junit"))
//	testImplementation("io.mockk:mockk:1.9")
//	testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
}

//tasks.named<Jar>("jar") {
//	manifest {
//		attributes("Main-Class" to "com.lhwdev.ktui.test.TestKt")
//	}
//
//	from(configurations.runtimeClasspath.get().map { if(it.isDirectory) it else zipTree(it) })
//}
