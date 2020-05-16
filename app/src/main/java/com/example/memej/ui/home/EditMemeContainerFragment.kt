package com.example.memej.ui.home


import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.memej.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class EditMemeContainerFragment : Fragment() {

    private lateinit var viewModel: EditMemeContainerViewModel
    private lateinit var root: View
    lateinit var arg: Bundle
    private lateinit var img: ShapeableImageView
    lateinit var s: EditText


    companion object {
        fun newInstance() = EditMemeContainerFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.edit_meme_container_fragment, container, false)
        arg = this.requireArguments()
        img = root.findViewById(R.id.post_image)
        root.findViewById<MaterialTextView>(R.id.post_tag).text = arg.getString("tag")
        s = root.findViewById(R.id.lineAddedEt)
        val x1: Float = arg.getFloat("x1")
        val y1: Float = arg.getFloat("y1")
        val x2: Float = arg.getFloat("x2")
        val y2: Float = arg.getFloat("y2")
        val c1: String? = arg.getString("c1")
        Log.e("K", "In Draw Text Box")

        //Initialze a paint object for the background
        val paint = Paint()
        paint.color = Color.parseColor(c1)
        paint.strokeWidth = 15F                                   //Standard Chosen
        paint.style = Paint.Style.FILL                            //Default to be set
        paint.isAntiAlias = true
        paint.isDither = true

        /*
        //Load image from the image and store in glide
        //Initialze the bitmap
        bitmap = Bitmap.createBitmap(300,300,Bitmap.Config.ALPHA_8)
//        img.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)
//        root.draw(canvas)
        Glide.with(this)
            .asBitmap()
            .centerCrop()
            .load(arg.getString("image_url"))
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    img.setImageBitmap(resource)
                    canvas.drawBitmap(resource, Matrix(), paint)

                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })*/


        Log.e("K", "Paint is {$c1} and s is {$s}")

        //Load image
        Glide.with(this)
            .load(arg.getString("image_url"))
            .into(img)


        //Img is the holder image now


//        val staticLayout = StaticLayout.Builder
//            .obtain(text, start, end, textPaint, width)
//            .build()          //For multiline
        s.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Recrete  canvas everytime
                val line = s.toString()
                // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                //1 is the gresId
                val bmp = drawTextToBitmap(context, line)
                val can = Canvas(bmp)
                can.drawText(line, 160F, 160F, paint)

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        //Try a rect
        //   canvas.drawRect(0F, 0F, 80F, 80F, paint)


        return root
    }

    private fun drawTextToBitmap(context: Context?, line: String): Bitmap {
        //Use res Id as the holder of image view
        val res = context?.resources
        val scale = res?.displayMetrics?.density
        var bitmap = BitmapFactory.decodeResource(res, R.id.post_image)

        if (bitmap == null) {
            //Not able to encode image
            bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        }
        var bitmapConfig = bitmap.config
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }


        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true)
//        val canvas = Canvas(bitmap)
//        // new antialised Paint
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.color = Color.parseColor("#FFFFFF")
//        paint.textSize = 15F

//        val fontFace = ResourcesCompat.getFont(requireContext(), R.font.acrobat)

        //Clear the canvas before drawing
/*
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawText(line, 140F, 140F, paint)
*/

        return bitmap
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditMemeContainerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
