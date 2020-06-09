package com.example.memej.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.memej.R
import com.example.memej.Utils.Communicator
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.NumLikes
import com.example.memej.responses.ProfileResponse
import com.example.memej.viewModels.ProfileViewModel
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Response

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var root: View
    private lateinit var viewModel: ProfileViewModel
    private lateinit var comm: Communicator
    private lateinit var sessionManager: SessionManager
    lateinit var pb: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.profile_fragment, container, false)
        comm = activity as Communicator
        sessionManager = context?.let { SessionManager(it) }!!
        pb = root.findViewById(R.id.pb_profile)
        //Create a lifecycle owner
        pb.visibility = View.VISIBLE

        getUser()
        //Get the total loves
        val service = RetrofitClient.makeCallForProfileParameters(requireContext())
        service.getNumLikesRecieved(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<NumLikes> {
                override fun onFailure(call: Call<NumLikes>, t: Throwable) {
                }

                override fun onResponse(call: Call<NumLikes>, response: Response<NumLikes>) {
                    //Response is number of likes
                    root.findViewById<TextView>(R.id.textViewTotalLikes).text =
                        response.body()?.likes.toString()
                }
            })

        //Get the total memes created.

        pb.visibility = View.GONE
        root.findViewById<ImageView>(R.id.likedMemes).setOnClickListener {
            // Run an api call
            //Replace with the Liked Memes Fragment, where all the operations will take place like meme world feature
            comm.goToLikedMemesPage()
        }


        return root
    }

    private fun getUser() {
        val service = RetrofitClient.getAuthInstance()
        service.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Log.e("Prof", "Fail")
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    Log.e("porf pass", response.body()?.profile.toString())
                    root.findViewById<MaterialTextView>(R.id.username).text =
                        response.body()?.profile?.username
                    root.findViewById<MaterialTextView>(R.id.name).text =
                        response.body()?.profile?.name
                    //   val db = context?.let { UserDatabase.create(it) }
                    pb.visibility = View.GONE
                }
            })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        //Get this viemodel into working


    }

}
