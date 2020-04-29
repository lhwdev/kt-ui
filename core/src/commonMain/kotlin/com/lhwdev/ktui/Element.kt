package com.lhwdev.ktui


val sEmptyAttrs = emptyArray<Any?>()


abstract class Element(val id: Int, val keyIndex: Int = -1) : UiScope, DiagnoseTree {
	lateinit var attrs: Array<Any?>
	
	var returnValue: Any? = null
	
	
	enum class LifecycleState { created, initialized, attached, detached }
	
	var lifecycleState: LifecycleState = LifecycleState.created
	
	lateinit var root: Root
		private set
	
	var parent: Element? = null
		private set
	
	var children = mutableListOf<Element>()
	
	var isDirty: Boolean = true
		private set
	
	private sealed class Event {
		object Attach : Event()
		object Update : Event()
		class LifecycleUpdate(val newState: LifecycleState) : Event()
	}
	
	private val pendingEvents = mutableListOf<Event>() // TODO: change to BooleanArray: performance!
	
	inline fun visitChildren(block: (child: Element) -> Unit) {
		children.forEach(block)
	}
	
	
	/// Building
	
	override fun requestRebuild() {
		root.requestRebuild(this)
	}
	
	internal fun initialize(root: Root, parent: Element?) { // called on building
		this.root = root
		this.parent = parent
		
		changeLifecycleToState(LifecycleState.initialized)
	}
	
	private fun changeLifecycleToState(state: LifecycleState) {
		lifecycleState = state
		onLifecycleStateChanged(state)
	}
	
	private fun pendEvent(event: Event) {
		pendingEvents += event
	}
	
	// called while rebuilding
	
	internal fun attach() {
		pendEvent(Event.Attach)
	}
	
	internal fun stateUpdated() {
		pendingEvents += Event.Update
		isDirty = true
	}
	
	internal fun skipBuilding() {
		// is this function needed?
	}
	
	internal fun detach() {
		onDispose()
		changeLifecycleToState(LifecycleState.detached)
		
		attrs = sEmptyAttrs // TODO: in case of memory leakage?
	}
	
	
	// called after rebuilding
	internal fun rebuilt() {
		// 1. onRebuilt
		onRebuilt()
		
		// 2. call pending events
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
		
		// 3. children
		children.forEach { it.rebuilt() }
	}
	
	
	// listeners
	
	open fun onLifecycleStateChanged(lifecycleState: LifecycleState) {}
	
	open fun onAttach() {}
	
	open fun onUpdate() {}
	
	// calling this happens while building
	open fun onDispose() {}
	
	open fun onRebuilt() {}
	
	
	/// Ambients

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
