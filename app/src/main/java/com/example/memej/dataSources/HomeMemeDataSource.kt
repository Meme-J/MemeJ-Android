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
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeMemeDataSource(val context: Context, val searchquery: queryBody, val pb: ProgressBar) :
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
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            tags = searchquery
        )
            .enqueue(object : Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 1 to fetch data!" + t.message.toString())
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE

                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {


                    Log.e(
                        "ON Resp DS",
                        response.body()?.lastMemeId.toString() + response.body()?.memes.toString()
                    )

                    if (response.isSuccessful) {
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
                    } else {
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT)
                            .show()
                        pb.visibility = View.GONE
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
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            tags = searchquery
        )
            .enqueue(object : Callback<homeMemeApiResponse> {
                override fun onFailure(call: Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 2 to fetch data!")
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {
                    if (response.isSuccessful) {
                        val listing = response.body()

                        val homePosts = listing?.memes
                        Log.e("DATA SOURCE", "Success 2 ")

                        if (homePosts != null) {
                            callback.onResult(
                                homePosts,
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