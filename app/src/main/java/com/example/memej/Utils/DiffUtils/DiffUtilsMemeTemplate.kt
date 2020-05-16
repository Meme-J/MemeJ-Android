package com.example.memej.Utils.DiffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.memej.entities.memeTemplate

class DiffUtilsMemeTemplate : DiffUtil.ItemCallback<memeTemplate>() {
//    override fun areItemsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.memeGroupId == newItem.memeGroupId
//    }
//
//    override fun areContentsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
//        return oldItem.img_url == newItem.img_url
//                && oldItem.tag == newItem.tag
//    }

    override fun areItemsTheSame(oldItem: memeTemplate, newItem: memeTemplate): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: memeTemplate, newItem: memeTemplate): Boolean {
        return oldItem.img_url == newItem.img_url
                && oldItem.numPlaceholders == newItem.numPlaceholders
    }
}