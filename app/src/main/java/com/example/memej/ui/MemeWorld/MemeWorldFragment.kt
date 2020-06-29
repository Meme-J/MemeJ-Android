package com.example.memej.ui.MemeWorld

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
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
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.viewModels.MemeWorldViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog

class MemeWorldFragment : Fragment(), OnItemClickListenerMemeWorld {

    companion object {
        fun newInstance() = MemeWorldFragment()
    }

    private val memeWorldViewModel: MemeWorldViewModel by viewModels()
    private lateinit var rv: RecyclerView
    private lateinit var memeWorldAdapter: MemeWorldAdapter
    lateinit var root: View
    lateinit var itemAnimator: RecyclerView.ItemAnimator
    lateinit var comm: Communicator
    lateinit var pb: ProgressBar
    lateinit var dialog: ProgressDialog
    lateinit var swl: SwipeRefreshLayout

    private var memesPresent = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.meme_world_fragment, container, false)

        rv = root.findViewById(R.id.rv_memeWorld)
        itemAnimator = DefaultItemAnimator()
        memeWorldAdapter = MemeWorldAdapter(requireContext(), this)
        pb = root.findViewById(R.id.pb_meme_world)
        pb.visibility = View.VISIBLE



        swl = root.findViewById<SwipeRefreshLayout>(R.id.swl_meme_world)

        swl.setOnRefreshListener {
            observeList()
        }


        //Create a dialog
        dialog = ProgressDialog(context)
        dialog.setMessage("Loading memes...")
        dialog.show()



        if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            observeList()
        } else {
            checkConnection()
        }



        comm = activity as Communicator
        dialog.dismiss()




        return root
    }

    private fun checkConnection() {
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
                    observeList()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialogInterface, which ->
                    dialogInterface.dismiss()
                    pb.visibility = View.GONE

                }
                .build()
            mDialog.show()

        } else {

            observeList()

        }
    }


    private fun initList() {
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = memeWorldAdapter
        runLayoutAnimation(rv)
        memeWorldAdapter.notifyDataSetChanged()
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
        memeWorldViewModel.getPosts(pr = pb).observe(viewLifecycleOwner, Observer {
            memeWorldAdapter.submitList(it)
        })

        initList()
    }


    override fun onItemClicked(_homeMeme: Meme_World) {
        //Meme World Container
        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "likedBy" to _homeMeme.likedBy,
            "likes" to _homeMeme.likes,
            "placeholders" to _homeMeme.placeholders,
            "numPlaceholders" to _homeMeme.templateId.numPlaceholders,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "imageUrl" to _homeMeme.templateId.imageUrl,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name,
            "textSize" to _homeMeme.templateId.textSize,
            "textColor" to _homeMeme.templateId.textColorCode
        )

        val i = Intent(activity, CompletedMemeActivity::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)


    }


}
