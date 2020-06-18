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
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeMemeDataSource(val context: Context, val searchquery: queryBody, val pb: ProgressBar) :
    PageKeyedDataSource<String, Meme_Home>() {

    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager =
        SessionManager(context)


    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_Home>
    ) {

        apiService.fetchEditableMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            tags = searchquery
        )
            .enqueue(object : Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {

                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE

                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {


                    if (response.isSuccessful) {
                        val listing = response.body()
                        val homePosts = listing?.memes
                        val size = homePosts?.size

                        //When a notmal request sent
                        if (searchquery.tags == "") {
                            Log.e("Query", "In empty query")
                            if (homePosts != null && homePosts.isNotEmpty()) {

                                Log.e("Query", "In empty query, not empty resp")

                                callback.onResult(

                                    homePosts,
                                    null,       //Last Key
                                    listing.lastMemeId         //Before value

                                )
                                pb.visibility = View.GONE

                            }

                            if (homePosts?.isEmpty()!!) {

                                Log.e("Query", "In empty query,empty resp")
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
                        } else if (!searchquery.tags.equals("")) {


                            Log.e("Query", "In  query, ")
                            //Create a new empty array list
                            val listFiltered: MutableList<Meme_Home> = mutableListOf()
                            Log.e("List filtered initial", listFiltered.toString())
                            Log.e("Size", size.toString())

                            if (size != null) {
                                for (i in 0 until size - 1) {
                                    val templateIdTag = homePosts.elementAt(i).templateId.tags
                                    val memeTag = homePosts.elementAt(i).tags
                                    Log.e("This", templateIdTag.toString())

                                    if (memeTag.contains(searchquery.tags)) {
                                        listFiltered.add(homePosts.elementAt(i))
                                    } else if (templateIdTag.contains(searchquery.tags)) {
                                        listFiltered.add(homePosts.elementAt(i))
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
        callback: LoadCallback<String, Meme_Home>
    ) {


        apiService.fetchEditableMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            tags = searchquery
        )
            .enqueue(object : Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {

                    Log.e("HomeDataSourceResponse", response.body()?.memes?.size.toString())
                    if (response.isSuccessful) {
                        val listing = response.body()

                        val homePosts = listing?.memes
                        val size = homePosts?.size

                        if (searchquery.tags == "") {
                            if (homePosts != null && homePosts.isNotEmpty()) {
                                callback.onResult(
                                    homePosts,
                                    listing.lastMemeId
                                )

                                if (homePosts.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "Unable to get meme with this tag",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    pb.visibility = View.GONE
                                    val i = Intent(context, MainActivity::class.java)
                                    context.startActivity(i)
                                }

                            }
                            pb.visibility = View.GONE
                        } else if (!searchquery.tags.equals("")) {


                            Log.e("Query", "In  query, ")
                            //Create a new empty array list
                            val listFiltered: MutableList<Meme_Home> = mutableListOf()
                            Log.e("List filtered initial", listFiltered.toString())
                            Log.e("Size", size.toString())

                            if (size != null) {
                                for (i in 0 until size - 1) {
                                    val templateIdTag = homePosts.elementAt(i).templateId.tags
                                    val memeTag = homePosts.elementAt(i).tags
                                    Log.e("This", templateIdTag.toString())

                                    if (memeTag.contains(searchquery.tags)) {
                                        listFiltered.add(homePosts.elementAt(i))
                                    } else if (templateIdTag.contains(searchquery.tags)) {
                                        listFiltered.add(homePosts.elementAt(i))
                                    }

                                }
                            }

                            Log.e("Query", listFiltered.toString() + listFiltered.size)

                            if (listFiltered.isNotEmpty()) {
                                Log.e("Query", "In non empty ")

                                callback.onResult(

                                    listFiltered,
                                    null     //Last Key
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