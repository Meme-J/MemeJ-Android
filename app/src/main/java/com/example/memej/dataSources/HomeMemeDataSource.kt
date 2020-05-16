package com.example.memej.dataSources

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.memej.entities.homeMeme
import com.example.memej.interfaces.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeMemeDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<String, homeMeme>() {

    //External variable to point to api client
    private val apiService = RetrofitClient.makeCallsForMemes()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, homeMeme>
    ) {
        scope.launch {
            try {
                val response = apiService.fetchEditableMemes(loadSize = params.requestedLoadSize)
                when {
                    response.isSuccessful -> {
                        val listing = response.body()?.data
                        val homePosts = listing?.children?.map { it.data }
                        callback.onResult(
                            homePosts ?: listOf(),
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

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, homeMeme>) {
        scope.launch {
            try {
                val response =
                    apiService.fetchEditableMemes(
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

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, homeMeme>) {
        scope.launch {
            try {
                val response =
                    apiService.fetchEditableMemes(
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