package com.example.memej.Utils

import android.os.Bundle

interface Communicator2 {

    fun passDataFromADdToNewMeme(bundle: Bundle)


    fun returnToSelectTemplate()

    fun passDataToEdit(bundle: Bundle)

    fun passDtataToComplete(bundle: Bundle)
}