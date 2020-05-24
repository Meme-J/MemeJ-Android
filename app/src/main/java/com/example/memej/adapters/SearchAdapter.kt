package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.responses.memeWorldResponses.SuggestionsResponse
import com.google.android.material.textview.MaterialTextView

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var searchItems: List<SuggestionsResponse> = listOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sugH = itemView.findViewById<MaterialTextView>(R.id.tv_sug)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_suggestion, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchItems.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sugH.text = searchItems[position].tag
    }


}