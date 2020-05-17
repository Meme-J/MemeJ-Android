package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.entities.avatars
import com.google.android.material.imageview.ShapeableImageView

class ChooseAvatarAdapter(val itemClickListener: OnItemClickListenerAvatar) :
    RecyclerView.Adapter<ChooseAvatarAdapter.MyVieHolder>() {

    //A reference
    var av: List<avatars> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVieHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cards_avatars, parent, false)
        return ChooseAvatarAdapter.MyVieHolder(view)
    }

    override fun getItemCount(): Int {
        //Return a random value (No object associated)
        return av.size

    }

    override fun onBindViewHolder(holder: MyVieHolder, position: Int) {

        holder.bindPost(av[position], itemClickListener)

    }

    //Inner Class
    class MyVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val avatarImage = itemView.findViewById<ShapeableImageView>(R.id.image_avatar)

        fun bindPost(_avatars: avatars, itemClickListener: OnItemClickListenerAvatar) {
            with(_avatars) {

                com.bumptech.glide.Glide.with(itemView)
                    .load(image_url)
                    .into(avatarImage)

                itemView.setOnClickListener {
                    itemClickListener.onItemClicked(_avatars)
                }
            }


        }
    }


    fun setItems(data: List<avatars>) {
        this.av = data
        notifyDataSetChanged()
    }
}


interface OnItemClickListenerAvatar {

    fun onItemClicked(_avatars: avatars)
}