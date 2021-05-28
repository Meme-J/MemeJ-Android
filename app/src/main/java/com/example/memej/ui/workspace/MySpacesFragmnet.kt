package com.example.memej.ui.workspace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.adapters.MySpacesAdapter
import com.example.memej.adapters.OnItemClickListenerMySpaces
import com.example.memej.databinding.MySpacesFragmnetFragmentBinding
import com.example.memej.responses.workspaces.UserWorkspaces
import com.example.memej.viewModels.MySpacesFragmnetViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.my_spaces_fragmnet_fragment.*

class MySpacesFragmnet : AppCompatActivity(), OnItemClickListenerMySpaces {

    companion object {
        fun newInstance() = MySpacesFragmnet()
    }

    private val viewModel: MySpacesFragmnetViewModel by viewModels()
    lateinit var b: MySpacesFragmnetFragmentBinding


    lateinit var adapter: MySpacesAdapter
    private val TAG = MySpacesFragmnet::class.java.simpleName

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        b = DataBindingUtil.setContentView(this, R.layout.my_spaces_fragmnet_fragment)
        val toolbar = b.toolbarDrawerElement

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //Create Workspace
        b.extendedFab.setOnClickListener {
            //Go to create workspace activity
            val i = Intent(this, CreateWorkspaceActivity::class.java)
            startActivity(i)

        }

        getWorkspaces()

        b.swlMySpaces.setOnRefreshListener {
            getWorkspaces()
            b.swlMySpaces.isRefreshing = false
        }




    }

    private fun getWorkspaces() {

        //Load the data

        viewModel.mySpacesFuction().observe(this, Observer { mResponse ->
            initiateAdapter(mResponse)

        })


    }

    private fun initiateAdapter(response: UserWorkspaces) {

        Log.e(TAG, " In init adapter " + response.workspaces.toString())


        //Check the user response and its null parameters
        if (response.workspaces.isEmpty()) {
            b.tvNoSpacesExists.apply {
                visibility = View.VISIBLE
            }

        } else {

            b.tvNoSpacesExists.visibility = View.GONE
            val rv = b.rvMySpaces
            adapter = MySpacesAdapter(this)
            adapter.lst = response.workspaces
            val layoutManager = GridLayoutManager(this, 2)
            rv.layoutManager = layoutManager
            runLayoutAnimation(rv)
            rv.adapter = adapter
        }
        b.swlMySpaces.isRefreshing = false


    }

    private fun createSnackBar(message: String?) {
        Snackbar.make(container_my_spaces, message.toString(), Snackbar.LENGTH_SHORT).show()
    }


    //Finish Activity
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.layout_fall_down
        )
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun clickThisItem(_listItem: UserWorkspaces.Workspace) {
        //Send as intent bundle
        val bundle = bundleOf(
            "name" to _listItem.name,
            "id" to _listItem._id
        )

        val i = Intent(this, WorkSpaceActivity::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)
    }


}
