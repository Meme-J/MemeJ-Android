package com.example.memej.textProperties.lib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.*
import com.example.memej.R
import java.io.File
import java.io.FileOutputStream
import java.util.*


class Photo private constructor(builder: Builder) :
    BrushInterface {

    private val mLayoutInflater: LayoutInflater
    private val context: Context
    private val parentView: RelativeLayout?
    private val imageView: ImageView
    private val deleteView: View?
    private val brushDrawingView: BrushView?
    private val addedViews: MutableList<View?>
    private val redoViews: MutableList<View?>
    private var mOnPhotoEditorListener: OnPhotoEditorListener? = null
    private val isTextPinchZoomable: Boolean
    private val mDefaultTextTypeface: Typeface?
    private val mDefaultEmojiTypeface: Typeface?

    private val startX: Int
    private val startY: Int
    private val endY: Int
    private val endX: Int


    fun addImage(desiredImage: Bitmap?) {
        val imageRootView =
            getLayout(ViewType.IMAGE)
        val imageView =
            imageRootView!!.findViewById<ImageView>(R.id.imgPhotoEditorImage)
        val frmBorder = imageRootView.findViewById<FrameLayout>(R.id.frmBorder)
        val imgClose =
            imageRootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
        imageView.setImageBitmap(desiredImage)
        val multiTouchListener =
            multiTouchListener
        multiTouchListener!!.setOnGestureControl(object :
            MultiTouchListener.OnGestureControl {
            override fun onClick() {
                val isBackgroundVisible =
                    frmBorder.tag != null && frmBorder.tag as Boolean
                frmBorder.setBackgroundResource(if (isBackgroundVisible) 0 else R.drawable.rounded_border_tv)
                imgClose.visibility = if (isBackgroundVisible) View.GONE else View.VISIBLE
                frmBorder.tag = !isBackgroundVisible
            }

            override fun onLongClick() {}
        })
        imageRootView.setOnTouchListener(multiTouchListener)
        addViewToParent(imageRootView, ViewType.IMAGE)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun addOldText(text: String?, colorCodeTextView: Int, size: Float) {
        addOldText(null, text, colorCodeTextView, size)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun addOldText(
        @Nullable textTypeface: Typeface?,
        text: String?,
        colorCodeTextView: Int,
        size: Float
    ) {
        brushDrawingView!!.brushDrawingMode = false

        val textRootView =
            getLatoutForOldText(ViewType.TEXT)
        val textInputTv = textRootView!!.findViewById<TextView>(R.id.tvPhotoEditorText)
        //Image closing button
//        val imgClose =
//            textRootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)


        val frmBorder = textRootView.findViewById<FrameLayout>(R.id.frmBorder)
        textInputTv.text = text
        textInputTv.setTextColor(colorCodeTextView)
        textInputTv.textSize = size

        if (textTypeface != null) {
            textInputTv.typeface = textTypeface
        }
        val multiTouchListener =
            multiTouchListener
        multiTouchListener!!.setOnGestureControl(object :
            MultiTouchListener.OnGestureControl {
            override fun onClick() {
                val isBackgroundVisible =
                    frmBorder.tag != null && frmBorder.tag as Boolean
                frmBorder.setBackgroundResource(if (isBackgroundVisible) 0 else R.drawable.rounded_border_tv)
//                imgClose.visibility = if (isBackgroundVisible) View.GONE else View.VISIBLE
                frmBorder.tag = !isBackgroundVisible
            }

            override fun onLongClick() {
                val textInput = textInputTv.text.toString()
                val currentTextColor = textInputTv.currentTextColor
                if (mOnPhotoEditorListener != null) {
                    mOnPhotoEditorListener!!.onEditTextChangeListener(
                        textRootView,
                        textInput,
                        currentTextColor
                    )
                }
            }
        })
//        textRootView.setOnTouchListener(multiTouchListener)
        addViewToParent(textRootView, ViewType.TEXT)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun addText(text: String?, colorCodeTextView: Int, size: Float) {
        addText(null, text, colorCodeTextView, size)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addText(
        @Nullable textTypeface: Typeface?,
        text: String?,
        colorCodeTextView: Int,
        size: Float
    ) {
        brushDrawingView!!.brushDrawingMode = false

        val textRootView =
            getLayout(ViewType.TEXT)
        val textInputTv = textRootView!!.findViewById<TextView>(R.id.tvPhotoEditorText)
        val imgClose =
            textRootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
        val frmBorder = textRootView.findViewById<FrameLayout>(R.id.frmBorder)
        textInputTv.text = text
        textInputTv.setTextColor(colorCodeTextView)
        textInputTv.textSize = size

        if (textTypeface != null) {
            textInputTv.typeface = textTypeface
        }
        val multiTouchListener =
            multiTouchListener
        multiTouchListener!!.setOnGestureControl(object :
            MultiTouchListener.OnGestureControl {
            override fun onClick() {
                val isBackgroundVisible =
                    frmBorder.tag != null && frmBorder.tag as Boolean
                frmBorder.setBackgroundResource(if (isBackgroundVisible) 0 else R.drawable.rounded_border_tv)
                imgClose.visibility = if (isBackgroundVisible) View.GONE else View.VISIBLE
                frmBorder.tag = !isBackgroundVisible
            }

            override fun onLongClick() {
                val textInput = textInputTv.text.toString()
                val currentTextColor = textInputTv.currentTextColor
                if (mOnPhotoEditorListener != null) {
                    mOnPhotoEditorListener!!.onEditTextChangeListener(
                        textRootView,
                        textInput,
                        currentTextColor
                    )
                }
            }
        })
        //Remove the listener
        // textRootView.setOnTouchListener(multiTouchListener)
        addViewToParent(textRootView, ViewType.TEXT)
    }

    fun editText(view: View, inputText: String?, colorCode: Int, size: Float) {
        editText(view, null, inputText, colorCode, size)
    }

    /**
     * This will update the text and color on provided view
     *
     * @param view         root view where text view is a child
     * @param textTypeface optional if provided
     * @param inputText    text to update textview
     * @param colorCode    color to update on textview
     * @param size         size of the text
     */

    fun editText(
        view: View,
        textTypeface: Typeface?,
        inputText: String?,
        colorCode: Int,
        size: Float
    ) {
        val inputTextView = view.findViewById<TextView>(R.id.tvPhotoEditorText)
        if (inputTextView != null && addedViews.contains(view) && !TextUtils.isEmpty(inputText)) {
            inputTextView.text = inputText
            if (textTypeface != null) {
                inputTextView.typeface = textTypeface
            }
            inputTextView.setTextColor(colorCode)
            inputTextView.textSize = size

            parentView!!.updateViewLayout(view, view.layoutParams)
            val i = addedViews.indexOf(view)
            if (i > -1) addedViews[i] = view
        }
    }

    fun addEmoji(emojiName: String?) {
        addEmoji(null, emojiName)
    }

    fun addEmoji(emojiTypeface: Typeface?, emojiName: String?) {
        brushDrawingView!!.brushDrawingMode = false
        val emojiRootView =
            getLayout(ViewType.EMOJI)
        val emojiTextView = emojiRootView!!.findViewById<TextView>(R.id.tvPhotoEditorText)
        val frmBorder = emojiRootView.findViewById<FrameLayout>(R.id.frmBorder)
        val imgClose =
            emojiRootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
        if (emojiTypeface != null) {
            emojiTextView.typeface = emojiTypeface
        }
        emojiTextView.textSize = 56f
        emojiTextView.text = emojiName
        val multiTouchListener =
            multiTouchListener
        multiTouchListener!!.setOnGestureControl(object :
            MultiTouchListener.OnGestureControl {
            override fun onClick() {
                val isBackgroundVisible =
                    frmBorder.tag != null && frmBorder.tag as Boolean
                frmBorder.setBackgroundResource(if (isBackgroundVisible) 0 else R.drawable.rounded_border_tv)
                imgClose.visibility = if (isBackgroundVisible) View.GONE else View.VISIBLE
                frmBorder.tag = !isBackgroundVisible
            }

            override fun onLongClick() {}
        })
        emojiRootView.setOnTouchListener(multiTouchListener)
        addViewToParent(emojiRootView, ViewType.EMOJI)
    }

    /**
     * Add to root view from image,emoji and text to our parent view
     *
     * @param rootView rootview of image,text and emoji
     */


    private fun addViewToParent(
        rootView: View?,
        viewType: ViewType
    ) {
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(startX, startY, endX, endY)

        //params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        parentView!!.addView(rootView, params)
        addedViews.add(rootView)
        if (mOnPhotoEditorListener != null) mOnPhotoEditorListener!!.onAddViewListener(
            viewType,
            addedViews.size
        )
    }//multiTouchListener.setOnMultiTouchListener(this);

    /**
     * Create a new instance and scalable touchview
     *
     * @return scalable multitouch listener
     */
    @get:NonNull
    private val multiTouchListener: MultiTouchListener?
        private get() =//multiTouchListener.setOnMultiTouchListener(this);
            parentView?.let {
                MultiTouchListener(
                    deleteView,
                    it,
                    imageView,
                    isTextPinchZoomable,
                    mOnPhotoEditorListener
                )
            }

    /**
     * Get root view by its type i.e image,text and emoji
     *
     * @param viewType image,text or emoji
     * @return rootview
     */

    private fun getLatoutForOldText(viewType: ViewType): View? {
        var rootView: View? = null

        when (viewType) {
            ViewType.TEXT -> {
                rootView = mLayoutInflater.inflate(R.layout.view_photo_text, null)
                val txtText = rootView.findViewById<TextView>(R.id.tvPhotoEditorText)
                if (txtText != null && mDefaultTextTypeface != null) {
                    txtText.gravity = Gravity.CENTER
                    if (mDefaultEmojiTypeface != null) {
                        txtText.typeface = mDefaultTextTypeface
                    }
                }
            }
            ViewType.IMAGE -> rootView =
                mLayoutInflater.inflate(R.layout.view_photo_editor_image, null)
            ViewType.EMOJI -> {
                rootView = mLayoutInflater.inflate(R.layout.view_photo_text, null)

                val txtTextEmoji =
                    rootView.findViewById<TextView>(R.id.tvPhotoEditorText)
                if (txtTextEmoji != null) {
                    if (mDefaultEmojiTypeface != null) {
                        txtTextEmoji.typeface = mDefaultEmojiTypeface
                    }
                    txtTextEmoji.gravity = Gravity.CENTER
                    txtTextEmoji.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }
            }
        }

        //This was the closing parameter
//        if (rootView != null) {
//            val imgClose =
//                rootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
//            val finalRootView: View = rootView
//            imgClose?.setOnClickListener { viewUndo(finalRootView) }
//        }
        return rootView


    }

    private fun getLayout(viewType: ViewType): View? {
        var rootView: View? = null
        when (viewType) {
            ViewType.TEXT -> {
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_text, null)
                val txtText = rootView.findViewById<TextView>(R.id.tvPhotoEditorText)
                if (txtText != null && mDefaultTextTypeface != null) {
                    txtText.gravity = Gravity.CENTER
                    if (mDefaultEmojiTypeface != null) {
                        txtText.typeface = mDefaultTextTypeface
                    }
                }
            }
            ViewType.IMAGE -> rootView =
                mLayoutInflater.inflate(R.layout.view_photo_editor_image, null)
            ViewType.EMOJI -> {
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_text, null)
                val txtTextEmoji =
                    rootView.findViewById<TextView>(R.id.tvPhotoEditorText)
                if (txtTextEmoji != null) {
                    if (mDefaultEmojiTypeface != null) {
                        txtTextEmoji.typeface = mDefaultEmojiTypeface
                    }
                    txtTextEmoji.gravity = Gravity.CENTER
                    txtTextEmoji.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }
            }
        }
        if (rootView != null) {
            val imgClose =
                rootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
            val finalRootView: View = rootView
            imgClose?.setOnClickListener { viewUndo(finalRootView) }
        }
        return rootView
    }


    fun setBrushDrawingMode(brushDrawingMode: Boolean) {
        brushDrawingView?.brushDrawingMode = brushDrawingMode
    }

    val brushDrawableMode: Boolean
        get() = brushDrawingView != null && brushDrawingView.brushDrawingMode


    fun setOpacity(@androidx.annotation.IntRange(from = 0, to = 100) opacity: Int) {
        var opacity = opacity
        if (brushDrawingView != null) {
            opacity = (opacity / 100.0 * 255.0).toInt()
            brushDrawingView.setOpacity(opacity)
        }
    }

    fun setBrushEraserSize(brushEraserSize: Float) {
        brushDrawingView?.setBrushEraserSize(brushEraserSize)
    }

    fun setBrushEraserColor(@ColorInt color: Int) {
        brushDrawingView?.setBrushEraserColor(color)
    }

    val eraserSize: Float?
        get() = brushDrawingView?.eraserSize


    var brushSize: Float?
        get() = brushDrawingView?.brushSize
        set(size) {
            if (size != null) {
                brushDrawingView?.brushSize = size
            }
        }

    var brushColor: Int?
        get() = brushDrawingView?.brushColor
        set(color) {
            if (color != null) {
                brushDrawingView?.brushColor = color
            }
        }

    fun brushEraser() {
        brushDrawingView?.brushEraser()
    }

    private fun viewUndo() {
        if (addedViews.size > 0) {
            parentView!!.removeView(addedViews.removeAt(addedViews.size - 1))
            if (mOnPhotoEditorListener != null) mOnPhotoEditorListener!!.onRemoveViewListener(
                addedViews.size
            )
        }
    }

    private fun viewUndo(removedView: View) {
        if (addedViews.size > 0) {
            if (addedViews.contains(removedView)) {
                parentView!!.removeView(removedView)
                addedViews.remove(removedView)
                redoViews.add(removedView)
                if (mOnPhotoEditorListener != null) mOnPhotoEditorListener!!.onRemoveViewListener(
                    addedViews.size
                )
            }
        }
    }

    fun undo(): Boolean {
        if (addedViews.size > 0) {
            val removeView = addedViews[addedViews.size - 1]
            if (removeView is BrushView) {
                return brushDrawingView != null && brushDrawingView.undo()
            } else {
                addedViews.removeAt(addedViews.size - 1)
                parentView!!.removeView(removeView)
                redoViews.add(removeView)
            }
            if (mOnPhotoEditorListener != null) {
                mOnPhotoEditorListener!!.onRemoveViewListener(addedViews.size)
            }
        }
        return addedViews.size != 0
    }

    fun redo(): Boolean {
        if (redoViews.size > 0) {
            val redoView = redoViews[redoViews.size - 1]
            if (redoView is BrushView) {
                return brushDrawingView != null && brushDrawingView.redo()
            } else {
                redoViews.removeAt(redoViews.size - 1)
                parentView!!.addView(redoView)
                addedViews.add(redoView)
            }
        }
        return redoViews.size != 0
    }

    private fun clearBrushAllViews() {
        brushDrawingView?.clearAll()
    }

    fun clearAllViews() {
        for (i in addedViews.indices) {
            parentView!!.removeView(addedViews[i])
        }
        if (addedViews.contains(brushDrawingView)) {
            parentView!!.addView(brushDrawingView)
        }
        addedViews.clear()
        redoViews.clear()
        clearBrushAllViews()
    }

    /**
     * Remove all helper boxes from text
     */
    @UiThread
    private fun clearTextHelperBox() {
        for (i in 0 until parentView!!.childCount) {
            val childAt = parentView.getChildAt(i)
            val frmBorder = childAt.findViewById<FrameLayout>(R.id.frmBorder)
            frmBorder?.setBackgroundResource(0)
            val imgClose =
                childAt.findViewById<ImageView>(R.id.imgPhotoEditorClose)
            if (imgClose != null) {
                imgClose.visibility = View.GONE
            }
        }
    }

    interface OnSaveListener {
        fun onSuccess(@NonNull imagePath: String?)

        fun onFailure(@NonNull exception: Exception?)
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    fun saveImage(
        @NonNull imagePath: String,
        @NonNull onSaveListener: OnSaveListener
    ) {
        Log.d(TAG, "Image Path: $imagePath")
        object : AsyncTask<String?, String?, Exception?>() {
            override fun onPreExecute() {
                super.onPreExecute()
                clearTextHelperBox()
                parentView!!.isDrawingCacheEnabled = false
            }

            @SuppressLint("MissingPermission")
            override fun doInBackground(vararg params: String?): Exception? {
                // Create a media file name
                val file = File(imagePath)
                return try {
                    val out = FileOutputStream(file, false)
                    if (parentView != null) {
                        parentView.isDrawingCacheEnabled = true
                        val drawingCache = parentView.drawingCache
                        drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                    out.flush()
                    out.close()
                    Log.e(TAG, "Filed Saved Successfully")
                    Log.d(TAG, "Filed Saved Successfully")

                    null
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "Failed to save File")
                    Log.d(TAG, "Failed to save File")

                    return e
                }

            }

            override fun onPostExecute(e: Exception?) {
                super.onPostExecute(e)
                if (e == null) {
                    clearAllViews()
                    onSaveListener.onSuccess(imagePath)
                } else {
                    onSaveListener.onFailure(e)
                }
            }
        }.execute()
    }

    private val isSDCARDMounted: Boolean
        private get() {
            val status = Environment.getExternalStorageState()
            return status == Environment.MEDIA_MOUNTED
        }

    fun setOnPhotoEditorListener(@NonNull onPhotoEditorListener: OnPhotoEditorListener?) {
        mOnPhotoEditorListener = onPhotoEditorListener
    }

    /**
     * Check if any changes made need to save
     *
     * @return true is nothing is there to change
     */
    val isCacheEmpty: Boolean
        get() = addedViews.size == 0 && redoViews.size == 0

    //##OVerriden Methods
    override fun onViewAdd(brushDrawingView: BrushView?) {
        if (redoViews.size > 0) {
            redoViews.removeAt(redoViews.size - 1)
        }
        addedViews.add(brushDrawingView)
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener!!.onAddViewListener(
                ViewType.BRUSH_DRAWING,
                addedViews.size
            )
        }
    }

    override fun onViewRemoved(brushDrawingView: BrushView?) {
        if (addedViews.size > 0) {
            val removeView = addedViews.removeAt(addedViews.size - 1)
            if (removeView !is BrushView) {
                parentView!!.removeView(removeView)
            }
            redoViews.add(removeView)
        }
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener!!.onRemoveViewListener(addedViews.size)
        }
    }


    override fun onStartDrawing() {
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener!!.onStartViewChangeListener(ViewType.BRUSH_DRAWING)
        }
    }

    override fun onStopDrawing() {
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener!!.onStopViewChangeListener(ViewType.BRUSH_DRAWING)
        }
    }

    class Builder(
        val context: Context,
        photoEditorView: ImageEditorView,
        var startX: Int,
        var startY: Int,
        var endX: Int,
        var endY: Int
    ) {
        val parentView: RelativeLayout
        val imageView: ImageView
        var deleteView: View? = null
        var brushDrawingView: BrushView
        var textTypeface: Typeface? = null
        var emojiTypeface: Typeface? = null

        //By Default pinch zoom on text is enabled
        var isTextPinchZoomable = true
        fun setDeleteView(deleteView: View?): Builder {
            this.deleteView = deleteView
            return this
        }

        fun setDefaultTextTypeface(textTypeface: Typeface?): Builder {
            this.textTypeface = textTypeface
            return this
        }

        fun setDefaultEmojiTypeface(emojiTypeface: Typeface?): Builder {
            this.emojiTypeface = emojiTypeface
            return this
        }

        fun setPinchTextScalable(isTextPinchZoomable: Boolean): Builder {
            this.isTextPinchZoomable = isTextPinchZoomable
            return this
        }

        fun setBrushDrawingView(brushDrawingView: BrushView): Builder {
            this.brushDrawingView = brushDrawingView
            return this
        }

        fun build(): Photo {
            return Photo(this)
        }

        init {
            parentView = photoEditorView
            imageView = photoEditorView.source!!
            brushDrawingView = photoEditorView.brushDrawingView!!
        }
    }

    companion object {
        private val TAG = ImageEditorView::class.java.simpleName
        private fun convertEmoji(emoji: String): String {
            val returnedEmoji: String
            returnedEmoji = try {
                val convertEmojiToInt = emoji.substring(2).toInt(16)
                getEmojiByUnicode(convertEmojiToInt)
            } catch (e: NumberFormatException) {
                ""
            }
            return returnedEmoji
        }

        private fun getEmojiByUnicode(unicode: Int): String {
            return String(Character.toChars(unicode))
        }

        fun getEmojis(context: Context): ArrayList<String> {
            val convertedEmojiList =
                ArrayList<String>()
            val emojiList =
                context.resources.getStringArray(R.array.photo_editor_emoji)
            for (emojiUnicode in emojiList) {
                convertedEmojiList.add(convertEmoji(emojiUnicode))
            }
            return convertedEmojiList
        }
    }

    init {
        context = builder.context
        parentView = builder.parentView
        imageView = builder.imageView
        deleteView = builder.deleteView
        brushDrawingView = builder.brushDrawingView
        isTextPinchZoomable = builder.isTextPinchZoomable
        mDefaultTextTypeface = builder.textTypeface
        mDefaultEmojiTypeface = builder.emojiTypeface
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        brushDrawingView.setBrushViewChangeListener(this)
        addedViews = ArrayList()
        redoViews = ArrayList()
        startX = builder.startX
        startY = builder.startY
        endX = builder.endX
        endY = builder.endY

    }
}
