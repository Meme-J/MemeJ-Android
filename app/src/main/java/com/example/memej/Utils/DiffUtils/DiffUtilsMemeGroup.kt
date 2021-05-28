package com.example.memej.Utils.DiffUtils


import androidx.recyclerview.widget.DiffUtil
import com.example.memej.models.responses.template.EmptyTemplateResponse

class DiffUtilsMemeGroup : DiffUtil.ItemCallback<EmptyTemplateResponse.Template>() {
    override fun areItemsTheSame(
        oldItem: EmptyTemplateResponse.Template,
        newItem: EmptyTemplateResponse.Template
    ): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(
        oldItem: EmptyTemplateResponse.Template,
        newItem: EmptyTemplateResponse.Template
    ): Boolean {
        return oldItem.imageUrl == newItem.imageUrl
                && oldItem._id == newItem._id
    }

}