package com.example.memej.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.dataSources.LikedMemesDataSource
import com.example.memej.responses.memeWorldResponses.Meme_World

class LikedMemes : Fragment(), OnItemClickListenerMemeWorld {

    companion object {
        fun newInstance() = LikedMemes()
    }

    private lateinit var viewModel: LikedMemesViewModel
    private lateinit var rv: RecyclerView
    private lateinit var memeWorldAdapter: MemeWorldAdapter
    lateinit var root: View
    lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.liked_memes_fragment, container, false)
        //All will be like meme world


        rv = root.findViewById(R.id.rv_likedMemes)
        memeWorldAdapter = MemeWorldAdapter(requireContext(), this)

        initializingList()


        val swl = root
            .findViewById<SwipeRefreshLayout>(R.id.swl_liked_memes)
        swl.setOnRefreshListener {

            initializingList()

            swl.isRefreshing = false
        }

        comm = activity as Communicator




        return root
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

                return LikedMemesDataSource(requireContext())
            }
        }
        return config?.let { LivePagedListBuilder(dataSourceFactory, it) }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LikedMemesViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClicked(_meme: Meme_World) {
        //Check who has liked
        Log.e("Liked Memes", _meme.templateId.name.toString())
//        Log.e("Liked Memes", _meme.likedBy.toString())

    }

}
