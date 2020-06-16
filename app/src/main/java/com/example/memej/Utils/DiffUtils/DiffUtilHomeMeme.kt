package com.example.memej.Utils.DiffUtils


import androidx.recyclerview.widget.DiffUtil
import com.example.memej.responses.homeMememResponses.Meme_Home

class DiffUtilsHomeMeme : DiffUtil.ItemCallback<Meme_Home>() {

    //Separate Logic for the home memes. They will have same id
    //# We can do this, the user will not get same template (ID ) again
    override fun areItemsTheSame(oldItem: Meme_Home, newItem: Meme_Home): Boolean {
        return oldItem._id == newItem._id && oldItem.stage == newItem.stage
    }

    override fun areContentsTheSame(oldItem: Meme_Home, newItem: Meme_Home): Boolean {
        return oldItem == newItem
    }

}