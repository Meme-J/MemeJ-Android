package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.Keep
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.models.responses.meme_world.MemeWorldApiResponses
import com.example.memej.models.responses.meme_world.Meme_World
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Get all the memes that are completed and those which are ongoing by me
@Keep
class MyMemesDataSource(val context: Context, val pb: ProgressBar) :
    PageKeyedDataSource<String, Meme_World>() {


    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager =
        SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_World>
    ) {

        apiService.getMyMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<MemeWorldApiResponses> {
                override fun onFailure(call: Call<MemeWorldApiResponses>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE

                }

                override fun onResponse(
                    call: Call<MemeWorldApiResponses>,
                    response: Response<MemeWorldApiResponses>
                ) {


                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memeWorldPosts = listing?.memes
                        val size = memeWorldPosts?.size

                        Log.e("MemeWorldDS", response.body().toString())

                        if (memeWorldPosts != null && memeWorldPosts.isNotEmpty()) {

                            callback.onResult(

                                memeWorldPosts,
                                null,       //Last Key
                                listing.lastMemeId         //Before value

                            )
                            pb.visibility = View.GONE

                        }
                        if (memeWorldPosts?.isEmpty()!!) {
                            Toast.makeText(
                                context,
                                "No memes available",
                                Toast.LENGTH_LONG
                            ).show()
                            pb.visibility = View.GONE
//                            val i = Intent(context, MainActivity::class.java)
//                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            context.startActivity(i)
                        }


                    } else {

                        val message = response.errorBody().toString()
                        Toast.makeText(context, "Unable to get my memes", Toast.LENGTH_SHORT).show()
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


        apiService.getMyMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer " + sessionManager.fetchAcessToken()

        )
            .enqueue(object : Callback<MemeWorldApiResponses> {
                override fun onFailure(call: Call<MemeWorldApiResponses>, t: Throwable) {

                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<MemeWorldApiResponses>,
                    response: Response<MemeWorldApiResponses>
                ) {


                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memePosts = listing?.memes
                        val size = memePosts?.size


                        if (memePosts != null && memePosts.isNotEmpty()) {
                            callback.onResult(
                                memePosts,
                                listing.lastMemeId
                            )
                        }

                        if (memePosts?.isEmpty()!!) {
                            Toast.makeText(
                                context,
                                "Unable to get memes",
                                Toast.LENGTH_LONG
                            ).show()
                            pb.visibility = View.GONE
//                            val i = Intent(context, MainActivity::class.java)
//                            context.startActivity(i)
                        }
                        pb.visibility = View.GONE


                    } else {

                        val message = response.errorBody().toString()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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