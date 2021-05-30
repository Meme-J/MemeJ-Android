package com.example.memej.ui.explore

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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.ExploreMemeAdapter
import com.example.memej.adapters.RandomListener
import com.example.memej.models.responses.home.HomeMemeApiResponse
import com.example.memej.models.responses.home.Meme_Home
import com.example.memej.ui.home.EditMemeContainerFragment
import com.example.memej.viewModels.ExploreViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView


class ExploreFragment : Fragment(), RandomListener {


    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var root: View
    private val viewModel: ExploreViewModel by viewModels()
    lateinit var sessionManager: SessionManager

    private lateinit var adapter: ExploreMemeAdapter

    private lateinit var cardStackView: CardStackView
    private lateinit var layoutManager: CardStackLayoutManager


    private lateinit var pb: ProgressBar
    private lateinit var swl: SwipeRefreshLayout
    private lateinit var fabRefresh: FloatingActionButton

    private lateinit var rv: RecyclerView

    private val TAG = ExploreFragment::class.java.simpleName


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_explore, container, false)
        pb = root.findViewById(R.id.pb_explore)

        //cardStackView = root.findViewById(R.id.stack_view)
        rv = root.findViewById(R.id.rv_explore)

        swl = root.findViewById(R.id.swl_explore)
        fabRefresh = root.findViewById(R.id.fab_refresh_explore)


        sessionManager =
            SessionManager(requireContext())

        adapter = ExploreMemeAdapter(this)

        //Get the layout manager

        rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv.adapter = adapter

//        layoutManager = CardStackLayoutManager(requireContext()).apply {
//            setSwipeableMethod(SwipeableMethod.Manual)
//            setOverlayInterpolator(LinearInterpolator())
//        }
//
//
//        layoutManager.setStackFrom(StackFrom.None)
//        layoutManager.setDirections(Direction.HORIZONTAL)
//        layoutManager.setDirections(Direction.VERTICAL)
//        layoutManager.setSwipeThreshold(0.2f)
//        layoutManager.setCanScrollHorizontal(true)
//        layoutManager.setCanScrollVertical(true)
//
//        cardStackView.adapter = adapter
//
//        cardStackView.itemAnimator.apply {
//            if (this is DefaultItemAnimator) {
//
//                supportsChangeAnimations = false
//            }
//        }


        //Initialization
        swl.setOnRefreshListener {
            getExploreMemes()
            swl.isRefreshing = false
        }


        //Make call for random memes
        getExploreMemes()

        //For refresh listener
        fabRefresh.setOnClickListener {
            getExploreMemes()
        }

        return root
    }


    private fun getExploreMemes() {


        val mSnackbarBody = root.findViewById<SwipeRefreshLayout>(R.id.swl_explore)

        try {
            viewModel.randomFunction(mSnackbarBody, pb)
                .observe(viewLifecycleOwner, Observer { mResponse ->
                    val success = viewModel.successful.value
                    if (success != null) {
                        pb.visibility = View.GONE

                        if (success) {
                            initiateAdapter(mResponse)
                        } else {
                            createSnackbar(viewModel.message.value)
                        }


                    } else {
                        pb.visibility = View.VISIBLE
                    }
                })
        } catch (e: Exception) {
            ErrorStatesResponse.logExceptions(e, TAG)
            createSnackbar(resources.getString(R.string.unableToLoad))
        }


    }

    private fun createSnackbar(value: String?) {

        Snackbar.make(root, value.toString(), Snackbar.LENGTH_SHORT).show()
        return
    }

    private fun initiateAdapter(mResponse: HomeMemeApiResponse?) {

        if (mResponse != null) {

            //Show the retry button
            //   fabRefresh.visibility = View.VISIBLE
            val memes = mResponse.memes
            adapter.setRandomPosts(memes)
            adapter.notifyDataSetChanged()


        } else {
            createSnackbar(resources.getString(R.string.unableToLoad))
        }

    }

    override fun initRandomMeme(_meme: Meme_Home) {
        //If we click, trugger a function

        val bundle = bundleOf(
            "id" to _meme._id,
            "lastUpdated" to _meme.lastUpdated,
            "numPlaceholders" to _meme.numPlaceholders,
            "placeHolders" to _meme.placeholders,
            "stage" to _meme.stage,
            "tags" to _meme.tags,
            "users" to _meme.users,
            "paint" to _meme.templateId.textColorCode,
            "size" to _meme.templateId.textSize,
            "templateIdCoordinates" to _meme.templateId.coordinates,
            "image" to _meme.templateId.imageUrl,
            "imageUrl" to _meme.templateId.imageUrl,
            "imageTags" to _meme.templateId.tags,
            "imageName" to _meme.templateId.name

        )

        val i = Intent(activity, EditMemeContainerFragment::class.java)
        i.putExtra("bundle", bundle)
        startActivity(i)


    }


}
