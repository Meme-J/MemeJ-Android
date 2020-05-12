package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeGroup
import com.example.memej.entities.memeGroup
import com.google.android.material.imageview.ShapeableImageView

class MemeGroupAdapter(val itemClickListener: OnItemClickListener) :
    PagedListAdapter<memeGroup, MemeGroupAdapter.MyViewHolder>(DiffUtilsMemeGroup()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_meme_template, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        getItem(position)?.let { holder.bindPost(it, itemClickListener) }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Bind the fetched items with the view holding classes


        val memeGroupTag = itemView.findViewById<TextView>(R.id.memeTagAddMeme)
        val memeGroupImage = itemView.findViewById<ShapeableImageView>(R.id.cv_memeGroup)


        fun bindPost(_memeGroup: memeGroup, clickListener: OnItemClickListener) {

            with(_memeGroup) {
                memeGroupTag.text = tag
                //Load image
                //This is where the glide is loaded
                Glide.with(itemView)
                    .load(img_url)
                    .centerCrop()
                    .into(memeGroupImage)

                itemView.setOnClickListener {
                    clickListener.onItemClicked(_memeGroup)
                }
            }
        }


    }
}

interface OnItemClickListener {
    fun onItemClicked(_memeGroup: memeGroup)
}


