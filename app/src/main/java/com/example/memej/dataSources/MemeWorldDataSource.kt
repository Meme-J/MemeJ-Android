package com.example.memej.dataSources

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.example.memej.MainActivity
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
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
    private val sessionManager =
        SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_World>
    ) {

        apiService.fetchMemeWorldMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            tag = searchQuery
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE

                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {

                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memeWorldPosts = listing?.memes
                        val size = memeWorldPosts?.size

                        if (searchQuery.tags == "") {
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
                                    "Unable to get meme with this tag",
                                    Toast.LENGTH_LONG
                                ).show()
                                pb.visibility = View.GONE
                                val i = Intent(context, MainActivity::class.java)
                                context.startActivity(i)
                            }
                        } else if (!searchQuery.tags.equals("")) {


                            Log.e("Query", "In  query, ")
                            //Create a new empty array list
                            val listFiltered: MutableList<Meme_World> = mutableListOf()
                            Log.e("List filtered initial", listFiltered.toString())
                            Log.e("Size", size.toString())

                            if (size != null) {
                                for (i in 0 until size - 1) {
                                    val templateIdTag = memeWorldPosts.elementAt(i).templateId.tags
                                    val memeTag = memeWorldPosts.elementAt(i).tags
                                    Log.e("This", templateIdTag.toString())

                                    if (memeTag.contains(searchQuery.tags)) {
                                        listFiltered.add(memeWorldPosts.elementAt(i))
                                    } else if (templateIdTag.contains(searchQuery.tags)) {
                                        listFiltered.add(memeWorldPosts.elementAt(i))
                                    }

                                }
                            }

                            Log.e("Query", listFiltered.toString() + listFiltered.size)

                            if (listFiltered.isNotEmpty()) {
                                Log.e("Query", "In non empty ")

                                callback.onResult(

                                    listFiltered,
                                    null,       //Last Key
                                    null
                                )
                                pb.visibility = View.GONE

                            } else if (listFiltered.isEmpty()) {
                                Log.e("Query", "In empty ")

                                Toast.makeText(
                                    context,
                                    "Unable to get meme with this tag",
                                    Toast.LENGTH_LONG
                                ).show()
                                pb.visibility = View.GONE
                                val i = Intent(context, MainActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(i)


                            }

                        }

                    } else {

                        val message = response.errorBody().toString()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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


        apiService.fetchMemeWorldMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer " + sessionManager.fetchAcessToken(),
            tag = searchQuery
        )
            .enqueue(object : Callback<memeApiResponses> {
                override fun onFailure(call: Call<memeApiResponses>, t: Throwable) {

                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<memeApiResponses>,
                    response: Response<memeApiResponses>
                ) {

                    if (response.isSuccessful) {
                        val listing = response.body()

                        val memePosts = listing?.memes

                        if (memePosts != null && memePosts.isNotEmpty()) {
                            callback.onResult(
                                memePosts,
                                listing.lastMemeId
                            )
                        }

                        if (memePosts?.isEmpty()!!) {
                            Toast.makeText(
                                context,
                                "Unable to get meme wioth this tag",
                                Toast.LENGTH_LONG
                            ).show()
                            pb.visibility = View.GONE
                            val i = Intent(context, MainActivity::class.java)
                            context.startActivity(i)
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