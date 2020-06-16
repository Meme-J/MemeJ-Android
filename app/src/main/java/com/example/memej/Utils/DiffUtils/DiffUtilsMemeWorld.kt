package com.example.memej.Utils.DiffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.memej.responses.memeWorldResponses.Meme_World

class DiffUtilsMemeWorld : DiffUtil.ItemCallback<Meme_World>() {
//    override fun areItemsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.memeGroupId == newItem.memeGroupId
//    }
//
//    override fun areContentsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.img_url == newItem.img_url
//                && oldItem.tag == newItem.tag
//    }

    override fun areItemsTheSame(oldItem: Meme_World, newItem: Meme_World): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(
        oldItem: Meme_World,
        newItem: Meme_World
    ): Boolean {
        return oldItem == newItem

    }
}