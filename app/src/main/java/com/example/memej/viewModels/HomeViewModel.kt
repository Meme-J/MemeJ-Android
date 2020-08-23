package com.example.memej.viewModels

import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.body.queryBody
import com.example.memej.dataSources.HomeMemeDataSource
import com.example.memej.responses.homeMememResponses.Meme_Home

class HomeViewModel : ViewModel() {

    var postsLiveData: LiveData<PagedList<Meme_Home>>
    lateinit var pb: ProgressBar

    init {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

    }

    fun getPosts(pb: ProgressBar): LiveData<PagedList<Meme_Home>> {

        this.pb = pb

        Log.e("Home", "In get posts" + postsLiveData.toString())
        return postsLiveData
    }


    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, Meme_Home> {


        Log.e("Home", "In init")
        val dataSourceFactory = object : DataSource.Factory<String, Meme_Home>() {
            override fun create(): DataSource<String, Meme_Home> {

                val inf = queryBody("")
                return HomeMemeDataSource(ApplicationUtil.getContext(), inf, pb)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }


}

