package com.example.memej.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.Instances.StylePickAttributes
import com.example.memej.R
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.Photo
import com.google.android.material.textview.MaterialTextView
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlin.properties.Delegates

class RandomMemeAdapter(val clickListener: RandomListener) :
    RecyclerView.Adapter<RandomMemeAdapter.MyViewHolder>(),
    CardStackListener {

    private var random: List<Meme_Home>? = listOf()     //Empty List

    //Setting
    fun setRandomPosts(rando: List<Meme_Home>) {
        this.random = rando
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val photoView = itemView.findViewById<ImageEditorView>(R.id.photoViewRandom)
//        val rvTag = itemView.findViewById<RecyclerView>(R.id.rv_tag_random)
//        val rvUsers = itemView.findViewById<RecyclerView>(R.id.rv_user_random)


        //Styles ATTribut
        private var paint_chosen by Delegates.notNull<Int>()
        private lateinit var type_face: Typeface
        private var size_chosen by Delegates.notNull<Float>()

        var whichFont = 0
        var whichPaint = Color.BLACK
        var whichProgress = 20

        val color = itemView.findViewById<MaterialTextView>(R.id.choose_colorR)
        val font = itemView.findViewById<MaterialTextView>(R.id.choose_fontR)
        val size = itemView.findViewById<MaterialTextView>(R.id.choose_sizeR)
        val colorIndicator = itemView.findViewById<CardView>(R.id.colorIndicatorR)

        fun bindPost(_meme: Meme_Home, clickListener: RandomListener) {
            with(_meme) {


                Glide.with(itemView.context)
                    .load(_meme.templateId.imageUrl)
                    .dontAnimate()
                    .fitCenter()
                    .centerInside()
                    .dontTransform()
                    .error(R.drawable.icon_placeholder)
                    .into(photoView.source!!)



                getCompleteImage(_meme, photoView, itemView.context)


                //Set listeners on the Edittext and end number of button
                val tf: Typeface = Typeface.DEFAULT
                type_face = tf
                paint_chosen = Color.parseColor("#000000")      //Default color
                size_chosen = 20f


                color.setOnClickListener {
                    //Create a variable instance
                    paint_chosen = StylePickAttributes.choosePaint(itemView.context, whichPaint)
                    whichPaint = paint_chosen
                    colorIndicator.setCardBackgroundColor(whichPaint)
                }



                itemView.setOnClickListener {
                    clickListener.initRandomMeme(_meme)
                }

            }

        }

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
                    context = context!!, photoEditorView = photoView!!,
                    startX = x1, startY = y1, endX = x2, endY = y2
                ).build()
                mPhotBuilView.addOldText(pl, colorInt, size = size.toFloat())

            }


        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RandomMemeAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_explore, parent, false)
        return RandomMemeAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return random?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Get wrt holder class
        random?.get(position)?.let { holder.bindPost(it, clickListener) }
        holder.setIsRecyclable(false)
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {


    }

    override fun onCardSwiped(direction: Direction?) {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {
        //Nothing to be done
    }


}


interface RandomListener {
    fun initRandomMeme(_meme: Meme_Home)
}