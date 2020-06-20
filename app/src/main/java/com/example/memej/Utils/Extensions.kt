package com.example.memej.Utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


// Extension function to share save bitmap in cache directory and share
fun Activity.shareCacheDirBitmap(uri: Uri?, name: String, bitmap: Bitmap) {


    try {
        val file = File("${this.cacheDir}/images.jpeg")

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))

        Log.e("PAckage name", this.packageName)

        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".FileProvider", file)
        Log.e("PAckage name", contentUri.toString())
//
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"        //All types of images
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        Log.e("Fail share", e.toString())
    }
}




//toString
//// Extension property to get bitmap from view
//val View.bitmap: Bitmap
//    get() {
//        // Screenshot taken for the specified root view and its child elements.
//        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        this.draw(canvas)
//        return bitmap
//    }


fun Bitmap.getImageUri(inContext: Context, inImage: Bitmap): Uri? {

    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)

}


// Extension method to save bitmap to internal storage
fun Bitmap.saveToInternalStorage(context: Context, name: String): Uri {
    // Get the context wrapper instance
    val wrapper = ContextWrapper(context)

    // Initializing a new file
    // The bellow line return a directory in internal storage

//    val directory = File(
//        Environment.getExternalStorageDirectory()
//            .toString() + "/memeJ"
//    )
//
//    if (!directory.exists()) {
//        directory.mkdirs()
//    }

    var file = wrapper.getDir("images", Context.MODE_PRIVATE)

    Log.e("In extensions", file.path.toString())

    // Create a file to save the image, random file name
    //file = File(file, "${UUID.randomUUID()}.png")

    file = File(file, "images.jpeg")
    Log.e("In extensions", file.path.toString())

    //Dont doiwnload from here
//    try {
//        // Get the file output stream
//        val stream: OutputStream = FileOutputStream(file)
//
//        // Compress bitmap
//        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
//
//        // Flush the stream
//        stream.flush()
//
//        // Close stream
//        stream.close()
//    } catch (e: IOException) { // Catch the exception
//        e.printStackTrace()
//    }

    // Return the saved image uri
    return Uri.parse(file.absolutePath)
}