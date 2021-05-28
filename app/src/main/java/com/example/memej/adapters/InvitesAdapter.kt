package com.example.memej.adapters

import android.app.Activity
import android.app.AlertDialog
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
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.body.workspaces.AcceptWorkspaceRequestBody
import com.example.memej.models.body.workspaces.RejectWorkspaceRequestBody
import com.example.memej.models.responses.workspaces.AcceptRequestsResponse
import com.example.memej.models.responses.workspaces.RejectRequestResponse
import com.example.memej.models.responses.workspaces.UserRequestResponse
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

        holder.check.setOnClickListener {
            acceptRequest(lst[position], position, holder)
        }

    }

    private fun acceptRequest(
        request: UserRequestResponse.Request,
        position: Int,
        holder: InvitesAdapter.MyViewHolder
    ) {
        val req = AcceptWorkspaceRequestBody.Request(request.from, request.id, request.name)
        val body = AcceptWorkspaceRequestBody(req)
        workspaceService.acceptRequests(
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            body = body
        ).enqueue(object : retrofit2.Callback<AcceptRequestsResponse> {
            override fun onFailure(call: Call<AcceptRequestsResponse>, t: Throwable) {
                val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                val snack = Snackbar.make(holder.itemView, message, Snackbar.LENGTH_SHORT)
                snack.show()

            }

            override fun onResponse(
                call: Call<AcceptRequestsResponse>,
                response: Response<AcceptRequestsResponse>
            ) {
                //If the rejection is successful
                if (response.body()?.msg == "Workspace joined successfully.") {

                    //Remove this temporarily
                    val snack =
                        Snackbar.make(
                            holder.itemView,
                            R.string.acceptSuccess,
                            Snackbar.LENGTH_SHORT
                        )
                    snack.show()

                    //Update UI

                    createDialog(position, request, holder)
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

    private fun createDialog(
        position: Int,
        request: UserRequestResponse.Request,
        holder: InvitesAdapter.MyViewHolder
    ) {

        //Called when the requests are accepted and we want to ask the user if he wants to switch to this space
        val dialog = AlertDialog.Builder(ctx as Activity)
        val name = request.name
        val WORKSPACE = "Do you want to switch to $name space?"
        dialog.setMessage(WORKSPACE)
        dialog.setNegativeButton(R.string.NO) { _, _ ->
            //Do nothing
        }
        dialog.setPositiveButton(R.string.SWITCH) { _, _ ->
            switchSpace(name, request.id)
        }

    }

    private fun switchSpace(name: String, id: String) {

    }

    private fun rejectRequests(
        request: UserRequestResponse.Request,
        position: Int,
        holder: MyViewHolder
    ) {
        val req = RejectWorkspaceRequestBody.Request(request.from, request.id, request.name)
        val body = RejectWorkspaceRequestBody(req)
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
