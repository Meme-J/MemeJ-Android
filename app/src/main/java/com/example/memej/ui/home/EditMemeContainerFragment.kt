package com.example.memej.ui.home


import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.lruCache
import androidx.core.graphics.withTranslation
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.Instances.ProjectResources
import com.example.memej.Instances.projectResources
import com.example.memej.MainActivity
import com.example.memej.R
import com.example.memej.Utils.SessionManager
import com.example.memej.adapters.*
import com.example.memej.databinding.EditMemeContainerFragmentBinding
import com.example.memej.entities.editMemeBody
import com.example.memej.entities.searchBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.SearchResponse
import com.example.memej.responses.editMemeApiResponse
import com.example.memej.responses.homeMememResponses.Coordinates
import com.example.memej.responses.homeMememResponses.HomeUsers
import com.example.memej.viewModels.EditMemeContainerViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditMemeContainerFragment : AppCompatActivity(), onUserClickType, onTagClickType {

    private val viewModel: EditMemeContainerViewModel by viewModels()
    private lateinit var root: EditMemeContainerFragmentBinding
    lateinit var arg: Bundle
    private lateinit var img: ShapeableImageView
    lateinit var edt: EditText
    private lateinit var paint_chosen: Paint
    private lateinit var type_face: Typeface
    val paths = ArrayList<Path>()
    val undonePaths = ArrayList<Path>()
    var sendButton: Boolean = false
    var saveCount: Int? = 0
    lateinit var sessionManager: SessionManager
    lateinit var pb: ProgressBar
    lateinit var adapterTagsAdded: TagEditAdapter
    lateinit var stringAdapter: ArrayAdapter<String>
    lateinit var mutableList: MutableList<String>
    lateinit var tagCheck: MaterialButton


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        root = DataBindingUtil.setContentView(this, R.layout.edit_meme_container_fragment)
        projectResources = ProjectResources(resources)

        type_face = resources.getFont(R.font.arial)

        arg = intent?.getBundleExtra("bundle")!!

        img = root.imagePostEdit
        edt = root.lineAddedEt
        tagCheck = root.tagEdit

        initializeEditFrame(arg)
        sessionManager = SessionManager(this)
        pb = root.pbEditFragment

        //Init
        adapterTagsAdded = TagEditAdapter()
        mutableList = mutableListOf()           //Empty

        //root.rel_layout.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.2f));

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


        //Tags list
        //Init the adapter
        stringAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)


        val tagsEt = root.autoCompleteTag

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
                    root.tagEdit.isEnabled =
                        true
                } else if (tagsEt.length() == 0) {

                    root.tagEdit.isEnabled =
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
        val btn = root.sendPostEdit
        btn.setOnClickListener {
            sendPost(edt.text.toString())
        }


    }

    private fun setInTagRv() {
        val rvTagEdits = root.rvInsertedTags
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
                        root.autoCompleteTag


                    stringAdapter =
                        ArrayAdapter(
                            this@EditMemeContainerFragment,
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
        val service = RetrofitClient.makeCallsForMemes(this)
        val inf = editMemeBody(arg.getString("id")!!, line, mutableList)

        service.editMeme(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
            .enqueue(object : Callback<editMemeApiResponse> {
                override fun onFailure(call: Call<editMemeApiResponse>, t: Throwable) {
                    Toast.makeText(
                        this@EditMemeContainerFragment,
                        t.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<editMemeApiResponse>,
                    response: Response<editMemeApiResponse>
                ) {
                    //Response will be good if the meme is created
                    if (response.body()!!.msg == "Meme Edited successfully") {
                        Toast.makeText(
                            this@EditMemeContainerFragment,
                            response.body()!!.msg,
                            Toast.LENGTH_LONG
                        ).show()
                        pb.visibility = View.GONE
                        //Go back to the main activity
                        val i = Intent(this@EditMemeContainerFragment, MainActivity::class.java)
                        startActivity(i)

                    } else {
                        Toast.makeText(
                            this@EditMemeContainerFragment,
                            response.body()!!.msg,
                            Toast.LENGTH_LONG
                        ).show()
                        pb.visibility = View.GONE

                    }
                }
            })

    }


    private fun initializeEditFrame(arg: Bundle) {

        //Layout Manager
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
        for (i in txt2!!) {
            tagsStr.add(i)
        }

        //Get the rv and adapter for the user and the tags already existing
        val rvTag = root.rvEditTag
        val tagAdapter = TagAdapter(this)
        tagAdapter.tagType = tagsStr
        rvTag.layoutManager = HorizontalLayout
        rvTag.adapter = tagAdapter


        //Populate the users in the same way
        val u = arg.getParcelableArrayList<HomeUsers>("users")
        val userStr = mutableListOf<String>()
        for (i in u!!) {
            userStr.add(i.username)
        }

        //SecondLayout
        val HorizontalUser = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val rvUser = root.rvEditUser
        val userAdater = UserAdapter(this)
        userAdater.userType = userStr
        rvUser.layoutManager = HorizontalUser
        rvUser.adapter = userAdater


        getImage()


    }

    private fun getImage() {
        Glide.with(img)
            .asBitmap()
            .load(arg.getString("imageUrl"))
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    //When we do not use the part
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val canvas = Canvas(resource)

                    img.draw(canvas)

                    img.setImageBitmap(resource)

                    getCompleteImage(resource, canvas, arg)

                }
            })

    }

    private fun getCompleteImage(
        bitmap: Bitmap?, canvas: Canvas, arg: Bundle
    ) {


        //Extract every placeholder, color, textSize
        //Use the num of placeholders in the  meme image to view those
        //Set paint and size

        //Create a path to track undo and changes
        val currentStage = arg.getInt("stage")


        for (i in 0..currentStage - 1) {

            //Use static layout to encounter the multiline issues
            //get the width as a parameter          //Keep it as a standard of 300.

//            val paint =
//                setPaint(
//                    arg.getStringArrayList("paint")!!.elementAt(i),
//                    arg.getIntegerArrayList("size")!!.elementAt(i)
//                )
            val paint = getTextPaint(
                arg.getStringArrayList("paint")!!.elementAt(i),
                arg.getIntegerArrayList("size")!!.elementAt(i)
            )

            val pl = arg.getStringArrayList("placeHolders")!![i]

            val x =
                arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!.elementAt(i).x
            val y =
                arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!.elementAt(i).y

            canvas.drawMultilineText(pl, paint, 300, x.toFloat(), y.toFloat())

            //canvas.drawText(pl, x.toFloat(), y.toFloat(), setPaint("#000000", size = s))
        }
        val xN =
            arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                .elementAt(currentStage).x
        val yN =
            arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")!!
                .elementAt(currentStage).y
        val currentPaint = getTextPaint(
            arg.getStringArrayList("paint")!!.elementAt(currentStage),
            arg.getIntegerArrayList("size")!!.elementAt(currentStage)
        )
        canvas.save()

        observeTextChange(bitmap, canvas, arg, xN, yN, currentPaint)
    }

    //For O Version (API 8)
    @RequiresApi(Build.VERSION_CODES.O)
    fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
        includePad: Boolean = true,
        ellipsizedWidth: Int = width,
        ellipsize: TextUtils.TruncateAt? = null,
        maxLines: Int = Int.MAX_VALUE,
        breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE,
        justificationMode: Int = Layout.JUSTIFICATION_MODE_NONE
    ) {

        val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
                "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize-" +
                "$maxLines-$breakStrategy-$hyphenationFrequency-$justificationMode"

        val staticLayout = StaticLayoutCache[cacheKey] ?: StaticLayout.Builder.obtain(
            text,
            start,
            end,
            textPaint,
            width
        )
            .setAlignment(alignment)
            .setTextDirection(textDir)
            .setLineSpacing(spacingAdd, spacingMult)
            .setIncludePad(includePad)
            .setEllipsizedWidth(ellipsizedWidth)
            .setEllipsize(ellipsize)
            .setMaxLines(maxLines)
            .setBreakStrategy(breakStrategy)
            .setHyphenationFrequency(hyphenationFrequency)
            .setJustificationMode(justificationMode)
            .build().apply { StaticLayoutCache[cacheKey] = this }

        staticLayout.draw(this, x, y)
    }


    //For M version (API 6)
    @RequiresApi(Build.VERSION_CODES.M)
    fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
        includePad: Boolean = true,
        ellipsizedWidth: Int = width,
        ellipsize: TextUtils.TruncateAt? = null,
        maxLines: Int = Int.MAX_VALUE,
        breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE
    ) {

        val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
                "$spacingMult-$spacingAdd-$includePad-$ellipsizedWidth-$ellipsize-" +
                "$maxLines-$breakStrategy-$hyphenationFrequency"

        val staticLayout = StaticLayoutCache[cacheKey] ?: StaticLayout.Builder.obtain(
            text,
            start,
            end,
            textPaint,
            width
        )
            .setAlignment(alignment)
            .setTextDirection(textDir)
            .setLineSpacing(spacingAdd, spacingMult)
            .setIncludePad(includePad)
            .setEllipsizedWidth(ellipsizedWidth)
            .setEllipsize(ellipsize)
            .setMaxLines(maxLines)
            .setBreakStrategy(breakStrategy)
            .setHyphenationFrequency(hyphenationFrequency)
            .build().apply { StaticLayoutCache[cacheKey] = this }

        staticLayout.draw(this, x, y)
    }

    //For rest verses

    fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
        breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE
    ) {


        val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-$textDir-" +
                "$spacingMult-$spacingAdd-$breakStrategy-$hyphenationFrequency"

        //Create a dynamic layout
//        val dynamicLayout = DynamicLayout.Builder
//            .obtain(text, textPaint, width)
//            .setAlignment(alignment)
//            .setTextDirection(textDir)
//            .setLineSpacing(spacingAdd, spacingMult)
//            .setBreakStrategy(breakStrategy)
//            .build()


        val staticLayout =
            StaticLayoutCache[cacheKey] ?: StaticLayout.Builder
                .obtain(text, start, end, textPaint, width)
                .setAlignment(alignment)
                .setTextDirection(textDir)
                .setLineSpacing(spacingAdd, spacingMult)
                .setBreakStrategy(breakStrategy)
                .build()
        staticLayout.draw(this, x, y)
    }

    //This is called after all the above is done
    private fun observeTextChange(
        bitmap: Bitmap?,
        canvas: Canvas,
        arg: Bundle,
        xN: Int,
        yN: Int,
        currentPaint: TextPaint
    ) {
        //        val newCanvas = CanvasEditorView(requireContext())
//        val layoutParams = RelativeLayout.LayoutParams(
//            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
//        )
//        layoutParams.setMargins(pxToDp(xN),pxToDp(yN),0,0)
//        newCanvas.layoutParams = layoutParams


        //Create an Edit Text

        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val path = Path()
                path.moveTo(
                    xN.toFloat(), yN.toFloat()
                )
                path.lineTo(xN.toFloat() + 400f, yN.toFloat())
                paths.add(path)

                //Invalidate the view

                //   canvas.drawTextOnPath(s.toString(),path,0f,10f,setPaint("#000000", currentSize))
                if (edt.text.isNotEmpty()) {
                    sendButton = true
                    root.sendPostEdit.isEnabled =
                        true
                } else if (edt.text.isEmpty()) {
                    sendButton = false
                    root.sendPostEdit.isEnabled =
                        false
                }

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

                img.invalidate()

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                //Draw a dynamicTextLayot

                canvas.drawDynamicLayout(
                    s.toString(),
                    currentPaint,
                    400,                    //Width
                    xN.toFloat(),
                    yN.toFloat()
                )
                //Invalidate
                img.invalidate()


            }
        })


    }


    fun Canvas.drawDynamicLayout(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
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
            Toast.makeText(
                this@EditMemeContainerFragment,
                "Version not supported",
                Toast.LENGTH_SHORT
            ).show()
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

    //Dynamic Provate Object
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

    private fun getColorWithAlpha(color: Int, ratio: Float): Int {
        var newColor = 0
        val alpha = Math.round(Color.alpha(color) * ratio).toInt()
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        newColor = Color.argb(alpha, r, g, b)
        return newColor
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
//        val inf = profileSearchBody(_user)
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


