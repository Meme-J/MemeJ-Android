package com.example.memej.BoundaryCallbacks

//import com.example.memej.database.HomeMemeDataBase
/*
class HomeMemeBoundaryCallback(private val db: HomeMemeDataBase) :
    PagedList.BoundaryCallback<homeMemeList>() {

    private val api = RetrofitClient.makeCallsForMemes()
    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        //1
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
            api.fetchEditableMemes()
                //2
                .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {

                    override fun onFailure(call: Call<homeMemeApiResponse>?, t: Throwable) {
                        //3
                        Log.e("k", "Failed to load data in Zero Case")
                        helperCallback.recordFailure(t)
                    }

                    override fun onResponse(
                        call: Call<homeMemeApiResponse>?,
                        response: Response<homeMemeApiResponse>
                    ) {
                        //4
                        val posts = response.body()?.data?.memes?.map { it.homeMemeResponse.memes }
                        executor.execute {
                            db.postDao().insert(posts ?: listOf())
                            helperCallback.recordSuccess()
                        }
                    }
                })
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: homeMemeList) {
        super.onItemAtEndLoaded(itemAtEnd)

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
            api.fetchEditableMemes(after = itemAtEnd.lastMemeId)
                .enqueue(object : retrofit2.Callback<homeMemeApiResponse> {

                    override fun onFailure(call: Call<homeMemeApiResponse>?, t: Throwable) {
                        Log.e("DATATaa", "Failed to load end data!")
                        helperCallback.recordFailure(t)
                    }

                    override fun onResponse(
                        call: Call<homeMemeApiResponse>?,
                        response: Response<homeMemeApiResponse>) {

                        val posts = response.body()?.data?.memes?.map { it.homeMemeResponse.memes }
                        executor.execute {
                            db.postDao().insert(posts ?: listOf())
                            helperCallback.recordSuccess()
                        }
                    }
                })
        }

    }
}
*/
