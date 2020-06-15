package com.example.memej.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.memej.R
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.RandomMemeAdapter
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.viewModels.ExploreViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeableMethod
import retrofit2.Call
import retrofit2.Response

class ExploreFragment : Fragment() {


    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var root: View
    private val viewModel: ExploreViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    private lateinit var adapter: RandomMemeAdapter
    private lateinit var layoutManager: CardStackLayoutManager
    lateinit var pb: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.explore_fragment, container, false)
        sessionManager =
            SessionManager(requireContext())
        adapter = RandomMemeAdapter()
        pb = root.findViewById(R.id.pb_explore)
        pb.visibility = View.VISIBLE

        //Card Layout Manager
        layoutManager = CardStackLayoutManager(requireContext()).apply {
            setSwipeableMethod(SwipeableMethod.Manual)
            setOverlayInterpolator(LinearInterpolator())
        }

        val sv = root.findViewById<CardStackView>(R.id.stack_view)
        sv.layoutManager = layoutManager
        sv.adapter = adapter
        sv.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

        getRandomMemes()
        pb.visibility = View.GONE




        return root
    }

    private fun getRandomMemes() {


        val service = RetrofitClient.makeCallsForMemes(requireContext())
        service.getRandom(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {
                    if (response.isSuccessful) {
                        adapter.setRandomPosts(response.body()!!.memes)
                        pb.visibility = View.GONE

                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE

                    }
                }
            })
    }


}
