package com.lhwdev.ktui


/**
 * Provides a widget's lifecycle, position, and state.
 * This class presents an immutable, limited widget tree and on building, you should use
 * [BuildScope], or if you want to have a full access you can use [Element].
 *
 * Generally you use this in a lambda passed into another functional widget.
 * ```kotlin
 * fun MyWidget(): Widget { +Text("Hello, world!") }
 * ```
 * In this code, [Widget] is as same as `BuildScope.() -> Unit`.
 *
 * [UiScope] contains information about:
 * - [ambient]
 * - rebuilding
 */
interface UiScope {
	/// Ambient
	
	/**
	 * Resolves an ambient value which is provided by its ancestors.
	 * If there are several value, the combination of them will be returned. In this case, the
	 * nearer value can override its ancestors' value.
	 *
	 * Calling this function makes this widget depend on the provider of the ambient.
	 * To not depend on the provider, see [peekAmbient].
	 */
//	fun <T : Any> ambient(ambient: Ambient<T>): T
	
	/**
	 * @see UiScope.ambient
	 */
//	fun <T : Any> peekAmbient(ambient: Ambient<T>): T
	
	
	/// Widget building
	
	/**
	 * Makes this widget rebuilt. Rebuilding may cause its dependents rebuilt or the screen area
	 * (limited to [RepaintBoundary]) invalidated.
	 */
	fun requestRebuild()
}
