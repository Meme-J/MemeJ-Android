package com.example.memej.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.dataSources.MemeGroupDataSource
import com.example.memej.responses.template.EmptyTemplateResponse

class SelectMemeGroupViewModel(application: Application) : AndroidViewModel(application) {


    //This is the observer class
    var postsLiveData: LiveData<PagedList<EmptyTemplateResponse.Template>>
    var context = application.applicationContext

    init {
        Log.e("K", "in INIT Start")

        val config = PagedList.Config.Builder()
            .setPageSize(30)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

        Log.e("K", "in INIT End")
    }

    fun getPosts(): LiveData<PagedList<EmptyTemplateResponse.Template>> = postsLiveData

//      initializedPagedListBuilder fetches the
//    pagedlist from our data source.
//    In our viewmodel also we pass the viewModelScope to the PostsDataSource factory.

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, EmptyTemplateResponse.Template> {
        Log.e("K", "in IPLB")
        val dataSourceFactory =
            object : DataSource.Factory<String, EmptyTemplateResponse.Template>() {
                override fun create(): DataSource<String, EmptyTemplateResponse.Template> {

                    return MemeGroupDataSource(context)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}