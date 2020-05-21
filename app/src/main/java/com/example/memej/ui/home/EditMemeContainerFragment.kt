package com.example.memej.ui.home


import android.app.AlertDialog
import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.R
import com.example.memej.responses.homeMememResponses.Coordinates
import com.example.memej.responses.homeMememResponses.HomeUsers
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class EditMemeContainerFragment : Fragment() {

    private lateinit var viewModel: EditMemeContainerViewModel
    private lateinit var root: View
    lateinit var arg: Bundle
    private lateinit var img: ShapeableImageView
    lateinit var edt: EditText
    private lateinit var paint_chosen: Paint
    private lateinit var type_face: Typeface

    companion object {
        fun newInstance() = EditMemeContainerFragment()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Default value of the paint is Black
        paint_chosen = setPaint("#000000")

        //Default value of typeface is Arial
        type_face = resources.getFont(R.font.arial)

        //Define values for the type face to test

        root = inflater.inflate(R.layout.edit_meme_container_fragment, container, false)
        arg = this.requireArguments()
        img = root.findViewById(R.id.imagePostEdit)
        //Set all the parameters
        initializeEditFrame(arg)
        //img = root.findViewById(R.id.post_image)
        edt = root.findViewById(R.id.lineAddedEt)
        val x1: Float = arg.getFloat("x1")
        val y1: Float = arg.getFloat("y1")
        val x2: Float = arg.getFloat("x2")
        val y2: Float = arg.getFloat("y2")
        val c1: String? = arg.getString("c1")
        //    root.rel_layout.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.2f));

        val colors = root.findViewById<MaterialTextView>(R.id.choose_color)
        val font = root.findViewById<MaterialTextView>(R.id.choose_font)


        //Choose Color
        colors.setOnClickListener {
            context?.let {
                MaterialColorPickerDialog
                    .Builder(it)                        // Pass Activity Instance
                    .setColorShape(ColorShape.SQAURE)
                    .setTitle("Pick a color")// Default ColorShape.CIRCLE
                    .setPositiveButton("Select")
                    .setNegativeButton("Cancel")
                    .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
                    .setColorRes(
                        resources.getIntArray(R.array.themeColors).toList()
                    )    // Pass Default Color
                    .setColorListener { color, colorHex ->
                        // Handle Color Selection
                        //Set the paint brush to be valued for this color
                        //Pass the color hex
                        paint_chosen = setPaint(colorHex)

                    }
                    .show()
            }

        }

        //Choose Font
        font.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Choose a font")

            //Create List
            val font_list = R.array.font_types

            val checkedItem = 0     //Arial

            builder.setSingleChoiceItems(
                font_list,
                checkedItem
            ) { dialog, which ->
                // user checked an item
                type_face = setFont(which)

            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                // user clicked OK
                //which is the int
                //What is teh global type face
                type_face = setFont(which)
                dialog.dismiss()

            }
            builder.setNegativeButton("Cancel", null)
            val dialog = builder.create()
            dialog.show()


        }

//        //How the send button features
//        if (edt.text.length != 0) {
//            root.findViewById<MaterialButton>(R.id.send_post_edit).isEnabled = true
//        }
////
//        edt.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                //Starting point
//
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                //The text hs changed, and the has been initialzed
//                //Recrete  canvas everytime
//                //Everytime a change is observed
//                val line = edt.toString()
//                val paint = setPaint("#000000")
//
//
//                Glide.with(img)
//                    .asBitmap()
//                    .load(arg.getString("image_url"))
//                    .into(object : SimpleTarget<Bitmap>() {
//                        override fun onResourceReady(
//                            resource: Bitmap,
//                            transition: Transition<in Bitmap>?
//                        ) {
//                            //Create a new bitmap
//                            val bitBeautyBitmap = createBitmap(resource)
//
//                            //First time
//
//                            var canvas = Canvas(bitBeautyBitmap)
//                            img.draw(canvas)
//                            img.setImageBitmap(bitBeautyBitmap)
//                            //On this loadd image, we have set the canvas to be true
//                            canvas.drawRect(160F, 160F, 190F, 190F, paint)
//                            canvas.drawText(line, 100F, 100F, paint)
//
//
//                        }
//                    })
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        })


//
//        Glide.with(img)
//            .asBitmap()
//            .load(arg.getString("image_url"))
//            .into(object : SimpleTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    //Create a new bitmap
//                    val bitBeautyBitmap = createBitmap(resource)
//                    canvas = Canvas(bitBeautyBitmap)
//                    img.draw(canvas)
//                    canvas.saveCount
//                    img.setImageBitmap(bitBeautyBitmap)
//                    //On this loadd image, we have set the canvas to be true
//                    val paint = setPaint("#000000")
//                    canvas.drawRect(160F, 160F, 190F, 190F, paint)
//
//
//                    val k = "Kavya"
//                    canvas.drawText(k, 100F, 100F, paint)
//
//                    s.addTextChangedListener(object : TextWatcher{
//                        override fun afterTextChanged(s: Editable?) {
//                            }
//
//                        override fun beforeTextChanged(
//                            s: CharSequence?,
//                            start: Int,
//                            count: Int,
//                            after: Int
//                        ) {
//                            }
//
//                        override fun onTextChanged(
//                            s: CharSequence?,
//                            start: Int,
//                            before: Int,
//                            count: Int
//                        ) {
//
//                            val line = s.toString()
//                            canvas.restoreToCount(1)
//                            img.setImageBitmap(bitBeautyBitmap)
//                            canvas.drawText(line, 160F, 200F, paint)
//                        }
//                    })
//
//                }
//            })


//        val staticLayout = StaticLayout.Builder
//            .obtain(text, start, end, textPaint, width)
//            .build()          //For multiline

        return root
    }

    private fun initializeEditFrame(arg: Bundle) {

        //Load image tags

        val txt = arg.getStringArrayList("tags").toString()
        val txt2 = arg.getStringArrayList("imageTags").toString()
        //This is a array basically
        txt.replace("[", "")
        txt.replace("[", "")
        txt2.replace("]", "")
        txt2.replace("]", "")
        val tags = txt + "\n" + txt2

        root.findViewById<MaterialTextView>(R.id.tagPost_edit).text = tags


        //Load Users
        val u = arg.getParcelableArrayList<HomeUsers>("users")
        var str = ""
        val i = 0

        for (i in 0 until u?.size!! - 1) {
            str += u.elementAt(i)?.username + ", "
        }
        root.findViewById<MaterialTextView>(R.id.userPost_edit).text = str


        //:Load Image with bitmaps and placeholders
        Glide.with(img)
            .asBitmap()
            .load(arg.getString("imageUrl"))
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    //When we do not use the part
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //Resource is the bitmap created
                    var bitmap = createBitmap(resource)
                    Log.e("OKAYY", "cn " + resource)
                    Log.e("OKAYY", "cn " + bitmap)

                    var canvas = Canvas(bitmap)
                    Log.e("OKAYY", "cn " + canvas)

                    img.draw(canvas)

                    img.setImageBitmap(bitmap)


                    //We have the canvas and bitmap initialized. Retrueve the previous drawings on it
                    getCompleteImage(bitmap, canvas, arg)


                }
            })


    }

    private fun getCompleteImage(
        bitmap: Bitmap?, canvas: Canvas, arg: Bundle
    ) {

        //Get the stage
        val stage = arg.getInt("stage")
        val numPlaceholders = arg.getInt("numPlaceHolders")
        //Null check (Not valid still)
        if (stage == numPlaceholders) {
            //Return something
            Toast.makeText(context, "Can not add any line here", Toast.LENGTH_SHORT).show()
        }

        //Get placeholders
        val placeHolders_array = arg.getStringArrayList("placeHolders")
        //This is an array. Get the co-ordinates from index 0 to stage-1, and fit them with paint on canvas with color black initialized
        Log.e("OKAYY", "cn " + placeHolders_array)

        //Get Co-ordinates
        val coordinates = arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")
        Log.e("OKAYY", "cn " + coordinates)

        //Generate this for every placeholder
        //Iterate from 0, stage-1
        var i = 0
        for (i in 0 until stage - 1) {
            val pl = placeHolders_array?.get(i)?.toString()
            val xi = coordinates?.elementAt(i)?.x
            val yi = coordinates?.elementAt(i)?.y
            //Add color hex code. All black now
            addInitialLines(pl, xi, yi, canvas, bitmap, arg)

        }
        Log.e("OKAYY", "cImage Complete")

        //Add a listener on the text changed listener


    }

    private fun addInitialLines(
        pl: String?,
        xi: Int?,
        yi: Int?,
        canvas: Canvas,
        bitmap: Bitmap?,
        arg: Bundle
    ) {

        //Draw text on the canvas
        //Take a paint
        val paint = setPaint("#FFFFFF")
        xi?.toFloat()?.let {
            yi?.toFloat()?.let { it1 ->
                if (pl != null) {
                    canvas.drawText(pl, it, it1, paint)
                }
            }
        }

    }

    private fun observeText(line: String, canvas: Canvas) {

        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Recrete  canvas everytime
                val line = s.toString()
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                val paint = setPaint("#000000")
                canvas.drawText(line, 160F, 160F, paint)

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }


    private fun applyFilter() {
//      Grey Scaled Matrix
        //Array for ARGB Color Filtes
        val invertMatrix: FloatArray = floatArrayOf(
            -1F, 0F, 0F, 0F, 255F,
            0F, -1F, 0F, 0F, 255F,
            0F, 0F, -1F, 0F, 255F,
            0F, 0F, 0F, 1F, 0F
        )


    }

    //Function to get paint again. This will be true for the value default color and the one chosen by the user
    private fun setPaint(color: String): Paint {
        //Where color is the string in #HEX code
        val paint = Paint()
        paint.color = Color.parseColor(color)
        paint.strokeWidth = 15F                                   //Standard Chosen
        paint.style = Paint.Style.FILL                            //Default to be set
        paint.isAntiAlias = true
        paint.isDither = true


        return paint
    }

    private fun setSize(size: Int): Float {
        val size_req = size.toFloat()
        return size_req

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFont(id: Int): Typeface {
        //Takes int
        val i = id
        var type: Typeface = resources.getFont(R.font.arial)
        when (i) {

            0 -> type = resources.getFont(R.font.arial)
            1 -> resources.getFont(R.font.long_fox_font)
            2 -> resources.getFont(R.font.bomb_font)
            3 -> resources.getFont(R.font.romot_reavers_font)
            4 -> resources.getFont(R.font.fonty_font)
        }
        return type        //Equal to the type

    }

    private fun getColorWithAlpha(color: Int, ratio: Float): Int {
        var newColor = 0
        val alpha = Math.round(Color.alpha(color) * ratio).toInt()
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        newColor = Color.argb(alpha, r, g, b)
        return newColor
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditMemeContainerViewModel::class.java)
    }

}
