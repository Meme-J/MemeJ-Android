package com.example.memej.Utils.DiffUtils


import androidx.recyclerview.widget.DiffUtil
import com.example.memej.entities.memeGroup

class DiffUtilsMemeGroup : DiffUtil.ItemCallback<memeGroup>() {
    override fun areItemsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
        return oldItem.memeGroupId == newItem.memeGroupId
    }

    override fun areContentsTheSame(oldItem: memeGroup, newItem: memeGroup): Boolean {
        return oldItem.img_url == newItem.img_url
                && oldItem.tag == newItem.tag
    }

}