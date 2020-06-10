package com.example.memej.viewModels

import android.app.Application
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.dataSources.MemeGroupDataSource
import com.example.memej.responses.template.EmptyTemplateResponse

class SelectMemeGroupViewModel(application: Application) : AndroidViewModel(application) {


    var postsLiveData: LiveData<PagedList<EmptyTemplateResponse.Template>>
    lateinit var pb: ProgressBar

    init {

        val config = PagedList.Config.Builder()
            .setPageSize(30)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

    }

    fun getPosts(pb: ProgressBar): LiveData<PagedList<EmptyTemplateResponse.Template>> {

        this.pb = pb
        return postsLiveData
    }

//      initializedPagedListBuilder fetches the
//    pagedlist from our data source.
//    In our viewmodel also we pass the viewModelScope to the PostsDataSource factory.

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, EmptyTemplateResponse.Template> {
        Log.e("K", "in IPLB")
        val dataSourceFactory =
            object : DataSource.Factory<String, EmptyTemplateResponse.Template>() {
                override fun create(): DataSource<String, EmptyTemplateResponse.Template> {

                    return MemeGroupDataSource(ApplicationUtil.getContext(), pb)
                }
            }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}