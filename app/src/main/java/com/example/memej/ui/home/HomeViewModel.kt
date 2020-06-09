package com.example.memej.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel

class HomeViewModel(val context: Context) : ViewModel() {

//    var postsLiveData: LiveData<PagedList<Meme_Home>>
//
//    init {
//        Log.e("K", "in INIT Start")
//
//        val config = PagedList.Config.Builder()
//            .setPageSize(30)               //Number of items to load in a page
//            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
//            .build()
//        postsLiveData = initializedPagedListBuilder(config).build()
//
//        Log.e(
//            "K",
//            " " + postsLiveData.value + " <- Value of live data \n has observers or not" + postsLiveData.hasObservers() + " List" + postsLiveData
//        )
//
//        Log.e("K", "in INIT End")
//    }
//
//    fun getPosts(): LiveData<PagedList<Meme_Home>> = postsLiveData
//
////      initializedPagedListBuilder fetches the
////    pagedlist from our datasource.
////    In our viewmodel also we pass the viewModelScope to the PostsDataSource factory.
//
//    private fun initializedPagedListBuilder(config: PagedList.Config):
//            LivePagedListBuilder<String, Meme_Home> {
//        Log.e("K", "in IPLB")
//        val dataSourceFactory = object : DataSource.Factory<String, Meme_Home>() {
//            override fun create(): DataSource<String, Meme_Home> {
//                Log.e("K", "iin the on create of ds")
//
//                return HomeMemeDataSource(context)
//            }
//        }
//        return LivePagedListBuilder(dataSourceFactory, config)
//    }
}