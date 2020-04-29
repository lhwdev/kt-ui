package com.lhwdev.ktui.utils


inline fun Exception(block: ExceptionBuilder.() -> Unit): Exception = with(ExceptionBuilder()) {
	block()
	DescribedException(describes, data)
}

inline fun exception(block: ExceptionBuilder.() -> Unit): Nothing = throw Exception(block)


class DescribedException(traces: List<DescribeNode>, data: Map<String, Any?>?) : Exception() {
	override val message: String by lazy {
		traces.joinToString(separator = "\n") {
			when(it) {
				is DescribeNode.Trace -> it.throwable.toString()
				is DescribeNode.Text -> it.text
			}
		}.let { text ->
			data?.let { text + "\n\n" + it.entries.joinToString { (k, v) -> "$k = $v" } } ?: text
		}
	}
}

class ExceptionBuilder {
	@PublishedApi
	internal val describes = mutableListOf<DescribeNode>()
	
	var data: MutableMap<String, Any?>? = null
	
	
	operator fun String.unaryPlus() {
		describes += DescribeNode.Text(this)
	}
	
	operator fun Throwable.unaryPlus() {
		describes += DescribeNode.Trace(this)
	}
	
	fun data(vararg data: Pair<String, Any?>) {
		this.data = mutableMapOf(*data)
	}
}


sealed class DescribeNode {
	class Text(val text: String) : DescribeNode()
	class Trace(val throwable: Throwable) : DescribeNode()
}
