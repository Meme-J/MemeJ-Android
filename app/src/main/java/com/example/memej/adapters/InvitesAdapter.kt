package com.example.memej.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.body.RejectRequestBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.workspaces.RejectRequestResponse
import com.example.memej.responses.workspaces.UserRequestResponse
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response


class InvitesAdapter(val itemClick: OnItemClickListenerInvites, val ctx: Context) :
    RecyclerView.Adapter<InvitesAdapter.MyViewHolder>() {


    var lst: MutableList<UserRequestResponse.Request> = mutableListOf()
    val sessionManager =
        SessionManager(ctx)
    val workspaceService = RetrofitClient.callWorkspaces(ctx)

    class MyViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {


        val NAME = itemView.findViewById<TextView>(R.id.tv_workspace_name)
        val SENDER_NAME = itemView.findViewById<TextView>(R.id.tv_workspace_sender)


        val check = itemView.findViewById<ImageView>(R.id.iv_accept_request)
        val cross = itemView.findViewById<ImageView>(R.id.iv_reject_request)


        //Bind a single item
        fun bindPost(
            _listItem: UserRequestResponse.Request,
            itemClick: OnItemClickListenerInvites
        ) {
            with(_listItem) {


                NAME.text =
                    _listItem.name

                SENDER_NAME.text = _listItem.from


            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_my_workspaces, parent, false)
        return InvitesAdapter.MyViewHolder(view, ctx)

    }

    override fun getItemCount(): Int {
        return lst.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindPost(lst[position], itemClick)
        holder.cross.setOnClickListener {
            rejectRequests(lst[position], position, holder)
        }

    }

    private fun rejectRequests(
        request: UserRequestResponse.Request,
        position: Int,
        holder: MyViewHolder
    ) {
        val req = RejectRequestBody.Request(request.from, request.id, request.name)
        val body = RejectRequestBody(req)
        workspaceService.rejectRequests(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        ).enqueue(object : retrofit2.Callback<RejectRequestResponse> {
            override fun onFailure(call: Call<RejectRequestResponse>, t: Throwable) {
                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                val snack = Snackbar.make(holder.itemView, message, Snackbar.LENGTH_SHORT)
                snack.show()

            }

            override fun onResponse(
                call: Call<RejectRequestResponse>,
                response: Response<RejectRequestResponse>
            ) {
                //If the rejection is successful
                if (response.body()?.msg == "Request rejected successfully.") {

                    //Remove this temporarily
                    val snack =
                        Snackbar.make(
                            holder.itemView,
                            R.string.rejectSuccess,
                            Snackbar.LENGTH_SHORT
                        )
                    snack.show()

                    //Update UI
                    removeAt(position)


                } else {

                    val snack =
                        Snackbar.make(
                            holder.itemView,
                            response.body()?.msg.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                    snack.show()

                }


            }
        })


    }


    fun removeAt(position: Int) {
        lst.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lst.size)
    }


}


interface OnItemClickListenerInvites {

    fun onCrossClick(_listItem: UserRequestResponse.Request)
    fun onCheckClick(_listItem: UserRequestResponse.Request)

}
