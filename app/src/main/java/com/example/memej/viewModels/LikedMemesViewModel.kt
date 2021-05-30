package com.example.memej.viewModels

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.dataSources.LikedMemesDataSource
import com.example.memej.models.responses.meme_world.Meme_World

class LikedMemesViewModel(application: Application) : AndroidViewModel(application) {

    var postsLiveData: LiveData<PagedList<Meme_World>>
    var context = application.applicationContext
    lateinit var pb: ProgressBar

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)               //Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

    }

    fun getPosts(pb: ProgressBar): LiveData<PagedList<Meme_World>> {
        this.pb = pb
        return postsLiveData
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, Meme_World> {

        val dataSourceFactory =
            object : DataSource.Factory<String, Meme_World>() {
                override fun create(): DataSource<String, Meme_World> {

                    return LikedMemesDataSource(context)
                }
            }
        return LivePagedListBuilder(dataSourceFactory, config)
    }


}
