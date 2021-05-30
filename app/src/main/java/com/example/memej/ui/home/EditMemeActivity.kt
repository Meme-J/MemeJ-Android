package com.example.memej.ui.home


import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.Path
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.*
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.body.edit.EditMemeBody
import com.example.memej.models.body.search.SearchBody
import com.example.memej.models.responses.edit.EditMemeApiResponse
import com.example.memej.models.responses.home.Coordinates
import com.example.memej.models.responses.home.HomeUsers
import com.example.memej.models.responses.search.SearchResponse
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.OnPhotoEditorListener
import com.example.memej.textProperties.lib.Photo
import com.example.memej.textProperties.lib.ViewType
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_edit_meme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class EditMemeActivity : AppCompatActivity(), onUserClickType, onTagClickType {

    //private val viewModel: EditMemeContainerViewModel by viewModels()


    private lateinit var arg: Bundle

    private lateinit var edt: EditText
    private lateinit var sendButton: MaterialButton

    //Global to be used
    private var paint_chosen by Delegates.notNull<Int>()
    private lateinit var type_face: Typeface
    private var size_chosen by Delegates.notNull<Float>()
    private lateinit var colorIndicator: CardView

    private var whichFont = 0
    private var whichPaint = Color.BLACK
    private var whichProgress = 20

    val paths = ArrayList<Path>()
    val undonePaths = ArrayList<Path>()
    var saveCount: Int? = 0


    private lateinit var sessionManager: SessionManager

    //Tags
    private lateinit var adapterTagsAdded: TagEditAdapter
    private lateinit var stringAdapter: ArrayAdapter<String>
    private lateinit var mutableList: MutableList<String>
    private lateinit var tagCheck: MaterialButton
    private lateinit var tagsEt: AutoCompleteTextView

    private lateinit var rvUser: RecyclerView
    private lateinit var rvTag: RecyclerView
    private lateinit var rvTagEdits: RecyclerView

    //Image
    private lateinit var photoVieGlobal: Photo
    private lateinit var photoView: ImageEditorView
    private lateinit var pb: ProgressBar


    var X1: Int = 0
    var Y1: Int = 0
    var X2: Int = 0
    var Y2: Int = 0


    private val TAG = EditMemeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_meme)

//          projectResources =
//            ProjectResources(resources)

        //Default Paint, TypeFace. Size
        val tf: Typeface = Typeface.DEFAULT
        type_face = tf
        paint_chosen = Color.parseColor("#000000")      //Default color
        size_chosen = 20f
        // colorIndicator = root.colorIndicator

        arg = intent?.getBundleExtra("bundle")!!

        this.apply {

            photoView = imageViewEditMeme
            edt = lineAddedEt
            tagCheck = tag_edit
            pb = loading_panel
            tagsEt = auto_completeTag
            sendButton = send_post_edit

            rvUser = rv_edit_user
            rvTag = rv_edit_tag
            rvTagEdits = rv_insertedTags

        }

        sessionManager =
            SessionManager(this)


        photoVieGlobal = Photo.Builder(this, photoView)
            .setPinchTextScalable(false)
            .build()

        initializeEditFrame(arg)


        //Init for tags
        adapterTagsAdded = TagEditAdapter()
        mutableList = mutableListOf()           //Empty list

        //root.rel_layout.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.2f));
//        val colors = root.chooseColor
//        val font = root.chooseFont
//        val size = root.chooseSize


        //request focus for edt
        edt.requestFocus()

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


        //Tags list
        //Init the adapter
        stringAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)



        val onItemClickTag =
            OnItemClickListener { adapterView, view, i, l ->
                mutableList.add(adapterView.getItemAtPosition(i).toString())
                setInTagRv()
                tagsEt.text = null
            }

        tagsEt.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable?) {
                //Activation of button
                if (tagsEt.length() != 0) {
                    tagCheck.isEnabled =
                        true
                } else if (tagsEt.length() == 0) {

                    tagCheck.isEnabled =
                        false
                }
                getTags(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                getTags(s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        tagsEt.onItemClickListener = onItemClickTag


        tagCheck.setOnClickListener {
            mutableList.add(tagsEt.text.toString())
            setInTagRv()
            tagsEt.text = null
        }

        //Send button
        sendButton.setOnClickListener {
            sendPost(edt.text.toString())
        }


    }

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

    //private fun chooseFont() {

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

//   private fun chooseColor() {
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
//
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
        val HorizontalLayoutInsertedTags: LinearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val adapterTagAdded = TagEditAdapter()
        Log.e("Edit", "Values in Mutable list" + mutableList.toString())
        adapterTagAdded.tagAdded = mutableList
        rvTagEdits.layoutManager = HorizontalLayoutInsertedTags
        rvTagEdits.adapter = adapterTagAdded


    }

    private fun getTags(s: String) {

        val service = RetrofitClient.makeCallsForMemes(this)
        val inf = SearchBody(s, "ongoing")
        service.getTags(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
            .enqueue(object : retrofit2.Callback<SearchResponse> {
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {


                    val str = mutableListOf<String>()
                    for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
                        str.add(y.tag)

                    }
                    Log.e("Edit", str.toString())
                    val actv =
                        tagsEt


                    stringAdapter =
                        ArrayAdapter(
                            this@EditMemeActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            str
                        )


                    actv.setAdapter(stringAdapter)
                }
            })
    }

    override fun onBackPressed(): Unit {
        finish()

    }

    private fun sendPost(line: String) {
        //Show the progress bar
        //pb.visibility = View.VISIBLE
        val service = RetrofitClient.makeCallsForMemes(this)
        val inf =
            EditMemeBody(
                arg.getString("id")!!,
                line,
                mutableList,
                arg.getInt("numPlaceholders")
            )

        //Create a profress dialog
        val dialog = ProgressDialog(this)
        dialog.setMessage("Editing this meme")
        dialog.show()

        service.editMeme(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
            .enqueue(object : Callback<EditMemeApiResponse> {
                override fun onFailure(call: Call<EditMemeApiResponse>, t: Throwable) {
                    Log.e("Edit", "In failure")
                    dialog.dismiss()
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    android.app.AlertDialog.Builder(this@EditMemeActivity)
                        .setTitle("Unable to edit")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .show()

                }

                override fun onResponse(
                    call: Call<EditMemeApiResponse>,
                    response: Response<EditMemeApiResponse>
                ) {
                    //Response will be good if the meme is created
                    if (response.body()!!.msg == "Meme Edited successfully") {
                        Log.e("Edit", "In resp okay")
                        dialog.dismiss()

                        onBackPressed()


                    } else {

                        Log.e("Edit", "In resp not")
                        dialog.dismiss()
                        android.app.AlertDialog.Builder(this@EditMemeActivity)
                            .setTitle("Unable to edit")
                            .setMessage(response.body()?.msg)
                            .setPositiveButton(android.R.string.ok) { _, _ -> }
                            .show()

                    }
                }
            })

    }


    private fun initializeEditFrame(arg: Bundle) {

        try {



            val HorizontalLayout: LinearLayoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            //Load image tags

            val txt = arg.getStringArrayList("tags")
            val txt2 = arg.getStringArrayList("imageTags")

            //This is a array basically
            //Create an array list  to call these tags

            //Check for these being blank in case of initialization/add meme
            val tagsStr = mutableListOf<String>()
            for (i in txt!!) {
                tagsStr.add(i)
            }

            //These are the tags for the original meme are sent only
            for (i in txt2!!) {
                tagsStr.add(i)
            }

            //Get the rv and adapter for the user and the tags already existing
            val tagAdapter = TagAdapter(this)
            tagAdapter.tagType = tagsStr
            rvTag.layoutManager = HorizontalLayout
            rvTag.adapter = tagAdapter


            //The name is not sent. Only username is sent
            //Populate the users in the same way
            val u = arg.getParcelableArrayList<HomeUsers>("users")
            val userStr = mutableListOf<String>()
            for (i in u!!) {
                userStr.add(i.username)
            }

            //SecondLayout
            val HorizontalUser = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            val userAdater = UserAdapter(this)
            userAdater.userType = userStr
            rvUser.layoutManager = HorizontalUser
            rvUser.adapter = userAdater

            //Set timestamp
//        root.timestampEdit.text = arg.getString("lastUpdated")
        } catch (e: Exception) {
            //Unable to initialize frame
            ErrorStatesResponse.logExceptions(e, TAG)
        }

        getImage()


    }

    private fun getImage() {


        photoView.source?.let {
            Glide.with(this)
                .load(arg.getString("imageUrl"))
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(it)
        }

        getCompleteImage()

    }

    private fun getCompleteImage(
//        bitmap: Bitmap?, canvas: Canvas
    ) {

        val currentStage = arg.getInt("stage")
        val c = 2 * currentStage - 1

        for (i in 0..c step 2) {


            val color = arg.getStringArrayList("paint")!!.elementAt(i / 2)
            val size = arg.getIntegerArrayList("size")!!.elementAt(i / 2)
            val colorInt = Color.parseColor(color)


            val pl = arg.getStringArrayList("placeHolders")!![i / 2]

            val x1 =
                arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!.elementAt(i).x
            val y1 =
                arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!.elementAt(i).y

            val x2 =
                arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                    .elementAt(i + 1).x
            val y2 =
                arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                    .elementAt(i + 1).y

            photoVieGlobal.addOldText(null, pl, colorInt, size.toFloat(), x1, y1, x2, y2)
        }

        val xN =
            arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                .elementAt(c + 1).x
        val yN =
            arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                .elementAt(c + 1).y
        val xB =
            arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                .elementAt(c + 2).x
        val yB =
            arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                .elementAt(c + 2).y


        val color = arg.getStringArrayList("paint")!!.elementAt(currentStage)
        val size = arg.getIntegerArrayList("size")!!.elementAt(currentStage)
        val colorInt = Color.parseColor(color)


        observeTextChange(arg, xN, yN, xB, yB, colorInt, size)

    }


    private fun observeTextChange(
        arg: Bundle,
        xN: Int,
        yN: Int,
        xB: Int,
        yB: Int,
        colorInt: Int,
        size: Int
    ) {

        //Sample two
        val photoEditorClass = Photo.Builder(this, photoView)
            .setPinchTextScalable(false)
            .build()

        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


                if (edt.text.isNotEmpty()) {
                    sendButton.isEnabled =
                        true
                } else if (edt.text.isEmpty()) {
                    sendButton.isEnabled =
                        false
                }


            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

//                photoEditorClass.editText(
//                    edt as View,
//                    s.toString(),
//                    colorInt,
//                    size.toFloat(),
//                    xN,
//                    yN,
//                    xB,
//                    yB
//                )

                photoEditorClass.clearAllViews()
                //Return nothing
                photoEditorClass.addText(
                    //     type_face,
                    s.toString(),
//                    paint_chosen,
                    colorInt,
                    size.toFloat(),
//                    size_chosen,
                    xN,
                    yN,
                    xB,
                    yB
                )

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {


//                photoEditorClass.editText(
//                    edt as View,
//                    s.toString(),
//                    colorInt,
//                    size.toFloat(),
//                    xN,
//                    yN,
//                    xB,
//                    yB
//                )

                photoEditorClass.clearAllViews()
                //Return nothing
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

            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

                edt.requestFocus()
            }

            override fun onRemoveViewListener(numberOfAddedViews: Int) {
                edt.requestFocus()
                edt.text = null
            }

            override fun onStartViewChangeListener(viewType: ViewType?) {
            }

            override fun onStopViewChangeListener(viewType: ViewType?) {
            }
        })

        //Get the layout parameters of the view
        //Finally
        // photoEditorClass.clearAllViews()
        //Return nothing
//        val arrayList =
//            photoEditorClass.addText(type_face, edt.text.toString(), paint_chosen, size_chosen)
//        Log.e("Array List", arrayList.toString())
//        //Extract coordinates
//        getCoordunatesOfCustomView(arrayList)

    }



    //Api not yet made
    override fun getUserType(_user: String) {

        //Use this as a query
        //This is what will happen when the tags and users are clicked
        //They will have a string value associated with them
        //Make a call to get the user
        //Activate the progress bar
//        pb.visibility = View.VISIBLE
//        val service = RetrofitClient.makeCallForProfileParameters(requireContext())
//        val inf = ProfileSearchBody(_user)
//        service.getProfileFromUsername(inf)
//            .enqueue(object : retrofit2.Callback<UserProfileResponse> {
//                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
//                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
//                        pb.visibility = View.GONE
//                    Log.e("K", t.message.toString())
//                }
//
//                override fun onResponse(
//                    call: Call<UserProfileResponse>,
//                    response: Response<UserProfileResponse>
//                ) {
//                    //Start an activity to the profile of this newly found user
//                      pb.visibility = View.GONE
//                    val i = Intent(context, UserProfile::class.java)
//                    val bundle = bundleOf(
//                        //This will have username, name, number of likes
//                    )
//                    i.putExtra("Response",bundle)
//                    startActivity(i)
//                }
//            })

    }

    override fun getTagType(_tag: String) {

    }


}

