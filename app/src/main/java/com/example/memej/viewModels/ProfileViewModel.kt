package com.example.memej.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.dataSources.LikedMemesDataSource
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.NumLikes
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.memeWorldResponses.Meme_World
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    val successfulUpdateLikes: MutableLiveData<Boolean> = MutableLiveData()
    val successfulUpdateProfile: MutableLiveData<Boolean> = MutableLiveData()

    var postsLiveDataLikedMemes: LiveData<PagedList<Meme_World>>
//    lateinit var profileUser: ProfileResponse.Profile
    //  lateinit var numberLikes: NumLikes

    lateinit var messageProfile: String
    lateinit var messageLikes: String
    lateinit var messageLikedMemes: String

    private val context = ApplicationUtil.getContext()
    private val sessionManager = SessionManager(context)

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(15)
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()

        postsLiveDataLikedMemes = initializedPagedListBuilder(config).build()

    }


    fun getProfileOfUser() {

        val service = RetrofitClient.getAuthInstance()

        service.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    successfulUpdateProfile.value = false
                    messageProfile =
                        ErrorStatesResponse.returnStateMessageForThrowable(throwable = t)

                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {

                    if (response.isSuccessful) {
                        val p = ProfileResponse.Profile("", "", "", "")
                        successfulUpdateProfile.value = true
                        //                    profileUser = response.body()?.profile ?: p
                        //                  Log.e("USer", profileUser.toString())
                    }
                }
            })

    }


    fun getNumberOfLikes() {

        val service = RetrofitClient.makeCallForProfileParameters(context)
        var likes = NumLikes(0)
        service.getNumLikesRecieved(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
            .enqueue(object : retrofit2.Callback<NumLikes> {
                override fun onFailure(call: Call<NumLikes>, t: Throwable) {
                    messageLikes = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    successfulUpdateLikes.value = false
                }

                override fun onResponse(call: Call<NumLikes>, response: Response<NumLikes>) {
                    //Response is number of likes
                    if (response.isSuccessful) {
                        successfulUpdateLikes.value = true

                        //                numberLikes = response.body() ?: likes
                    }
                }
            })

    }


    fun getPostsOfLikedMemes(): LiveData<PagedList<Meme_World>> {
        return postsLiveDataLikedMemes
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, Meme_World> {

        val dataSourceFactory =
            object : DataSource.Factory<String, Meme_World>() {
                override fun create(): DataSource<String, Meme_World> {

                    return LikedMemesDataSource(context)
                }
            }
        return LivePagedListBuilder(dataSourceFactory, config)

    }


}
