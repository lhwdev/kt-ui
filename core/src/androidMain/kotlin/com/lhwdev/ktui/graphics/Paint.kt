package com.lhwdev.ktui.graphics


import android.graphics.Paint as APaint
import android.graphics.BlendMode as ABlendMode


actual typealias FrameworkPaint = android.graphics.Paint


private val sVoidPaint = FrameworkPaint().apply {
	alpha = 0
}


actual fun Paint.toFrameworkPaint(): FrameworkPaint = APaint().also { paint ->
	paint.color = color.toUInt().toInt()
	stroke?.let {
		paint.strokeWidth = it.width
		paint.strokeCap = when(it.cap) {
			StrokeCap.butt -> APaint.Cap.BUTT
			StrokeCap.round -> APaint.Cap.ROUND
			StrokeCap.square -> APaint.Cap.SQUARE
		}
		
		paint.strokeJoin = when(it.join) {
			StrokeJoin.miter -> APaint.Join.MITER
			StrokeJoin.round -> APaint.Join.ROUND
			StrokeJoin.bevel -> APaint.Join.BEVEL
		}
		
		paint.strokeMiter // TODO
	}
	
	paint.style = if(stroke == null)
		if(doFill) APaint.Style.FILL else return sVoidPaint
	else
		if(doFill) APaint.Style.FILL_AND_STROKE else APaint.Style.STROKE
	
	paint.isAntiAlias = isAntialias
	
	paint.blendMode = when(blendMode) {
		BlendMode.clear -> ABlendMode.CLEAR
		BlendMode.src -> ABlendMode.SRC
		BlendMode.dst -> ABlendMode.DST
		BlendMode.srcOver -> ABlendMode.SRC_OVER
		BlendMode.dstOver -> ABlendMode.DST_OVER
		BlendMode.srcIn -> ABlendMode.SRC_IN
		BlendMode.dstIn -> ABlendMode.DST_IN
		BlendMode.srcOut -> ABlendMode.SRC_OUT
		BlendMode.dstOut -> ABlendMode.DST_OUT
		BlendMode.srcATop -> ABlendMode.SRC_ATOP
		BlendMode.dstATop -> ABlendMode.DST_ATOP
		BlendMode.xor -> ABlendMode.XOR
		BlendMode.difference -> ABlendMode.DIFFERENCE
	}
	
	colorFilter?.let { paint.colorFilter = it }
	shader?.let { paint.shader = it }
}
