plugins {
	`kotlin-dsl`
}


repositories {
	jcenter()
	google()
	mavenCentral()
//	maven("https://dl.bintray.com/kotlin/kotlin-eap")
}


dependencies {
	implementation("com.android.tools.build:gradle:4.0.2") // up-to-date-androidGradleBuildTool
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20") // up-to-date-kotlinVersion
}
