package com.example.memej.textProperties

import android.graphics.Path
import android.text.DynamicLayout

//Refactor to dynamic layout

class TextPath : Path() {
    private var currentLayout: DynamicLayout? = null
    private var currentLine = 0
    private var lastTop = -1f
    private var heightOffset = 0f

    fun setCurrentLayout(layout: DynamicLayout, start: Int, yOffset: Float) {
        currentLayout = layout
        currentLine = layout.getLineForOffset(start)
        lastTop = -1f
        heightOffset = yOffset
    }

    override fun addRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        dir: Direction
    ) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        top += heightOffset
        bottom += heightOffset
        if (lastTop == -1f) {
            lastTop = top
        } else if (lastTop != top) {
            lastTop = top
            currentLine++
        }
        val lineRight = currentLayout!!.getLineRight(currentLine)
        val lineLeft = currentLayout!!.getLineLeft(currentLine)
        if (left >= lineRight) {
            return
        }
        if (right > lineRight) {
            right = lineRight
        }
        if (left < lineLeft) {
            left = lineLeft
        }


        if (bottom != currentLayout!!.height.toFloat()) {
            bottom = currentLayout!!.spacingAdd
        } else {
            bottom = 0f
        }

        super.addRect(
            left,
            top,
            right,
            bottom,
            dir
        )

    }
}
