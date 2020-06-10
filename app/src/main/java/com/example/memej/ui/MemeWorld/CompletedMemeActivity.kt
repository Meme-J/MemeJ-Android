package com.example.memej.ui.MemeWorld

import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.withTranslation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.Instances.LoadImage
import com.example.memej.Instances.UserInstance
import com.example.memej.R
import com.example.memej.Utils.SessionManager
import com.example.memej.adapters.TagAdapter
import com.example.memej.adapters.UserAdapter
import com.example.memej.adapters.onTagClickType
import com.example.memej.adapters.onUserClickType
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.homeMememResponses.Coordinates
import com.example.memej.responses.homeMememResponses.HomeUsers
import com.example.memej.responses.memeWorldResponses.User
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_base_host.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CompletedMemeActivity : Fragment(), onUserClickType, onTagClickType {

    lateinit var root: View
    lateinit var arg: Bundle
    lateinit var memeUrl: String
    lateinit var memeCanvasUrl: String
    lateinit var rvTag: RecyclerView
    lateinit var rvUser: RecyclerView
    lateinit var image: ImageView
    lateinit var likes: TextView

    //RequestCode
    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.activity_completed_meme, base_activty_host, false)
        arg = this.requireArguments()
        memeUrl = arg.getString("imageUrl").toString()
        rvTag = root.findViewWithTag(R.id.rv_complete_user_tag)
        rvUser = root.findViewWithTag(R.id.rv_complete_user)
        image = root.findViewById<ImageView>(R.id.like_completed_meme_btn)
        likes = root.findViewById<TextView>(R.id.num_likes_completed_meme)

        initalizeTheMemePost()


        root.findViewById<ImageView>(R.id.like_completed_meme_btn).setOnClickListener {
            likeDislike()
        }



        root.findViewById<Button>(R.id.share_completed_meme).setOnClickListener {
            //Share this meme
            //Convert the image into bitmap
            val FILE_PROVIDER = "com.example.memej.FileProvider"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            //SHare title
            intent.putExtra(Intent.EXTRA_TITLE, "Share with:")

            //get the loaclly saved url
            val uri = Uri.parse(memeUrl.toString())
            intent.putExtra(Intent.EXTRA_STREAM, uri)

            intent.clipData = ClipData.newUri(
                context?.contentResolver,
                context?.getString(R.string.app_name),
                context?.let { it1 ->
                    FileProvider.getUriForFile(
                        it1, FILE_PROVIDER,
                        File(memeUrl)
                    )
                }
            )
            //Permission Flags
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            Log.e("Share", "In sharing" + intent.extras.toString())

            context?.startActivity(Intent.createChooser(intent, null))

        }

//        root.findViewById<Button>(R.id.download_completed_meme).setOnClickListener {
//            //Set downloaders
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                askPermissions()
//            }
//            //else {
//            //downloadMeme()
//            //        downloadImage(memeUrl)
//            //  }
//
//        }

        return root


    }

    private fun downloadMeme() {

        //Create a directory to store the image
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

//        val memeImage = root.findViewById<ShapeableImageView>(R.id.post_image_mw)
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //this one
//                put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
//                put(MediaStore.MediaColumns.IS_PENDING, 1)
//            }
//        }
    }


    private fun likeDislike() {

        val service = RetrofitClient.makeCallsForMemes(requireContext())
        val sessionManager = SessionManager(requireContext())

        service.likeMeme(
            arg.getString("id").toString(),
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<LikeOrNotResponse> {
                override fun onFailure(call: Call<LikeOrNotResponse>, t: Throwable) {
                    //Not able to get
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<LikeOrNotResponse>,
                    response: Response<LikeOrNotResponse>
                ) {
                    //Get the response
                    if (response.body()?.msg == "Meme unliked successfully.") {

                        likes.text =
                            arg.getString("likes")
                        likes.setTextColor(Color.GRAY)

                        //Set drawable into working
                        image.setImageResource(R.drawable.ic_like_empty)
                        image.setBackgroundResource(R.drawable.ic_like_empty)


                    } else if (response.body()?.msg == "Meme liked successfully.") {

                        likes.text =
                            arg.getString("likes")

                        //Set drawable into workin
                        likes.setTextColor(Color.RED)
                        image.setImageResource(R.drawable.ic_favorite)
                        image.setBackgroundResource(R.drawable.ic_favorite)

                    }

                }
            })
    }


    private fun initalizeTheMemePost() {

        //Set the tags
        val txt = arg.getStringArrayList("tags")
        val txt2 = arg.getStringArrayList("imageTags")

        //This is a array basically
        //Create an array list  to call these tags

        //Check for these being blank in case of initialization/add meme
        val tagsStr = mutableListOf<String>()
        for (i in txt!!) {
            tagsStr.add(i)
        }
        for (i in txt2!!) {
            tagsStr.add(i)
        }

        //Get the rv and adapter for the user and the tags already existing
        val rvTag = root.findViewById<RecyclerView>(R.id.rv_edit_tag)
        val tagAdapter = TagAdapter(this)
        tagAdapter.tagType = tagsStr
        rvTag.layoutManager = GridLayoutManager(context, 2)
        rvTag.adapter = tagAdapter


        //Populate the users in the same way
        val u = arg.getParcelableArrayList<HomeUsers>("users")
        val userStr = mutableListOf<String>()
        for (i in u!!) {
            userStr.add(i.username)
        }

        val rvUser = root.findViewById<RecyclerView>(R.id.rv_edit_user)
        val userAdater = UserAdapter(this)
        userAdater.userType = userStr
        rvUser.layoutManager = GridLayoutManager(context, 2)
        rvUser.adapter = userAdater


        //Set time stamp
        root.findViewById<MaterialTextView>(R.id.timeStamp_memeW).text =
            arg.getString("lastUpdated")

        //Set Image
        Glide.with(this)
            .asBitmap()
            .load(arg.getString("imageUrl"))
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val memeImage = root.findViewById<ShapeableImageView>(R.id.post_image_mw)
                    val canvas = Canvas(resource)

                    memeImage.draw(canvas)

                    memeImage.setImageBitmap(resource)
                    getCompleteImage(resource, canvas)
                }
            })


        //Check if the post is liked already or not
        val userInstance = UserInstance(requireContext())


        //Check if you have liked the meme or not previously
        val user_likers = arg.getParcelableArrayList<User>("likedBy")
        if (user_likers != null) {
            if (user_likers.contains(userInstance)) {
                //Color and number and image
                //Already liked by the person


                likes.text =
                    arg.getString("likes")

                likes.setTextColor(Color.RED)
                //Set drawable into working
                image.setImageResource(R.drawable.ic_favorite)
                image.setBackgroundResource(R.drawable.ic_favorite)


            } else {


                likes.text =
                    arg.getString("likes")

                //Set drawable into working
                image.setImageResource(R.drawable.ic_like_empty)
                image.setBackgroundResource(R.drawable.ic_like_empty)

            }
        }

    }


    private fun getCompleteImage(bitmap: Bitmap?, canvas: Canvas) {

        //Extract every placeholder, color, textSize
        //Use the num of placeholders in the  meme image to view those
        //Set paint and size

        val placeHolders_array = arg.getStringArrayList("placeHolders")
        //This is an array. Get the co-ordinates from index 0 to stage-1, and fit them with paint on canvas with color black initialized

        //Get Co-ordinates
        val coordinates = arg.getParcelableArrayList<Coordinates>("templateIdCoordinates")

        for (i in 0..arg.getInt("numPlaceHolders") - 1) {
            val paint = LoadImage().getTextPaint(
                arg.getStringArrayList("textColor")?.get(i).toString(),
                arg.getIntegerArrayList("textSize")?.get(i)!!.toInt()
            )

            val pl = placeHolders_array?.get(i)?.toString()
            val xi = coordinates?.elementAt(i)?.x
            val yi = coordinates?.elementAt(i)?.y

            //Draw The static Multiliner
            if (xi != null) {
                if (yi != null) {
                    canvas.drawMultilineText(pl.toString(), paint, 400, xi.toFloat(), yi.toFloat())
                }
            }
        }

        canvas.save()
    }


    fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        textDir: TextDirectionHeuristic = TextDirectionHeuristics.LTR,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
        breakStrategy: Int = Layout.BREAK_STRATEGY_SIMPLE,
        hyphenationFrequency: Int = Layout.HYPHENATION_FREQUENCY_NONE
    ) {


        val staticLayout = StaticLayout.Builder
            .obtain(text, start, end, textPaint, width)
            .setAlignment(alignment)
            .setTextDirection(textDir)
            .setLineSpacing(spacingAdd, spacingMult)
            .setBreakStrategy(breakStrategy)
            .build()
        staticLayout.draw(this, x, y)
    }

    fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {


        canvas.withTranslation(x, y) {
            draw(this)
        }
    }

    override fun getUserType(_user: String) {

    }

    override fun getTagType(_tag: String) {

    }


}


//    // binding.downloadCompletedMeme.setOnClickListener {
////Set a directory to set the pictures
//
////
//
//
////    //
////    private fun downloadImage(imageUrl: String) {
////
////        val directory = File(Environment.DIRECTORY_PICTURES)
////
////        if (!directory.exists()) {
////            directory.mkdirs()
////        }
////
////        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
////
////        //Pass the url image to be downloaded
////        //val downloadUri = Uri.parse(url)
////
//////        val url2 =
//////            "data:image/jpeg;base64,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"
////        val downloadUri = Uri.parse(imageUrl)
////
////        //Set the name to meme Template and not this. This will through the name of the meme.
////        val name = arg.getString("imageName")
////        val request = DownloadManager.Request(downloadUri).apply {
////            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
////                .setAllowedOverRoaming(false)
////                .setTitle(name?.substring(name.lastIndexOf("/") + 1))
////                .setDescription("")
////                .setDestinationInExternalPublicDir(
////                    directory.toString(),
////                    name?.substring(name.lastIndexOf("/") + 1)
////                )
////        }
////        val downloadId = downloadManager.enqueue(request)
////        val query = DownloadManager.Query().setFilterById(downloadId)
////        Thread(Runnable {
////            var downloading = true
////            while (downloading) {
////                val cursor: Cursor = downloadManager.query(query)
////                cursor.moveToFirst()
////                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
////                    downloading = false
////                }
////                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
////                if (DownloadManager.STATUS_SUCCESSFUL) {
////                    this.runOnUiThread {
////                        Toast.makeText(this,DownloadManager.STATUS_SUCCESSFUL , Toast.LENGTH_SHORT).show()
////                    }
////                }
////
////                cursor.close()
////            }
////        }).start()
//
//    }
//
//    private fun statusMessage(url: String, directory: File, status: Int): String? {
//        var msg = ""
//        msg = when (status) {
//            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
//            DownloadManager.STATUS_PAUSED -> "Paused"
//            DownloadManager.STATUS_PENDING -> "Pending"
//            DownloadManager.STATUS_RUNNING -> "Downloading..."
//            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
//                url.lastIndexOf("/") + 1
//            )
//            else -> "There's nothing to download"
//        }
//        return msg
//
//
//    }
//
//    private fun askPermissions() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    context as Activity,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//            ) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                AlertDialog.Builder(context)
//                    .setTitle("Permission required")
//                    .setMessage("Permissions to access the device storage")
//                    .setPositiveButton("Accept") { dialog, id ->
//                        ActivityCompat.requestPermissions(
//                            context as Activity,
//                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
//                        )
//                    }
//                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
//                    .show()
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(
//                    context as Activity,
//                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
//                )
//                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//
//            }
//        } else {
//            // Permission has already been granted
//            //Show progress dialog
//            val pd = ProgressDialog(context)
//            pd.setMessage("Downloading image, please wait ...")
//            pd.isIndeterminate = true
//            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
//            pd.setCancelable(true)
//            pd.setProgressNumberFormat("%1d KB/%2d KB")
//
//
//            downloadImage(memeUrl)
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // permission was granted, yay!
//                    // Download the Image
//                    downloadImage(memeUrl)
//                } else {
//
//                }
//                return
//            }
//            // Add other 'when' lines to check for other
//            // permissions this app might request.
//            else -> {
//                // Ignore all other requests.
//            }
//        }
//    }


