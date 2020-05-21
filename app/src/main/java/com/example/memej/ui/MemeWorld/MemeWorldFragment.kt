package com.example.memej.ui.MemeWorld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.responses.memeWorldResponses.Meme

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

        //Initialize the adapter
        // memeWorldAdapter = MemeWorldAdapter(this)


        //  observeLiveData()

        //initializeList()
        //comm = activity as Communicator





        return root
    }

//    private fun observeLiveData() {
//        //observe live data emitted by view model
//        Log.e("K", "In OLD")
//
//        memeWorldViewModel.getPosts().observe(viewLifecycleOwner, Observer {
//            memeWorldAdapter.submitList(it)
//        })
//    }

    private fun initializeList() {
        Log.e("K", "In IL")

        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = memeWorldAdapter
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        memeWorldViewModel = ViewModelProviders.of(this).get(MemeWorldViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClicked(_memeWorld: Meme) {
        //We have _memeWorld instance.
        //Take this to that another activity to lik/share/do everything
        //Access Meme World Id
//        val b = bundleOf("lastMemeId" to _memeWorld.lastMemeId,
//                                "memes"             to _memeWorld.memes
//                                )
        val i = Intent(activity, CompletedMemeActivity::class.java)
        // i.putExtra("lastMemeId", _memeWorld.lastMemeId)
        i.putExtra("image_id", _memeWorld.id)
        startActivity(i)


    }

}
