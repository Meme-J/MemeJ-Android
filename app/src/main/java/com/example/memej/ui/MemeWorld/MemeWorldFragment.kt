package com.example.memej.ui.MemeWorld

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.dataSources.MemeWorldDataSourcae
import com.example.memej.responses.memeWorldResponses.Meme_World

class MemeWorldFragment : Fragment(), OnItemClickListenerMemeWorld {

    companion object {
        fun newInstance() = MemeWorldFragment()
    }

    private lateinit var memeWorldViewModel: MemeWorldViewModel
    private lateinit var rv: RecyclerView
    private lateinit var memeWorldAdapter: MemeWorldAdapter
    lateinit var root: View
    lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.meme_world_fragment, container, false)


        rv = root.findViewById(R.id.rv_memeWorld)
        memeWorldAdapter = MemeWorldAdapter(requireContext(), this)

        initializingList()


//        val swl = root
//            .findViewById<SwipeRefreshLayout>(R.id.swl_meme_world)
//        swl.setOnRefreshListener {
//
//            initializingList()
//
//            swl.isRefreshing = false
//        }

        comm = activity as Communicator


        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        memeWorldViewModel = ViewModelProviders.of(this).get(MemeWorldViewModel::class.java)
        // TODO: Use the ViewModel
    }


    private fun initializingList() {

        Log.e("HF", "Initialzed config")
        //Create Config
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()


        Log.e("HF", "Initialzed Live Data")

        //Use Live Data
        val liveData = initializedPagedListBuilder(config)?.build()
        Log.e(
            "HF",
            "Retrunrs and gives live data" + liveData + " " + liveData?.value + " " + liveData.toString()
        )

        //Populate the adapter
        liveData?.observe(viewLifecycleOwner, Observer<PagedList<Meme_World>> { pagedList ->
            Log.e("HF", "In the observer")
            Log.e(
                "HF",
                "VAlue sth paged list is given by {$pagedList}" + pagedList.dataSource + " " + pagedList.config + " " + pagedList.isDetached
            )

            memeWorldAdapter.submitList(pagedList)
        })

        Log.e("HF", "Initialzed rv, adapter")
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = memeWorldAdapter
        Log.e("HF", "Is somewhere")


    }

    private fun initializedPagedListBuilder(config: PagedList.Config?): LivePagedListBuilder<String, Meme_World>? {

        val dataSourceFactory = object : DataSource.Factory<String, Meme_World>() {
            override fun create(): DataSource<String, Meme_World> {
                Log.e("K", "in inialtialzed page list builder")

                return MemeWorldDataSourcae(requireContext())
            }
        }
        return config?.let { LivePagedListBuilder(dataSourceFactory, it) }
    }

    override fun onItemClicked(_homeMeme: Meme_World) {
        //Meme World Container
        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "likedBy" to _homeMeme.likedBy,
            "likes" to _homeMeme.likes,
            "placeholders" to _homeMeme.placeholders,
            "meme" to _homeMeme.templateId.numPlaceholders,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "imageUrl" to _homeMeme.templateId.imageUrl,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name,
            "textSize" to _homeMeme.templateId.textSize,
            "textColor" to _homeMeme.templateId.textColorCode
        )
        Log.e("HF", "" + _homeMeme.lastUpdated.toString())

        Log.e("HF", "" + _homeMeme._id.toString())

        Log.e("HF", "" + _homeMeme.templateId._id.toString())
        Log.e("HF", "" + _homeMeme.templateId.imageUrl.toString())

        comm.passDataToMemeWorld(bundle)

    }


}
