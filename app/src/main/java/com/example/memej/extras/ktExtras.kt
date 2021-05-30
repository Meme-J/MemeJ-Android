package com.example.memej.extras

//Font pallete

//Global to be used
//private var paint_chosen by Delegates.notNull<Int>()
//private lateinit var type_face: Typeface
//private var size_chosen by Delegates.notNull<Float>()
//private lateinit var colorIndicator: CardView
//
//private var whichFont = 0
//private var whichPaint = Color.BLACK
//private var whichProgress = 20

//val tf: Typeface = Typeface.DEFAULT
//type_face = tf
//paint_chosen = Color.parseColor("#000000")      //Default color
//size_chosen = 20f
//// colorIndicator = root.colorIndicator

//        root.rel_layout.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.2f));
//        val colors = root.chooseColor
//        val font = root.chooseFont
//        val size = root.chooseSize


//        colors.setOnClickListener {
//            chooseColor()
//        }
//        font.setOnClickListener {
//            chooseFont()
//        }
//        size.setOnClickListener {
//            chooseSizeText()
//        }
//        colorIndicator.setOnClickListener {
//            chooseColor()
//        }


//    private fun chooseSizeText() {
//        //Use a seek bar
//
//        val popDialog =
//            AlertDialog.Builder(this)
//
//
//        val seek = SeekBar(this)
//        seek.max = 40
//        seek.progress = whichProgress
//        seek.keyProgressIncrement = 2
//
//        popDialog.setTitle("Select Size")
//        popDialog.setView(seek)
//        popDialog.setMessage("Choose a size")
//
//        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seekBar: SeekBar?,
//                progress: Int,
//                fromUser: Boolean
//            ) {
//                size_chosen = progress.toFloat()
//                whichProgress = progress
//            }
//
//            override fun onStartTrackingTouch(arg0: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Log.e("Size", seek.progress.toString())
//            }
//        })
//
//        popDialog.setPositiveButton("OK",
//            object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//
//                    dialog.dismiss()
//                }
//            })
//        popDialog.create()
//        popDialog.show()
//
//
//    }

//private fun chooseFont() {

//        var typeface = Typeface.DEFAULT
//        //Choose Font
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Choose a font")
//
//        //Create List
//        val font_list = R.array.font_types
//
//        val checkedItem = whichFont     //Arial
//
//        builder.setSingleChoiceItems(
//            font_list,
//            checkedItem
//        ) { dialog, which ->
//            // user checked an item
//            whichFont = which
//            when (which) {
//                0 -> typeface = Typeface.createFromAsset(assets, "arial.ttf")
//                1 -> typeface = Typeface.createFromAsset(assets, "long_fox_font.ttf")
//                2 -> typeface = Typeface.createFromAsset(assets, "bomb_font.ttf")
//                3 -> typeface = Typeface.createFromAsset(assets, "romot_reavers_font.ttf")
//                4 -> typeface = Typeface.createFromAsset(assets, "fonty_font.ttf")
//            }
//            type_face = typeface
//
//        }
//
//        builder.setPositiveButton(
//            "OK"
//        ) { dialog, which ->
//
//
//            when (which) {
//                0 -> typeface = Typeface.createFromAsset(assets, "arial.ttf")
//                1 -> typeface = Typeface.createFromAsset(assets, "long_fox_font.ttf")
//                2 -> typeface = Typeface.createFromAsset(assets, "bomb_font.ttf")
//                3 -> typeface = Typeface.createFromAsset(assets, "romot_reavers_font.ttf")
//                4 -> typeface = Typeface.createFromAsset(assets, "fonty_font.ttf")
//            }
//            type_face = typeface
//            Log.e("Font", typeface.toString())
//            dialog.dismiss()
//
//        }
//        builder.setNegativeButton("Cancel", null)
//
//        val dialog = builder.create()
//        dialog.show()
//
//
//    }

//   private fun chooseColor() {
//
//        this.let {
//            MaterialColorPickerDialog
//                .Builder(it)                                              // Pass Activity Instance
//                .setColorShape(ColorShape.SQAURE)
//                .setTitle("Pick a color") // Default ColorShape.CIRCLE
//                .setPositiveButton("Select")
//                .setDefaultColor(whichPaint)
//                .setNegativeButton("Cancel")
//                .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
//                .setColorRes(
//
//                    resources.getIntArray(R.array.themeColors).toList()
//                )    // Pass Default Color
//                .setColorListener { color, colorHex ->
//                    // Handle Color Selection
//
//                    //Set the paint brush to be valued for this color
//                    //Pass the color hex
//                    paint_chosen = color
//                    colorIndicator.setCardBackgroundColor(color)
//                    whichPaint = color
//                    Log.e("Color", paint_chosen.toString())
//                }
//                .show()
//        }
//
//    }
