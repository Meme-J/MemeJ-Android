package com.example.memej.textProperties.lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import java.util.*

class BrushView : View {
    private var mBrushSize = 25f
    var eraserSize = 50f

    private var mOpacity = 255
    private val mLinePaths: MutableList<LinePath> =
        ArrayList()
    private val mRedoLinePaths: MutableList<LinePath> =
        ArrayList()
    private var mDrawPaint: Paint? = null
    private var mDrawCanvas: Canvas? = null
    private var mBrushDrawMode = false
    private var mPath: Path? = null
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mBrushViewChangeListener: BrushInterface? = null

    @JvmOverloads
    constructor(
        context: Context?,
        attrs: AttributeSet? = null
    ) : super(context, attrs) {
        setupBrushDrawing()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        setupBrushDrawing()
    }

    fun setupBrushDrawing() {
        //Caution: This line is to disable hardware acceleration to make eraser feature work properly
        setLayerType(LAYER_TYPE_HARDWARE, null)
        mDrawPaint = Paint()
        mPath = Path()
        mDrawPaint!!.isAntiAlias = true
        mDrawPaint!!.isDither = true
        mDrawPaint!!.color = Color.BLACK
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.strokeWidth = mBrushSize
        mDrawPaint!!.alpha = mOpacity
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)
        this.visibility = GONE
    }

    private fun refreshBrushDrawing() {
        mBrushDrawMode = true
        mPath = Path()
        mDrawPaint!!.isAntiAlias = true
        mDrawPaint!!.isDither = true
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.strokeWidth = mBrushSize
        mDrawPaint!!.alpha = mOpacity
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)
    }

    fun brushEraser() {
        mBrushDrawMode = true
        mDrawPaint!!.strokeWidth = eraserSize
        mDrawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }


    fun setOpacity(@androidx.annotation.IntRange(from = 0, to = 255) opacity: Int) {
        mOpacity = opacity
        brushDrawingMode = true
    }

    var brushDrawingMode: Boolean
        get() = mBrushDrawMode
        set(brushDrawMode) {
            mBrushDrawMode = brushDrawMode
            if (brushDrawMode) {
                this.visibility = VISIBLE
                refreshBrushDrawing()
            }
        }

    fun setBrushEraserSize(brushEraserSize: Float) {
        eraserSize = brushEraserSize
        brushDrawingMode = true
    }

    fun setBrushEraserColor(@ColorInt color: Int) {
        mDrawPaint!!.color = color
        brushDrawingMode = true
    }

    var brushSize: Float
        get() = mBrushSize
        set(size) {
            mBrushSize = size
            brushDrawingMode = true
        }

    var brushColor: Int
        get() = mDrawPaint!!.color
        set(color) {
            mDrawPaint!!.color = color
            brushDrawingMode = true
        }

    fun clearAll() {
        mLinePaths.clear()
        mRedoLinePaths.clear()
        if (mDrawCanvas != null) {
            mDrawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        }
        invalidate()
    }

    fun setBrushViewChangeListener(brushViewChangeListener: BrushInterface?) {
        mBrushViewChangeListener = brushViewChangeListener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mDrawCanvas = Canvas(canvasBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        for (linePath in mLinePaths) {
            canvas.drawPath(linePath.drawPath, linePath.drawPaint)
        }
        canvas.drawPath(mPath!!, mDrawPaint!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(@NonNull event: MotionEvent): Boolean {
        return if (mBrushDrawMode) {
            val touchX = event.x
            val touchY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchStart(touchX, touchY)
                MotionEvent.ACTION_MOVE -> touchMove(touchX, touchY)
                MotionEvent.ACTION_UP -> touchUp()
            }
            invalidate()
            true
        } else {
            false
        }
    }

    private inner class LinePath internal constructor(
        drawPath: Path?,
        drawPaints: Paint?
    ) {
        val drawPaint: Paint
        val drawPath: Path

        init {
            drawPaint = Paint(drawPaints)
            this.drawPath = Path(drawPath)
        }
    }

    fun undo(): Boolean {
        if (mLinePaths.size > 0) {
            mRedoLinePaths.add(mLinePaths.removeAt(mLinePaths.size - 1))
            invalidate()
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener!!.onViewRemoved(this)
        }
        return mLinePaths.size != 0
    }

    fun redo(): Boolean {
        if (mRedoLinePaths.size > 0) {
            mLinePaths.add(mRedoLinePaths.removeAt(mRedoLinePaths.size - 1))
            invalidate()
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener!!.onViewAdd(this)
        }
        return mRedoLinePaths.size != 0
    }

    private fun touchStart(x: Float, y: Float) {
        mRedoLinePaths.clear()
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mTouchX = x
        mTouchY = y
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener!!.onStartDrawing()
        }
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mTouchX)
        val dy = Math.abs(y - mTouchY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mTouchX, mTouchY, (x + mTouchX) / 2, (y + mTouchY) / 2)
            mTouchX = x
            mTouchY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mTouchX, mTouchY)
        // Commit the path to our offscreen
        mDrawCanvas!!.drawPath(mPath!!, mDrawPaint!!)
        // kill this so we don't double draw
        mLinePaths.add(LinePath(mPath, mDrawPaint))
        mPath = Path()
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener!!.onStopDrawing()
            mBrushViewChangeListener!!.onViewAdd(this)
        }
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
}