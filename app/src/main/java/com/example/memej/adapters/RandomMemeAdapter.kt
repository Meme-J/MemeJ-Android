package com.example.memej.adapters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.Instances.LoadImage
import com.example.memej.R
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class RandomMemeAdapter : RecyclerView.Adapter<RandomMemeAdapter.MyViewHolder>(),
    CardStackListener {

    private var random: List<Meme_Home>? = null

    //Setting
    fun setRandomPosts(rando: List<Meme_Home>) {
        this.random = rando
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Initialize all that have been left out lol
        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.imagePostExp)
        val memeTimeLU = itemView.findViewById<MaterialTextView>(R.id.timestamp_exp)


        fun bindPost(_meme: Meme_Home) {
            with(_meme) {

                memeTimeLU.text = lastUpdated

                Glide.with(itemView)
                    .asBitmap()
                    .load(templateId.imageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val canvas = Canvas(resource)

                            memeImage.draw(canvas)

                            memeImage.setImageBitmap(resource)

                            //Get the Object Image
                            LoadImage().getOngoingImage(canvas, resource, _meme)

                        }
                    })
            }
            //In this case, It will be able to adapt on this layout only

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RandomMemeAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_meme_post, parent, false)
        return RandomMemeAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return random?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Get wrt holder class
        random?.get(position)?.let { holder.bindPost(it) }
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        if (direction == Direction.Top || direction == Direction.Bottom) {
            Log.e("Random", "In Up/down")
        }
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