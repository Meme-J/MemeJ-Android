package com.example.memej.ui.memes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.dataSources.HomeMemeDataSource
import com.example.memej.responses.homeMememResponses.Meme_Home


class MemeByTag : Fragment(), OnItemClickListenerHome {

    lateinit var root: View
    lateinit var arg: Bundle
    lateinit var adapter: HomeMemeAdapter
    lateinit var rv: RecyclerView
    lateinit var comm: Communicator
    lateinit var pb: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_meme_by_tag, container, false)
        arg = this.requireArguments()

        pb = root
            .findViewById(R.id.pb_meme_tag)
        rv = root.findViewById(R.id.rv_meme_by_tag)
        adapter = HomeMemeAdapter(this)
        initializingList()
        comm = activity as Communicator


        return root
    }

    private fun initializingList() {

        //Create Config
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()

        //Use Live Data
        val liveData = initializedPagedListBuilder(config)?.build()

        //Populate the adapter

        liveData?.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer<PagedList<Meme_Home>> { pagedList ->

                adapter.submitList(pagedList)
            })

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = adapter
        //After this has been done, make the progress bar hidden
        pb.visibility = View.GONE

    }

    private fun initializedPagedListBuilder(config: PagedList.Config?): LivePagedListBuilder<String, Meme_Home>? {

        val dataSourceFactory = object : DataSource.Factory<String, Meme_Home>() {
            override fun create(): DataSource<String, Meme_Home> {
                Log.e("K", "in inialtialzed page list builder")

                return HomeMemeDataSource(requireContext())
            }
        }
        return config?.let { LivePagedListBuilder(dataSourceFactory, it) }
    }

    override fun onItemClicked(_homeMeme: Meme_Home) {
        //Leading to the edit Container
        val bundle = bundleOf(
            "id" to _homeMeme._id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "numPlaceHolders" to _homeMeme.numPlaceholders,
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

        comm.passDataFromHome(bundle)

    }


}
