package com.example.memej.ui.home

//import com.example.memej.adapters.OnItemClickListenerHome
//import com.example.memej.database.HomeMemeDataBase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.dataSources.HomeMemeDataSource
import com.example.memej.responses.homeMememResponses.Meme_Home
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), OnItemClickListenerHome {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rv: RecyclerView
    private lateinit var homeMemeAdapter: HomeMemeAdapter
    lateinit var root: View
    lateinit var comm: Communicator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        homeViewModel =
//            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)

        Log.e("HomeFrag", "Initialzing the rv, adapter")
        rv = root.findViewById(R.id.rv_home)
        homeMemeAdapter = HomeMemeAdapter(this)

        Log.e("HomeFrag", homeMemeAdapter.itemCount.toString())

        //This is the method originally
        initializingList()

        root.findViewById<SwipeRefreshLayout>(R.id.swl_home).setOnRefreshListener {
            initializingList()
            swl_home.isRefreshing = false
        }
        Log.e("HF", "Doing Good")

        comm = activity as Communicator


        return root
    }

    //
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
        liveData?.observe(viewLifecycleOwner, Observer<PagedList<Meme_Home>> { pagedList ->
            Log.e("HF", "In the observer")
            Log.e(
                "HF",
                "VAlue sth paged list is given by {$pagedList}" + pagedList.dataSource + " " + pagedList.config + " " + pagedList.isDetached
            )

            homeMemeAdapter.submitList(pagedList)
        })

        Log.e("HF", "Initialzed rv, adapter")
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = homeMemeAdapter
        Log.e("HF", "Is somewhere")


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
        Log.e("HF", "Item Clicked")

//        Toast.makeText(context, _homeMeme.users.toString(), Toast.LENGTH_LONG).show()
        val bundle = bundleOf(
            "id" to _homeMeme.id,
            "lastUpdated" to _homeMeme.lastUpdated,
            "numPlaceHolders" to _homeMeme.numPlaceholders,
            "placeHolders" to _homeMeme.placeholders,
            "stage" to _homeMeme.stage,
            "tags" to _homeMeme.tags,
            "users" to _homeMeme.users,
            "templateIdCoordinates" to _homeMeme.templateId.coordinates,
            "image" to _homeMeme.templateId.imageUrl,
            "imageUrl" to _homeMeme.templateId.id,
            "imageTags" to _homeMeme.templateId.tags,
            "imageName" to _homeMeme.templateId.name

        )
        Log.e("HF", "" + _homeMeme.lastUpdated.toString())

        comm.passDataFromHome(bundle)

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

