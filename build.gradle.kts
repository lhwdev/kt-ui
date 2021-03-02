plugins {
	val kotlinVersion = "1.4.30"
	
	kotlin("multiplatform") version kotlinVersion apply false
	kotlin("jvm") version kotlinVersion apply false
	kotlin("plugin.serialization") version kotlinVersion apply false
	
	id("com.android.application") apply false
}

allprojects {
	repositories {
		jcenter()
		google()
		mavenCentral()
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
		maven("https://dl.bintray.com/jetbrains/intellij-plugin-service")
		maven("https://plugins.gradle.org/m2/")
	}
}
