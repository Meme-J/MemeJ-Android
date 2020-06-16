package com.example.memej.Utils

import android.os.Bundle
import androidx.fragment.app.Fragment

interface Communicator {

    fun passDataFromHome(bundle: Bundle)

    fun passDataToMemeWorld(bundle: Bundle)

    fun goToLikedMemesPage()

    fun goToMemesByTagPage(bundle: Bundle)

    fun goBackToHomePage()

    fun goToSearchResult(bundle: Bundle)

    fun goToProfilePage()

    fun openLikedMemeFromActivity(bundle: Bundle)

    fun goToAFragmnet(fragment: Fragment)
}
