package com.example.memej.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import java.util.*

class UserAdapter(val itemClick: onUserClickType) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    var userType: List<String> = listOf()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val USERNAME = itemView.findViewById<TextView>(R.id.getUserName)
        val card_view = itemView.findViewById<CardView>(R.id.card_user_creator)

        //Create random colors

        fun bindPost(_user: String, itemClick: onUserClickType) {
            with(_user) {

                val x = _user.take(2).toUpperCase(Locale.ROOT)
                USERNAME.text = x

                //Set background color
                val rnd = Random()
                val currentColor =
                    Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                card_view.setCardBackgroundColor(currentColor)


                itemView.setOnClickListener {
                    itemClick.getUserType(_user)
                }


            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_user_creators, parent, false)
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