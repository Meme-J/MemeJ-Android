package com.example.memej.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.memej.entities.memeGroup
import com.example.memej.interfaces.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

//Create a scope
class MemeGroupDataSource(private val scope: CoroutineScope, private val context: Context) :
    PageKeyedDataSource<String, memeGroup>() {

    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes(context)

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, memeGroup>
    ) {
        Log.e("DATA SOURCE", "Load Initial ")
        scope.launch {
            try {
                val response = apiService.fetchMemeGroups(loadSize = params.requestedLoadSize)
                when {
                    response.isSuccessful -> {
                        Log.e("DATA SOURCE", "Success 1 ")
                        val listing = response.body()?.data
                        val memeGroupPosts = listing?.children?.map { it.data }
                        callback.onResult(
                            memeGroupPosts ?: listOf(),
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

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, memeGroup>) {
        scope.launch {
            try {
                val response =
                    apiService.fetchMemeGroups(
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

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, memeGroup>) {
        scope.launch {
            try {
                val response =
                    apiService.fetchMemeGroups(
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