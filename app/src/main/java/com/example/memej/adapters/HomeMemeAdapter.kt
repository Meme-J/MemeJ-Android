package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsHomeMeme
import com.example.memej.entities.homeMeme
import com.google.android.material.imageview.ShapeableImageView

class HomeMemeAdapter(val itemClickListener: OnItemClickListenerHome) :
    PagedListAdapter<homeMeme, HomeMemeAdapter.MyViewHolder>(DiffUtilsHomeMeme()) {

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


        //val userRv = itemView.findViewById<RecyclerView>(R.id.rv_usersListPost)
        //Below may provoke error because of hidden view
        // val memeTags = itemView.findViewById<MaterialTextView>(R.id.post_tag)
        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.cv_editMemePostHome)
        //The add tag and send will be in the other hand request
        //Implement Child Recycler View for the user post (Horizontal)

        fun bindPost(_homeMeme: homeMeme, clickListener: OnItemClickListenerHome) {

            with(_homeMeme) {
                //memeTags.text = tag
                //Load image
                //This is where the glide is loaded

                Glide.with(itemView)
                    .load(img_url)
                    .into(memeImage)

                itemView.setOnClickListener {
                    clickListener.onItemClicked(_homeMeme)
                }
            }
        }


    }
}

interface OnItemClickListenerHome {
    fun onItemClicked(_homeMeme: homeMeme)
}


