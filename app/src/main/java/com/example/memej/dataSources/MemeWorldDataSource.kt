package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.SessionManager
import com.example.memej.entities.queryBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemeWorldDataSourcae(val context: Context, val searchQuery: queryBody, val pb: ProgressBar) :
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
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            tag = searchQuery
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 1 to fetch data!" + t.message.toString())
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {

                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memeWorldPosts = listing?.memes

                        if (memeWorldPosts != null) {

                            callback.onResult(

                                memeWorldPosts,
                                null,       //Last Key
                                listing.lastMemeId         //Before value

                            )
                        }
                    } else {
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT)
                            .show()
                        pb.visibility = View.GONE
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
            accessToken = sessionManager.fetchAcessToken(),
            tag = searchQuery
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 2 to fetch data!")
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {

                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memePosts = listing?.memes
                        Log.e("DATA SOURCE", "Success 2 ")

                        if (memePosts != null) {
                            callback.onResult(
                                memePosts,
                                listing.lastMemeId
                            )
                        }
                    } else {
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT)
                            .show()
                        pb.visibility = View.GONE
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