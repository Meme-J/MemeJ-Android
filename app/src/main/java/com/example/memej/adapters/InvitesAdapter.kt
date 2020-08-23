package com.example.memej.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.responses.workspaces.UserWorkspaces

class InvitesAdapter(val itemClick: OnItemClickListenerInvites) :
    RecyclerView.Adapter<InvitesAdapter.MyViewHolder>() {


    //Initialize an empty list of the dataclass T
    var lst: List<UserWorkspaces.Workspace> = listOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val NAME = itemView.findViewById<TextView>(R.id.tv_my_spaces_space_name)

        //Bind a single item
        fun bindPost(_listItem: UserWorkspaces.Workspace, itemClick: OnItemClickListenerInvites) {
            with(_listItem) {


                NAME.text =
                    _listItem.name

                itemView.setOnClickListener {
                    itemClick.clickThisItem(_listItem)
                }


            }
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
    fun clickThisItem(_listItem: UserWorkspaces.Workspace)
}
