package com.example.memej.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.dataSources.SearchTemplateDataSource
import com.example.memej.responses.template.EmptyTemplateResponse

class SearchTemplateViewModel(application: Application) : AndroidViewModel(application) {


    var postsLiveData: LiveData<PagedList<EmptyTemplateResponse.Template>>
    lateinit var tagName: String

    init {

        val config = PagedList.Config.Builder()
            .setPageSize(30)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

    }

    fun getPosts(str: String): LiveData<PagedList<EmptyTemplateResponse.Template>> {
        tagName = str
        return postsLiveData
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, EmptyTemplateResponse.Template> {
        val dataSourceFactory =
            object : DataSource.Factory<String, EmptyTemplateResponse.Template>() {
                override fun create(): DataSource<String, EmptyTemplateResponse.Template> {

                    return SearchTemplateDataSource(ApplicationUtil.getContext(), tagName)
                }
            }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}