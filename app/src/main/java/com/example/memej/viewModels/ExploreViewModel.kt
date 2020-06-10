package com.example.memej.viewModels

import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.memej.responses.homeMememResponses.Meme_Home

class ExploreViewModel : ViewModel() {

    //Create an instance for Liva Data
    lateinit var postRandom: LiveData<Meme_Home>
    lateinit var pb: ProgressBar


    fun getPosts(pb: ProgressBar): LiveData<Meme_Home> {
        this.pb = pb
        return postRandom
    }



}
