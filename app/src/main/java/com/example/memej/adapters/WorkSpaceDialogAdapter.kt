package com.example.memej.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.models.responses.workspaces.UserWorkspaces
import java.util.*

class WorkSpaceDialogAdapter(val itemClick: OnWorkSpaceChangedListener) :
    RecyclerView.Adapter<WorkSpaceDialogAdapter.MyViewHolder>() {


    var workSpacesList: List<UserWorkspaces.Workspace> = listOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val NAME = itemView.findViewById<TextView>(R.id.workspace_dialog_name)
        val card_view = itemView.findViewById<CardView>(R.id.workspace_dialog_image_view_cv)
        val initial = itemView.findViewById<TextView>(R.id.workspace_dialog_avatar_text)

        val backgroundLayout = itemView.findViewById<ConstraintLayout>(R.id.cl_back)
        //Create random colors


        private val preferencesUtils = PreferenceUtil

        private val currentSpace = preferencesUtils.getCurrentSpaceFromPreference()

        fun bindPost(_workspace: UserWorkspaces.Workspace, itemClick: OnWorkSpaceChangedListener) {
            with(_workspace) {


                val x = _workspace.name.take(1).toUpperCase(Locale.ROOT)
                initial.text = x

                //Set background color
                val rnd = Random()
                val currentColor =
                    Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                card_view.setCardBackgroundColor(currentColor)

                NAME.text = _workspace.name


                //Inflate Background if this is the current space
                if (_workspace == currentSpace) {
                    backgroundLayout.setBackgroundColor(itemView.context.getColor(R.color.spaceShader))
                }

                itemView.setOnClickListener {
                    itemClick.switchWorkspace(_workspace)
                }


            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dialog_workspace_list, parent, false)
        return WorkSpaceDialogAdapter.MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return workSpacesList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindPost(workSpacesList[position], itemClick)

    }

}


interface OnWorkSpaceChangedListener {
    fun switchWorkspace(_workspace: UserWorkspaces.Workspace)
}
