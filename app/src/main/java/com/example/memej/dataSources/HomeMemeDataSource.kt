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
import com.example.memej.models.body.search.QueryBody
import com.example.memej.models.responses.home.HomeMemeApiResponse
import com.example.memej.models.responses.home.Meme_Home
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Keep
class HomeMemeDataSource(val context: Context, val searchquery: QueryBody, val pb: ProgressBar) :
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
            search = searchquery
        )
            .enqueue(object : Callback<HomeMemeApiResponse> {
                override fun onFailure(call: Call<HomeMemeApiResponse>, t: Throwable) {

                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE

                }

                override fun onResponse(
                    call: Call<HomeMemeApiResponse>,
                    response: Response<HomeMemeApiResponse>
                ) {


                    Log.e("Response", response.body().toString())
                    Log.e("Value query", searchquery.toString())


                    if (response.isSuccessful) {
                        val listing = response.body()
                        val homePosts = listing?.memes
                        val size = homePosts?.size

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
                                "No Memes available",
                                Toast.LENGTH_SHORT
                            ).show()
//                            pb.visibility = View.GONE
//                            val i = Intent(context, MainActivity::class.java)
//                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            context.startActivity(i)

                        }

                    } else {
                        val message = response.errorBody().toString()
                        Toast.makeText(context, "Unable to get memes", Toast.LENGTH_SHORT).show()
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
            search = searchquery
        )
            .enqueue(object : Callback<HomeMemeApiResponse> {
                override fun onFailure(call: Call<HomeMemeApiResponse>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<HomeMemeApiResponse>,
                    response: Response<HomeMemeApiResponse>
                ) {

                    Log.e("HomeDataSourceResponse", response.body()?.memes?.size.toString())
                    if (response.isSuccessful) {
                        val listing = response.body()

                        val homePosts = listing?.memes
                        val size = homePosts?.size

                        if (homePosts != null && homePosts.isNotEmpty()) {
                            callback.onResult(
                                homePosts,
                                listing.lastMemeId
                            )
                        }
                        if (homePosts!!.isEmpty()) {
                            Toast.makeText(
                                context,
                                "No memes available",
                                Toast.LENGTH_SHORT
                            ).show()
                            pb.visibility = View.GONE
//                            val i = Intent(context, MainActivity::class.java)
//                            context.startActivity(i)
                        }


                        pb.visibility = View.GONE


                    } else {
                        val message = response.errorBody().toString()
                        Toast.makeText(context, "Unable to get memes", Toast.LENGTH_SHORT).show()
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