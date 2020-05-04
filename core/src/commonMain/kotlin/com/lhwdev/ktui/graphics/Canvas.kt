package com.lhwdev.ktui.graphics

import com.lhwdev.ktui.text.TextStyle
import kotlin.math.PI


// this class is relatively high-level api
// but using this still needs some optimization like 'not overdrawing too much'
interface Canvas {
	// Basic property
	
	val width: Float
	val height: Float
	
//	val density: Float // TODO: here, or in the DrawScope, or just remove?
	
	
	/// Drawing
	
	fun drawRect(rect: Rect, paint: Paint) {
		drawRect(rect.left, rect.top, rect.right, rect.bottom, paint)
	}
	
	fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint)
	
	fun drawRRect(rRect: RRect, paint: Paint)
	
	fun drawText(text: CharSequence, start: Int, end: Int, x: Float, y: Float, paint: TextStyle)
	
	fun drawText(text: CharSequence, start: Int, end: Int, position: Position, paint: TextStyle) {
		drawText(text, start, end, position.x, position.y, paint)
	}
	
	fun drawText(text: CharSequence, x: Float, y: Float, paint: TextStyle) {
		drawText(text, 0, text.length, x, y, paint)
	}
	
	fun drawText(text: CharSequence, position: Position, paint: TextStyle) {
		drawText(text, 0, text.length, position, paint)
	}
	
	fun drawLine(a: Position, b: Position, paint: Paint) {
		drawLine(a.x, b.x, a.y, b.y, paint)
	}
	
	fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, paint: Paint)
	
	fun drawPoints(points: FloatArray, offset: Int = 0, count: Int = 0, paint: Paint)
	
	fun drawPath(path: Path, paint: Paint)
	
	fun drawCircle(position: Position, radius: Float, paint: Paint) {
		drawCircle(position.x, position.y, radius, paint)
	}
	
	fun drawCircle(x: Float, y: Float, radius: Float, paint: Paint)
	
	fun drawOval(rect: Rect, paint: Paint) {
		drawOval(rect.left, rect.top, rect.width, rect.height, paint)
	}
	
	fun drawOval(x: Float, y: Float, width: Float, height: Float, paint: Paint)
	
	fun drawArc(rect: Rect, startAngleDegrees: Float = 0f, sweepAngleDegrees: Float = 360f, paint: Paint) {
		drawArc(rect.left, rect.top, rect.width, rect.height, startAngleDegrees, sweepAngleDegrees, paint)
	}
	
	fun drawArc(x: Float, y: Float, width: Float, height: Float,
				startAngleDegrees: Float = 0f, sweepAngleDegrees: Float = 360f, paint: Paint)
	
	fun drawArcRad(rect: Rect, startAngleRadians: Float = 0f, sweepAngleRadians: Float = PI.toFloat() * 2f, paint: Paint) {
		drawArc(rect, startAngleRadians.toDegree(), sweepAngleRadians.toDegree(), paint)
	}
	
	fun drawArcRad(x: Float, y: Float, width: Float, height: Float,
				   startAngleDegrees: Float = 0f, sweepAngleDegrees: Float = PI.toFloat() * 2f, paint: Paint) {
		drawArc(x - width, y - height, x + width, y + height, startAngleDegrees.toDegree(), sweepAngleDegrees.toDegree(), paint)
	}
	
	fun drawImage(image: Image, src: Rect? = null, dest: Rect? = null, paint: Paint? = null)
	
	fun fillImage(image: Image, src: Rect? = null, paint: Paint? = null) {
		drawImage(image, src = src, paint = paint)
	}
	
	
	/// Metrics & Clipping
	
	val saveCount: Int
	
	fun save(): Int
	
	fun saveLayer(paint: Paint): Int
	
	fun restore()
	
	fun restore(count: Int)
	
	
	/// Metrics
	
	fun translate(dx: Float = 0f, dy: Float = 0f)
	
	fun scale(sx: Float = 1f, sy: Float = sx)
	
	fun rotate(degrees: Float)
	
	fun rotateRad(radians: Float) {
		rotate(radians.toDegree())
	}
	
	fun skew(sxDegrees: Float = 0f, xyDegrees: Float = 0f)
	
	fun skewRad(sxRadians: Float, syRadians: Float) {
		skew(sxRadians.toRadian(), sxRadians.toRadian())
	}
	
	
	// Clipping
	
	fun clipRect(rect: Rect)
	
	fun clipOutRect(rect: Rect)
	
	
	fun clipPath(path: Path)
	
	fun clipOutPath(path: Path)
}


inline fun <R> Canvas.save(block: Canvas.() -> R): R {
	val count = save()
	return try {
		block()
	} finally {
		restore(count)
	}
}

inline fun <R> Canvas.layer(paint: Paint, block: Canvas.() -> R): R {
	val count = saveLayer(paint)
	return try {
		block()
	} finally {
		restore(count)
	}
}

inline fun <R> Canvas.translate(dx: Float = 0f, dy: Float = 0f, block: Canvas.() -> R) = save {
	translate(dx, dy)
	block()
}

inline fun <R> Canvas.scale(sx: Float = 1f, sy: Float = sx, block: Canvas.() -> R) = save {
	scale(sx, sy)
	block()
}

inline fun <R> Canvas.rotate(degrees: Float, block: Canvas.() -> R) = save {
	rotate(degrees)
	block()
}

inline fun <R> Canvas.rotateRad(radians: Float, block: Canvas.() -> R): R =
	rotate(radians.toDegree(), block)

inline fun <R> Canvas.skew(sxDegrees: Float = 0f, syDegrees: Float = 0f, block: Canvas.() -> R) =
	save {
		skew(sxDegrees, syDegrees)
		block()
	}

inline fun <R> Canvas.skewRad(sxRadians: Float = 0f, syRadians: Float = 0f, block: Canvas.() -> R) =
	save {
		skewRad(sxRadians, syRadians)
		block()
	}



