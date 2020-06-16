package com.example.memej.Instances

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

//Retruns a horizontal layout
object RvHorizontalLayout {
    fun getLinearHorizontalLayout(context: Context): LinearLayoutManager {

        val linear = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return linear

    }

}