package com.lhwdev.ktui.graphics

import kotlin.math.PI


interface Canvas {
	// Basic property
	
	val width: Int
	
	val height: Int
	
	val density: Int
	
	
	/// Drawing
	
	fun drawRect(rect: Rect, paint: Paint)
	
	fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
		drawRect(Rect(left, top, right, bottom), paint)
	}
	
	fun drawRRect(rRect: RRect, paint: Paint)
	
	fun drawText(text: CharSequence, start: Int, end: Int, position: Position, paint: Paint)
	
	fun drawText(text: CharSequence, position: Position, paint: Paint) {
		drawText(text, 0, text.length, position, paint)
	}
	
	fun drawLine(a: Position, b: Position, paint: Paint)
	
	fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, paint: Paint) {
		drawLine(Position(x1, y1), Position(x2, y2), paint)
	}
	
	fun drawPoints(points: FloatArray, offset: Int = 0, count: Int = 0, paint: Paint)
	
	fun drawPath(path: Path, paint: Paint)
	
	fun drawCircle(position: Position, radius: Float, paint: Paint)
	
	fun drawCircle(x: Float, y: Float, radius: Float, paint: Paint) {
		drawCircle(Position(x, y), radius, paint)
	}
	
	fun drawOval(rect: Rect, paint: Paint)
	
	fun drawOval(x: Float, y: Float, width: Float, height: Float, paint: Paint) {
		drawOval(Rect(x - width, y - height, x + width, y + height), paint)
	}
	
	fun drawArc(rect: Rect, startAngleDegrees: Float = 0f, sweepAngleDegrees: Float = 360f, paint: Paint)
	
	fun drawArc(x: Float, y: Float, width: Float, height: Float, startAngleDegrees: Float = 0f, sweepAngleDegrees: Float = 360f, paint: Paint) {
		drawArc(Rect(x - width, y - height, x + width, y + height), startAngleDegrees, sweepAngleDegrees, paint)
	}
	
	fun drawArcRad(rect: Rect, startAngleRadians: Float = 0f, sweepAngleRadians: Float = PI.toFloat() * 2f, paint: Paint) {
		drawArc(rect, startAngleRadians.toDegree(), sweepAngleRadians.toDegree(), paint)
	}
	
	fun drawArcRad(x: Float, y: Float, width: Float, height: Float, startAngleDegrees: Float = 0f, sweepAngleDegrees: Float = PI.toFloat() * 2f, paint: Paint) {
		drawArcRad(Rect(x - width, y - height, x + width, y + height), startAngleDegrees, sweepAngleDegrees, paint)
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

inline fun <R> Canvas.layer(paint: Paint, block: Canvas.() -> R): R = save {
	saveLayer(paint)
	block()
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



