package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeTemplate
import com.example.memej.entities.memeTemplate
import com.google.android.material.imageview.ShapeableImageView

//Wrt activty
class MemeTemplateAdapter(val itemClickListener: OnItemClickListenerMeme) :
    PagedListAdapter<memeTemplate, MemeTemplateAdapter.MyViewHolder>(DiffUtilsMemeTemplate()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_meme, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        getItem(position)?.let { holder.bindPost(it, itemClickListener) }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Bind the fetched items with the view holding classes

        //Holders
        val memeGroupImage = itemView.findViewById<ShapeableImageView>(R.id.cv_memeTemplate)


        fun bindPost(_memeTemplate: memeTemplate, clickListener: OnItemClickListenerMeme) {

            with(_memeTemplate) {
                //Load image
                //This is where the glide is loaded
                Glide.with(itemView)
                    .load(img_url)
                    .centerCrop()
                    .into(memeGroupImage)

                itemView.setOnClickListener {
                    clickListener.onItemClickedForTemplateForMeme(_memeTemplate)
                }
            }
        }


    }
}

interface OnItemClickListenerMeme {
    fun onItemClickedForTemplateForMeme(_memeTemplate: memeTemplate)
}


