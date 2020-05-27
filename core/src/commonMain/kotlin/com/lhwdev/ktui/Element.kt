package com.lhwdev.ktui

import com.lhwdev.ktui.elements.AmbientElement
import com.lhwdev.ktui.utils.assert
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


val sInternalEmptyAttrs = emptyArray<Any?>()


@Suppress("NOTHING_TO_INLINE")
inline fun <S, T> Element<S>.stateCache(noinline block: (S) -> T) = StateCacheProperty(this, block)

class StateCacheProperty<S, T>(val element: Element<S>, val block: (S) -> T) : ReadOnlyProperty<Any?, T> {
	private var lastKey = 0
	private var isInitialized = false
	private var value: T? = null
	
	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		if(!isInitialized || element.stateKey != lastKey) {
			value = block(element.state)
			lastKey = element.stateKey
			isInitialized = true
		}
		
		@Suppress("UNCHECKED_CAST")
		return value as T
	}
}


abstract class Element<T> : DiagnoseTree {
	var id: Int = 0
		private set
	
	private var privateState: T? = null
	
	@Suppress("UNCHECKED_CAST")
	val state: T
		get() = privateState as T
	
	var stateKey = 0
		private set
	
	private var privateKey: Any? = EMPTY
	
	val key: Any? get() = privateKey
	
	internal var index = 0 // TODO: make slot table flat, not nested
	
	
	enum class LifecycleState { created, initialized, attached, detached }
	
	var lifecycleState: LifecycleState = LifecycleState.created
		private set
	
	lateinit var root: Root
		private set
	
	var parent: Element<*>? = null
		private set
	
	abstract val children: List<Element<*>>
	
	var isDirty: Boolean = true
		private set
	
	val isAttached: Boolean get() = lifecycleState == LifecycleState.attached
	
	private sealed class Event {
		object Attach : Event()
		object Update : Event()
		class LifecycleUpdate(val newState: LifecycleState) : Event()
	}
	
	private val pendingEvents = mutableListOf<Event>() // TODO: change to BooleanArray: performance!
	
	inline fun visitChildren(block: (child: Element<*>) -> Unit) {
		children.forEach(block)
	}
	
	
	/// Building
	
	internal fun initialize(id: Int, state: T, root: Root, parent: Element<*>?) { // called on building
		this.id = id
		this.privateState = state
		this.root = root
		this.parent = parent
		
		changeLifecycleToState(LifecycleState.initialized)
	}
	
	fun requestRebuild() {
		isDirty = true
		root.requestRebuild(this)
	}
	
	private fun changeLifecycleToState(state: LifecycleState) {
		lifecycleState = state
		onLifecycleStateChanged(state)
	}
	
	private fun pendEvent(event: Event) {
		pendingEvents += event
	}
	
	fun appendChild(child: Element<*>) {
		insertChild(children.size, child)
	}
	
	abstract fun insertChild(index: Int, child: Element<*>)
	
	abstract fun removeChild(index: Int)
	
	fun removeChild(child: Element<*>) {
		val index = children.indexOf(child)
		assert(index != -1)
		removeChild(index)
	}
	
	fun removeChildren() {
		for(i in children.indices)
			removeChild(i)
	}
	
	open fun setChild(index: Int, child: Element<*>) {
		removeChild(index)
		insertChild(index, child)
	}
	
	
	// called while rebuilding
	
	// #1. on insertChild()
	open fun attach() { // recursive
		pendEvent(Event.Attach)
		
		// TODO: should be recursive?
		visitChildren { it.attach() }
	}
	
	// #2. on commitStart~()
	// don't need to be recursive: done by BuildScope
	open fun stateUpdated(newState: T) {
		pendingEvents += Event.Update
		privateState = newState
		isDirty = true
		stateKey++
		
		onRebuilding()
	}
	
	open fun skipBuilding() {
		onRebuilding()
	}
	
	internal fun detach() { // recursive
		onDispose()
		changeLifecycleToState(LifecycleState.detached)
		
		visitChildren { it.detach() }
	}
	
	
	// called after rebuilding
	
	internal fun rebuilt() { // recursive
		// 1. clear dirty status
		isDirty = false
		
		// 2. onRebuilt
		onRebuilt()
		
		// 3. call pending events
		if(pendingEvents.isNotEmpty()) {
			pendingEvents.forEach {
				when(it) {
					Event.Attach -> {
						onAttach()
						changeLifecycleToState(LifecycleState.attached)
					}
					Event.Update -> onUpdate()
					is Event.LifecycleUpdate -> onLifecycleStateChanged(it.newState)
				}
			}
			pendingEvents.clear()
		}
		
		// 4. children
		visitChildren { it.rebuilt() }
	}
	
	// listeners
	
	
	open fun onLifecycleStateChanged(lifecycleState: LifecycleState) {}
	
	open fun onAttach() {}
	
	open fun onUpdate() {}
	
	// calling this happens while building
	open fun onRebuilding() {}
	
	open fun onRebuilt() {}
	
	// calling this happens while building
	open fun onDispose() {}
	
	
	fun emit(key: Any?, event: Any): Boolean = onEvent(key, event)
	
	open fun onEvent(key: Any?, event: Any): Boolean {
		visitChildren { if(it.onEvent(key, event)) return true }
		return false
	}
	
	
	/// Ambients
	
	private lateinit var ambients: Set<AmbientElement<*>>
	
	// divided to thisAmbient and childrenAmbient to prevent recursive problem
	internal open fun updateAmbients(thisAmbient: Set<AmbientElement<*>>, childrenAmbient: Set<AmbientElement<*>>) {
		ambients = thisAmbient
		visitChildren { it.updateAmbients(childrenAmbient, childrenAmbient) }
	}
	
	fun <T> ambient(ambient: Ambient<T>): T {
		for(element in ambients)
			@Suppress("UNCHECKED_CAST")
			element.getValue(ambient)?.let { return it.get(this) as T }
		return (ambient.defaultValue ?: error("No ambient found: $ambient")).invoke()
	}
//	private lateinit var ambients: Set<AmbientData<*>>
//
//	open fun updateAmbients(parentAmbients: Set<AmbientData<*>>) {
//		ambients = parentAmbients
//
//		visitChildren { child ->
//			child.updateAmbients(ambients)
//		}
//	}
//
//	private val dependencyAmbients by lazy { mutableSetOf<AmbientData<*>>() }
//
//	private fun findAmbient(ambient: Ambient<*>) = ambients.find { ambient === it.ambient }
//
//	override fun <T : Any> ambient(ambient: Ambient<T>) = findAmbient(ambient)?.let {
//		dependencyAmbients.plusAssign(it)
//		@Suppress("UNCHECKED_CAST")
//		it.value as T?
//	} ?: ambient.defaultValue!!
//
//	@Suppress("UNCHECKED_CAST")
//	override fun <T : Any> peekAmbient(ambient: Ambient<T>) =
//		findAmbient(ambient)?.value as T? ?: ambient.defaultValue!!
	
	override fun diagnose(): List<DiagnoseNode> {
		TODO()
	}
	
}
