package com.example.memej.Utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


// Extension function to share save bitmap in cache directory and share
/**
 *  Converion into bitmap and sharing of image, text extensions
 *
 */

fun Activity.shareCacheDirBitmap(uri: Uri?, name: String, bitmap: Bitmap) {


    try {
        val file = File("${this.cacheDir}/images.jpeg")

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))

        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".FileProvider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"        //All types of images
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}

fun Activity.shareLinkFromWorkspace(link: String) {

    val share = Intent()
    share.apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra(Intent.EXTRA_TEXT, link)
    }

    this.startActivity(Intent.createChooser(share, "Share link: "))

    //Set on back pressed


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


    val wrapper = ContextWrapper(context)
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)

    file = File(file, "images.jpeg")

    return Uri.parse(file.absolutePath)
}