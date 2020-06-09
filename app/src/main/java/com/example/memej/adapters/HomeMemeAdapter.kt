package com.example.memej.adapters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.Instances.LoadImage
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsHomeMeme
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class HomeMemeAdapter(val itemClickListener: OnItemClickListenerHome) :

    PagedListAdapter<Meme_Home, HomeMemeAdapter.MyViewHolder>(DiffUtilsHomeMeme()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_home_meme, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        getItem(position)?.let { holder.bindPost(it, itemClickListener) }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Bind the fetched items with the view holding classes


        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.cv_editMemePostHome)
        val memeTimeLU = itemView.findViewById<MaterialTextView>(R.id.home_meme_timestamp)
        val load = itemView.findViewById<ProgressBar>(R.id.loading_panel)

        fun bindPost(_homeMeme: Meme_Home, clickListener: OnItemClickListenerHome) {

            memeTimeLU.text = _homeMeme.lastUpdated.toString()

            Glide.with(itemView)
                .asBitmap()
                .load(_homeMeme.templateId.imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        //Drawable
                        load.visibility = View.GONE
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        //get the loading progress bar to false
                        load.visibility = View.GONE
                        val canvas = Canvas(resource)

                        memeImage.draw(canvas)

                        memeImage.setImageBitmap(resource)

                        //Get the Object Image
                        Log.e("ADPATER HOME", _homeMeme.stage.toString())
                        LoadImage().getOngoingImage(canvas, resource, _homeMeme)


                    }
                })


            itemView.setOnClickListener {
                clickListener.onItemClicked(_homeMeme)
            }
        }


    }
}

interface OnItemClickListenerHome {
    fun onItemClicked(_homeMeme: Meme_Home)
}


