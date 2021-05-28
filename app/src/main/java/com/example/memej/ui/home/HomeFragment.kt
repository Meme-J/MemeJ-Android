package com.example.memej.ui.home

//import com.example.memej.adapters.OnItemClickListenerHome
//import com.example.memej.database.HomeMemeDataBase
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.models.responses.home.Meme_Home
import com.example.memej.viewModels.HomeViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog

class HomeFragment : Fragment(), OnItemClickListenerHome {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var rv: RecyclerView
    private lateinit var homeMemeAdapter: HomeMemeAdapter
    lateinit var root: View
    lateinit var pb: ProgressBar
    lateinit var itemAnimator: RecyclerView.ItemAnimator
    lateinit var dialog: ProgressDialog
    lateinit var swl: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pb = root.findViewById(R.id.pb_home)
        rv = root.findViewById(R.id.rv_home)
        itemAnimator = DefaultItemAnimator()
        homeMemeAdapter = HomeMemeAdapter(this)
        pb.visibility = View.VISIBLE
        swl = root.findViewById(R.id.swl_home)


        swl.setOnRefreshListener {
            observeList()
        }

        //Create a dialog
        dialog =
            ProgressDialog.show(activity, null, "Loading...", true)

        if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            observeList()
        } else {
            checkConnection()
        }


        dialog.dismiss()

    }

    private fun checkConnection() {

        dialog.dismiss()
        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            //Create a dialog
            pb.visibility = View.GONE
            dialog.dismiss()
            val mDialog = MaterialDialog.Builder(requireContext() as Activity)
                .setTitle("Oops")
                .setMessage("No internet connection")
                .setCancelable(true)
                .setAnimation(R.raw.inter2)
                .setPositiveButton(
                    "Retry"
                ) { dialogInterface, which ->
                    dialogInterface.dismiss()
                    dialog.dismiss()
                    observeList()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialogInterface, which ->
                    dialogInterface.dismiss()
                    dialog.dismiss()
                    pb.visibility = View.GONE

                }
                .build()
            mDialog.show()

        } else {

            observeList()

        }


    }


    private fun initList() {

        Log.e("Home", "In init list")
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = homeMemeAdapter
        runLayoutAnimation(rv)
        homeMemeAdapter.notifyDataSetChanged()
        pb.visibility = View.GONE
        dialog.dismiss()
        swl.isRefreshing = false
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


    private fun observeList() {

        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            dialog.dismiss()
            checkConnection()
        }

        swl.isRefreshing = true
        pb.visibility = View.VISIBLE
        homeViewModel.getPosts(pb = pb).observe(
            viewLifecycleOwner, Observer
            {

                homeMemeAdapter.submitList(it)
            })

        initList()
    }


    override fun onItemClicked(_homeMeme: Meme_Home) {

        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "numPlaceholders" to _homeMeme.numPlaceholders,
            "placeHolders" to _homeMeme.placeholders,
            "stage" to _homeMeme.stage,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "paint" to _homeMeme.templateId.textColorCode,
            "size" to _homeMeme.templateId.textSize,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "image" to _homeMeme.templateId.imageUrl,
            "imageUrl" to _homeMeme.templateId.imageUrl,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name

        )

        val i = Intent(activity, EditMemeContainerFragment::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)
        //It will again go in the home bundle
    }


}




