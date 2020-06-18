package com.example.memej.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.responses.SearchResponse
import com.google.android.material.textview.MaterialTextView

class SearchAdapter(val itemClickListener: onClickSearch) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var searchItems: List<SearchResponse.Suggestion> = listOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sugH = itemView.findViewById<MaterialTextView>(R.id.tv_sug)

        fun bindPost(_sug: SearchResponse.Suggestion, itemClickListener: onClickSearch) {

            sugH.text = _sug.tag

            //Implement on Item Search
            itemView.setOnClickListener {
                itemClickListener.getSuggestion(_sug)
            }

        }

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

        //get the thing
        Log.e("Search Adapter", searchItems[position].tag + " " + position)
        holder.bindPost(searchItems[position], itemClickListener)
    }

    fun setAdapterSearch(suggestion: List<SearchResponse.Suggestion>) {
        this.searchItems = suggestion
        notifyDataSetChanged()
    }


}


interface onClickSearch {
    fun getSuggestion(_sug: SearchResponse.Suggestion)
}

