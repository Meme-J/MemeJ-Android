package com.example.memej.Utils.DiffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.memej.responses.memeWorldResponses.Meme

class DiffUtilsMemeWorld : DiffUtil.ItemCallback<Meme>() {
//    override fun areItemsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.memeGroupId == newItem.memeGroupId
//    }
//
//    override fun areContentsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.img_url == newItem.img_url
//                && oldItem.tag == newItem.tag
//    }

    override fun areItemsTheSame(oldItem: Meme, newItem: Meme): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Meme,
        newItem: Meme
    ): Boolean {
        return oldItem.id == newItem.id
                && oldItem.stage == newItem.stage

    }
}