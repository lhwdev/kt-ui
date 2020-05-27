package com.lhwdev.ktui

import com.lhwdev.ktui.elements.WidgetElement


val EMPTY = Any()

@Suppress("NOTHING_TO_INLINE")
inline fun internalKeyOf(attrs: Array<Any?>, keyIndex: Int) =
	if(keyIndex == -1) EMPTY else attrs[keyIndex]

fun internalResolveChanges(attrsBefore: Array<Any?>, attrs: Array<Any?>, stateInt: Int, isDirty: Boolean): Int {
	fun Int.stateAt(index: Int) = (this shr (index * 2)) and 0x3
	
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


// Widget implementation v3

abstract class BuildScope {
	abstract val currentElement: Element<*>
	
	// entry point from the plugin
	abstract fun start(idState: Long, attrs: Array<Any?>, keyIndex: Int): Int
	
	abstract fun startTransactWithElement(id: Int, key: Any?): Element<*>?
	
	abstract fun <T> commitStartWithElement(last: Element<T>, state: T, isUpdated: Boolean)
	
	abstract fun <T> commitStartWithElementOnCreated(element: Element<T>, state: T)
	
	// entry point from the plugin
	abstract fun end()
	
	
	// entry point from the plugin
	abstract fun startExpr(id: Long)
	
	// entry point from the plugin
	abstract fun endExpr()
	
	
	/// functions only available when currentElement is WidgetElements, or they will throw error
	
	abstract fun nextItem(): Any?
	
	abstract fun updateItem(item: Any?)
}


inline fun <S, E : Element<S>> BuildScope.startWithElement(id: Int, state: S, key: Any? = EMPTY, elementCreator: () -> E): E {
	@Suppress("UNCHECKED_CAST")
	val last = startTransactWithElement(id, key) as E?
		?: return elementCreator().also { commitStartWithElementOnCreated(it, state) }
	
	commitStartWithElement(last, state, stateChanged(last.state, state))
	return last
}

inline fun BuildScope.startWithElement(id: Int, key: Any? = EMPTY, elementCreator: () -> Element<Unit>) {
	startWithElement(id, Unit, key, elementCreator)
}

inline fun <S, E : Element<S>> BuildScope.element(id: Int, state: S, key: Any? = EMPTY, elementCreator: () -> E, block: E.() -> Unit) {
	startWithElement(id, state, key, elementCreator).block()
	end()
}

inline fun <E : Element<Unit>> BuildScope.element(id: Int, key: Any? = EMPTY, elementCreator: () -> E, block: E.() -> Unit) =
	element(id, Unit, key, elementCreator, block)

inline fun BuildScope.widget(id: Int, key: Any? = EMPTY, block: WidgetElement.() -> Unit) =
	widgetElement(id, key, ::WidgetElement, block)

inline fun <E : Element<Array<Any?>>> BuildScope.widgetElement(id: Int, key: Any? = EMPTY, elementCreator: () -> E, block: E.() -> Unit) =
	element(id, sInternalEmptyAttrs, key, elementCreator, block)

inline fun <S, E : Element<S>> BuildScope.leafElement(id: Int, state: S, key: Any? = EMPTY, elementCreator: () -> E): E {
	val element = startWithElement(id, state, key, elementCreator)
	end()
	return element
}

inline fun <E : Element<Unit>> BuildScope.leafElement(id: Int, key: Any? = EMPTY, elementCreator: () -> E) =
	leafElement(id, Unit, key, elementCreator)
