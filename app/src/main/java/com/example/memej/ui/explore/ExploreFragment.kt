package com.example.memej.ui.explore

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.memej.R
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.adapters.RandomListener
import com.example.memej.adapters.RandomMemeAdapter
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import com.example.memej.viewModels.ExploreViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog
import com.yuyakaido.android.cardstackview.*
import retrofit2.Call
import retrofit2.Response


class ExploreFragment : Fragment(), RandomListener {


    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var root: View
    private val viewModel: ExploreViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    private lateinit var adapter: RandomMemeAdapter
    private lateinit var layoutManager: CardStackLayoutManager
    lateinit var pb: ProgressBar

    lateinit var dialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.explore_fragment, container, false)
        sessionManager =
            SessionManager(requireContext())
        adapter = RandomMemeAdapter(this)
        pb = root.findViewById(R.id.pb_explore)
        pb.visibility = View.VISIBLE

        dialog =
            ProgressDialog.show(activity, null, "Loading...", true)

        //Card Layout Manager
        layoutManager = CardStackLayoutManager(requireContext()).apply {
            setSwipeableMethod(SwipeableMethod.Manual)
            setOverlayInterpolator(LinearInterpolator())
        }


        //The edit text will be according to
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
        )


        val sv = root.findViewById<CardStackView>(R.id.stack_view)
        sv.layoutManager = layoutManager
        sv.adapter = adapter
        sv.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }


        layoutManager.setStackFrom(StackFrom.None)
        layoutManager.setDirections(Direction.HORIZONTAL)
        layoutManager.setSwipeThreshold(0.1f)
        layoutManager.setCanScrollHorizontal(true)
        layoutManager.setCanScrollVertical(false)


        //Rewind
//        sv.rewind()

        //Check if the last item


//        root.findViewById<TextView>(R.id.load_more).setOnClickListener {
//            getRandomMemes()
//        }


        if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            getRandomMemes()
        } else {
            checkConnection()
        }


        pb.visibility = View.GONE
        dialog.dismiss()

        return root
    }

    private fun checkConnection() {

        val mDialog = MaterialDialog.Builder(requireContext() as Activity)
            .setTitle("Oops")
            .setMessage("No internet connection")
            .setCancelable(true)
            .setAnimation(R.raw.inter2)
            .setPositiveButton(
                "Retry"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                getRandomMemes()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()

                pb.visibility = View.GONE

            }
            .build()
        mDialog.show()


    }

    private fun getRandomMemes() {

        Log.e("Random", "In get Random meme")
        //Recheck for states
        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            checkConnection()
        }

        val service = RetrofitClient.makeCallsForMemes(requireContext())

        service.getRandom(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("Throwanle", t.toString())
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.e("Random", response.body()?.memes!!.toString())
                        response.body()?.memes?.let { adapter.setRandomPosts(it) }
                        pb.visibility = View.GONE

                    } else {
                        val message = response.errorBody().toString()
                        Toast.makeText(context, "Unable to create meme", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun initRandomMeme(_meme: Meme_Home) {
        TODO("Not yet implemented")
    }


}
