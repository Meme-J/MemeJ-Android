package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeWorld
import com.example.memej.responses.memeWorldResponses.Meme
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class MemeWorldAdapter(val itemClickListener: OnItemClickListenerMemeWorld) :
    PagedListAdapter<Meme, MemeWorldAdapter.MyViewHolder>(DiffUtilsMemeWorld()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_meme_world, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        getItem(position)?.let { holder.bindPost(it, itemClickListener) }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Bind the fetched items with the view holding classes


        //val userRv = itemView.findViewById<RecyclerView>(R.id.rv_usersListPost)
        //Below may provoke error because of hidden view
        // val memeTags = itemView.findViewById<MaterialTextView>(R.id.post_tag)
        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.memeWorld_image)
        val memeTime = itemView.findViewById<MaterialTextView>(R.id.memeWorldTimeStamp)

        //The add tag and send will be in the other hand request
        //Implement Child Recycler View for the user post (Horizontal)

        fun bindPost(_meme: Meme, clickListener: OnItemClickListenerMemeWorld) {

            with(_meme) {
                //memeTags.text = tag
                //Load image
                //This is where the glide is loaded
                //Check this loader
                memeTime.text = _meme.lastUpdated             //To get the tag
                Glide.with(itemView)
                    .load(_meme.imageUrl)
                    .into(memeImage)

                itemView.setOnClickListener {
                    clickListener.onItemClicked(_meme)
                }
            }
        }


    }
}

interface OnItemClickListenerMemeWorld {
    fun onItemClicked(_meme: Meme)
}


