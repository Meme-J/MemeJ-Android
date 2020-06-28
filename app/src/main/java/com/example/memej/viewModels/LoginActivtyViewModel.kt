package com.example.memej.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.PreferenceManager
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.LoginBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LoginResponse
import com.example.memej.responses.ProfileResponse
import io.reactivex.annotations.NonNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivtyViewModel : ViewModel() {


    var tag = LoginActivtyViewModel::class.java.simpleName

    private val preferenceManager: PreferenceManager = PreferenceManager()
    private val authDataManager = RetrofitClient.getAuthInstance()
    private val sessionManager: SessionManager = SessionManager(ApplicationUtil.getContext())
    private val preferenceUtils: PreferenceUtil = PreferenceUtil

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    val successProfile: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var messageProfile: String

    private val context = ApplicationUtil.getContext()


    @SuppressLint("CheckResult")
    fun login(@NonNull login: LoginBody) {

        authDataManager.loginUser(login).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val messageR = ErrorStatesResponse.returnStateMessageForThrowable(t)
                message = messageR
                successful.value = false
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {

                    if (response.body()?.msg == "Login successful.") {
                        successful.value = true
                        sessionManager.saveAuth_access_Token(
                            LoginResponse(
                                response.body()!!.msg,
                                response.body()!!.user
                            ).user.accessToken
                        )

                        sessionManager.saveAuth_refresh_Token(
                            (LoginResponse(
                                response.body()!!.msg,
                                response.body()!!.user
                            )).user.refreshToken
                        )

                        preferenceManager.putAuthToken(
                            (LoginResponse(
                                response.body()!!.msg,
                                response.body()!!.user
                            )).user.accessToken
                        )


                    } else {
                        successful.value = false
                        message = response.body()!!.msg
                    }
                } else {
                    successful.value = false
                }

            }
        })
    }


    fun getProfileOfUser() {

        authDataManager.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    //Background task to get profile
                    messageProfile = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    successProfile.value = false
                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {

                    if (response.isSuccessful) {

                        successProfile.value = true
                        setPreferencesUser(response.body()!!.profile)
                    }
                }
            })

    }

    private fun setPreferencesUser(user: ProfileResponse.Profile) {
        preferenceUtils.setUserFromPreference(user)
    }


}