package com.example.memej.ui.memeTemplate

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Path
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.Communicator2
import com.example.memej.Utils.ErrorStatesResponse
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
import com.example.memej.textProperties.ConversionUtil
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.OnPhotoEditorListener
import com.example.memej.textProperties.lib.Photo
import com.example.memej.textProperties.lib.ViewType
import com.example.memej.viewModels.NewMemeContainerViewModel
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class NewMemeContainer : AppCompatActivity(), onTagClickType {

    companion object {
        fun newInstance() = NewMemeContainer()
    }


    private lateinit var viewModel: NewMemeContainerViewModel
    lateinit var arg: Bundle

    // private lateinit var img: ShapeableImageView
    lateinit var edt: EditText
    val paths = ArrayList<Path>()
    val undonePaths = ArrayList<Path>()
    var sendButton: Boolean = false
    var saveCount: Int? = 0
    lateinit var sessionManager: SessionManager
    lateinit var comm: Communicator2
    lateinit var pb: ProgressBar
    lateinit var tagCheck: MaterialButton


    //Global to be used
    private var paint_chosen by Delegates.notNull<Int>()
    private lateinit var type_face: Typeface
    private var size_chosen by Delegates.notNull<Float>()
    lateinit var colorIndicator: CardView

    var whichFont = 0
    var whichPaint = Color.BLACK
    var whichProgress = 20


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
        edt = findViewById(R.id.lineAddedEtNew)
        tagCheck = findViewById(R.id.tag_editNew)
        photoView = findViewById(R.id.imageView)

        edt.requestFocus()
        //Default Paint, TypeFace. Size
        val tf: Typeface = Typeface.DEFAULT
        type_face = tf
        paint_chosen = Color.parseColor("#000000")      //Default color
        size_chosen = 20f
//        colorIndicator = findViewById(R.id.colorIndicator_new)


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

//        val colors = findViewById<MaterialTextView>(R.id.choose_color_new)
//        val font = findViewById<MaterialTextView>(R.id.choose_font_new)
//        val size = findViewById<MaterialTextView>(R.id.choose_size_new)


//        colors.setOnClickListener {
//            chooseColor()
//        }
//        font.setOnClickListener {
//            chooseFont()
//        }
//        size.setOnClickListener {
//            chooseSizeText()
//        }
//        colorIndicator.setOnClickListener {
//            chooseColor()
//        }


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

//
//    private fun chooseSizeText() {
//        //Use a seek bar
//
//        val popDialog =
//            AlertDialog.Builder(this)
//
//
//        val seek = SeekBar(this)
//        seek.max = 40
//        seek.progress = whichProgress
//        seek.keyProgressIncrement = 2
//
//        popDialog.setTitle("Select Size")
//        popDialog.setView(seek)
//        popDialog.setMessage("Choose a size")
//
//        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seekBar: SeekBar?,
//                progress: Int,
//                fromUser: Boolean
//            ) {
//                size_chosen = progress.toFloat()
//                whichProgress = progress
//            }
//
//            override fun onStartTrackingTouch(arg0: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Log.e("Size", seek.progress.toString())
//            }
//        })
//
//        popDialog.setPositiveButton("OK",
//            object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//
//                    dialog.dismiss()
//                }
//            })
//        popDialog.create()
//        popDialog.show()
//
//
//    }

//    private fun chooseFont() {
//
//        var typeface = Typeface.DEFAULT
//        //Choose Font
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Choose a font")
//
//        //Create List
//        val font_list = R.array.font_types
//
//        val checkedItem = whichFont     //Arial
//
//        builder.setSingleChoiceItems(
//            font_list,
//            checkedItem
//        ) { dialog, which ->
//            // user checked an item
//            whichFont = which
//            when (which) {
//                0 -> typeface = Typeface.createFromAsset(assets, "arial.ttf")
//                1 -> typeface = Typeface.createFromAsset(assets, "long_fox_font.ttf")
//                2 -> typeface = Typeface.createFromAsset(assets, "bomb_font.ttf")
//                3 -> typeface = Typeface.createFromAsset(assets, "romot_reavers_font.ttf")
//                4 -> typeface = Typeface.createFromAsset(assets, "fonty_font.ttf")
//            }
//            type_face = typeface
//
//        }
//
//        builder.setPositiveButton(
//            "OK"
//        ) { dialog, which ->
//
//
//            when (which) {
//                0 -> typeface = Typeface.createFromAsset(assets, "arial.ttf")
//                1 -> typeface = Typeface.createFromAsset(assets, "long_fox_font.ttf")
//                2 -> typeface = Typeface.createFromAsset(assets, "bomb_font.ttf")
//                3 -> typeface = Typeface.createFromAsset(assets, "romot_reavers_font.ttf")
//                4 -> typeface = Typeface.createFromAsset(assets, "fonty_font.ttf")
//            }
//            type_face = typeface
//            Log.e("Font", typeface.toString())
//            dialog.dismiss()
//
//        }
//        builder.setNegativeButton("Cancel", null)
//
//        val dialog = builder.create()
//        dialog.show()
//
//
//    }
//
//    private fun chooseColor() {
//
//        this.let {
//            MaterialColorPickerDialog
//                .Builder(it)                                              // Pass Activity Instance
//                .setColorShape(ColorShape.SQAURE)
//                .setTitle("Pick a color") // Default ColorShape.CIRCLE
//                .setPositiveButton("Select")
//                .setDefaultColor(whichPaint)
//                .setNegativeButton("Cancel")
//                .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
//                .setColorRes(
//                    resources.getIntArray(R.array.themeColors).toList()
//                )    // Pass Default Color
//                .setColorListener { color, colorHex ->
//                    // Handle Color Selection
//
//                    //Set the paint brush to be valued for this color
//                    //Pass the color hex
//                    paint_chosen = color
//                    colorIndicator.setCardBackgroundColor(color)
//                    whichPaint = color
//                    Log.e("Color", paint_chosen.toString())
//                }
//                .show()
//        }
//
//    }

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
        val service = RetrofitClient.makeCallsForMemes(this)
        val inf =
            createMemeBody(arg.getInt("numPlaceholders"), line, mutableList, arg.getString("id")!!)

        val dialog = ProgressDialog(this)
        dialog.setMessage("Creating meme")
        dialog.show()

        service.createMeme(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
            .enqueue(object : Callback<editMemeApiResponse> {
                override fun onFailure(call: Call<editMemeApiResponse>, t: Throwable) {

                    dialog.dismiss()
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    android.app.AlertDialog.Builder(this@NewMemeContainer)
                        .setTitle("Unable to create")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .show()

                }

                override fun onResponse(
                    call: Call<editMemeApiResponse>,
                    response: Response<editMemeApiResponse>
                ) {

                    if (response.body()?.msg == "Meme created successfully") {

                        dialog.dismiss()
                        val i = Intent(this@NewMemeContainer, MainActivity::class.java)
                        startActivity(i)
                        finish()

                    } else {

                        dialog.dismiss()
                        val message = response.body()?.msg
                        android.app.AlertDialog.Builder(this@NewMemeContainer)
                            .setTitle("Unable to create meme")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok) { _, _ -> }
                            .show()


                    }
                }
            })

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

        //Fit the timestamp of last updtaed for template
        getImage()


    }


    private fun getImage() {

        photoView.source?.let {
            Glide.with(this)
                .load(arg.getString("imageUrl"))
                .dontAnimate()
                .fitCenter()
                .dontTransform()
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(it)
        }
        getCompleteCoordinatesToBeUsed()

//        Glide.with(this)
//            .asBitmap()
//            .load(arg.getString("imageUrl"))
//            .placeholder(R.drawable.icon_placeholder)
//            .error(R.drawable.icon_placeholder)
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                ) {
//                    val canvas = Canvas(resource)
//                    photoView.source?.draw(canvas)
//                    photoView.source?.setImageBitmap(resource)
//                    getCompleteCoordinatesToBeUsed(resource, canvas)
//
//                }
//            })

    }

    private fun getCompleteCoordinatesToBeUsed() {


        //Convert the px values

        val xN = arg.getParcelableArrayList<Coordinates>("coordinate")!!
            .elementAt(0).x

        val yN = arg.getParcelableArrayList<Coordinates>("coordinate")!!
            .elementAt(0).y

        val xB = arg.getParcelableArrayList<Coordinates>("coordinate")!!
            .elementAt(1).x
        val yB =
            arg.getParcelableArrayList<Coordinates>("coordinate")!!
                .elementAt(1).y


        val color = arg.getStringArrayList("textColorCode")!!.elementAt(0)
        val size = ConversionUtil.pxToSp(arg.getIntegerArrayList("textSize")!!.elementAt(0))
        val colorInt = Color.parseColor(color)
        Log.e(
            "Values",
            xN.toString() + " " + yN.toString() + " " + xB.toString() + " " + yB.toString() + " " + size.toString()
        )

        observeTextChange(arg, xN, yN, colorInt, size, xB, yB)
    }


    private fun observeTextChange(
        arg: Bundle,
        xN: Int,
        yN: Int,
        colorInt: Int,
        size: Float,
        xB: Int,
        yB: Int
    ) {


        //Sample two
        val photoEditorClass = Photo.Builder(this, photoView)
            .setPinchTextScalable(false)
            .build()




        photoView.source!!.adjustViewBounds = true
        val ht = photoView.source?.height
        val wd = photoView.source?.width
        val lh = photoView.source?.layoutParams?.height
        val lw = photoView.source?.layoutParams?.width


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

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Create a demo view just before init

                photoEditorClass.clearAllViews()
                photoEditorClass.addText(
//                    type_face,
                    s.toString(),
                    colorInt,
                    size.toFloat(),
//                    paint_chosen,
//                    size_chosen,
                    xN,
                    yN,
                    xB,
                    yB
                )

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                photoEditorClass.clearAllViews()
                photoEditorClass.addText(
//                    type_face,
                    s.toString(),
                    colorInt,
                    size.toFloat(),
//                    paint_chosen,
//                    size_chosen,
                    xN,
                    yN,
                    xB,
                    yB
                )


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

    override fun getTagType(_tag: String) {
        TODO("Not yet implemented")
    }

}
