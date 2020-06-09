package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R

class UserAdapter(val itemClick: onUserClickType) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    var userType: List<String> = listOf()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val USERNAME = itemView.findViewById<TextView>(R.id.getUserName)

        fun bindPost(_user: String, itemClick: onUserClickType) {
            with(_user) {
                USERNAME.text = _user
                itemView.setOnClickListener {
                    itemClick.getUserType(_user)
                }


            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        return UserAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userType.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindPost(userType[position], itemClick)

    }

}

interface onUserClickType {
    fun getUserType(_user: String)
}