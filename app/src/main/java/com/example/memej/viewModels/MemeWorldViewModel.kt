package com.example.memej.viewModels

import android.content.Context
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.boundaryCallbacks.MemeWorldBoundaryCallback
import com.example.memej.databases.MemeWorldDatabse
import com.example.memej.entities.queryBody
import com.example.memej.responses.memeWorldResponses.Meme_World


class MemeWorldViewModel : ViewModel() {

    var postsLiveData: LiveData<PagedList<Meme_World>>
    lateinit var pb: ProgressBar
    lateinit var ctx: Context

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    var message: String = ""

    init {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(15)
            .setPageSize(15)
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
        val database = MemeWorldDatabse.getDatabase(ctx)
        val livePageListBuilder = LivePagedListBuilder<Int, Meme_World>(
            database.memeWorldDao().posts(),
            config
        )
        val sb = queryBody("")
        livePageListBuilder.setBoundaryCallback(MemeWorldBoundaryCallback(database, ctx, sb))
        return livePageListBuilder
    }


}
