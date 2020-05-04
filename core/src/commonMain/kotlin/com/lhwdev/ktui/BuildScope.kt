package com.lhwdev.ktui


typealias ElementCreator<T> = () -> Element<T>


val EMPTY = Any()

@Suppress("NOTHING_TO_INLINE")
inline fun internalKeyOf(attrs: Array<Any?>, keyIndex: Int) = if(keyIndex == -1) EMPTY else attrs[keyIndex]

fun internalResolveChanges(attrsBefore: Array<Any?>, attrs: Array<Any?>, stateInt: Int, isDirty: Boolean): Int {
	inline fun Int.stateAt(index: Int) = (this shr (index * 2)) and 0x3
	
	var changes = 0
	
	if(isDirty) changes = changes or 0x80000000.toInt() // 1000 0000 0000 0000 0000 0000 0000 0000
	
	for(i in attrs.indices) {
		val changedNow = when(val stateNow = stateInt.stateAt(i)) {
			0x0 /* 00 */ -> !stateChanged(attrsBefore[i], attrs[i])
			0x2 /* 10 */ -> false
			0x3 /* 11 */ -> true
			else -> error("Unexpected value $stateNow")
		}
		
		changes = changes or ((if(changedNow) 1 else 0) shl i)
	}
	
	return changes
}


expect fun stateChanged(a: Any?, b: Any?): Boolean

@Suppress("NOTHING_TO_INLINE")
inline fun arrayEquals(a: Any, b: Any) = when {
	a is Array<*> && b is Array<*> -> a.contentDeepEquals(b)
	a is ByteArray && b is ByteArray -> a.contentEquals(b)
	a is ShortArray && b is ShortArray -> a.contentEquals(b)
	a is IntArray && b is IntArray -> a.contentEquals(b)
	a is LongArray && b is LongArray -> a.contentEquals(b)
	a is FloatArray && b is FloatArray -> a.contentEquals(b)
	a is DoubleArray && b is DoubleArray -> a.contentEquals(b)
	a is CharArray && b is CharArray -> a.contentEquals(b)
	a is BooleanArray && b is BooleanArray -> a.contentEquals(b)
	
	a is UByteArray && b is UByteArray -> a.contentEquals(b)
	a is UShortArray && b is UShortArray -> a.contentEquals(b)
	a is UIntArray && b is UIntArray -> a.contentEquals(b)
	a is ULongArray && b is ULongArray -> a.contentEquals(b)
	
	else -> false
}

abstract class BuildScope {
	abstract val currentElement: Element<*>
	
	// entry point from the plugin
	abstract fun start(idState: Long, attrs: Array<Any?>, keyIndex: Int): Int
	
	abstract fun startTransactWithElement(id: Int, key: Any?): Element<*>?
	
	abstract fun <T> commitStartWithElement(last: Element<T>, state: T, isUpdated: Boolean)
	
	abstract fun <T> commitStartWithElementOnCreated(element: Element<T>, state: T)
	
	// TODO: Widget with return value is not skippable
	// entry point from the plugin
	abstract fun end(returnValue: Any?)
	
	// entry point from the plugin
	abstract fun end()
	
	// entry point from the plugin
	abstract fun endSkip(): Any?
	
	
	/// functions only available when currentElement is WidgetElements, or they will throw error
	
	abstract fun nextItem(): Any?
	
	abstract fun updateItem(item: Any?)
}


inline fun <T> BuildScope.startWithElement(id: Int, state: T, key: Any? = EMPTY, elementCreator: ElementCreator<T>) {
	@Suppress("UNCHECKED_CAST")
	val last = startTransactWithElement(id, key) as Element<T>?
	
	if(last == null) commitStartWithElementOnCreated(elementCreator(), state)
	else commitStartWithElement(last, state, stateChanged(last.state, state))
}
