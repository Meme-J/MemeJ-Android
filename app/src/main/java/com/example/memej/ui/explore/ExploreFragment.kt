package com.example.memej.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.memej.R
import com.example.memej.Utils.SessionManager
import com.example.memej.adapters.RandomMemeAdapter
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
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
    private lateinit var viewModel: ExploreViewModel
    lateinit var sessionManager: SessionManager
    private lateinit var adapter: RandomMemeAdapter
    private lateinit var layoutManager: CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.explore_fragment, container, false)
        //Create a new adapter
        sessionManager = SessionManager(requireContext())
        adapter = RandomMemeAdapter()
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

        val service = RetrofitClient.makeCallsForMemes(requireContext())
        service.getRandom(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("Random", "Unable to get random memes")
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {
                    Log.e("Random Resp",
                        " " + response.body() + " " + response.errorBody()
                            .toString() + response.code() + " " + response.body()?.memes.toString()
                    )
                    //Map the response
                    adapter.setRandomPosts(response.body()!!.memes)


                }
            })



        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
