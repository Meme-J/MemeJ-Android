package com.example.memej.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.HomeMemeAdapter
import com.example.memej.adapters.OnItemClickListenerHome
import com.example.memej.entities.homeMeme

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
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)


        rv = root.findViewById(R.id.rv_home)

        //Initialize the adapter
        homeMemeAdapter = HomeMemeAdapter(this)
//        Log.e("K", "RV Initialized")
//        //Observe for changes
        observeLiveData()
        Log.e("K", "Returns from observe Live data")

        //Get data
        initializeList()
        Log.e("K", "Returns from Initialized list")

        //Ovveride methods for onClick
        //Initialze The communicator
        comm = activity as Communicator


        return root
    }

    private fun observeLiveData() {
        //observe live data emitted by view model
        Log.e("K", "In OLD")

        homeViewModel.getPosts().observe(viewLifecycleOwner, Observer {
            homeMemeAdapter.submitList(it)
        })
    }

    private fun initializeList() {
        Log.e("K", "In IL")

        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = homeMemeAdapter
    }

    override fun onItemClicked(_homeMeme: homeMeme) {
        //Get a lot of changes done
        //1) Create a text box with the required texts
        //Load the iamge into a dialog ( Something like that)
        //Rendering commands stored in DisplayList
        //Send it to the edit_meme_container_class
        //Create a bundle to send everything
        val b = bundleOf(
            "image_url" to _homeMeme.img_url,
            "memeGroupId" to _homeMeme.memeGroupId,
            "memeId" to _homeMeme.memeId,
            "timestamp" to _homeMeme.timestamp,
            "tag" to _homeMeme.tag,
            "memeCheckCount" to _homeMeme.memeCheckCount,
            "num_tb" to _homeMeme.num_tb,
            "x1" to _homeMeme.x1,
            "y1" to _homeMeme.y1,
            "x2" to _homeMeme.x2,
            "y2" to _homeMeme.y2,
            "c1" to _homeMeme.c1,
            "c2" to _homeMeme.c2
            //Implementation to get the profile as well
        )

        //Change the color of the back activity to some

//        root.findNavController()
//            .navigate(R.id.action_navigation_home_to_editMemeContainerFragment)

//        Pass this bundle
        comm.passDataFromHome(b)
    }


}
