package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Keep
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R


@Keep
class TagEditAdapter :
    RecyclerView.Adapter<TagEditAdapter.MyViewHolder>() {

    var tagAdded: MutableList<String> = mutableListOf()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val TAG = itemView.findViewById<TextView>(R.id.getTagNameNew)
        val cross = itemView.findViewById<ImageView>(R.id.getTagCross)
        val card = itemView.findViewById<CardView>(R.id.container_layout_tag)

        fun bindPost(_tag: String) {

            with(_tag) {
                TAG.text = _tag
                //OnClickListener for the tag cross

            }
        }


    }

    fun updateList(_tag: String, position: Int) {
        tagAdded.add(_tag)
        notifyItemInserted(position)        //Not needed
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        tagAdded.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tagAdded.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_tag, parent, false)
        return TagEditAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return tagAdded.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.cross.setOnClickListener {
            removeAt(position)
        }



        holder.bindPost(tagAdded[position])

    }

}

