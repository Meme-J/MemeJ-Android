package com.example.memej.Utils

import android.os.Bundle

interface Communicator {

    fun passDataFromHome(bundle: Bundle)

    fun passDataToMemeWorld(bundle: Bundle)

    fun goToLikedMemesPage()

}