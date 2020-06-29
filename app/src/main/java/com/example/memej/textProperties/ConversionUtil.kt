package com.example.memej.textProperties

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import com.example.memej.Utils.ApplicationUtil.Companion.getContext
import java.text.SimpleDateFormat
import java.util.*


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
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun dpToRequiredScaledDensity(context: Context, dp: Int): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale
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

    fun convertTimeToEpoch(timestamp: String): String {
        val calendar = Calendar.getInstance()
        val timezone = TimeZone.getDefault()

        val sourceFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val destFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm")

        sourceFormat.timeZone = timezone
        val convertedDate = sourceFormat.parse(timestamp)!!
        return destFormat.format(convertedDate)


    }


}
