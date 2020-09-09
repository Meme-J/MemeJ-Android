package com.example.memej.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.RandomListener
import com.example.memej.adapters.RandomMemeAdapter
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.viewModels.ExploreViewModel
import com.google.android.material.snackbar.Snackbar
import com.yuyakaido.android.cardstackview.CardStackLayoutManager


class ExploreFragment : Fragment(), RandomListener {


    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var root: View
    private val viewModel: ExploreViewModel by viewModels()
    lateinit var sessionManager: SessionManager

    private lateinit var adapter: RandomMemeAdapter
    private lateinit var layoutManager: CardStackLayoutManager
    lateinit var viewPager: ViewPager2
    lateinit var pb: ProgressBar

    lateinit var leftNav: ImageView
    lateinit var rightNav: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.explore_fragment, container, false)
        sessionManager =
            SessionManager(requireContext())
        adapter = RandomMemeAdapter(this)
        pb = root.findViewById(R.id.pb_layout)
        pb.visibility = View.VISIBLE


        //Initiate viewpager
        viewPager = root.findViewById(R.id.viewpager_explore)
        viewPager.adapter = adapter


        //To hide the plus button
        //The edit text will be according to
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
        )



        if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            getRandomMemes()
        } else {
            loadAnimations()
        }


        pb.visibility = View.GONE

        leftNav = root.findViewById(R.id.left_nav)
        rightNav = root.findViewById(R.id.right_nav)


        rightNav.setOnClickListener(View.OnClickListener {
            if (viewPager.currentItem < viewPager.right) {
                viewPager.setCurrentItem(
                    viewPager.currentItem + 1,
                    true
                )
                leftNav.visibility = View.VISIBLE
            }

            //When last item
            else {
                rightNav.visibility = View.GONE
            }


        })

        leftNav.setOnClickListener(View.OnClickListener {
            if (viewPager.currentItem > viewPager.left) {
                viewPager.setCurrentItem(
                    viewPager.currentItem - 1,
                    true
                )

                rightNav.visibility = View.VISIBLE

            } else {
                leftNav.visibility = View.GONE
            }
        })

        return root
    }


    private fun loadAnimations() {

        pb.visibility = View.GONE
        root.findViewById<LottieAnimationView>(R.id.anim_explore).visibility = View.VISIBLE
        leftNav.visibility = View.GONE
        rightNav.visibility = View.GONE

//        val snack = Snackbar.make(root, R.string.no_internet_str, Snackbar.LENGTH_INDEFINITE)
//        snack.setAction(R.string.retry, View.OnClickListener {
//            //When the retry is clicked
//            //When the retry is clicked
//            //Dismiss the snack
//            snack.dismiss()
//            getRandomMemes()
//
//        })
//
//        snack.show()

    }


    private fun getRandomMemes() {

        //Recheck for states
        pb.visibility = View.VISIBLE
        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            loadAnimations()
        }

        viewModel.randomFunction().observe(viewLifecycleOwner, Observer { mResponse ->
            val success = viewModel.successful.value
            if (success != null) {
                if (success) {
                    initiateAdapter(mResponse)
                } else {
                    createSnackbar(viewModel.message.value)
                }


            }
        })


    }

    private fun createSnackbar(value: String?) {

        Snackbar.make(root, value.toString(), Snackbar.LENGTH_SHORT).show()
        return
    }

    private fun initiateAdapter(mResponse: homeMemeApiResponse?) {

        if (mResponse != null) {

            val memes = mResponse.memes
            adapter.setRandomPosts(memes)
            adapter.notifyDataSetChanged()
            viewPager.adapter = adapter

        }

    }

    override fun initRandomMeme(_meme: Meme_Home) {

    }


}
