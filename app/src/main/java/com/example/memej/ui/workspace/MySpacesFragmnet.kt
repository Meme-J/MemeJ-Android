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
        b.swlMySpaces.isRefreshing = true

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //Create Workspace
        b.extendedFab.setOnClickListener {
            //Go to create workspace activity
            val i = Intent(this, CreateWorkspaceActivity::class.java)
            startActivity(i)
        }


        Log.e(TAG, "In the my spaces created fragment")
        getWorkspaces()
        //Set refreshing true
        b.swlMySpaces.isRefreshing = false

        b.swlMySpaces.setOnRefreshListener {
            getWorkspaces()
        }

    }

    private fun getWorkspaces() {

        Log.e(TAG, "In get workspaces function. Value of viewmodel is " + viewModel.toString())
        Log.e(
            TAG,
            "Viewmodel responses are " + viewModel.message + viewModel.successful.value.toString() + "\nResponses: " + viewModel.mySpacesResponse.toString()
        )


        viewModel.getMySpaces()

        //Use ViewModels
        viewModel.successful.observe(this, Observer { successful ->
            b.swlMySpaces.isRefreshing = false
            Log.e(TAG, "In call viewmodel")

            if (successful != null) {
                Log.e(TAG, "In sucessful not null")

                if (successful) {
                    Log.e(TAG, "In sucessful true")

                    if (viewModel.mySpacesResponse?.workspaces?.isEmpty()!!) {
                        //Create a tv, show that there are no workspaces
                        b.tvNoSpacesExists.apply {
                            visibility = View.VISIBLE
                        }
                        Log.e(TAG, "In empty lists")

                    } else {
                        viewModel.mySpacesResponse?.let { initiateAdapter(it) }
                        Log.e(TAG, "In sucessful true with responses")

                    }


                } else {
                    Log.e(TAG, "In sucessful false and message is ${viewModel.message}")

                    //When the response is false
                    createSnackBar(viewModel.message.value)
                }

            }

            Log.e(TAG, "In sucessful null")


        })

        Log.e(
            TAG,
            "Viewmodel responses later are " + viewModel.message + viewModel.successful.value.toString() + "\nResponses: " + viewModel.mySpacesResponse.toString()
        )
        Log.e(TAG, "In end of get workspace")


    }

    private fun initiateAdapter(response: UserWorkspaces) {
        val rv = b.rvMySpaces
        adapter = MySpacesAdapter(this)
        adapter.lst = response.workspaces
        val layoutManager = GridLayoutManager(this, 2)
        rv.layoutManager = layoutManager
        runLayoutAnimation(rv)
        rv.adapter = adapter
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
