package com.example.memej.entities


data class TextBox(
    val num_tb: Int,
    //Order of the coordinates if [num_tb][4] where xTL, yTL, xBR, yBR
    //For text, num of TB = 2;
    //Store them in a better data structure
    val xT1: Float,
    val yT1: Float,
    val xB1: Float,
    val yB1: Float,
    val xT2: Float,
    val yT2: Float,
    val xB2: Float,
    val yB2: Float,
    val c1: String,    //Color of TB1
    val c2: String     //Color of TB2

)