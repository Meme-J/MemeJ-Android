package com.example.memej.dataSources

import androidx.paging.PageKeyedDataSource
import com.example.memej.responses.memeWorldResponses.memeWorldResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

class MemeWorldDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<String, memeWorldResponse>() {
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, memeWorldResponse>
    ) {


    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, memeWorldResponse>
    ) {


    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, memeWorldResponse>
    ) {


    }

    //Invalidate Scope
    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}