package com.example.memej.ui.MemeWorld

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

    private lateinit var b: InvitesFragmnetFragmentBinding

    lateinit var adapter: InvitesAdapter
    private val TAG = InvitesFragmnet::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DataBindingUtil.setContentView(this, R.layout.invites_fragmnet_fragment)
        val toolbar = b.toolbarDrawerElement
        b.swlInvites.isRefreshing = true

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        getInvites()

        b.swlInvites.setOnRefreshListener {
            getInvites()
            b.swlInvites.isRefreshing = false

        }

        b.swlInvites.isRefreshing = false

    }


    private fun getInvites() {

        //Check for messages
        viewModel.loadFunction().observe(this, Observer { mResponse ->
            //Response is the invites response
            initAdapter(mResponse)
        })

    }


    private fun initAdapter(response: UserRequestResponse) {

        Log.e(TAG, "In init adapter")
        if (response.requests.isEmpty()) {
            b.tvEmptyInvites.apply {
                visibility = View.VISIBLE
            }
        } else {
            b.tvEmptyInvites.visibility = View.GONE
            val rv = b.rvInvites
            adapter = InvitesAdapter(this, this)
            adapter.lst = response.requests.toMutableList()
            val layoutManager = LinearLayoutManager(this)
            rv.layoutManager = layoutManager
            runLayoutAnimation(rv)
            adapter.notifyDataSetChanged()

        }

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


    override fun onCrossClick(
        _listItem: UserRequestResponse.Request
    ) {


    }


    override fun onCheckClick(
        _listItem: UserRequestResponse.Request
    ) {
    }

    //Remove observers


    /**
     * Add the delete and accept requests function
     */

}
