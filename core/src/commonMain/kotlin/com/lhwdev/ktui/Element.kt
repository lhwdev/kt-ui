package com.lhwdev.ktui

import com.lhwdev.ktui.elements.AmbientElement
import com.lhwdev.ktui.utils.assert


val sInternalEmptyAttrs = emptyArray<Any?>()


abstract class Element<T> : DiagnoseTree {
	var id: Int = 0
		private set
	
	private var privateState: T? = null
	
	@Suppress("UNCHECKED_CAST")
	val state: T get() = privateState as T
	
	private var privateKey: Any? = EMPTY
	
	val key: Any? get() = privateKey
	
	
	enum class LifecycleState { created, initialized, attached, detached }
	
	var lifecycleState: LifecycleState = LifecycleState.created
	
	lateinit var root: Root
		private set
	
	var parent: Element<*>? = null
		private set
	
	abstract val children: List<Element<*>>
	
	var isDirty: Boolean = true
		private set
	
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
	
	
	/// Ambients
	
	private lateinit var ambients: Set<AmbientElement<*>>
	
	// divided to thisAmbient and childrenAmbient to prevent recursive problem
	internal open fun updateAmbients(thisAmbient: Set<AmbientElement<*>>, childrenAmbient: Set<AmbientElement<*>>) {
		ambients = thisAmbient
		visitChildren { it.updateAmbients(childrenAmbient, childrenAmbient) }
	}
	
	@Suppress("UNCHECKED_CAST")
	fun <T> ambient(ambient: Ambient<T>): T {
		for(element in ambients)
			if(element.ambient === ambient) return element.getValue(this) as T
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
