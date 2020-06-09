package com.example.memej.ui.home

//import com.example.memej.adapters.OnItemClickListenerHome
//import com.example.memej.database.HomeMemeDataBase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.dataSources.HomeMemeDataSource
import com.example.memej.entities.queryBody
import com.example.memej.responses.homeMememResponses.Meme_Home

class HomeFragment : Fragment(), OnItemClickListenerHome {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rv: RecyclerView
    private lateinit var homeMemeAdapter: HomeMemeAdapter
    lateinit var root: View
    lateinit var comm: Communicator
    var tagToBeSearched: String = ""           //This will be null in case called from Home
    lateinit var pb: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        homeViewModel =
//            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        pb = root.findViewById(R.id.pb_home)
        rv = root.findViewById(R.id.rv_home)
        homeMemeAdapter = HomeMemeAdapter(this)


        //This is the method originally
        initializingList(tagToBeSearched)


        comm = activity as Communicator


        return root
    }


    private fun initializingList(tagToBeSearched: String) {

        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()


        //Use Live Data
        val liveData = initializedPagedListBuilder(config)?.build()

        //Populate the adapter

        liveData?.observe(viewLifecycleOwner, Observer<PagedList<Meme_Home>> { pagedList ->

            homeMemeAdapter.submitList(pagedList)
        })

        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = homeMemeAdapter


    }

    private fun initializedPagedListBuilder(config: PagedList.Config?): LivePagedListBuilder<String, Meme_Home>? {

        val dataSourceFactory = object : DataSource.Factory<String, Meme_Home>() {
            override fun create(): DataSource<String, Meme_Home> {

                //From Home, query is blank
                val inf = queryBody("")
                return HomeMemeDataSource(requireContext(), inf, pb = pb)
            }
        }
        return config?.let { LivePagedListBuilder(dataSourceFactory, it) }
    }

    override fun onItemClicked(_homeMeme: Meme_Home) {

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
        //It will again go in the home bundle
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


}

