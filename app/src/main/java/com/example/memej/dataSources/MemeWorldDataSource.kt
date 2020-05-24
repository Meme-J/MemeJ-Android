package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemeWorldDataSourcae(val context: Context) :
    PageKeyedDataSource<String, Meme_World>() {


    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager = SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_World>
    ) {
        Log.e("DATA SOURCE", "In load Intial ")
        apiService.fetchMemeWorldMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 1 to fetch data!" + t.message.toString())
                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {
                    val listing = response.body()
                    Log.e("DATA SOURCE", " " + listing?.lastMemeId)

                    val memeWorldPosts = listing?.memes
                    Log.e(
                        "DATA SOURCE",
                        "Home post object" + memeWorldPosts + " " + memeWorldPosts?.size
                    )
                    Log.e("DATA SOURCE", "Success 1 ")

                    if (memeWorldPosts != null) {
                        Log.e("DATA SOURCE", "Homeposts is not null ")

                        callback.onResult(

                            memeWorldPosts,
                            null,       //Last Key
                            listing.lastMemeId         //Before value

                        )
                    }
                }
            }
            )

    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, Meme_World>
    ) {


        Log.e("DATA SOURCE", "In load After")
        apiService.fetchMemeWorldMemes(
            loadSize = params.requestedLoadSize,
            accessToken = sessionManager.fetchAcessToken()
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 2 to fetch data!")
                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {
                    val listing = response.body()

                    val memePosts = listing?.memes
                    Log.e("DATA SOURCE", "Success 2 ")

                    if (memePosts != null) {
                        callback.onResult(
                            memePosts,
                            listing.lastMemeId
                        )
                    }
                }
            })
    }


    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Meme_World>
    ) {

    }


}