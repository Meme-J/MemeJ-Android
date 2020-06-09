package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeMemeDataSource(val context: Context) :
    PageKeyedDataSource<String, Meme_Home>() {

    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager = SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_Home>
    ) {
        Log.e("DATA SOURCE", "In load Intial ")
        apiService.fetchEditableMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 1 to fetch data!" + t.message.toString())
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {
                    val listing = response.body()
                    Log.e("DATA SOURCE", " " + listing?.lastMemeId)

                    val homePosts = listing?.memes
                    Log.e("DATA SOURCE", "Home post object" + homePosts + " " + homePosts?.size)

                    if (homePosts != null) {

                        callback.onResult(

                            homePosts,
                            null,       //Last Key
                            listing.lastMemeId         //Before value

                        )
                    }
                }
            }
            )

//
//        scope.launch {
//            try {
//                val response = apiService.fetchEditableMemes(loadSize = params.requestedLoadSize)
//                Log.e("DATA SOURCE", "Response= " + response.code() + " " + response.body() + " " + response +" " + response.message()+" " + response.isSuccessful() +" " + response.headers())
////
//                when {
//                    response.isSuccessful -> {
//                        val listing = response.body()
//                        val homePosts = listing?.map { it.memes }
//                      Log.e("Dataa Source", "\n\n$listing"+ " " + "\n\n${listing?.size}"+ " \nLast meme Id: " + listing?.map { it.lastMemeId } )
//                        Log.e("Data Source-homePost",homePosts.toString() +" ")
//                        callback.onResult(
//                            homePosts ?: listOf(),
//                            null,                                  //Before Parameter
//                            listing?.map { it.lastMemeId }.toString()             //After Parameter
//                        )
//
//
//                    }
//                }
//            } catch (exception: Exception) {
//                Log.e("K", "Error 1")
//            }
//        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, Meme_Home>
    ) {


        Log.e("DATA SOURCE", "In load After")
        apiService.fetchEditableMemes(
            loadSize = params.requestedLoadSize,
            accessToken = sessionManager.fetchAcessToken()
        )
            .enqueue(object : Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 2 to fetch data!")
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {
                    val listing = response.body()

                    val homePosts = listing?.memes
                    Log.e("DATA SOURCE", "Success 2 ")

                    if (homePosts != null) {
                        callback.onResult(
                            homePosts,
                            listing.lastMemeId
                        )
                    }
                }
            })
    }


    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Meme_Home>
    ) {

    }

//Invalidate Scope
//private val scope : CoroutineScope in Constructor priamry
//    override fun invalidate() {
//        super.invalidate()
//        scope.cancel()
//    }


}