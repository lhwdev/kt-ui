import com.asm.build.kotlinVersion
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
	kotlin("jvm")
	id("org.jetbrains.intellij")
	id("com.github.johnrengelman.shadow") version "5.2.0"
}


dependencies {
	implementation(kotlin("stdlib-jdk8"))
	
	implementation(project(":compiler-plugin"))
	
	compileOnly("org.jetbrains.kotlin:kotlin-compiler:$kotlinVersion")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	pluginName = "kt-ui"
	version = "2020.1"
	
	setPlugins("java", "org.jetbrains.kotlin:1.4-M1-release-IJ2020.1-1@eap-next")
}

//afterEvaluate {
//	configurations.forEach { println("\u001B[1m${it.name}\u001B[0m ${it.isCanBeResolved} ${it.extendsFrom.joinToString { "\u001B[1;31m${it.name}\u001B[0m" }} | ${it.allDependencies.joinToString { with(it) { "\u001B[0;32m$group:\u001B[1;34m$name\u001B[0m" } }} // ${if(it.isCanBeResolved)it.resolve().joinToString { "\u001B[92m${it.name}\u001B[0m" } else ""}") }
//}
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
