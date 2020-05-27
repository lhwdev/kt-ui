plugins {
	kotlin("jvm")
	kotlin("kapt")
}


tasks {
	withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
	
	named<Test>("test") {
		outputs.upToDateWhen { false }
		useJUnit()
	}
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	
	implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${com.asm.build.kotlinVersion}")
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:${com.asm.build.kotlinVersion}")
	implementation(kotlin("test"))
	implementation(kotlin("test-junit"))
	implementation("io.mockk:mockk:1.9.3")
	runtimeOnly("net.bytebuddy:byte-buddy:1.10.6")
	implementation("io.github.classgraph:classgraph:4.8.63")
}
