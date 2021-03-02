import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.lhwdev.build.*


plugins {
	kotlin("jvm")
	id("org.jetbrains.intellij") version "0.6.5"
	id("com.github.johnrengelman.shadow") version "5.2.0"
	
	id("common-plugin")
}

kotlin {
	setup()
}


dependencies {
	implementation(project(":compiler-plugin"))
	
	compileOnly("org.jetbrains.kotlin:kotlin-compiler:$kotlinVersion")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	pluginName = "kt-ui"
	version = "2020.3.2"
	
	// https://plugins.jetbrains.com/plugin/6954-kotlin/versions/stable
	setPlugins("java", "org.jetbrains.kotlin:203-1.4.30-release-IJ7148.5")
}

tasks {
	runIde {
		classpath = files(named("shadowJar"))
	}
	
	named<ShadowJar>("shadowJar") {
		configurations = listOf(project.configurations.runtimeClasspath.get())
	}
	
	patchPluginXml {
//		sinceBuild("193.5233")
//		untilBuild("202.*")
		
		changeNotes("""
    	<ul>
			<li>Added </li>
		</ul>
	 """)
	}

//	named<Jar>("jar") {
//		finalizedBy("shadowJar")
//	}
	
	compileKotlin {
		kotlinOptions.jvmTarget = "1.8"
	}
	
	compileTestKotlin {
		kotlinOptions.jvmTarget = "1.8"
	}
}
