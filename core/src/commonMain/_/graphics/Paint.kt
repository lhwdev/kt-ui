package com.lhwdev.ktui.graphics

import com.lhwdev.ktui.Color


expect class FrameworkPaint


inline fun Paint(init: Paint.() -> Unit): Paint = Paint().apply(init)


// TODO: maskFilter(blur)
expect class Paint() {
	
	var alpha: Float
	
	/**
	 * Whether to apply anti-aliasing to lines and images drawn on the canvas.
	 */
	var isAntialias: Boolean
	
	/**
	 * The color to use when stroking or filling a shape.
	 *
	 * Defaults to opaque black.
	 *
	 * See also:
	 *
	 *  - [doFill] and [doStroke], which controls whether to stroke or fill (or both).
	 *  - [colorFilter], which overrides [color].
	 *  - [shader], which overrides [color] with more elaborate effects.
	 */
	var color: Color
	
	/**
	 * The shader to use when stroking or filling a shape.
	 *
	 * When this is null, the [color] is used instead.
	 *
	 * See also:
	 *
	 *  - [Gradient], a shader that paints a color gradient.
	 *  - [ImageShader], a shader that tiles an [Image].
	 *  - [colorFilter], which overrides [shader].
	 *  - [color], which is used if [shader] and [colorFilter] are null.
	 */
	var shader: Shader?
	
	/**
	 * A color filter to apply when a shape is drawn or when a layer is composited.
	 *
	 * See [ColorFilter] for details.
	 *
	 * When a shape is being drawn, [colorFilter] overrides [color] and [shader].
	 */
	
	var colorFilter: ColorFilter?
	
	/**
	 * A blend mode to apply when a shape is drawn or a layer is composited.
	 *
	 * The source colors are from the shape being drawn (e.g. from [Canvas.drawPath]) or layer being
	 * composited (the graphics that were drawn between the [Canvas.layer] and [Canvas.restore]
	 * calls), after applying the [colorFilter], if any.
	 *
	 * The destination colors are from the background onto which the shape or layer is being
	 * composited.
	 *
	 * Defaults to [BlendMode.srcOver].
	 *
	 * See also:
	 *
	 *  - [Canvas.layer], which uses its [Paint]'s [blendMode] to composite the layer.
	 *  - [BlendMode], which discusses the user of [Canvas.saveLayer] with [blendMode].
	 */
	var blendMode: BlendMode
	
	var doFill: Boolean
	
	var doStroke: Boolean
	
	/**
	 * How wide to make edges drawn when [doStroke] is set to true.
	 * The width is given in logical pixels measured in the direction orthogonal to the direction of
	 * the path.
	 * Defaults to 0.0, which correspond to a hairline width.
	 */
	var strokeWidth: Float
	
	/**
	 * The kind of finish to place on the end of lines drawn when [doStroke] is set to true.
	 *
	 * Defaults to [StrokeCap.butt], i.e. no caps.
	 */
	
	var strokeCap: StrokeCap
	
	/**
	 * The kind of finish to place on the joins between segments.
	 *
	 * This applies to paths drawn when [doStroke] is set to true, it does not apply to points drawn
	 * as lines with [Canvas.drawPoints].
	 *
	 * Defaults to [StrokeJoin.miter], i.e. sharp corners. See also
	 * [strokeMiterLimit] to control when miters are replaced by bevels.
	 */
	var strokeJoin: StrokeJoin
	
	/**
	 * The limit for miters to be drawn on segments when the join is set to [StrokeJoin.miter] and
	 * [doStroke] is set to true. If this limit is exceeded, then a [StrokeJoin.bevel] join will be
	 * drawn instead. This may cause some 'popping' of the corners of a path if the
	 * angle between line segments is animated.
	 *
	 * This limit is expressed as a limit on the length of the miter.
	 *
	 * Defaults to 4.0.  Using zero as a limit will cause a [StrokeJoin.bevel] join to be used all
	 * the time.
	 */
	var strokeMiterLimit: Float
}


/**
 * Styles to use for line endings.
 *
 * See [Paint.strokeCap].
 */
enum class StrokeCap {
	/**
	 * Begin and end contours with a flat edge and no extension.
	 */
	butt,
	/**
	 * Begin and end contours with a semi-circle extension.
	 */
	round,
	/**
	 * Begin and end contours with a half square extension. This is similar to extending each
	 * contour by half the stroke width (as given by [Paint.strokeWidth]).
	 */
	square
}

/**
 * Styles to use for line joins.
 *
 * This only affects line joins for polygons drawn by [Canvas.drawPath] and rectangles, not points
 * drawn as lines with [Canvas.drawPoints].
 *
 * See [Paint.strokeJoin].
 */
enum class StrokeJoin {
	/**
	 * Joins between line segments form sharp corners.
	 */
	miter,
	
	/**
	 * Joins between line segments are semi-circular.
	 */
	round,
	
	/**
	 * Joins between line segments connect the corners of the butt ends of the
	 * line segments to give a beveled appearance.
	 */
	bevel
}
