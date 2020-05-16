package com.example.memej.Utils.DiffUtils


import androidx.recyclerview.widget.DiffUtil
import com.example.memej.entities.homeMeme

class DiffUtilsHomeMeme : DiffUtil.ItemCallback<homeMeme>() {

    //Separate Logic for the home memes. They will have same id
    //# We can do this, the user will not get same template (ID ) again
    override fun areItemsTheSame(oldItem: homeMeme, newItem: homeMeme): Boolean {
        return oldItem.memeId == newItem.memeId
    }

    override fun areContentsTheSame(oldItem: homeMeme, newItem: homeMeme): Boolean {
        return oldItem.memeId == newItem.memeId
                && oldItem.memeCheckCount == newItem.memeCheckCount
    }

}