package com.example.memej.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.dataSources.MemeTemplateDataSource
import com.example.memej.entities.memeTemplate

class TemplateViewModel(val context: Context) : ViewModel() {


    //This is the observer class
    var postsLiveData: LiveData<PagedList<memeTemplate>>

    init {
        Log.e("K", "in INIT Start")

        val config = PagedList.Config.Builder()
            .setPageSize(30)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

        Log.e("K", "in INIT End")
    }

    //This id the memeGroupId
    fun getPosts(id: Int): LiveData<PagedList<memeTemplate>> = postsLiveData

//      initializedPagedListBuilder fetches the
//    pagedlist from our data source.
//    In our viewmodel also we pass the viewModelScope to the PostsDataSource factory.

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, memeTemplate> {
        Log.e("K", "in IPLB")
        val dataSourceFactory = object : DataSource.Factory<String, memeTemplate>() {
            override fun create(): DataSource<String, memeTemplate> {
                return MemeTemplateDataSource(viewModelScope, context)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

}