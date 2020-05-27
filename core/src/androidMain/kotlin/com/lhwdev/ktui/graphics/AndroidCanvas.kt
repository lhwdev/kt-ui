package com.lhwdev.ktui.graphics

import android.graphics.RectF
import android.os.Build
import com.lhwdev.ktui.text.TextStyle
import com.lhwdev.ktui.text.toFrameworkPaintInternal
import android.graphics.Canvas as ACanvas

// TODO: units

class AndroidCanvas(val delegate: ACanvas) : Canvas {
	override val width get() = delegate.width.toFloat() // TODO
	override val height get() = delegate.height.toFloat() // TODO
	
	override fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
		delegate.drawRect(left, top, right, bottom, paint.toFrameworkPaint())
	}
	
	override fun drawRRect(rRect: RRect, paint: Paint) {
		TODO()
	}
	
	override fun drawText(text: CharSequence, start: Int, end: Int, x: Float, y: Float, style: TextStyle) {
		delegate.drawText(text, start, end, x, y, style.toFrameworkPaintInternal())
	}
	
	override fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, paint: Paint) {
		delegate.drawLine(x1, y1, x2, y2, paint.toFrameworkPaint())
	}
	
	override fun drawPoints(points: FloatArray, offset: Int, count: Int, paint: Paint) {
		delegate.drawPoints(points, offset, count, paint.toFrameworkPaint())
	}
	
	override fun drawPath(path: Path, paint: Paint) {
		delegate.drawPath(path.toFrameworkPath(), paint.toFrameworkPaint())
	}
	
	override fun drawCircle(centerX: Float, centerY: Float, radius: Float, paint: Paint) {
		delegate.drawCircle(centerX, centerY, radius, paint.toFrameworkPaint())
	}
	
	override fun drawOval(x: Float, y: Float, width: Float, height: Float, paint: Paint) {
		// delegate.drawOval(x, y, x + width, y + height, paint.toFrameworkPaint()) // added in api 21
		delegate.drawOval(RectF(x, y, x + width, y + height), paint.toFrameworkPaint())
	}
	
	override fun drawArc(x: Float, y: Float, width: Float, height: Float, startAngleDegrees: Float, sweepAngleDegrees: Float, paint: Paint) {
		// delegate.drawArc(x, y, x + width, y + height, startAngleDegrees, sweepAngleDegrees, false, paint.toFrameworkPaint()) // added in api 21
		delegate.drawArc(RectF(x, y, x + width, y + height), startAngleDegrees, sweepAngleDegrees, false, paint.toFrameworkPaint())
	}
	
	override fun drawImage(image: Image, src: Rect?, dest: Rect?, paint: Paint?) {
		TODO()
	}
	
	
	override val saveCount get() = delegate.saveCount
	
	override fun save() = delegate.save()
	
	override fun saveLayer(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) =
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			delegate.saveLayer(RectF(left, top, right, bottom), paint.toFrameworkPaint())
		else clipRect(left, top, right, bottom) {
			delegate.saveLayer(null, paint.toFrameworkPaint())
		}
	
	override fun saveLayer(paint: Paint) = delegate.saveLayer(null, paint.toFrameworkPaint())
	
	override fun restore() {
		delegate.restore()
	}
	
	override fun restore(count: Int) {
		delegate.restoreToCount(count)
	}
	
	override fun translate(dx: Float, dy: Float) {
		delegate.translate(dx, dy)
	}
	
	override fun scale(sx: Float, sy: Float) {
		delegate.scale(sx, sy)
	}
	
	override fun rotate(degrees: Float) {
		delegate.rotate(degrees)
	}
	
	override fun skew(sxDegrees: Float, xyDegrees: Float) {
		delegate.skew(sxDegrees, xyDegrees)
	}
	
	override fun clipRect(left: Float, top: Float, right: Float, bottom: Float) {
		delegate.clipRect(left, top, right, bottom)
	}
	
	override fun clipOutRect(left: Float, top: Float, right: Float, bottom: Float) {
		delegate.clipOutRect(left, top, right, bottom)
	}
	
	override fun clipPath(path: Path) {
		delegate.clipPath(path.toFrameworkPath())
	}
	
	override fun clipOutPath(path: Path) {
		delegate.clipOutPath(path.toFrameworkPath())
	}
}
