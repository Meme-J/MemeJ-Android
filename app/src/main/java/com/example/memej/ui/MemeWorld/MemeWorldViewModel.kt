package com.example.memej.ui.MemeWorld

import androidx.lifecycle.ViewModel

//import com.example.memej.dataSources.MemeWorldDataSource

class MemeWorldViewModel : ViewModel() {

//    var postsLiveData: LiveData<PagedList<memeWorldResponse>>
//
//    init {
//        Log.e("K", "in INIT Start")
//
//        val config = PagedList.Config.Builder()
//            .setPageSize(5)               //Number of items to load in a page
//            .setEnablePlaceholders(false)   //There is holder disabled till the data is loaded
//            .build()
//        postsLiveData = initializedPagedListBuilder(config).build()
//
//        Log.e("K", "in INIT End")
//    }
//
//    fun getPosts(): LiveData<PagedList<memeWorldResponse>> = postsLiveData
//
////      initializedPagedListBuilder fetches the
////    pagedlist from our data source.
////    In our viewmodel also we pass the viewModelScope to the PostsDataSource factory.
//
//    private fun initializedPagedListBuilder(config: PagedList.Config):
//            LivePagedListBuilder<String, memeWorldResponse> {
//        Log.e("K", "in IPLB")
//        val dataSourceFactory = object : DataSource.Factory<String, memeWorldResponse>() {
//            override fun create(): DataSource<String, memeWorldResponse> {
//                return MemeWorldDataSource(viewModelScope)
//            }
//        }
//        return LivePagedListBuilder<String, memeWorldResponse>(dataSourceFactory, config)
//    }
//
//
//

}
