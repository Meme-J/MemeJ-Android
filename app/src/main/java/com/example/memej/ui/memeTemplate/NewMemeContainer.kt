package com.example.memej.ui.memeTemplate

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.lruCache
import androidx.core.graphics.withTranslation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.memej.R
import com.example.memej.Utils.Communicator2
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.TagAdapter
import com.example.memej.adapters.TagEditAdapter
import com.example.memej.adapters.onTagClickType
import com.example.memej.entities.createMemeBody
import com.example.memej.entities.searchBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.editMemeApiResponse
import com.example.memej.responses.homeMememResponses.Coordinates
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.OnPhotoEditorListener
import com.example.memej.textProperties.lib.Photo
import com.example.memej.textProperties.lib.ViewType
import com.example.memej.viewModels.NewMemeContainerViewModel
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewMemeContainer : AppCompatActivity(), onTagClickType {

    companion object {
        fun newInstance() = NewMemeContainer()
    }


    private lateinit var viewModel: NewMemeContainerViewModel
    lateinit var arg: Bundle

    // private lateinit var img: ShapeableImageView
    lateinit var edt: EditText
    private lateinit var paint_chosen: Paint
    private lateinit var type_face: Typeface
    val paths = ArrayList<Path>()
    val undonePaths = ArrayList<Path>()
    var sendButton: Boolean = false
    var saveCount: Int? = 0
    lateinit var sessionManager: SessionManager
    lateinit var comm: Communicator2
    lateinit var pb: ProgressBar
    lateinit var tagCheck: MaterialButton

    lateinit var adapterTagsAdded: TagEditAdapter
    lateinit var stringAdapter: ArrayAdapter<String>
    lateinit var mutableList: MutableList<String>
    lateinit var rvTagEdits: RecyclerView
    lateinit var HorizontalLayoutInsertedTags: LinearLayoutManager
    lateinit var adapterTagAdded: TagEditAdapter

    //    lateinit var photoView: PhotoEditorView
    lateinit var photoView: ImageEditorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_meme_container_fragment)

        arg = intent?.getBundleExtra("bundle")!!

        //   img = findViewById(R.id.imagePostNew)
        edt = findViewById(R.id.lineAddedEtNew)
        tagCheck = findViewById(R.id.tag_editNew)
//        photoView = findViewById(R.id.photoView)
        photoView = findViewById(R.id.imageView)
        rvTagEdits = findViewById<RecyclerView>(R.id.rv_insertedTagsNew)
        HorizontalLayoutInsertedTags = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        adapterTagAdded = TagEditAdapter()

        initializeEditFrame(arg)


        sessionManager =
            SessionManager(this)
        pb = findViewById(R.id.pb_new_fragment)


        // val colors = root.findViewById<MaterialTextView>(R.id.choose_color)
        // val font = root.findViewById<MaterialTextView>(R.id.choose_font)

        /*
            //Choose Color
            colors.setOnClickListener {
                context?.let {
                    MaterialColorPickerDialog
                        .Builder(it)                        // Pass Activity Instance
                        .setColorShape(ColorShape.SQAURE)
                        .setTitle("Pick a color")// Default ColorShape.CIRCLE
                        .setPositiveButton("Select")
                        .setNegativeButton("Cancel")
                        .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
                        .setColorRes(
                            resources.getIntArray(R.array.themeColors).toList()
                        )    // Pass Default Color
                        .setColorListener { color, colorHex ->
                            // Handle Color Selection
                            //Set the paint brush to be valued for this color
                            //Pass the color hex
                            paint_chosen = setPaint(colorHex)

                        }
                        .show()
                }

            }

            //Choose Font
            font.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Choose a font")

                //Create List
                val font_list = R.array.font_types

                val checkedItem = 0     //Arial

                builder.setSingleChoiceItems(
                    font_list,
                    checkedItem
                ) { dialog, which ->
                    // user checked an item
                    type_face = setFont(which)

                }
                builder.setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    // user clicked OK
                    //which is the int
                    //What is teh global type face
                    type_face = setFont(which)
                    dialog.dismiss()

                }
                builder.setNegativeButton("Cancel", null)
                val dialog = builder.create()
                dialog.show()


            }

        */

        //Init mutable list
        mutableList = mutableListOf()
        stringAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)


        val tagsEt = findViewById<AutoCompleteTextView>(R.id.auto_completeTagNew)

        val onItemClickTag =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->

                mutableList.add(adapterView.getItemAtPosition(i).toString())
                setInTagRv()
                tagsEt.text = null
            }

        tagsEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //Activation of button
                //find the button
                val tagCheck = findViewById<MaterialButton>(R.id.tag_editNew)
                if (tagsEt.length() != 0) {
                    tagCheck.isEnabled =
                        true
                } else if (tagsEt.length() == 0) {

                    tagCheck.isEnabled =
                        false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                getTags(s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getTags(s.toString())
            }
        })

        tagsEt.onItemClickListener = onItemClickTag


        tagCheck.setOnClickListener {
            mutableList.add(tagsEt.text.toString())
            setInTagRv()
            tagsEt.text = null
        }

        //Send button
        val btn = findViewById<MaterialButton>(R.id.send_post_new)
        btn.setOnClickListener {
            sendPost(edt.text.toString())
        }


    }


    private fun setInTagRv() {
        Log.e("Edit", "Values in Mutable list" + mutableList.toString())
        adapterTagAdded.tagAdded = mutableList
        rvTagEdits.layoutManager = HorizontalLayoutInsertedTags
        rvTagEdits.adapter = adapterTagAdded


    }

    private fun getTags(s: String) {

        val service = RetrofitClient.makeCallsForMemes(this)
        val inf = searchBody(s, "ongoing")
        service.getTags(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
            .enqueue(object : retrofit2.Callback<SearchResponse> {
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {

                    Log.e("Edit", "In tag search response")

                    val str = mutableListOf<String>()
                    for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
                        str.add(y.tag)

                    }
                    Log.e("Edit", str.toString())
                    val actv =
                        findViewById<AutoCompleteTextView>(R.id.auto_completeTagNew)

                    stringAdapter =
                        ArrayAdapter(
                            this@NewMemeContainer,
                            android.R.layout.simple_dropdown_item_1line,
                            str
                        )


                    actv.setAdapter(stringAdapter)
                }
            })
    }


    private fun sendPost(line: String) {
        //Show the progress bar
        pb.visibility = View.VISIBLE
        Log.e("send", "CP1")
        val service = RetrofitClient.makeCallsForMemes(this)
        val inf =
            createMemeBody(arg.getInt("numPlaceholders"), line, mutableList, arg.getString("id")!!)

        Log.e("send", "CP1")

        Log.e("send", inf.toString() + "input")

        service.createMeme(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
            .enqueue(object : Callback<editMemeApiResponse> {
                override fun onFailure(call: Call<editMemeApiResponse>, t: Throwable) {

                    Log.e("send", "InFail")
                    Toast.makeText(
                        this@NewMemeContainer,
                        t.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<editMemeApiResponse>,
                    response: Response<editMemeApiResponse>
                ) {

                    Log.e("send", "InResp" + response.toString())
                    //Response will be good if the meme is created
                    //Log the response
                    Log.e(
                        "send",
                        response.body()
                            .toString() + response.errorBody() + response.code() + "\n" + response.body()?.msg
                                + " \n" + response.body()?.meme + "\n" + response.headers()
                            .toString()
                    )

                    if (response.isSuccessful) {

                        Log.e("send", "InRespSuccess")
                        if (response.body()?.msg == "Meme created successfully") {
                            Toast.makeText(
                                this@NewMemeContainer,
                                response.body()!!.msg,
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e("send", "InGood Message")
                            pb.visibility = View.GONE
                            //Go back to the main activity
                            //On Creating a new meme retrn to the main activty of return to the parent activity
                            //Intent back to main activty
                            val i = Intent(
                                this@NewMemeContainer,
                                SelectMemeTemplateActivity::class.java
                            )
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(
                                this@NewMemeContainer,
                                response.body()?.msg,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {

                        Log.e("send", response.errorBody().toString())
                        Log.e("send", "InResp Fail")
                    }
                }

            })

        Log.e("send", "In exit retro call")
    }


    private fun initializeEditFrame(arg: Bundle) {
        //Layout
        val HorizontalLayout: LinearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        //Load template tags
        val txt = arg.getStringArrayList("tags")


        val tagsStr = mutableListOf<String>()
        //No image tags for init
        for (i in txt!!) {
            tagsStr.add(i)
        }

        //Get the rv and adapter for the user and the tags already existing
        val rvTag = findViewById<RecyclerView>(R.id.rv_edit_tag)
        val tagAdapter = TagAdapter(this)
        tagAdapter.tagType = tagsStr
        rvTag.layoutManager = HorizontalLayout
        rvTag.adapter = tagAdapter

        //There are no users

        getImage()


    }


    private fun getImage() {

        Glide.with(this)
            .asBitmap()
            .load(arg.getString("imageUrl"))
            .placeholder(R.drawable.icon_placeholder)
            .error(R.drawable.icon_placeholder)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    val canvas = Canvas(resource)
                    photoView.source?.draw(canvas)
                    photoView.source?.setImageBitmap(resource)
                    getCompleteCoordinatesToBeUsed(resource, canvas)

                }
            })

    }

    private fun getCompleteCoordinatesToBeUsed(
        bitmap: Bitmap?, canvas: Canvas
    ) {


        val xN =
            arg.getParcelableArrayList<Coordinates>("coordinate")!!
                .elementAt(0).x
        val yN =
            arg.getParcelableArrayList<Coordinates>("coordinate")!!
                .elementAt(0).y

        val xB =
            arg.getParcelableArrayList<Coordinates>("coordinate")!!
                .elementAt(1).x
        val yB =
            arg.getParcelableArrayList<Coordinates>("coordinate")!!
                .elementAt(1).y

        val currentPaint = getTextPaint(
            arg.getStringArrayList("textColorCode")!!.elementAt(0),
            arg.getIntegerArrayList("textSize")!!.elementAt(0)
        )

        canvas.save()
        val color = arg.getStringArrayList("textColorCode")!!.elementAt(0)
        val size = arg.getIntegerArrayList("textSize")!!.elementAt(0)
        val colorInt = Color.parseColor(color)

        observeTextChange(bitmap, canvas, arg, xN, yN, currentPaint, colorInt, size, xB, yB)
    }


    private fun observeTextChange(
        bitmap: Bitmap?,
        canvas: Canvas,
        arg: Bundle,
        xN: Int,
        yN: Int,
        currentPaint: TextPaint,
        colorInt: Int,
        size: Int,
        xB: Int,
        yB: Int
    ) {


        //Sample two
        val photoEditorClass = Photo.Builder(this, photoView, xN, yN, xB, yB)
            .setPinchTextScalable(false)
            .build()


//        //Instance of PhotoEditor
//        val mPhotoEditor = PhotoEditor.Builder(this, photoView)
//            .setPinchTextScalable(false)        //False for zooming of image
//            .build()


        photoView.source!!.adjustViewBounds = true
        val ht = photoView.source?.height
        val wd = photoView.source?.width
        val lh = photoView.source?.layoutParams?.height
        val lw = photoView.source?.layoutParams?.width


        //Check on Edt
//        Log.e("View", " " + mPhotoEditor.toString())
        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (edt.text.isNotEmpty()) {
                    sendButton = true
                    findViewById<MaterialButton>(R.id.send_post_new).isEnabled =
                        true
                } else if (edt.text.isEmpty()) {
                    sendButton = false
                    findViewById<MaterialButton>(R.id.send_post_new).isEnabled =
                        false
                }
                Log.e(
                    "Child Count",
                    photoView.childCount.toString() + "w " + ht + " " + wd + " " + lh + " " + lw
                )

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                photoEditorClass.clearAllViews()
                photoEditorClass.addText(s.toString(), colorInt, size.toFloat())


            }
        })


        photoEditorClass.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
                edt.requestFocus()
            }

            override fun onStartViewChangeListener(viewType: ViewType?) {

            }

            override fun onRemoveViewListener(numberOfAddedViews: Int) {
                edt.text = null
                edt.requestFocus()
            }

            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
                edt.requestFocus()
            }

            override fun onStopViewChangeListener(viewType: ViewType?) {

            }
        })


    }


    fun Canvas.drawDynamicLayout(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        editText: EditText,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,

        breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
                    "$spacingMult-$spacingAdd" +
                    "$breakStrategy-$hyphenationFrequency"


            val dynamicLayout = DynamicLayoutCache[cacheKey] ?: DynamicLayout.Builder
                .obtain(text, textPaint, width)
                .setAlignment(alignment)
                .setTextDirection(textDir)
                .setLineSpacing(spacingAdd, spacingMult)
                .setBreakStrategy(breakStrategy)
                .build()

            dynamicLayout.draw(this, x, y)


        } else {
            Toast.makeText(this@NewMemeContainer, "Version not supported", Toast.LENGTH_SHORT)
                .show()
        }

    }

    //Helper Classes
    private fun DynamicLayout.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.withTranslation(x, y) {
            draw(this)
        }
    }

    fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {


        canvas.withTranslation(x, y) {
            draw(this)
        }
    }

    private object StaticLayoutCache {

        private const val MAX_SIZE = 50 // Arbitrary max number of cached items
        private val cache = lruCache<String, StaticLayout>(MAX_SIZE)

        operator fun set(key: String, staticLayout: StaticLayout) {
            cache.put(key, staticLayout)
        }

        operator fun get(key: String): StaticLayout? {
            return cache[key]
        }
    }

    private object DynamicLayoutCache {
        private const val MAX_SIZE = 50 // Arbitrary max number of cached items
        private val cache = lruCache<String, DynamicLayout>(MAX_SIZE)

        operator fun set(key: String, dynamicLayout: DynamicLayout) {
            cache.put(key, dynamicLayout)
        }

        operator fun get(key: String): DynamicLayout? {
            return cache[key]
        }
    }

    private fun getTextPaint(color: String, size: Int?): TextPaint {
        val paint = TextPaint()
        paint.color = Color.parseColor(color)
        paint.strokeWidth = 15F
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.FILL //Default to be set
        paint.isAntiAlias = true
        paint.textSize = size?.let { setSize(it) }!!
        paint.isDither = true

        return paint
    }

    private fun setSize(size: Int): Float {
        val size_req = size.toFloat()
        return size_req

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFont(id: Int): Typeface {
        //Takes int
        val i = id
        var type: Typeface = resources.getFont(R.font.arial)
        when (i) {

            0 -> type = resources.getFont(R.font.arial)
            1 -> resources.getFont(R.font.long_fox_font)
            2 -> resources.getFont(R.font.bomb_font)
            3 -> resources.getFont(R.font.romot_reavers_font)
            4 -> resources.getFont(R.font.fonty_font)
        }
        return type        //Equal to the type

    }

    //Back Behaviour maintained
    private fun getColorWithAlpha(color: Int, ratio: Float): Int {
        var newColor = 0
        val alpha = Math.round(Color.alpha(color) * ratio).toInt()
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        newColor = Color.argb(alpha, r, g, b)
        return newColor
    }


    override fun getTagType(_tag: String) {
        TODO("Not yet implemented")
    }

}
