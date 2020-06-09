package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.template.EmptyTemplateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Create a scope
class MemeGroupDataSource(val context: Context) :
    PageKeyedDataSource<String, EmptyTemplateResponse.Template>() {


    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager = SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, EmptyTemplateResponse.Template>
    ) {
        Log.e("DATA SOURCE", "In load Intial ")
        apiService.getTemplate(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<EmptyTemplateResponse> {
                override fun onFailure(call: Call<EmptyTemplateResponse>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 1 to fetch data!" + t.message.toString())
                }

                override fun onResponse(
                    call: Call<EmptyTemplateResponse>,
                    response: Response<EmptyTemplateResponse>
                ) {
                    val listing = response.body()

                    val homePosts = listing?.templates
                    Log.e("DATA SOURCE", "Home post object" + homePosts + " " + homePosts?.size)

                    if (homePosts != null) {

                        callback.onResult(

                            homePosts,
                            null,       //Last Key
                            null         //Before value

                        )
                    }
                }
            }
            )

    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, EmptyTemplateResponse.Template>
    ) {


        Log.e("DATA SOURCE", "In load After")
        apiService.getTemplate(
            loadSize = params.requestedLoadSize,
            accessToken = sessionManager.fetchAcessToken()
        )
            .enqueue(object : Callback<EmptyTemplateResponse> {
                override fun onFailure(call: Call<EmptyTemplateResponse>, t: Throwable) {
                    Log.e("DATA SOURCE", "Failed 2 to fetch data!")
                }

                override fun onResponse(
                    call: Call<EmptyTemplateResponse>,
                    response: Response<EmptyTemplateResponse>
                ) {
                    val listing = response.body()

                    val homePosts = listing?.templates
                    Log.e("DATA SOURCE", "Success 2 ")

                    if (homePosts != null) {
                        callback.onResult(
                            homePosts,
                            null
                        )
                    }
                }
            })
    }


    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, EmptyTemplateResponse.Template>
    ) {

    }


}