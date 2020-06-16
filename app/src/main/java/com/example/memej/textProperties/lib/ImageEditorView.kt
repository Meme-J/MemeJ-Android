package com.example.memej.textProperties.lib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.example.memej.R

class ImageEditorView : RelativeLayout {


    var source: ImageView? = null
        private set
    var brushDrawingView: BrushView? = null
        private set


    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    @SuppressLint("Recycle")
    private fun init(@Nullable attrs: AttributeSet?) {
        //Setup image attributes
        source = ImageView(context)
        source!!.id = imgSrcId
        source!!.adjustViewBounds = true
        val imgSrcParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imgSrcParam.addRule(CENTER_IN_PARENT, TRUE)
        if (attrs != null) {
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.PhotoEditorView)
            val imgSrcDrawable = a.getDrawable(R.styleable.PhotoEditorView_photo_src)
            if (imgSrcDrawable != null) {
                source!!.setImageDrawable(imgSrcDrawable)
            }
        }

        //Setup brush view
        brushDrawingView = BrushView(context)
        brushDrawingView!!.visibility = View.GONE
        brushDrawingView!!.id = brushSrcId
        //Align brush to the size of image view

        val brushParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        brushParam.addRule(CENTER_IN_PARENT, TRUE)
        brushParam.addRule(ALIGN_TOP, imgSrcId)
        brushParam.addRule(ALIGN_BOTTOM, imgSrcId)


        //Add image source
        addView(source, imgSrcParam)
        //Add brush view
        addView(brushDrawingView, brushParam)
    }


    companion object {
        private const val imgSrcId = 1
        private const val brushSrcId = 2
        private const val downloadConst = 3
    }
}
