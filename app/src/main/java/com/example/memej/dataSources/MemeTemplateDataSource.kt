package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.memej.entities.memeTemplate
import com.example.memej.interfaces.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MemeTemplateDataSource(private val scope: CoroutineScope, private val context: Context) :
    PageKeyedDataSource<String, memeTemplate>() {

    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, memeTemplate>
    ) {
        scope.launch {
            try {
                val response = apiService.fetchMemeTemplates(loadSize = params.requestedLoadSize)
                when {
                    response.isSuccessful -> {
                        val listing = response.body()?.data
                        val memeTemplatePosts = listing?.children?.map { it.data }
                        callback.onResult(
                            memeTemplatePosts ?: listOf(),
                            listing?.before,
                            listing?.after
                        )
                    }
                }
            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed1 to fetch data!")
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, memeTemplate>
    ) {
        scope.launch {
            try {
                val response =
                    apiService.fetchMemeTemplates(
                        loadSize = params.requestedLoadSize,
                        after = params.key

                    )
                when {
                    response.isSuccessful -> {
                        val listing = response.body()?.data
                        val items = listing?.children?.map { it.data }
                        callback.onResult(items ?: listOf(), listing?.after)
                    }
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed2 to fetch data!")
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, memeTemplate>
    ) {
        scope.launch {
            try {
                val response =
                    apiService.fetchMemeTemplates(
                        loadSize = params.requestedLoadSize,
                        before = params.key
                    )
                when {
                    response.isSuccessful -> {
                        val listing = response.body()?.data
                        val items = listing?.children?.map { it.data }
                        callback.onResult(items ?: listOf(), listing?.after)
                    }
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed3 to fetch data!")
            }
        }

    }

    //Invalidate Scope
    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}

//Write this class when you have api
