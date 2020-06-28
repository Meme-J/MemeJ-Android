package com.example.memej.ui.profile

import android.app.Activity
import android.content.Intent
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
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.databinding.ProfileFragmentBinding
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.NumLikes
import com.example.memej.responses.ProfileResponse
import com.example.memej.viewModels.ProfileViewModel
import com.google.android.material.textview.MaterialTextView
import com.shreyaspatil.MaterialDialog.MaterialDialog
import retrofit2.Call
import retrofit2.Response

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()

    }

    private lateinit var root: View

    //    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var comm: Communicator
    private lateinit var sessionManager: SessionManager
    lateinit var pb: ProgressBar
    private val preferenceUtils = PreferenceUtil

    private lateinit var fragmentProfileBinding: ProfileFragmentBinding
    private val profileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    //   override fun getLayoutResourceId(): Int = R.layout.profile_fragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


//        fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
//        return fragmentProfileBinding.root






        root = inflater.inflate(R.layout.profile_fragment, container, false)
        comm = activity as Communicator
        sessionManager = context?.let {
            SessionManager(
                it
            )
        }!!
        pb = root.findViewById(R.id.pb_profile)
        //Create a lifecycle owner
        pb.visibility = View.VISIBLE


        getUser()           //This all are necessary
        getLikes()
        callLikes()


        pb.visibility = View.GONE

        root.findViewById<ImageView>(R.id.likedMemes).setOnClickListener {
            //Start intent to the Liked Memes Activty
            val i = Intent(activity, LikedMemes::class.java)
            startActivity(i)
        }


        return root
    }

    private fun getLikes() {

        if (preferenceUtils.likes == 0) {
            callLikes()
        } else {
            pb.visibility = View.GONE

            root.findViewById<TextView>(R.id.textViewTotalLikes).text =
                preferenceUtils.likes.toString()

        }


    }

    private fun callLikes() {

        Log.e("Profile", "InCall Likes")

        val service = RetrofitClient.makeCallForProfileParameters(requireContext())
        service.getNumLikesRecieved(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<NumLikes> {
                override fun onFailure(call: Call<NumLikes>, t: Throwable) {
                    pb.visibility = View.GONE

                }

                override fun onResponse(call: Call<NumLikes>, response: Response<NumLikes>) {
                    //Response is number of likes
                    if (response.isSuccessful) {
                        root.findViewById<TextView>(R.id.textViewTotalLikes).text =
                            response.body()?.likes.toString()
                        pb.visibility = View.GONE

                        setPreferencesLikes(response.body()!!)
                        preferenceUtils.setNumberOfLikesFromPreference(response.body()!!)
                    }
                }
            })
    }

    private fun setPreferencesUser(user: ProfileResponse.Profile) {
        preferenceUtils.setUserFromPreference(user)
    }

    private fun setPreferencesLikes(likes: NumLikes) {
        preferenceUtils.setNumberOfLikesFromPreference(likes)
    }

    private fun getUser() {

        //If there is blank in the pref
        if (preferenceUtils.username == "") {

            //Check connection here
            if (ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
                callUser()
            } else {
                checkConnection()
            }
        } else {

            root.findViewById<MaterialTextView>(R.id.username).text =
                preferenceUtils.getUserFromPrefernece().username
//            root.findViewById<MaterialTextView>(R.id.name).text =
//                preferenceUtils.getUserFromPrefernece().name
            pb.visibility = View.GONE

        }


    }

    fun checkConnection() {
        val mDialog = MaterialDialog.Builder(requireContext() as Activity)
            .setTitle("Oops")
            .setMessage("No internet connection")
            .setCancelable(true)
            .setAnimation(R.raw.inter2)
            .setPositiveButton(
                "Retry"
            ) { dialogInterface, which ->
                dialogInterface.dismiss()
                callUser()
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

    private fun callUser() {

        if (!ErrorStatesResponse.checkIsNetworkConnected(requireContext())) {
            checkConnection()
        }


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

                    if (response.isSuccessful) {
                        root.findViewById<MaterialTextView>(R.id.username).text =
                            response.body()?.profile?.username
//                        root.findViewById<MaterialTextView>(R.id.name).text =
//                            response.body()?.profile?.name
                        pb.visibility = View.GONE
                        setPreferencesUser(response.body()!!.profile)
                    }
                }
            })
    }


}
