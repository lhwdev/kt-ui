@file:Suppress("NOTHING_TO_INLINE")

package com.lhwdev.ktui.plugin.compiler

import java.io.*
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.CodingErrorAction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.min


private val logFile = File("D:/LHW/asm/app/new/com.asmx.core/compiler-test-template/all.log")

//private val logRemoteControl = Socket("localhost", 6424)
//private val logRemoteWriter = ObjectOutputStream(logRemoteControl.getOutputStream())
private val logFileWriter: Writer? =
	logFile.writer().apply {
		write("Begin log: " + Date().toString() + "\n")
	}
//	logRemoteControl.getOutputStream().writer()
//	null

private const val doIndentAutomatic = true
private val sDateFormat = SimpleDateFormat("mm:ss.SSS", Locale.ENGLISH)


private object LogOutWriter : Writer() {
	override fun write(cbuf: CharArray, off: Int, len: Int) {
		logInternalWithoutNewline(String(cbuf))
	}
	
	override fun flush() {}
	override fun close() {}
}

private object LogErrWriter : Writer() {
	override fun write(cbuf: CharArray, off: Int, len: Int) {
		logInternalWithoutNewline(String(cbuf), color = ConsoleColors.RED)
	}
	
	override fun flush() {}
	override fun close() {}
}

private val out: PrintStream
private val err: PrintStream
private val dummyInit = run {
	out = System.out
	err = System.err
//	System.setOut(PrintStream(WriterOutputStream(LogOutWriter)))
//	System.setErr(PrintStream(WriterOutputStream(LogErrWriter)))
	logFileWriter?.let { Runtime.getRuntime().addShutdownHook(thread(start = false) { it.close() }) }
	log("")
}


interface LogStream {
	fun doLog(content: Any?)
}

interface ColorLogStream : LogStream {
	fun doLog(content: Any?, color: String)
}


// TODO: close

data class LogConfig(val stackTrace: Array<StackTraceElement>? = null, val indents: Int? = null, val mute: Boolean = false, val isIndentFixed: Boolean = false) {
	fun merge(other: LogConfig) =
		LogConfig(
			stackTrace = other.stackTrace ?: stackTrace,
			indents = other.indents ?: indents,
			mute = other.mute || mute,
			isIndentFixed = other.isIndentFixed || isIndentFixed
		)
}

object LogConfigLocal : ThreadLocal<MutableList<LogConfig>>() {
	fun push(config: LogConfig): LogConfig {
		val value = get()
		val new = value.last().merge(config)
		value.add(new)
		return new
	}
	
	fun top() = get().last()
	
	fun pop(): LogConfig = get().run {
		removeAt(size - 1)
	}
	
	
	inline operator fun <R> invoke(config: LogConfig, block: () -> R): R {
		push(config)
		
		return try {
			block()
		} finally {
			pop()
		}
	}
	
	override fun initialValue() = mutableListOf(LogConfig(stackTrace = arrayOf(), indents = -1))
}


private enum class CompareStackTraceResult { same, descendant, ancestor, different }

@Suppress("NOTHING_TO_INLINE")
private inline fun isSameFunction(a: StackTraceElement, b: StackTraceElement) =
	a.classLoaderName == b.classLoaderName &&
		a.moduleName == b.moduleName &&
		a.fileName == b.fileName &&
		a.className == b.className &&
		a.methodName == b.methodName // not compare line number

private fun compareStackTrace(last: Array<StackTraceElement>?, current: Array<StackTraceElement>): CompareStackTraceResult {
	if(last == null) return CompareStackTraceResult.descendant
	
	// 1. find common
	val limit = min(last.size, current.size)
	var commonCount = limit
	for(i in 0 until limit) if(!isSameFunction(last[last.lastIndex - i], current[current.lastIndex - i])) {
		commonCount = i
		break
	}
	
	return when(commonCount) {
		last.size -> // current is the descendant of last
			if(commonCount == current.size) CompareStackTraceResult.same
			else CompareStackTraceResult.descendant
		current.size -> // current is the ancestor of last
			CompareStackTraceResult.ancestor
		else -> CompareStackTraceResult.different // different stack
	}
}

val logConfig get() = LogConfigLocal.top()

inline fun <R> muteLog(block: () -> R) =
	LogConfigLocal(LogConfig(mute = true), block)

inline fun <R> fixIndents(block: () -> R) =
	LogConfigLocal(LogConfig(isIndentFixed = true), block)

fun logln() {
	log("\n")
}

fun log(content: Any?) {
	logInternalWithoutNewline("$content\n")
}

fun log(content: Any?, color: String = "", prefix: String = "", suffix: String = "") {
	logInternalWithoutNewline(content = "$content\n", color = color, prefix = prefix, suffix = suffix)
}

fun logColor(content: Any?, color: String) {
	logInternalWithoutNewline(content.toString().replace(ConsoleColors.RESET, color) + "\n", color = color, suffix = ConsoleColors.RESET)
}

fun log2(content: Any?) =
	logColor(content, ConsoleColors.GREEN_BRIGHT)

fun log3(content: Any?) =
	logColor(content, ConsoleColors.CYAN_BRIGHT)

fun log4(content: Any?) =
	logColor(content, ConsoleColors.BLUE_BRIGHT)

fun log5(content: Any?) =
	logColor(content, ConsoleColors.YELLOW_BRIGHT)

fun log6(content: Any?) =
	logColor(content, ConsoleColors.RED_BRIGHT)


val logStream = object : LogStream {
	override fun doLog(content: Any?) {
		log(content)
	}
}

fun logStream(color: String) = object : LogStream {
	override fun doLog(content: Any?) {
		logColor(content, color)
	}
}

inline fun logIndent(block: () -> Unit) {
	LogConfigLocal(logConfig.let { it.copy(indents = it.indents!! + 1, isIndentFixed = true) }, block)
}

inline fun <T> T.withLog(): T {
	log(this)
	return this
}

inline fun <T> T.withLog(toString: (T) -> Any?): T {
	log(toString(this))
	return this
}

private var isLastNewline = true

fun logInternalWithoutNewline(content: String, color: String = ConsoleColors.RESET, prefix: String = "", suffix: String = "") {
//	print("P${content.length}>")
	if(content.isEmpty())
		return
	
	if(!isLastNewline && '\n' !in content) {
		printOut("$color$content")
		isLastNewline = false
		return
	}
	
	val lastConfig = logConfig
	val isIndentFixed = lastConfig.isIndentFixed
	val currentStackTraceLength: Int
	val indents: Int
	
	if(isIndentFixed) {
		currentStackTraceLength = lastConfig.stackTrace!!.size
		indents = lastConfig.indents!!
	} else {
		val currentStackTrace = Throwable().stackTrace.let { it.sliceArray(it.indexOfLast { item -> item.fileName == "log.kt" } + 1 until it.size) }
		currentStackTraceLength = currentStackTrace.size
		
		var compareResult: CompareStackTraceResult
		loop@ while(true) {
			compareResult = compareStackTrace(logConfig.stackTrace, currentStackTrace)
			
			when(compareResult) {
				CompareStackTraceResult.same -> LogConfigLocal.pop()
				CompareStackTraceResult.descendant -> break@loop
				CompareStackTraceResult.ancestor, CompareStackTraceResult.different -> LogConfigLocal.pop()
			}
		}
		
		val config = logConfig
		if(config.mute) return
		
		indents = if(doIndentAutomatic) {
			config.indents!! + 1
		} else 0
		val newConfig = LogConfig(stackTrace = currentStackTrace, indents = indents)
		LogConfigLocal.push(newConfig)
	}
	val text = content.split('\n')
	
	val header = "${ConsoleColors.WHITE}${sDateFormat.format(Date())} | $color${currentStackTraceLength.toStringFilling(3)}${ConsoleColors.WHITE} | $color$prefix" + (0 until indents).joinToString(separator = "") { "  " }
//	val header = headerPrefix
	val builder = StringBuilder()
	builder.append(/*if(text[0].isEmpty()) "" else*/ if(isLastNewline) header + text[0] + suffix else color + prefix + text[0] + suffix)
	val size = text.size
	builder.append('\n')
	for(i in 1 until size)
		text[i].let {
			if(it.isEmpty()) builder.append(it)
			else builder.append(header).append(it).append(suffix)
			builder.append('\n')
		}
	builder.setLength(builder.length - 1)
	val string = builder.toString()
	printOut(string)
	
	isLastNewline = content.last() == '\n'

//	logRemoteWriter.writeObject(currentStackTrace)
//	logRemoteWriter.writeUTF(string)
}

private fun printOut(text: String) {
	out.print(text)
	logFileWriter?.apply {
		write(text)
		flush()
	}
}

private fun Int.toStringFilling(i: Int): String {
	val string = toString()
	if(string.length >= i) return string
	val prefix = StringBuilder(i - string.length)
	for(i in 0 until i - string.length)
		prefix.append('0')
	return prefix.toString() + string
}


fun printlnWithColor(content: Any?, defaultColor: String = ConsoleColors.RESET) {
	val text = content.toString()
	val o = StringBuilder()
	var i = 0
	while(i != text.length) {
		
		when(val c = text[i]) {
			'\"' -> {
				o.append(ConsoleColors.GREEN_BRIGHT).append('\"')
				i++
				while(i < text.length && !(text[i] == '\"' && text.getOrNull(i - 1) != '\\'))
					o.append(text[i++])
			}
			
			else -> {
				val charColor = when(c) {
					in "[]{}.,;/?()" -> ConsoleColors.WHITE
					in "0123456789" -> ConsoleColors.RED_BRIGHT
					in "!@#$%^&*-+=" -> ConsoleColors.CYAN_BRIGHT
					else -> defaultColor
				}
				o.append("$charColor$c")
				
				i++
			}
		}
	}
	o.append(ConsoleColors.RESET)
	log(o)
}


class WriterOutputStream(writer: Writer, decoder: CharsetDecoder, bufferSize: Int, writeImmediately: Boolean) : OutputStream() {
	private val writer: Writer
	private val decoder: CharsetDecoder
	private val writeImmediately: Boolean
	private val decoderIn: ByteBuffer
	private val decoderOut: CharBuffer
	
	constructor(writer: Writer, decoder: CharsetDecoder) : this(writer, decoder, 1024, false)
	constructor(writer: Writer, charset: Charset, bufferSize: Int, writeImmediately: Boolean) : this(writer, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), bufferSize, writeImmediately)
	constructor(writer: Writer, charset: Charset) : this(writer, charset, 1024, false)
	constructor(writer: Writer, charsetName: String?, bufferSize: Int, writeImmediately: Boolean) : this(writer, Charset.forName(charsetName), bufferSize, writeImmediately)
	constructor(writer: Writer, charsetName: String?) : this(writer, charsetName, 1024, false)
	
	constructor(writer: Writer) : this(writer, Charset.defaultCharset() as Charset, 1024, false)
	
	@Throws(IOException::class)
	override fun write(b: ByteArray, off: Int, len: Int) {
		var off = off
		var len = len
		while(len > 0) {
			val c = min(len, decoderIn.remaining())
			decoderIn.put(b, off, c)
			processInput(false)
			len -= c
			off += c
		}
		if(writeImmediately) {
			flushOutput()
		}
	}
	
	@Throws(IOException::class)
	override fun write(b: ByteArray) {
		this.write(b, 0, b.size)
	}
	
	@Throws(IOException::class)
	override fun write(b: Int) {
		this.write(byteArrayOf(b.toByte()), 0, 1)
	}
	
	@Throws(IOException::class)
	override fun flush() {
		flushOutput()
		writer.flush()
	}
	
	@Throws(IOException::class)
	override fun close() {
		processInput(true)
		flushOutput()
		writer.close()
	}
	
	@Throws(IOException::class)
	private fun processInput(endOfInput: Boolean) {
		decoderIn.flip()
		while(true) {
			val coderResult = this.decoder.decode(decoderIn, decoderOut, endOfInput)
			if(!coderResult.isOverflow) {
				if(coderResult.isUnderflow) {
					decoderIn.compact()
					return
				} else {
					throw IOException("Unexpected coder result")
				}
			}
			flushOutput()
		}
	}
	
	@Throws(IOException::class)
	private fun flushOutput() {
		if(decoderOut.position() > 0) {
			writer.write(decoderOut.array(), 0, decoderOut.position())
			decoderOut.rewind()
		}
	}
	
	init {
		decoderIn = ByteBuffer.allocate(128)
		this.writer = writer
		this.decoder = decoder
		this.writeImmediately = writeImmediately
		decoderOut = CharBuffer.allocate(bufferSize)
	}
}
