package com.example.memej.textProperties.lib

import android.view.View


//Kotlin
interface OnPhotoEditorListener {
    fun onEditTextChangeListener(
        rootView: View?,
        text: String?,
        colorCode: Int
    )

    fun onAddViewListener(
        viewType: ViewType?,
        numberOfAddedViews: Int
    )

    fun onRemoveViewListener(numberOfAddedViews: Int)
    fun onStartViewChangeListener(viewType: ViewType?)
    fun onStopViewChangeListener(viewType: ViewType?)
}
