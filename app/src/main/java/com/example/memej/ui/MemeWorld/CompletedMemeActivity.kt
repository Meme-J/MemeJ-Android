package com.example.memej.ui.MemeWorld

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.Utils.shareCacheDirBitmap
import com.example.memej.adapters.TagAdapter
import com.example.memej.adapters.UserAdapter
import com.example.memej.adapters.onTagClickType
import com.example.memej.adapters.onUserClickType
import com.example.memej.databinding.ActivityCompletedMemeBinding
import com.example.memej.entities.likeMemeBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.memeWorldResponses.Coordinate
import com.example.memej.responses.memeWorldResponses.User
import com.example.memej.textProperties.ConversionUtil
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.Photo
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_completed_meme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class CompletedMemeActivity : AppCompatActivity(), onUserClickType, onTagClickType {


    lateinit var arg: Bundle
    lateinit var memeUrl: String
    lateinit var memeCanvasUrl: String
    lateinit var rvTag: RecyclerView
    lateinit var rvUser: RecyclerView

    //    lateinit var image: LikeButton
//    lateinit var likes: TextView
    private val preferenceUtils = PreferenceUtil

    //private val viewmodel: CompletdMemeViewModel by viewModels()
    lateinit var bmp: Bitmap
    lateinit var photoView: ImageEditorView
    lateinit var photoGlobalEditor: Photo
    lateinit var progressDownload: ProgressDialog


    //RequestCode
    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101
    lateinit var root: ActivityCompletedMemeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Root is the binding elemnt


        root = DataBindingUtil.setContentView(this, R.layout.activity_completed_meme)

        arg = intent?.getBundleExtra("bundle")!!
        memeUrl = arg.getString("imageUrl").toString()
        val memeId = arg.getString("id")       //This was the meme id
        rvTag = root.rvCompleteUserTag
        rvUser = root.rvCompleteUser
//        image = root.starButton
        photoView = root.photoView
//        likes = root.numLikesCompletedMeme
        val download = root.downloadCompletedMeme
        val share = root.shareCompletedMeme
        progressDownload = ProgressDialog(this)


        //Get the Corners of the image View
        //Initialzie the global view
        val x2 = photoView.layoutParams.width
        val y2 = photoView.layoutParams.height
        Log.e("This width", x2.toString() + y2.toString())


        photoGlobalEditor = Photo.Builder(this, photoView)
            .setPinchTextScalable(false)
            .build()


        initalizeTheMemePost()


        //Check its children
        Log.e("This", photoGlobalEditor.childCount.toString())


        //Like the meme
//        image.setOnLikeListener(object : OnLikeListener {
//            override fun liked(likeButton: LikeButton?) {
//                likeDislike(memeId)
//                //Observer on the number of likes
//            }
//
//            override fun unLiked(likeButton: LikeButton?) {
//                likeDislike(memeId)
//            }
//        })


        //Download image
        download.setOnClickListener {
            downloadCompletedMeme()
        }

        //Share image
        share.setOnClickListener {
            shareMeme()
        }


    }

    @SuppressLint("SetWorldReadable")
    private fun shareMeme() {

        //Get layout
        val imageName = arg.getString("imageName") + "_" + arg.getString("lastUpdated")
        val imagePath = imageName + ".jpg"

        Log.e("Share", "In share00")

        val map = ConvertToBitmap(photoView)
        val file = File(externalCacheDir, "images.png")
        val uri = Uri.fromFile(file)

        this.shareCacheDirBitmap(uri, "images", map)


    }

    protected fun ConvertToBitmap(layout: ImageEditorView): Bitmap {

        var map: Bitmap?
        layout.isDrawingCacheEnabled = true
        layout.buildDrawingCache()
        return layout.drawingCache.also({ map = it })
    }


    private fun downloadCompletedMeme() {
        checKPermissions()

    }


    private fun downloadMeme() {


        val directory = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/" + getString(R.string.app_name)
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }


        //Download Manager

        val time: Date = Calendar.getInstance().time
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val imageName = arg.getString("imageName") + "_" + time.toString()
        val dirPath = directory.path
        val imagePath = dirPath + imageName + ".jpg"

        Log.e(
            "Directories",
            dirPath.toString() + directory.totalSpace.toString() + imagePath.toString()
        )




        photoGlobalEditor.saveImage(imagePath, object : Photo.OnSaveListener {
            override fun onSuccess(imagePath: String?) {
                super.onSuccess(imagePath)
                Log.e("Save", "In success")
                val snack = Snackbar.make(
                    container_completeMeme,
                    "Meme will be downloaded successfully",
                    Snackbar.LENGTH_SHORT
                )
                snack.show()

            }

            override fun onFailure(exception: Exception?) {
                super.onFailure(exception)

                Log.e("failed download", exception.toString())
                val snack = Snackbar.make(
                    container_completeMeme,
                    "Unable to download this meme",
                    Snackbar.LENGTH_SHORT
                )
                snack.show()

            }

        })

    }

    private fun checKPermissions() {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permissions to access the device storage")
                    .setPositiveButton("Accept") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {

            downloadMeme()

        }

    }
    //##
    //Is liked or not? Or a view model to obeserve a liked meme

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadMeme()

                } else {
                    val snack = Snackbar.make(
                        container_completeMeme,
                        "Permission denied",
                        Snackbar.LENGTH_LONG
                    )
                    snack.show()
                }
                return
            }
            else -> {

            }
        }
    }


    private fun likeDislike(memeId: String?) {

        val ctx = ApplicationUtil.getContext()
        val service = RetrofitClient.makeCallsForMemes(ctx)
        val sessionManager =
            SessionManager(ctx)

        Log.e("Like", "InLike")
        val inf = likeMemeBody(
            memeId.toString()
        )


        service.likeMeme(
            inf,
            accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
        )
            .enqueue(object : Callback<LikeOrNotResponse> {
                override fun onFailure(call: Call<LikeOrNotResponse>, t: Throwable) {
                    val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                    val snack =
                        Snackbar.make(container_completeMeme, message, Snackbar.LENGTH_SHORT)
                    snack.show()

                    //Revert with the state usage
//                    if (image.isLiked) {
//                        image.isLiked = false
//                    } else if (!image.isLiked) {
//                        image.isLiked = true
//                    }


                }

                override fun onResponse(
                    call: Call<LikeOrNotResponse>,
                    response: Response<LikeOrNotResponse>
                ) {
                    //Get the response
                    if (response.isSuccessful) {
                        if (response.body()?.msg == "Meme unliked successfully.") {

//                            likes.text =
//                                arg.getString("likes")
//                            likes.setTextColor(Color.GRAY)
//
//
//                            image.isLiked = false

                        } else if (response.body()?.msg == "Meme liked successfully.") {

//                            likes.text =
//                                arg.getString("likes")
//
//                            Log.e("Like Dislike", "like success")
//                            //Set drawable into workin
//                            likes.setTextColor(Color.RED)
//                            image.isLiked = true

                        }

                    }

                }
            })

        Log.e("Like", "EndLike")
    }


    private fun initalizeTheMemePost() {

        //Set the tags
        //Layout Manager
        val HorizontalLayout: LinearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        //Load image tags

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
        val rvTag = root.rvCompleteUserTag
        val tagAdapter = TagAdapter(this)
        tagAdapter.tagType = tagsStr
        rvTag.layoutManager = HorizontalLayout
        rvTag.adapter = tagAdapter


        //Populate the users in the same way
        val u = arg.getParcelableArrayList<User>("users")
        val userStr = mutableListOf<String>()
        for (i in u!!) {
            userStr.add(i.username)
        }

        //SecondLayout
        val HorizontalUser = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val rvUser = root.rvCompleteUser
        val userAdater = UserAdapter(this)
        userAdater.userType = userStr
        rvUser.layoutManager = HorizontalUser
        rvUser.adapter = userAdater

        //Set time stamp
        val strm = arg.getString("lastUpdated")
        root.timeStampMemeW.text = ConversionUtil.convertTimeToEpoch(strm.toString())

        getImage()

    }


    private fun getImage() {

        photoView.source?.let {
            Glide.with(this)
                .load(arg.getString("imageUrl"))
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(it)
        }

        getCompleteImage()

    }


    private fun getCompleteImage() {

        //Extract every placeholder, color, textSize
        //Use the num of placeholders in the  meme image to view those
        //Set paint and size
        val totalPlaces = arg.getInt("numPlaceholders")
        val c = 2 * totalPlaces - 1

        Log.e("Complete", totalPlaces.toString())

        for (i in 0..c step 2) {
            Log.e("Complete", "In for")


            val color = arg.getStringArrayList("textColor")!!.elementAt(i / 2)
            val size = arg.getIntegerArrayList("textSize")!!.elementAt(i / 2)
            val colorInt = Color.parseColor(color)


            val pl = arg.getStringArrayList("placeholders")!![i / 2]

            val x1 =
                arg.getParcelableArrayList<Coordinate>("templateIdCoordinates")!!.elementAt(i).x
            val y1 =
                arg.getParcelableArrayList<Coordinate>("templateIdCoordinates")!!.elementAt(i).y

            val x2 =
                arg.getParcelableArrayList<Coordinate>("templateIdCoordinates")!!
                    .elementAt(i + 1).x
            val y2 =
                arg.getParcelableArrayList<Coordinate>("templateIdCoordinates")!!
                    .elementAt(i + 1).y

            Log.e("Complete", color.toString() + x1.toString() + size.toString())

            photoGlobalEditor.addOldText(null, pl, colorInt, size.toFloat(), x1, y1, x2, y2)
        }

        Log.e("Complete", photoView.childCount.toString())


    }


    override fun getUserType(_user: String) {

    }

    override fun getTagType(_tag: String) {

    }


}




