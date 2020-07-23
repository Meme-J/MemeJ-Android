package com.example.memej.boundaryCallbacks

import android.content.Context
import android.util.Log
import androidx.paging.PagedList
import com.example.memej.Utils.PagingUtils.PagingRequestHelper
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.databases.LikedMemesDatabase
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executors

class LikedMemesBoundaryCallbacks(
    private val myMemesDb: LikedMemesDatabase,
    context: Context
) :
    PagedList.BoundaryCallback<Meme_World>() {


    private val api = RetrofitClient.makeCallForProfileParameters(context)
    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)
    private val sessionManager =
        SessionManager(context)

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        //1
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
            api.getLikedMemes(
                loadSize = 30,
                accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
            ).enqueue(object : retrofit2.Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {

                    Log.e("Failed 1", "Falied at zero load")
                    helperCallback.recordFailure(t)

                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {

                    if (response.body()?.memes!!.isNotEmpty()) {

                        val listing = response.body()
                        val memeWorldPosts = listing?.memes

                        executor.execute {
                            myMemesDb.likedMemesDao().insert(memeWorldPosts ?: listOf())
                            helperCallback.recordSuccess()
                        }
                    }
                }
            })
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Meme_World) {

        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
            api.getLikedMemes(
                loadSize = 15,
                accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
            )
                .enqueue(object : retrofit2.Callback<memeApiResponses> {

                    override fun onFailure(call: Call<memeApiResponses>?, t: Throwable) {
                        Log.e("Fail2Like", "Failed to load data!")
                        helperCallback.recordFailure(t)
                    }

                    override fun onResponse(
                        call: Call<memeApiResponses>?,
                        response: Response<memeApiResponses>
                    ) {
                        val listing = response.body()
                        val memeWorldPosts = listing?.memes


                        executor.execute {
                            myMemesDb.likedMemesDao().insert(memeWorldPosts ?: listOf())
                            helperCallback.recordSuccess()
                        }
                    }
                })
        }
    }

}