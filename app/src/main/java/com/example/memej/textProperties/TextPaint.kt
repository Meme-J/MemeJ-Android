package com.example.memej.textProperties

import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint

class TextPaint : TextPaint() {

    fun TextPaint(color: String, size: Int): TextPaint {

        val paint = android.text.TextPaint()
        paint.color = Color.parseColor(color)
        paint.strokeWidth = 15F
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.FILL //Default to be set
        paint.isAntiAlias = true
        paint.textSize = size.let { setSize(it) }
        paint.isDither = true

        return paint
    }

    private fun setSize(size: Int): Float {
        val size_req = size.toFloat()
        return size_req

    }
}





