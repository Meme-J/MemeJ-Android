package com.example.memej.dataSources

import android.content.Context
import android.widget.Toast
import androidx.annotation.Keep
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.responses.memeWorldResponses.memeApiResponses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Keep
class LikedMemesDataSource(val context: Context) :
    PageKeyedDataSource<String, Meme_World>() {


    val ctx = ApplicationUtil.getContext()
    private val apiService = RetrofitClient.makeCallForProfileParameters(ctx)
    private val sessionManager =
        SessionManager(ctx)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_World>
    ) {

        apiService.getLikedMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )

            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

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
                        Toast.makeText(
                            context,
                            "Unable to get liked memes",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            )

    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, Meme_World>
    ) {


        apiService.getLikedMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {

                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memePosts = listing?.memes

                        if (memePosts != null) {
                            callback.onResult(
                                memePosts,
                                listing.lastMemeId
                            )
                        }
                    } else {
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT)
                            .show()
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