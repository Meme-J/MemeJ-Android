package com.example.memej.ui.memeTemplate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.dataSources.MemeGroupDataSource
import com.example.memej.entities.memeGroup

class SelectMemeGroupViewModel : ViewModel() {


    //This is the observer class
    var postsLiveData: LiveData<PagedList<memeGroup>>

    init {
        Log.e("K", "in INIT Start")

        val config = PagedList.Config.Builder()
            .setPageSize(30)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

        Log.e("K", "in INIT End")
    }

    fun getPosts(): LiveData<PagedList<memeGroup>> = postsLiveData

//      initializedPagedListBuilder fetches the
//    pagedlist from our data source.
//    In our viewmodel also we pass the viewModelScope to the PostsDataSource factory.

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, memeGroup> {
        Log.e("K", "in IPLB")
        val dataSourceFactory = object : DataSource.Factory<String, memeGroup>() {
            override fun create(): DataSource<String, memeGroup> {
                return MemeGroupDataSource(viewModelScope)
            }
        }
        return LivePagedListBuilder<String, memeGroup>(dataSourceFactory, config)
    }
}