package com.lhwdev.ktui.graphics

import com.lhwdev.ktui.Color
import com.lhwdev.ktui.Immutable


expect class FrameworkPaint


expect fun Paint.toFrameworkPaint(): FrameworkPaint

inline fun buildPaint(block: PaintBuilder.() -> Unit): Paint = PaintBuilder().apply(block).build()


// TODO: maskFilter(blur)
@Immutable
data class Paint(
	/**
	 * The color to use when stroking or filling a shape.
	 *
	 * Defaults to opaque black.
	 *1
	 * See also:
	 *
	 *  - [doFill] and [stroke], which controls whether to stroke or fill (or both).
	 *  - [colorFilter], which overrides [color].
	 *  - [shader], which overrides [color] with more elaborate effects.
	 */
	val color: Color,
	
	
	val stroke: Stroke? = null,
	
	/**
	 * Whether to fill a shape when it is drawn.
	 */
	val doFill: Boolean = true,
	
	/**
	 * Whether to apply anti-aliasing to lines and images drawn on the canvas.
	 */
	val isAntialias: Boolean = false,
	
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
	val blendMode: BlendMode = BlendMode.srcOver,
	
	/**
	 * A color filter to apply when a shape is drawn or when a layer is composited.
	 *
	 * See [ColorFilter] for details.
	 *
	 * When a shape is being drawn, [colorFilter] overrides [color] and [shader].
	 */
	val colorFilter: ColorFilter? = null,
	
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
	val shader: Shader? = null
) {
	val alpha get() = color.alpha
	
	fun mutate() = PaintBuilder(this)
	
	inline fun mutate(block: PaintBuilder.() -> Unit) = PaintBuilder(this).apply(block).build()
}

class PaintBuilder() {
	/**
	 * The color to use when stroking or filling a shape.
	 *
	 * Defaults to opaque black.
	 *
	 * See also:
	 *
	 *  - [doFill] and [stroke], which controls whether to stroke or fill (or both).
	 *  - [colorFilter], which overrides [color].
	 *  - [shader], which overrides [color] with more elaborate effects.
	 */
	var color: Color = Color.black
	
	
	var stroke: Stroke? = null
	
	/**
	 * Whether to fill a shape when it is drawn.
	 */
	
	var doFill: Boolean = true
	
	/**
	 * Whether to apply anti-aliasing to lines and images drawn on the canvas.
	 */
	var isAntialias: Boolean = false
	
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
	var blendMode: BlendMode = BlendMode.srcOver
	
	/**
	 * A color filter to apply when a shape is drawn or when a layer is composited.
	 *
	 * See [ColorFilter] for details.
	 *
	 * When a shape is being drawn, [colorFilter] overrides [color] and [shader].
	 */
	var colorFilter: ColorFilter? = null
	
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
	var shader: Shader? = null
	
	
	constructor(paint: Paint) {
		color = paint.color
		stroke = paint.stroke
		doFill = paint.doFill
		isAntialias = paint.isAntialias
		blendMode = paint.blendMode
		colorFilter = paint.colorFilter
		shader = paint.shader
	}
	
	
	inline fun stroke(block: StrokeBuilder.() -> Unit) {
		stroke = (stroke?.let { StrokeBuilder(it) } ?: StrokeBuilder()).apply(block).build()
	}
	
	
	fun build() = Paint(
		color = color,
		stroke = stroke,
		doFill = doFill,
		isAntialias = isAntialias,
		blendMode = blendMode,
		colorFilter = colorFilter,
		shader = shader
	)
	
	fun copy() = PaintBuilder().also {
		it.color = color
		it.stroke = stroke
		it.doFill = doFill
		it.isAntialias = isAntialias
		it.blendMode = blendMode
		it.colorFilter = colorFilter
		it.shader = shader
	}
}


val defaultStroke = Stroke()


@Immutable
data class Stroke(
	/**
	 * How wide to make edges drawn.
	 * The width is given in logical pixels measured in the direction orthogonal to the direction of
	 * the path.
	 * Defaults to 0.0, which correspond to a hairline width.
	 */
	val width: Float = 0f,
	
	/**
	 * The kind of finish to place on the end of lines drawn.
	 *
	 * Defaults to [StrokeCap.butt], i.e. no caps.
	 */
	val cap: StrokeCap = StrokeCap.butt,
	
	/**
	 * The kind of finish to place on the joins between segments.
	 *
	 * This applies to paths drawn, it does not apply to points drawn
	 * as lines with [Canvas.drawPoints].
	 *
	 * Defaults to [StrokeJoin.miter], i.e. sharp corners. See also
	 * [strokeMiterLimit] to control when miters are replaced by bevels.
	 */
	val join: StrokeJoin = StrokeJoin.miter,
	
	/**
	 * The limit for miters to be drawn on segments when the join is set to [StrokeJoin.miter]. If
	 * this limit is exceeded, then a [StrokeJoin.bevel] join will be
	 * drawn instead. This may cause some 'popping' of the corners of a path if the
	 * angle between line segments is animated.
	 *
	 * This limit is expressed as a limit on the length of the miter.
	 *
	 * Defaults to 4.0.  Using zero as a limit will cause a [StrokeJoin.bevel] join to be used all
	 * the time.
	 */
	val strokeMiterLimit: Float = 4.0f
)

/**
 * Styles to use for line endings.
 *
 * See [Stroke.cap].
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
	 * contour by half the stroke width (as given by [Stroke.width]).
	 */
	square
}

/**
 * Styles to use for line joins.
 *
 * This only affects line joins for polygons drawn by [Canvas.drawPath] and rectangles, not points
 * drawn as lines with [Canvas.drawPoints].
 *
 * See [Stroke.join].
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

class StrokeBuilder() {
	/**
	 * How wide to make edges drawn.
	 * The width is given in logical pixels measured in the direction orthogonal to the direction of
	 * the path.
	 * Defaults to 0.0, which correspond to a hairline width.
	 */
	var width: Float = 0f
	
	/**
	 * The kind of finish to place on the end of lines drawn.
	 *
	 * Defaults to [StrokeCap.butt], i.e. no caps.
	 */
	var cap: StrokeCap = StrokeCap.butt
	
	/**
	 * The kind of finish to place on the joins between segments.
	 *
	 * This applies to paths drawn, it does not apply to points drawn
	 * as lines with [Canvas.drawPoints].
	 *
	 * Defaults to [StrokeJoin.miter], i.e. sharp corners. See also
	 * [strokeMiterLimit] to control when miters are replaced by bevels.
	 */
	var join: StrokeJoin = StrokeJoin.miter
	
	/**
	 * The limit for miters to be drawn on segments when the join is set to [StrokeJoin.miter]. If
	 * this limit is exceeded, then a [StrokeJoin.bevel] join will be
	 * drawn instead. This may cause some 'popping' of the corners of a path if the
	 * angle between line segments is animated.
	 *
	 * This limit is expressed as a limit on the length of the miter.
	 *
	 * Defaults to 4.0.  Using zero as a limit will cause a [StrokeJoin.bevel] join to be used all
	 * the time.
	 */
	var strokeMiterLimit: Float = 4.0f
	
	
	constructor(stroke: Stroke) {
		width = stroke.width
		cap = stroke.cap
		join = stroke.join
		strokeMiterLimit = stroke.strokeMiterLimit
	}
	
	fun build() = Stroke(
		width = width,
		cap = cap,
		join = join,
		strokeMiterLimit = strokeMiterLimit
	)
	
	fun copy() = StrokeBuilder().also {
		width = it.width
		cap = it.cap
		join = it.join
		strokeMiterLimit = it.strokeMiterLimit
	}
}
