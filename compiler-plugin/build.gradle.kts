import com.lhwdev.build.*


plugins {
	kotlin("jvm")
	kotlin("kapt")
//	`java-gradle-plugin`
//	`maven-publish`
	
	id("common-plugin")
}

kotlin {
	setup()
}

//gradlePlugin {
//	plugins {
//		create("ktUiPlugin") {
//			id = "com.lhwdev.ktui.plugin"
//			implementationClass = "com.lhwdev.ktui.compilerPlugin.UiGradlePlugin"
//		}
//	}
//}


tasks.named<Test>("test") {
	outputs.upToDateWhen { false }
	useJUnit()
}

//publishing {
//	publications {
//		create<MavenPublication>("maven") {
//			groupId = "com.lhwdev.ktui"
//			artifactId = "compiler-plugin"
//			version = compilerPluginVersion
//
//			from(components["java"])
//		}
//	}
//}


dependencies {
	compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
	compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion")
	
	compileOnly(gradleApi())
	
	compileOnly("com.google.auto.service:auto-service:1.0-rc4")
	kapt("com.google.auto.service:auto-service:1.0-rc4")
//	compileOnly("org.jetbrains.kotlin:kotlin-intellij-core:$kotlinVersion")
//	compileOnly("org.jetbrains.kotlin:kotlin-platform-api:$kotlinVersion")
	
	// kotlin
	implementation(kotlin("reflect"))
	
	// test
	testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
	testImplementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion")
	testImplementation(kotlin("test"))
	testImplementation(kotlin("test-junit"))
	testImplementation("io.mockk:mockk:1.9.3")
	testRuntimeOnly("net.bytebuddy:byte-buddy:1.10.6")
	testImplementation("io.github.classgraph:classgraph:4.8.63")
//	testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
}
