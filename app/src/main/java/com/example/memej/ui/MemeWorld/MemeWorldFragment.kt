package com.example.memej.ui.MemeWorld

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.adapters.MemeWorldAdapter
import com.example.memej.adapters.OnItemClickListenerMemeWorld
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.viewModels.MemeWorldViewModel

class MemeWorldFragment : Fragment(), OnItemClickListenerMemeWorld {

    companion object {
        fun newInstance() = MemeWorldFragment()
    }

    private val memeWorldViewModel: MemeWorldViewModel by viewModels()
    private lateinit var rv: RecyclerView
    private lateinit var memeWorldAdapter: MemeWorldAdapter
    lateinit var root: View
    lateinit var comm: Communicator
    var tagRequired: String = ""
    lateinit var pb: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.meme_world_fragment, container, false)


        rv = root.findViewById(R.id.rv_memeWorld)
        memeWorldAdapter = MemeWorldAdapter(requireContext(), this)
        pb = root.findViewById(R.id.pb_meme_world)
        pb.visibility = View.VISIBLE

        observeList()
        initList()

        comm = activity as Communicator


        return root
    }


    private fun initList() {
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = memeWorldAdapter
        pb.visibility = View.GONE

    }

    private fun observeList() {
        memeWorldViewModel.getPosts(pr = pb).observe(viewLifecycleOwner, Observer {
            memeWorldAdapter.submitList(it)
        })
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

        comm.passDataToMemeWorld(bundle)

    }


}
