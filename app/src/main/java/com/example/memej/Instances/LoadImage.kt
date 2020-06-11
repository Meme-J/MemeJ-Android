package com.example.memej.Instances

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.*
import androidx.core.graphics.withTranslation
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.memeWorldResponses.Meme_World

class LoadImage {


    fun getCompleteImage(canvas: Canvas, bitmap: Bitmap, response: Meme_World) {
        for (i in 0..response.templateId.numPlaceholders - 1) {
            //get the previou types
            val paint =
                getTextPaint(response.templateId.textColorCode[i], response.templateId.textSize[i])
            val pl = response.placeholders[i]
            val x = response.templateId.coordinates.elementAt(i).x
            val y = response.templateId.coordinates.elementAt(i).y
            canvas.drawMultilineText(pl, paint, 500, x.toFloat(), y.toFloat())
            canvas.save()
        }
    }

    fun getOngoingImage(canvas: Canvas, bitmap: Bitmap, response: Meme_Home) {

        for (i in 0..response.stage - 1) {
            //get the previou types
            val paint =
                getTextPaint(response.templateId.textColorCode[i], response.templateId.textSize[i])
            val pl = response.placeholders[i]
            val x = response.templateId.coordinates.elementAt(i).x
            val y = response.templateId.coordinates.elementAt(i).y
            canvas.drawMultilineText(pl, paint, 500, x.toFloat(), y.toFloat())
            canvas.save()
        }

    }

    fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {


        canvas.withTranslation(x, y) {
            draw(this)
        }
    }


    fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
        breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE
    ) {


        val staticLayout = StaticLayout.Builder
            .obtain(text, start, end, textPaint, width)
            .setAlignment(alignment)
            .setTextDirection(textDir)
            .setLineSpacing(spacingAdd, spacingMult)
            .setBreakStrategy(breakStrategy)
            .build()
        staticLayout.draw(this, x, y)
    }


    fun getTextPaint(color: String, size: Int): TextPaint {
        //Where color is the string in #HEX code

        val paint = TextPaint()
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

    fun setSize(size: Int): Float {
        val size_req = size.toFloat()
        return size_req

    }


}
