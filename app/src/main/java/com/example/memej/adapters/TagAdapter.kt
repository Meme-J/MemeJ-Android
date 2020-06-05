package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R

class TagAdapter(val itemClick: onTagClickType) :
    RecyclerView.Adapter<TagAdapter.MyViewHolder>() {

    var tagType: List<String> = listOf()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val TAG = itemView.findViewById<TextView>(R.id.getUserName)

        fun bindPost(_tag: String, itemClick: onTagClickType) {
            with(_tag) {
                TAG.text = _tag
                itemView.setOnClickListener {
                    itemClick.getTagType(_tag)
                }


            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        return TagAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return tagType.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindPost(tagType[position], itemClick)

    }

}

interface onTagClickType {
    fun getTagType(_tag: String)
}