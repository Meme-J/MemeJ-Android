package com.example.memej.ui.home

//import com.example.memej.adapters.OnItemClickListenerHome
//import com.example.memej.database.HomeMemeDataBase
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.viewModels.HomeViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog

class HomeFragment : Fragment(), OnItemClickListenerHome {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var rv: RecyclerView
    private lateinit var homeMemeAdapter: HomeMemeAdapter
    lateinit var root: View
    lateinit var comm: Communicator
    lateinit var pb: ProgressBar
    lateinit var itemAnimator: RecyclerView.ItemAnimator
    lateinit var dialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false)
        pb = root.findViewById(R.id.pb_home)
        rv = root.findViewById(R.id.rv_home)
        itemAnimator = DefaultItemAnimator()
        homeMemeAdapter = HomeMemeAdapter(this)
        pb.visibility = View.VISIBLE

        //Create a dialog
        dialog = ProgressDialog(activity)
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
        rv.adapter = homeMemeAdapter
        homeMemeAdapter.notifyDataSetChanged()
        pb.visibility = View.GONE
        dialog.dismiss()
    }

    private fun observeList() {

        //Create a material dialog for network states

        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            dialog.dismiss()
            checkConnection()
        }


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

//With Room
//    private fun initializedPagedListBuilder(config: PagedList.Config):
//            LivePagedListBuilder<Int, Meme_Home> {
//
//        val database = HomeMemeDataBase.create(requireContext())
//        val livePageListBuilder = LivePagedListBuilder<Int, Meme_Home>(
//            database.postDao().posts(),
//            config)
//        //Attach a boundary callnack
//       // livePageListBuilder.setBoundaryCallback(HomeMemeBoundaryCallback(database))
//        return livePageListBuilder
//    }




