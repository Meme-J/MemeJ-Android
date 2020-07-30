package com.example.memej.textProperties.lib

interface BrushInterface {

    fun onViewAdd(brushDrawingView: BrushView?)

    fun onViewRemoved(brushDrawingView: BrushView?)

    fun onStartDrawing()

    fun onStopDrawing()


}