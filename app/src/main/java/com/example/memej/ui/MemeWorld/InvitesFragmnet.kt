package com.example.memej.ui.MemeWorld

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.adapters.InvitesAdapter
import com.example.memej.adapters.OnItemClickListenerInvites
import com.example.memej.databinding.InvitesFragmnetFragmentBinding
import com.example.memej.responses.workspaces.UserRequestResponse
import com.example.memej.viewModels.InvitesFragmnetViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.invites_fragmnet_fragment.*

class InvitesFragmnet : AppCompatActivity(), OnItemClickListenerInvites {

    companion object {
        fun newInstance() = InvitesFragmnet()
    }

    private val viewModel: InvitesFragmnetViewModel by viewModels()

    lateinit var b: InvitesFragmnetFragmentBinding


    lateinit var adapter: InvitesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DataBindingUtil.setContentView(this, R.layout.invites_fragmnet_fragment)
        val toolbar = b.toolbarDrawerElement
        b.swlInvites.isRefreshing = true

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getInvites()

        b.swlInvites.isRefreshing = false

        b.swlInvites.setOnRefreshListener {
            getInvites()
        }

    }

    private fun getInvites() {

        val response = viewModel.loadInvites()
        val success = viewModel.successful.value
        val message = viewModel.message.value

        //Use success
        if (success != null) {
            if (response == null) {
                createSnackbar(message)
            } else {
                initAdapter(response)

            }
        }

        //Do nothing when success is null
    }

    private fun initAdapter(response: UserRequestResponse) {

        //Init rv
        val rv = b.rvInvites
        adapter = InvitesAdapter(this)
        adapter.lst = response.requests
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        runLayoutAnimation(rv)
        adapter.notifyDataSetChanged()

        b.swlInvites.isRefreshing = false
    }


    /**
     * Helper functions
     */
    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.layout_fall_down
        )
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun createSnackbar(message: String?) {
        Snackbar.make(container_invites, message.toString(), Snackbar.LENGTH_SHORT).show()
    }

    //Finish activity on back pressed
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun clickThisItem(_listItem: UserRequestResponse.Request) {
        //Do nothing in this
    }

}
