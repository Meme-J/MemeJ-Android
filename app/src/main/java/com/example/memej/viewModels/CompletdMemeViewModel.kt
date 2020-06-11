package com.example.memej.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.memeWorldResponses.Meme_World


class CompletdMemeViewModel : ViewModel() {

    private var mNumLikes = MutableLiveData<Int>()

    lateinit var _meme: Meme_World
    val ctx = ApplicationUtil.getContext()

    init {

        //    mNumLikes = getLikesOfTheImage()
    }

//    private fun getLikesOfTheImage(): MutableLiveData<Int> {
//
//
//
//
//
//    }

    fun geNumLikes(): MutableLiveData<Int> {
        return mNumLikes
    }

    //Function to call
    //Call meme _ world
    val service = RetrofitClient.makeCallsForMemes(ctx)
    val sessionManager = SessionManager(ctx)


}