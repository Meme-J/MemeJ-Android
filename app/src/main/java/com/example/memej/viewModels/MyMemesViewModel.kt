package com.example.memej.viewModels

import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.dataSources.MyMemesDataSource
import com.example.memej.entities.queryBody
import com.example.memej.responses.memeWorldResponses.Meme_World

class MyMemesViewModel : ViewModel() {

    var postsLiveData: LiveData<PagedList<Meme_World>>
    lateinit var pb: ProgressBar

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    init {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)              //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

    }

    fun getPosts(pr: ProgressBar): LiveData<PagedList<Meme_World>> {

        pb = pr
        return postsLiveData
    }


    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, Meme_World> {

        val dataSourceFactory = object : DataSource.Factory<String, Meme_World>() {
            override fun create(): DataSource<String, Meme_World> {

                val inf = queryBody("")
                return MyMemesDataSource(ApplicationUtil.getContext(), pb)
            }
        }
        return LivePagedListBuilder<String, Meme_World>(dataSourceFactory, config)
    }


}
