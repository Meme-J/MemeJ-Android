package com.example.memej.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.models.responses.home.Meme_Home
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.Photo
import com.google.android.material.card.MaterialCardView
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.internal.CardStackSetting
import com.yuyakaido.android.cardstackview.internal.CardStackState

@Keep
class ExploreMemeAdapter(private val clickListener: RandomListener) :
    RecyclerView.Adapter<ExploreMemeAdapter.MyViewHolder>(),
    CardStackListener,
    onUserClickType,
    onTagClickType {

    private var random: List<Meme_Home>? = listOf()     //Empty List

    private var state = CardStackState()
    private var setting = CardStackSetting()
    var numberItemsLeft: Int = 0


    //Setting
    fun setRandomPosts(rando: List<Meme_Home>) {
        this.random = rando
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


//        val photoView: ImageEditorView = itemView.findViewById(R.id.photoViewExplore)
//
//        val rvTag = itemView.findViewById<RecyclerView>(R.id.rv_explore_tag)
//        val rvUsers = itemView.findViewById<RecyclerView>(R.id.rv_explore_user)
//
//
//        //Styles ATTribut
//        private var paint_chosen by Delegates.notNull<Int>()
//        private lateinit var type_face: Typeface
//        private var size_chosen by Delegates.notNull<Float>()
//
//        var whichFont = 0
//        var whichPaint = Color.BLACK
//        var whichProgress = 20
//
////        val color: MaterialTextView = itemView.findViewById(R.id.choose_colorR)
////        val font :MaterialTextView = itemView.findViewById(R.id.choose_fontR)
////        val size :MaterialTextView = itemView.findViewById(R.id.choose_sizeR)
////        val colorIndicator: CardView = itemView.findViewById(R.id.colorIndicatorR)
//
//        val mLayout: LinearLayout = itemView.findViewById(R.id.ll_explore_editable)
//        val edt: EditText = itemView.findViewById(R.id.lineAddedEtR)
//        val tagEdt: AutoCompleteTextView = itemView.findViewById(R.id.auto_completeTagR)
//        val rvTagsEdit: RecyclerView = itemView.findViewById(R.id.rv_insertedTagsR)
//        val sendPost: MaterialButton = itemView.findViewById(R.id.send_post_editR)
//        val addTag: MaterialButton = itemView.findViewById(R.id.tag_editR)
//
//        lateinit var sessionManager: SessionManager
//        lateinit var adapterTagsAdded: TagEditAdapter
//        lateinit var stringAdapter: ArrayAdapter<String>
//        lateinit var mutableList: MutableList<String>
//
//        lateinit var comm: Communicator


        /**
         * For the new layout
         */

        private var cardView = itemView.findViewById<MaterialCardView>(R.id.cvExploreFront)
        private var photoView = itemView.findViewById<ImageEditorView>(R.id.photoViewExploreFront)

        fun bindPost(_meme: Meme_Home, clickListener: RandomListener) {
            with(_meme) {


                Glide.with(itemView.context)
                    .load(_meme.templateId.imageUrl)
                    .dontAnimate()
                    .dontTransform()
                    .error(R.drawable.icon_placeholder)
                    .into(photoView.source!!)



                getCompleteImage(_meme, photoView, itemView.context)


//                //Init session manager
//                sessionManager = SessionManager(itemView.context)
//
//                //Set listeners on the Edittext and end number of button
//                val tf: Typeface = Typeface.DEFAULT
//                type_face = tf
//                paint_chosen = Color.parseColor("#000000")      //Default color
//                size_chosen = 20f
//
//
//                //Addition of tags
//                adapterTagsAdded = TagEditAdapter()
//                mutableList = mutableListOf()           //Empty list
//                stringAdapter =
//                    ArrayAdapter(itemView.context, android.R.layout.simple_dropdown_item_1line)
//
//                val onItemClickTag =
//                    AdapterView.OnItemClickListener { adapterView, view, i, l ->
//                        mutableList.add(adapterView.getItemAtPosition(i).toString())
//                        setInTagRv()
//                        tagEdt.text = null
//                    }
//
//                //For Tags
//                tagEdt.addTextChangedListener(object : TextWatcher {
//
//
//                    override fun afterTextChanged(s: Editable?) {
//                        //Activation of button
//                        if (tagEdt.length() != 0) {
//                            addTag.isEnabled =
//                                true
//                        } else if (tagEdt.length() == 0) {
//
//                            addTag.isEnabled =
//                                false
//                        }
//                    }
//
//                    override fun beforeTextChanged(
//                        s: CharSequence?,
//                        start: Int,
//                        count: Int,
//                        after: Int
//                    ) {
//                        getTags(s.toString())
//                    }
//
//                    override fun onTextChanged(
//                        s: CharSequence?,
//                        start: Int,
//                        before: Int,
//                        count: Int
//                    ) {
//                        getTags(s.toString())
//                    }
//                })
//
//                tagEdt.onItemClickListener = onItemClickTag
//
//                addTag.setOnClickListener {
//                    mutableList.add(tagEdt.text.toString())
//                    setInTagRv()
//                    tagEdt.text = null
//                }
//
//                //Init for meme tags, meme template tags, meme users
//                //Hide users rv
//                rvUsers.visibility = View.GONE
//                initFrame(_meme, itemView.context)
//
//                sendPost.setOnClickListener {
//
//                    val service = RetrofitClient.makeCallsForMemes(itemView.context)
//                    val inf =
//                        EditMemeBody(
//                            _meme._id,
//                            edt.text.toString(),
//                            mutableList,
//                            _meme.numPlaceholders
//                        )
//
//                    //Create a profress dialog
//                    val dialog = ProgressDialog(itemView.context)
//                    dialog.setMessage("Editing this meme")
//                    dialog.show()
//
//                    service.editMeme(
//                        accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
//                        info = inf
//                    )
//                        .enqueue(object : Callback<EditMemeApiResponse> {
//                            override fun onFailure(call: Call<EditMemeApiResponse>, t: Throwable) {
//                                Log.e("Edit", "In failure")
//                                dialog.dismiss()
//                                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
//                                android.app.AlertDialog.Builder(itemView.context)
//                                    .setTitle("Unable to edit")
//                                    .setMessage(message)
//                                    .setPositiveButton(android.R.string.ok) { _, _ -> }
//                                    .show()
//
//                            }
//
//                            override fun onResponse(
//                                call: Call<EditMemeApiResponse>,
//                                response: Response<EditMemeApiResponse>
//                            ) {
//                                //Response will be good if the meme is created
//                                if (response.body()!!.msg == "Meme Edited successfully") {
//                                    Log.e("Edit", "In resp okay")
//                                    dialog.dismiss()
//
//
//                                    //Go to home fragment
//                                    comm = itemView.context as Communicator
//                                    comm.goToHome()
//
//
//                                } else {
//
//                                    Log.e("Edit", "In resp not")
//                                    dialog.dismiss()
//                                    android.app.AlertDialog.Builder(itemView.context)
//                                        .setTitle("Unable to edit")
//                                        .setMessage(response.body()?.msg)
//                                        .setPositiveButton(android.R.string.ok) { _, _ -> }
//                                        .show()
//
//                                }
//                            }
//                        })
//
//
//                }
//
                itemView.setOnClickListener {
                    //Show the ll
                    //   mLayout.visibility = View.VISIBLE
                    clickListener.initRandomMeme(_meme)
                }
//
            }
//
        }
//
//        private fun initFrame(_meme: Meme_Home, context: Context) {
//
//            //Layout Manager
//            val HorizontalLayout: LinearLayoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//            //Load image tags
//
//            //Set all the tags of the meme
//            val txt = _meme.tags
//            val txt2 = _meme.templateId.tags
//
//            //Check for these being blank in case of initialization/add meme
//            val tagsStr = mutableListOf<String>()
//            for (i in txt) {
//                tagsStr.add(i)
//            }
//
//            //#In random memes, the tags of image are not sent
////            for (i in txt2) {
////                tagsStr.add(i)
////            }
//
//            //Get the rv and adapter for the user and the tags already existing
//            val tagAdapter = TagAdapter(itemClick = object : onTagClickType {
//                override fun getTagType(_tag: String) {
//
//                }
//            })
//            Log.e("Random", tagsStr.toString())
//
//            tagAdapter.tagType = tagsStr
//            rvTag.layoutManager = HorizontalLayout
//            rvTag.adapter = tagAdapter
//
//
//            //Reduce space from this
////            //Populate the users in the same way
////            val u = _meme.users
////            val userStr = mutableListOf<String>()
////            for (i in u!!) {
////                userStr.add(i.username)
////            }
////
////            //SecondLayout
////            val HorizontalUser = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
////            val userAdater = UserAdapter(itemClick = object : onUserClickType {
////                override fun getUserType(_user: String) {
////
////                }
////            })
////            userAdater.userType = userStr
////            rvUsers.layoutManager = HorizontalUser
////            rvUsers.adapter = userAdater
//
//        }
//
//        //Get tags
//        private fun getTags(s: String) {
//
//            val service = RetrofitClient.makeCallsForMemes(itemView.context)
//            val inf = SearchBody(s, "ongoing")
//            service.getTags(accessToken = "Bearer ${sessionManager.fetchAcessToken()}", info = inf)
//                .enqueue(object : retrofit2.Callback<SearchResponse> {
//                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
//
//                    }
//
//                    override fun onResponse(
//                        call: Call<SearchResponse>,
//                        response: Response<SearchResponse>
//                    ) {
//
//                        Log.e("Edit", "In tag search response")
//
//                        val str = mutableListOf<String>()
//                        for (y: SearchResponse.Suggestion in response.body()!!.suggestions) {
//                            str.add(y.tag)
//
//                        }
//                        Log.e("Edit", str.toString())
//                        val actv =
//                            tagEdt
//
//
//                        stringAdapter =
//                            ArrayAdapter(
//                                itemView.context,
//                                android.R.layout.simple_dropdown_item_1line,
//                                str
//                            )
//
//
//                        actv.setAdapter(stringAdapter)
//                    }
//                })
//        }
//
//        //Set tags in the rv
//        private fun setInTagRv() {
//
//            val HorizontalLayoutInsertedTags: LinearLayoutManager = LinearLayoutManager(
//                itemView.context,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//            val adapterTagAdded = TagEditAdapter()
//            adapterTagAdded.tagAdded = mutableList
//            rvTagsEdit.layoutManager = HorizontalLayoutInsertedTags
//            rvTagsEdit.adapter = adapterTagAdded
//
//
//        }
//

        private fun getCompleteImage(
            _homeMeme: Meme_Home,
            photoView: ImageEditorView?,
            context: Context?
        ) {

            val currentStage = _homeMeme.stage
            val c = 2 * currentStage - 1

            for (i in 0..c step 2) {


                val color = _homeMeme.templateId.textColorCode.elementAt(i / 2)
                val size = _homeMeme.templateId.textSize.elementAt(i / 2)
                val colorInt = Color.parseColor(color)


                val pl = _homeMeme.placeholders[i / 2]

                val x1 =
                    _homeMeme.templateId.coordinates.elementAt(i).x
                val y1 =
                    _homeMeme.templateId.coordinates.elementAt(i).y

                val x2 =
                    _homeMeme.templateId.coordinates.elementAt(i + 1).x
                val y2 =
                    _homeMeme.templateId.coordinates.elementAt(i + 1).y

                val mPhotBuilView = Photo.Builder(
                    context = context!!, photoEditorView = photoView!!
                ).setPinchTextScalable(false)
                    .build()

                mPhotBuilView.addOldText(
                    null,
                    pl,
                    colorInt,
                    size.toFloat(),
                    x1,
                    y1,
                    x2 = x2,
                    y2 = y2
                )

            }


            //Get the now coordinates
            val xN =
                _homeMeme.templateId.coordinates
                    .elementAt(c + 1).x
            val yN =
                _homeMeme.templateId.coordinates
                    .elementAt(c + 1).y
            val xB =
                _homeMeme.templateId.coordinates.elementAt(c + 2).x
            val yB =
                _homeMeme.templateId.coordinates.elementAt(c + 2).y


            val color = _homeMeme.templateId.textColorCode.elementAt(currentStage)
            val size = _homeMeme.templateId.textSize.elementAt(currentStage)
            val colorInt = Color.parseColor(color)

            // addText(colorInt, size.toFloat(), xN, yN, xB, yB)
        }

//
//        private fun addText(
//            colorInt: Int,
//            size: Float,
//            xN: Int,
//            yN: Int,
//            xB: Int,
//            yB: Int
//        ) {
//
//            //Edt text listemer
//            //Sample two
//            val photoEditorClass = Photo.Builder(itemView.context, photoView)
//                .setPinchTextScalable(false)
//                .build()
//
//            edt.addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//
//
//                    if (edt.text.isNotEmpty()) {
//                        sendPost.isEnabled =
//                            true
//                    } else if (edt.text.isEmpty()) {
//                        sendPost.isEnabled =
//                            false
//                    }
//
//
//                }
//
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                    photoEditorClass.clearAllViews()
//                    //Return nothing
//                    photoEditorClass.addText(
//                        //     type_face,
//                        s.toString(),
////                    paint_chosen,
//                        colorInt,
//                        size.toFloat(),
////                    size_chosen,
//                        xN,
//                        yN,
//                        xB,
//                        yB
//                    )
//
//                }
//
//                override fun onTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    before: Int,
//                    count: Int
//                ) {
//
//                    photoEditorClass.clearAllViews()
//                    //Return nothing
//                    photoEditorClass.addText(
////                    type_face,
//                        s.toString(),
//                        colorInt,
//                        size.toFloat(),
////                    paint_chosen,
////                    size_chosen,
//                        xN,
//                        yN,
//                        xB,
//                        yB
//                    )
//
//                }
//            })
//
//
//            photoEditorClass.setOnPhotoEditorListener(object : OnPhotoEditorListener {
//                override fun onEditTextChangeListener(
//                    rootView: View?,
//                    text: String?,
//                    colorCode: Int
//                ) {
//                    edt.requestFocus()
//                }
//
//                override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
//
//                    edt.requestFocus()
//                }
//
//                override fun onRemoveViewListener(numberOfAddedViews: Int) {
//                    edt.requestFocus()
//                    edt.text = null
//                }
//
//                override fun onStartViewChangeListener(viewType: ViewType?) {
//                }
//
//                override fun onStopViewChangeListener(viewType: ViewType?) {
//                }
//            })
//
//        }
//
//
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreMemeAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_explore_front, parent, false)
        return ExploreMemeAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return random?.size ?: 0
    }

    fun updateNumberLeft() {
        numberItemsLeft -= 1

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Get wrt holder class
        random?.get(position)?.let { holder.bindPost(it, clickListener) }
        holder.setIsRecyclable(false)

    }

    override fun onCardDisappeared(view: View?, position: Int) {
        setting.canScrollVertical = false

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

        //Settings to scroll non vertical
        setting.canScrollVertical = false
        //Returns swipe setting
        if (direction == Direction.Top || direction == Direction.Bottom) {
            state.status = CardStackState.Status.Idle
        }


    }

    override fun onCardSwiped(direction: Direction?) {
        setting.canScrollVertical = false
        updateNumberLeft()

    }

    override fun onCardCanceled() {
        setting.canScrollVertical = false

    }

    override fun onCardAppeared(view: View?, position: Int) {
        setting.canScrollVertical = false

    }

    override fun onCardRewound() {
        //Nothing to be done
    }

    //On click methods
    override fun getUserType(_user: String) {
    }

    override fun getTagType(_tag: String) {
    }


}


interface RandomListener {

    fun initRandomMeme(_meme: Meme_Home)


}