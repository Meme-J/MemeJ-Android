package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
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

        fun bindPost(_homeMeme: Meme_Home, clickListener: OnItemClickListenerHome) {

            with(_homeMeme) {

                memeTimeLU.text = _homeMeme.lastUpdated
                //Get the painting here
//                Glide.with(itemView)
//                    .load(_homeMeme.templateId.imageUrl)
//                    .into(memeImage)
                itemView.setOnClickListener {
                    clickListener.onItemClicked(_homeMeme)
                }
            }
        }


    }
}

interface OnItemClickListenerHome {
    fun onItemClicked(_homeMeme: Meme_Home)
}


