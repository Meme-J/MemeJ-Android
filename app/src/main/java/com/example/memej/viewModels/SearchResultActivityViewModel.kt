package com.example.memej.viewModels

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.dataSources.HomeMemeDataSource
import com.example.memej.dataSources.MemeWorldDataSource
import com.example.memej.models.body.search.QueryBody
import com.example.memej.models.responses.home.Meme_Home
import com.example.memej.models.responses.meme_world.Meme_World

class SearchResultActivityViewModel(application: Application) : AndroidViewModel(application) {


    var homeResponse: LiveData<PagedList<Meme_Home>>
    var memeWorldResponse: LiveData<PagedList<Meme_World>>
    var context = application.applicationContext

    //Get the tag and progress bar
    lateinit var tagName: String
    lateinit var pb: ProgressBar

    init {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        homeResponse = initializedPagedListBuilderOngoing(config).build()
        memeWorldResponse = initializedPagedListBuilderComplete(config).build()

    }

    fun getComplete(str: String, pb: ProgressBar): LiveData<PagedList<Meme_World>> {
        tagName = str
        this.pb = pb
        return memeWorldResponse
    }


    fun getOnGoing(str: String, pb: ProgressBar): LiveData<PagedList<Meme_Home>> {
        tagName = str
        this.pb = pb
        return homeResponse
    }

    private fun initializedPagedListBuilderOngoing(config: PagedList.Config): LivePagedListBuilder<String, Meme_Home> {

        val dataSourceFactory = object : DataSource.Factory<String, Meme_Home>() {
            override fun create(): DataSource<String, Meme_Home> {

                val inf = QueryBody(tagName)

                return HomeMemeDataSource(context, inf, pb)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

    private fun initializedPagedListBuilderComplete(config: PagedList.Config): LivePagedListBuilder<String, Meme_World> {

        val dataSourceFactory = object : DataSource.Factory<String, Meme_World>() {
            override fun create(): DataSource<String, Meme_World> {

                val inf = QueryBody(tagName)
                return MemeWorldDataSource(context, inf, pb)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}