package com.example.memej.textProperties

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View


class ClickSpan(private val color: Int, val text: String) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.isUnderlineText = true
    }

    override fun onClick(widget: View) {
        // do something when text clicked
        // this method not working on canvas :D
    }

}
