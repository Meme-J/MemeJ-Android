package com.example.memej.textProperties.Types

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.view.View
import androidx.core.content.ContextCompat
import com.example.memej.R
import com.example.memej.textProperties.ConversionUtil


class SimpleTextView(context: Context) : View(context) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = "This is a simple text on canvas"
    private var leftMargin = 0
    private var topMargin = 0

    init {
        textPaint.color = ContextCompat.getColor(getContext(), R.color.black)
        textPaint.textSize = ConversionUtil.spToPx(getContext(), 20).toFloat()
        topMargin = com.example.memej.textProperties.ConversionUtil.dpToPx(getContext(), 32)
        leftMargin = topMargin
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(text, leftMargin.toFloat(), topMargin.toFloat(), textPaint)
    }

}