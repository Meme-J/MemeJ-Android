package com.example.memej.Utils.DiffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.memej.responses.memeWorldResponses.memeWorldResponse

class DiffUtilsMemeWorld : DiffUtil.ItemCallback<memeWorldResponse>() {
//    override fun areItemsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.memeGroupId == newItem.memeGroupId
//    }
//
//    override fun areContentsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.img_url == newItem.img_url
//                && oldItem.tag == newItem.tag
//    }

    override fun areItemsTheSame(oldItem: memeWorldResponse, newItem: memeWorldResponse): Boolean {
        return oldItem.memes == newItem.memes
    }

    override fun areContentsTheSame(
        oldItem: memeWorldResponse,
        newItem: memeWorldResponse
    ): Boolean {
        return oldItem.memes == newItem.memes

    }
}