package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.template.EmptyTemplateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Create a scope
class MemeGroupDataSource(val context: Context, val pb: ProgressBar) :
    PageKeyedDataSource<String, EmptyTemplateResponse.Template>() {


    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager = SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, EmptyTemplateResponse.Template>
    ) {

        apiService.getTemplate(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<EmptyTemplateResponse> {
                override fun onFailure(call: Call<EmptyTemplateResponse>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<EmptyTemplateResponse>,
                    response: Response<EmptyTemplateResponse>
                ) {

                    if (response.isSuccessful) {
                        val listing = response.body()

                        val homePosts = listing?.templates

                        if (homePosts != null) {

                            callback.onResult(

                                homePosts,
                                null,       //Last Key
                                null         //Before value

                            )
                        }
                        pb.visibility = View.GONE
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE
                    }

                }
            }
            )

    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, EmptyTemplateResponse.Template>
    ) {


        apiService.getTemplate(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<EmptyTemplateResponse> {
                override fun onFailure(call: Call<EmptyTemplateResponse>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    pb.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<EmptyTemplateResponse>,
                    response: Response<EmptyTemplateResponse>
                ) {
                    if (response.isSuccessful) {
                        val listing = response.body()

                        val homePosts = listing?.templates
                        Log.e("DATA SOURCE", "Success 2 ")

                        if (homePosts != null) {
                            callback.onResult(
                                homePosts,
                                null
                            )
                        }
                        pb.visibility = View.GONE
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE
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