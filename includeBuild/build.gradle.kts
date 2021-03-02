plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
}


group = "com.lhwdev.include-build"
version = "SNAPSHOT"

repositories {
	mavenCentral()
	google()
	jcenter()
}

gradlePlugin {
	plugins.register("common-plugin") {
		id = "common-plugin"
		implementationClass = "com.lhwdev.build.CommonPlugin"
	}
}

dependencies {
	compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
	compileOnly("com.android.tools.build:gradle:4.0.2")
}
