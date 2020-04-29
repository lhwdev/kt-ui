package com.lhwdev.ktui.graphics


/**
 * Algorithms to use when painting on the canvas.
 *
 * When drawing a shape or image onto a canvas, different algorithms can be used to blend the
 * pixels. The different values of [BlendMode] specify different such algorithms.
 *
 * All blend modes has two inputs, **src**(stands for **source**) and **dst**(stands for
 * **destination**).
 *
 * ## Terms
 * - **alpha**: the transparency of each pixel. (`a`)
 * - `color * alpha`: multiplying the color by the alpha, like `Rgb(red * alpha, green * alpha,
 *   blue * alpha)`.
 * - **color**: the color of each pixel, without alpha, pre-multiplied by the alpha. (`rgb * a`)
 *   Pre-multiplying allows expressions to be shorter, for example the expression of [srcOver] would
 *   be `alpha = src.alpha + dst.alpha * (1.0 - src.alpha); color = src.color * src.alpha +
 *   dst.color * dst.alpha * (1.0 - src.alpha)` but when using pre-multiplying, it becomes `all =
 *   src.all + dst.all * (1.0 - src.alpha)`.
 * - **all**: the color of each pixel, pre-multiplied by the alpha. (`a, rgb * a` = alpha, color)
 * - src/dst.(~): ~ of the source/destination. `src.alpha` stands for the alpha value of each pixel
 * of the source image.
 */
enum class BlendMode {
	/**
	 * Just clear. Drop both the source and destination images, leaving **nothing**.
	 *
	 * ```kotlin
	 * alpha = 0
	 * ```
	 * This corresponds to the "clear" Porter-Duff operator.
	 */
	clear,
	
	/**
	 * Drop the destination image, **only paint the source image**.
	 *
	 * ```kotlin
	 * all = src.all
	 * ```
	 * This corresponds to the "Copy" Porter-Duff operator.
	 */
	src,
	
	/**
	 * Drop the source image, **only paint the destination image**.
	 * This leaves the destination image untouched.
	 *
	 * ```kotlin
	 * all = dst.all
	 * ```
	 * This corresponds to the "Destination" Porter-Duff operator.
	 */
	dst,
	
	/**
	 * Composite **the source image over the destination image**.
	 *
	 * This is the default value. It represents the most intuitive case, where shapes are painted on
	 * top of what is below, with transparent areas showing the destination layer.
	 * This is the opposite of [dstOver].
	 *
	 * ```kotlin
	 * all = src.all + dst.all * (1.0 - src.alpha)
	 * ```
	 * This corresponds to the "Source over Destination" Porter-Duff operator,
	 * also known as the Painter's Algorithm.
	 *
	 * @see dstOver
	 */
	srcOver,
	
	/**
	 * Composite **the source image under the destination image**.
	 * This is the opposite of [srcOver].
	 *
	 * ```kotlin
	 * all = src.all * (1.0 - dst.alpha) + dst.all
	 * ```
	 * This corresponds to the "Destination over Source" Porter-Duff operator.
	 *
	 * @see srcOver
	 */
	dstOver,
	
	/**
	 * Show the source image, but only where the two images overlap.
	 *
	 * The destination image is not rendered, it is treated merely as a mask. The color channels of
	 * the destination are ignored, only the opacity has an effect.
	 * This is the opposite of [dstIn].
	 *
	 * ```kotlin
	 * all = src.all * dst.alpha
	 * ```
	 * This corresponds to the "Source in Destination" Porter-Duff operator.
	 *
	 * @src dstIn
	 */
	srcIn,
	
	/**
	 * Show the destination image, but only where the two images overlap.
	 *
	 * The source image is not rendered, it is treated merely as a mask. The color channels of the
	 * source are ignored, only the opacity has an effect.
	 * This is the opposite of [srcIn].
	 *
	 * ```kotlin
	 * all = dst.all * src.alpha
	 * ```
	 *
	 * This corresponds to the "Destination in Source" Porter-Duff operator.
	 *
	 * @see srcIn
	 */
	dstIn,
	
	/**
	 * Show the source image, but only where the two images do not overlap.
	 *
	 * The destination image is not rendered, it is treated merely as a mask. The color channels of
	 * the destination are ignored, only the opacity has an effect.
	 * This is the opposite of [dstOut].
	 *
	 * ```kotlin
	 * all = src.all * (1.0 - dst.alpha)
	 * ```
	 *
	 * This corresponds to the "Source out Destination" Porter-Duff operator.
	 *
	 * @see dstIn
	 */
	srcOut,
	
	/**
	 * Show the destination image, but only where the two images do not overlap.
	 *
	 * The source image is not rendered, it is treated merely as a mask. The color channels of the
	 * source are ignored, only the opacity has an effect.
	 * This is the opposite of [srcOut].
	 *
	 * ```kotlin
	 * all = dst.all * (1.0 - src.alpha)
	 * ```
	 *
	 * This corresponds to the "Destination out Source" Porter-Duff operator.
	 *
	 * @see srcOut
	 */
	dstOut,
	
	/**
	 * Composite the source image over the destination image, but only where it overlaps the
	 * destination.
	 *
	 * This is essentially the [srcOver] operator, but with the output's opacity channel being set
	 * to that of the destination image instead of being a combination of both image's opacity
	 * channels.
	 * For a variant with the destination on top instead of the source, see [dstATop].
	 *
	 * ```kotlin
	 * all = src.all * dst.alpha + dst.all * (1.0 - src.alpha)
	 * ```
	 *
	 * This corresponds to the "Source atop Destination" Porter-Duff operator.
	 *
	 * @see dstATop
	 */
	srcATop,
	
	/**
	 * Composite the destination image over the source image, but only where it overlaps the source.
	 *
	 * This is essentially the [dstOver] operator, but with the output's opacity channel being set to
	 * that of the source image instead of being a combination of both image's opacity channels.
	 * For a variant with the source on top instead of the destination, see [srcATop].
	 *
	 * ```kotlin
	 * all = src.all * (1.0 - dst.alpha) + dst.all * src.alpha
	 * ```
	 *
	 * This corresponds to the "Destination atop Source" Porter-Duff operator.
	 */
	dstATop,
	
	/**
	 * Apply a bitwise xor operator to the source and destination images.
	 *
	 * This leaves transparency where they would overlap.
	 *
	 * ```kotlin
	 * all = src.all * (1.0 - dst.alpha) + dst.all * (1.0 - src.alpha)
	 *
	 * This corresponds to the "Source xor Destination" Porter-Duff operator.
	 */
	xor,

//	 TODO: richer documentations
	
	/**
	 * Subtract the smaller value from the bigger value for each channel.
	 * Compositing black has no effect; compositing white inverts the colors of the other image.
	 * The opacity of the output image is computed in the same way as for srcOver.
	 * The effect is similar to exclusion but harsher.
	 */
	difference,

//	TODO: implement these things
//	Reference: Flutter documentations, javafx.scene.effect.BlendMode, SkBlendMode

//	/**
//	 * Composite the source and destination image by choosing the lowest value from each color
//	 * channel.
//	 *
//	 * ```kotlin
//	 * alpha = src.alpha + dst.alpha - src.alpha * dst.alpha
//	 *
//	 * ```
//	 *
//	 * The opacity of the output image is computed in the same way as for [srcOver].
//	 */
//	darken,
//	lighten,
//	multiply,
//	screen,
//	plus,
//	overlay,
	
}
