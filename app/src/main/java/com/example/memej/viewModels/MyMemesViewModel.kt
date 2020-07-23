package com.example.memej.viewModels

import android.content.Context
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.boundaryCallbacks.MyMemesBoundaryCallbacks
import com.example.memej.databases.MyMemesDatabase
import com.example.memej.responses.memeWorldResponses.Meme_World

class MyMemesViewModel : ViewModel() {

    lateinit var ctx: Context
    var postsLiveData: LiveData<PagedList<Meme_World>>
    lateinit var pb: ProgressBar

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    init {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(15)
            .setPageSize(15)//Number of items to load in a page
            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
            .build()
        postsLiveData = initializedPagedListBuilder(config).build()

    }

    fun getPosts(pr: ProgressBar): LiveData<PagedList<Meme_World>> {

        pb = pr
        return postsLiveData
    }


    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Meme_World> {

        ctx = ApplicationUtil.getContext()
        val database = MyMemesDatabase.getDatabase(ctx)
        val livePageListBuilder = LivePagedListBuilder<Int, Meme_World>(
            database.myMemesDao().posts(),
            config
        )
        livePageListBuilder.setBoundaryCallback(MyMemesBoundaryCallbacks(database, ctx))
        return livePageListBuilder
    }
}
