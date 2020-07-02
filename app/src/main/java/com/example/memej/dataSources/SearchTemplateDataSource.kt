package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.Keep
import androidx.paging.PageKeyedDataSource
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.searchTemplate
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.template.EmptyTemplateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Keep
class SearchTemplateDataSource(val context: Context, val tagName: String) :
    PageKeyedDataSource<String, EmptyTemplateResponse.Template>() {


    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)
    private val sessionManager =
        SessionManager(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, EmptyTemplateResponse.Template>
    ) {

        apiService.getSearchedTemplates(
            loadSize = params.requestedLoadSize,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}",
            info = searchTemplate(tagName)
        )
            .enqueue(object : Callback<EmptyTemplateResponse> {
                override fun onFailure(call: Call<EmptyTemplateResponse>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

                    } else {
                        val message = response.errorBody().toString()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

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
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

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

                    } else {
                        val message = response.errorBody().toString()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

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