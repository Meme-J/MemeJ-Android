package com.example.memej.dataSources

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.example.memej.entities.memeTemplate
import kotlinx.coroutines.CoroutineScope

class MemeTemplateDataSource(private val scope: CoroutineScope, private val context: Context) :
    PageKeyedDataSource<String, memeTemplate>() {
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, memeTemplate>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, memeTemplate>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, memeTemplate>
    ) {
        TODO("Not yet implemented")
    }
}

//Write this class when you have api
