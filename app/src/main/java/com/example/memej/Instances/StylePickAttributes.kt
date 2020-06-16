package com.example.memej.Instances

import android.content.Context
import android.util.Log
import com.example.memej.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

object StylePickAttributes {


    //Return the color int
    fun choosePaint(context: Context, whichPaint: Int): Int {

        var paint_chosen = R.color.black

        this.let {
            MaterialColorPickerDialog
                .Builder(context)                                              // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE)
                .setTitle("Pick a color") // Default ColorShape.CIRCLE
                .setPositiveButton("Select")
                .setDefaultColor(whichPaint)
                .setNegativeButton("Cancel")
                .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
                .setColorRes(
                    context.resources.getIntArray(R.array.themeColors).toList()
                )    // Pass Default Color
                .setColorListener { color, colorHex ->
                    // Handle Color Selection

                    //Set the paint brush to be valued for this color
                    //Pass the color hex
                    paint_chosen = color
                    Log.e("Color", paint_chosen.toString())
                }
                .show()
        }

        return paint_chosen
    }


}