package com.example.memej.Instances

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class DrawTextView @JvmOverloads constructor(

    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TEXT = " "
    }


    private var drawText = TEXT
    private val drawTextCoordinate = CoordinateTV(x, y)

    var customText: String = ""
        set(value) {
            field = value
            invalidate()
        }

    var drawBox: Boolean = false
        set(value) {
            field = value
            invalidate()
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Can not get View
        if (width == 0 || height == 0) return
        //Et the text as required
        drawText = if (customText.isBlank()) TEXT else customText


        canvas.drawText(
            drawText,
            drawTextCoordinate.x,
            drawTextCoordinate.y,
            projectResources.paint
        )


    }


    class CoordinateTV(var x: Float, var y: Float)

}
