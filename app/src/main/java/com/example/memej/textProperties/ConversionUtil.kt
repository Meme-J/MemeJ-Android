package com.example.memej.textProperties

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import com.example.memej.Utils.ApplicationUtil.Companion.getContext


object ConversionUtil {
    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun pxToDp(context: Context, px: Int): Float {
        val GESTURE_THRESHOLD_DP = 16.0f
        val scale = context.resources.displayMetrics.density


        Log.e(
            "PX to dp",
            "The dpi " + scale.toString() + " " + context.resources.displayMetrics.densityDpi.toFloat()
                .toString() + " " + DisplayMetrics.DENSITY_DEFAULT.toString() + " metrics"
        )


        return px / (context.resources.displayMetrics.density)

//        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pxToSp(px: Int): Float {
        val sp: Float = px / getContext().resources.displayMetrics.scaledDensity
        return sp
    }

    fun spToPx(context: Context, sp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    //Get the density of the device


// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH

// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH
}
