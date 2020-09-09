package com.example.memej.Utils.ui

import android.content.Context
import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ExplorePagerTransformer : ViewPager2.PageTransformer {
    private var maxTranslateOffsetX = 0
    private var viewPager: ViewPager2? = null

    //Init constructor
    fun ExplorePagerTransformer(context: Context) {
        maxTranslateOffsetX = ConversionUtil.dpToPx(context, 180)
    }

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager2
        }
        val leftInScreen = view.left - viewPager!!.scrollX
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate =
            offsetX.toFloat() * 0.38f / viewPager!!.measuredWidth
        val scaleFactor = 1 - Math.abs(offsetRate)
        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
        }
    }


}