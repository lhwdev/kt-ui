import com.lhwdev.build.*

plugins {
	kotlin("jvm")
	kotlin("kapt")
	
	id("common-plugin")
}


kotlin {
	setup()
}

tasks {
	named<Test>("test") {
		outputs.upToDateWhen { false }
		useJUnit()
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion")
	implementation("io.mockk:mockk:1.9.3")
	runtimeOnly("net.bytebuddy:byte-buddy:1.10.6")
	implementation("io.github.classgraph:classgraph:4.8.63")
}
