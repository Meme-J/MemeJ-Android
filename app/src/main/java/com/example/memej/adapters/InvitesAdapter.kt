package com.example.memej.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.responses.workspaces.UserRequestResponse


class InvitesAdapter(val itemClick: OnItemClickListenerInvites) :
    RecyclerView.Adapter<InvitesAdapter.MyViewHolder>() {


    var lst: List<UserRequestResponse.Request> = listOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val NAME = itemView.findViewById<TextView>(R.id.tv_workspace_name)
        val SENDER_NAME = itemView.findViewById<TextView>(R.id.tv_workspace_sender)

        val check = itemView.findViewById<ImageView>(R.id.iv_accept_request)
        val cross = itemView.findViewById<ImageView>(R.id.iv_reject_request)


        //Class functions


        //Bind a single item
        fun bindPost(
            _listItem: UserRequestResponse.Request,
            itemClick: OnItemClickListenerInvites
        ) {
            with(_listItem) {


                NAME.text =
                    _listItem.name

                SENDER_NAME.text = _listItem.from

                check.setOnClickListener {
                    acceptRequest(itemView.context)
                }

                cross.setOnClickListener {
                    rejectRequest(itemView.context)
                }

                itemView.setOnClickListener {
                    itemClick.clickThisItem(_listItem)
                }


            }
        }

        private fun rejectRequest(context: Context?) {

        }

        private fun acceptRequest(context: Context?) {

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_my_workspaces, parent, false)
        return InvitesAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return lst.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindPost(lst[position], itemClick)

    }

}


interface OnItemClickListenerInvites {
    fun clickThisItem(_listItem: UserRequestResponse.Request)
}
