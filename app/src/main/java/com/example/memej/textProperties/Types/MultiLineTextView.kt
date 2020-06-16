package com.example.memej.textProperties.Types

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.DynamicLayout
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import com.example.memej.textProperties.ConversionUtil

class MultilineTextView(
    context: Context,
    private var text: String,
    private var textPaint: TextPaint,
    private var xT: Float,
    private var yT: Float,
    private var xB: Float,
    private var yB: Float,
    private var maxWidth: Int,
    private var canvas: Canvas
) : View(context) {

    private var leftMargin = 0
    private var topMargin = 0
    private var rightMargin = 0
    private var screenWidth = 0
    private var staticLayout: StaticLayout? = null
    private var dynamicLayout: DynamicLayout? = null

    init {

//        textPaint.color = Already Taken Care
//        textPaint.textSize = ConversionUtil.spToPx(
//            context,
//            20
//        ).toFloat()
//        textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/iran_sans_light.ttf")

        rightMargin =
            ConversionUtil.dpToPx(context, 32)
        topMargin = rightMargin
        leftMargin = topMargin
        screenWidth = context.resources.displayMetrics.widthPixels

        //When the width is not predefined
        //val maxWidth = screenWidth - leftMargin - rightMargin
        val spacingAdd = 0
        val spacingMult = 1

        Log.e("New", "In Init of Multiline")

//        //Static View
//        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val sb = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, maxWidth)
//                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
//                .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
//                .setIncludePad(false)
//            sb.build()
//        } else {
//            StaticLayout(
//                text,
//                textPaint,
//                maxWidth,
//                Layout.Alignment.ALIGN_NORMAL,
//                spacingMult.toFloat(),
//                spacingAdd.toFloat(),
//                false
//            )
//        }

        //if your text will change use DynamicLayout instead of StaticLayout
        dynamicLayout = if (Build.VERSION.SDK_INT >= 28) {
            val sb = DynamicLayout.Builder.obtain(text, textPaint, maxWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
                .setIncludePad(false)
            sb.build()
        } else {
            DynamicLayout(
                text,               //For some reason removed text
                textPaint,
                maxWidth,
                Layout.Alignment.ALIGN_NORMAL,
                spacingMult.toFloat(),
                spacingAdd.toFloat(),
                false
            )
        }
        dynamicLayout!!.draw(canvas)
        Log.e("New", "The dynamic layout" + dynamicLayout.toString())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        canvas.save()
//        canvas.translate(xT, yT)
//        staticLayout!!.draw(canvas)
//        canvas.restore()

//        canvas.drawLine(
//            0f,
//            (ConversionUtil.dpToPx(
//                context,
//                48
//            ) + staticLayout!!.height).toFloat(),
//            screenWidth.toFloat(), (ConversionUtil.dpToPx(
//                context,
//                48
//            ) + staticLayout!!.height).toFloat(), textPaint
//        )

        Log.e("New", "In onDraw")

        canvas.save()
        canvas.translate(xT, yT)
        dynamicLayout!!.draw(canvas)
        canvas.restore()



        Log.e("New", "Save count" + canvas.saveCount)
    }


}