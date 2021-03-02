package com.lhwdev.ktui

import io.github.classgraph.ClassGraph
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.ManualLanguageFeatureSetting
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.cli.jvm.plugins.ServiceLoaderLite
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.incremental.classpathAsList
import java.io.File
import java.net.URI
import java.nio.file.Paths
import kotlin.test.Test


//@Suppress("MemberVisibilityCanBePrivate")

const val DEST = "D:\\LHW\\asm\\app\\new\\com.asmx.core\\compiler-test-template\\dest"


fun printlnColor(text: Any?, color: String) {
	text.toString().split('\n').forEach { println("$color$it${ConsoleColors.RESET}") }
}


class Test {
	@Test
	fun `test compiler`() {
		File(DEST).run {
			deleteRecursively()
			mkdir()
		}
		var has = false
		
		K2JVMCompiler().exec(object : MessageCollector {
			
			override fun clear() {
				has = false
			}
			
			override fun hasErrors() = has
			
			override fun report(
				severity: CompilerMessageSeverity,
				message: String,
				location: CompilerMessageSourceLocation?
			) {
				
				if(severity == CompilerMessageSeverity.ERROR || severity == CompilerMessageSeverity.EXCEPTION) has =
					true
				val color = when(severity) {
					CompilerMessageSeverity.ERROR -> ConsoleColors.RED
					CompilerMessageSeverity.EXCEPTION -> ConsoleColors.RED_BOLD
					CompilerMessageSeverity.INFO -> ConsoleColors.GREEN
					CompilerMessageSeverity.LOGGING -> ConsoleColors.WHITE
					CompilerMessageSeverity.OUTPUT -> ConsoleColors.WHITE_BRIGHT
					CompilerMessageSeverity.STRONG_WARNING -> ConsoleColors.YELLOW
					CompilerMessageSeverity.WARNING -> ConsoleColors.YELLOW_BRIGHT
				}
				
				printlnColor("[KT] $message", color)
				
				if(location != null)
					println(">> at $location")
			}
		}, Services.EMPTY, K2JVMCompilerArguments().apply {
			useIR = true
			classpathAsList = mutableListOf<File>().apply {
				val hostClasspaths = getHostClasspaths().also { println(it) }
				add(findInHostClasspath(hostClasspaths, "kotlin-stdlib.jar",
					Regex("(kotlin-stdlib|kotlin-runtime)(-[0-9]+\\.[0-9]+(\\.[0-9]+)?)([-0-9a-zA-Z]+)?\\.jar"))!!)
				add(findInHostClasspath(hostClasspaths, "kotlin-stdlib-jdk*.jar",
					Regex("kotlin-stdlib-jdk[0-9]+(-[0-9]+\\.[0-9]+(\\.[0-9]+)?)([-0-9a-zA-Z]+)?\\.jar"))!!)
				add(findInHostClasspath(hostClasspaths, "kotlin-stdlib-common.jar",
					Regex("kotlin-stdlib-common(-[0-9]+\\.[0-9]+(\\.[0-9]+)?)([-0-9a-zA-Z]+)?\\.jar"))!!)
			}
			
			noStdlib = true
			pluginClasspaths = arrayOf(getResourcesPath())
			destination = DEST
			includeRuntime = true
			internalArguments = listOf(LanguageFeatureSetting(LanguageFeature.NonParenthesizedAnnotationsOnFunctionalTypes, LanguageFeature.State.ENABLED))
			
			freeArgs = listOf(
//				"""
//				"D:/LHW/asm/app/new/com.asmx.core/compiler-test-template/source"
//			""".trimIndent(),
				*File("D:\\LHW\\asm\\app\\new\\com.asmx.core\\ui\\test\\src\\main\\kotlin").listFilesRecursive().map(File::getAbsolutePath).toTypedArray(),
//				*File("D:\\LHW\\asm\\app\\new\\com.asmx.core\\ui\\core\\src\\commonMain\\kotlin").listFilesRecursive().map(File::getAbsolutePath).toTypedArray(),
//				*File("D:\\LHW\\asm\\app\\new\\com.asmx.core\\ui\\core\\src\\androidMain\\kotlin").listFilesRecursive().map(File::getAbsolutePath).toTypedArray(),
			)
		})
		
		if(has) throw object : IllegalStateException("Compilation exited with any error(s)") {
			override fun fillInStackTrace() = this
		}
//		val result = KotlinCompilation().apply {
//			sources = listOf(kotlinSource)
//
//			compilerPlugins = listOf(UiComponentRegistrar())
//			commandLineProcessors = listOf(UiCommandLineProcessor())
//
//			inheritClassPath = true
//			messageOutputStream = System.out
//			kotlincArguments = listOf("-Xuse-ir")
//		}.compile()
//		log(result.compiledClassAndResourceFiles.joinToString { it.toString() })
////		log(result.classLoader.loadClass("AbcKt").getMethod("Main").toString())
////		val kClazz = result.classLoader.loadClass("AbcKt")
////		log(kClazz.getMethod("abc").invoke(kClazz.getConstructor().newInstance()))
//
	}
	
	private fun getResourcesPath(): String {
		val resourceName = "META-INF/services/org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar"
		return this::class.java.classLoader.getResources(resourceName)
			.asSequence()
			.mapNotNull { url ->
				val uri = URI.create(url.toString().removeSuffix("/$resourceName"))
				when(uri.scheme) {
					"jar" -> Paths.get(URI.create(uri.schemeSpecificPart.removeSuffix("!")))
					"file" -> Paths.get(uri)
					else -> return@mapNotNull null
				}.toAbsolutePath()
			}
//			.also { it.joinToString { it.toString() }.also { log(it) } }
			.find { resourcesPath ->
				ServiceLoaderLite.findImplementations(ComponentRegistrar::class.java, listOf(resourcesPath.toFile())).isNotEmpty()
//					.any { implementation -> implementation == IrDumper::class.java.name }
			}!!.toString()
		
	}
	
	private fun getHostClasspaths(): List<File> {
		val classGraph = ClassGraph()
			.enableSystemJarsAndModules()
			.removeTemporaryFilesAfterScan()
		
		val classpaths = classGraph.classpathFiles
		val modules = classGraph.modules.mapNotNull { it.locationFile }
		
		return (classpaths + modules).distinctBy(File::getAbsolutePath)
	}
	
	private fun findInHostClasspath(hostClasspaths: List<File>, simpleName: String, regex: Regex): File? {
		val jarFile = hostClasspaths.firstOrNull { classpath ->
			classpath.name.matches(regex)
			//TODO("check that jar file actually contains the right classes")
		}

//		if(jarFile == null)
//			println("Searched host classpaths for $simpleName and found no match")
//		else
//			println("Searched host classpaths for $simpleName and found ${jarFile.path}")
		
		return jarFile
	}
}

private fun File.listFilesRecursive(): List<File> {
	val list = mutableListOf<File>()
	listFiles()!!.forEach { if(it.isFile) list += it else list += it.listFilesRecursive() }
	return list
}


fun LanguageFeatureSetting(languageFeature: LanguageFeature, state: LanguageFeature.State) =
	ManualLanguageFeatureSetting(languageFeature, state, "-XXLanguage:${when(state) {
		LanguageFeature.State.ENABLED -> '+'
		LanguageFeature.State.DISABLED -> '-'
		else -> error("unsupported state: $state")
	}
	}$languageFeature")
