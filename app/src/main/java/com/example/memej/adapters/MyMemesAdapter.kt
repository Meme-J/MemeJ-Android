package com.example.memej.adapters

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.responses.homeMememResponses.homeMemeApiResponse
import retrofit2.Response

class MyMemesAdapter(val context: Context) :
    PageKeyedDataSource<String, Meme_Home>() {

    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager = SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Meme_Home>
    ) {

        apiService.getMyMemes(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {
                override fun onFailure(call: retrofit2.Call<homeMemeApiResponse>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: retrofit2.Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {


                    val listing = response.body()

                    val homePosts = listing?.memes

                    if (homePosts != null) {

                        callback.onResult(

                            homePosts,
                            null,       //Last Key
                            listing.lastMemeId         //Before value
                        )
                    }
                }
            })


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
        apiService.getMyMemes(
            loadSize = params.requestedLoadSize,
            accessToken = sessionManager.fetchAcessToken()
        )
            .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {
                override fun onFailure(call: retrofit2.Call<homeMemeApiResponse>, t: Throwable) {
                    Log.e("FAil", "Failed After")
                }

                override fun onResponse(
                    call: retrofit2.Call<homeMemeApiResponse>,
                    response: Response<homeMemeApiResponse>
                ) {
                    val listing = response.body()

                    val homePosts = listing?.memes

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