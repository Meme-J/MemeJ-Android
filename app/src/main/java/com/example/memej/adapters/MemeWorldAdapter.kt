package com.example.memej.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Keep
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeWorld
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.Utils.shareCacheDirBitmap
import com.example.memej.Utils.ui.ConversionUtil
import com.example.memej.body.likeMemeBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.Photo
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.like.LikeButton
import com.like.OnLikeListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Keep
class MemeWorldAdapter(val context: Context, val itemClickListener: OnItemClickListenerMemeWorld) :
    PagedListAdapter<Meme_World, MemeWorldAdapter.MyViewHolder>(DiffUtilsMemeWorld()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_meme_world, parent, false)
        return MyViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        getItem(position)?.let { holder.bindPost(it, itemClickListener) }
        holder.setIsRecyclable(false)
    }


    class MyViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        val sessionManager =
            SessionManager(context)
        val service = RetrofitClient.makeCallsForMemes(context)

        val memeTime = itemView.findViewById<MaterialTextView>(R.id.meme_timestamp)
        val likeDrawIo = itemView.findViewById<LikeButton>(R.id.starBtnMeme)
        val photoView = itemView.findViewById<ImageEditorView>(R.id.photoViewMemeWorld)
        val numLikes = itemView.findViewById<TextView>(R.id.num_likes_memeAdapter)

        val share = itemView.findViewById<Button>(R.id.shareComplete)

        fun bindPost(_meme: Meme_World, clickListener: OnItemClickListenerMemeWorld) {

            with(_meme) {

                //TimeStamp

                memeTime.text =
                    ConversionUtil.convertTimeToEpoch(_meme.lastUpdated)             //To get the tag


                val user_likers = _meme.likedBy


                likeDrawIo.isLiked = user_likers.isNotEmpty()




                photoView.source?.let {
                    Glide.with(itemView.context)
                        .load(_meme.templateId.imageUrl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .dontTransform()
                        .error(R.drawable.icon_placeholder)
                        .into(it)
                }

                getCompleteImage(_meme, photoView, itemView.context)
                numLikes.text = _meme.likes.toString()

                //Like the meme
                likeDrawIo.setOnLikeListener(object : OnLikeListener {
                    override fun liked(likeButton: LikeButton?) {
                        // numLikes.text = (numLikes.text.toString().toInt() + 1).toString()
                        //Disable this button
                        likeDrawIo.isEnabled = false
                        likeMeme(_meme)

                    }

                    override fun unLiked(likeButton: LikeButton?) {
//                        numLikes.text = (numLikes.text.toString().toInt() - 1).toString()
                        likeDrawIo.isEnabled = false
                        likeMeme(_meme)
                    }
                })

                share.setOnClickListener {

                    //  val imageName = arg.getString("imageName") + "_" + arg.getString("lastUpdated")
                    // val imagePath = imageName + ".jpg"

                    Log.e("Share from main", "In share00")

                    val map = ConvertToBitmap(photoView)
                    val file = File(itemView.context.externalCacheDir, "images.png")
                    val uri = Uri.fromFile(file)

                    (itemView.context as Activity).shareCacheDirBitmap(uri, "images", map)


                }

                itemView.setOnClickListener {
                    clickListener.onItemClicked(_meme)
                    //This listener is for the whole item
                }
            }
        }

        private fun ConvertToBitmap(layout: ImageEditorView): Bitmap {

            var map: Bitmap?
            layout.isDrawingCacheEnabled = true
            layout.buildDrawingCache()
            return layout.drawingCache.also({ map = it })
        }

        private fun likeMeme(_meme: Meme_World) {


            //Revert the state
            Log.e("ADapter", "In like meme")
            val inf = likeMemeBody(_meme._id)
            service.likeMeme(
                inf,
                accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
            )
                .enqueue(object : Callback<LikeOrNotResponse> {
                    override fun onFailure(call: Call<LikeOrNotResponse>, t: Throwable) {
                        //Not able to get
                        val message = ErrorStatesResponse.returnStateMessageForThrowable(t)
                        val snack = Snackbar.make(itemView, message, Snackbar.LENGTH_SHORT)
                        snack.show()


                        //Revert with the state usage
                        //Do nothing with the number of likes
                        if (likeDrawIo.isLiked) {
                            likeDrawIo.isLiked = false
                        } else if (!likeDrawIo.isLiked) {
                            likeDrawIo.isLiked = true
                        }


                    }

                    override fun onResponse(
                        call: Call<LikeOrNotResponse>,
                        response: Response<LikeOrNotResponse>
                    ) {


                        if (response.body()?.msg == "Meme unliked successfully.") {

                            Log.e("ADapter", "In response, meme unliked")

                            likeDrawIo.isLiked = false
                            numLikes.text = (numLikes.text.toString().toInt() - 1).toString()
                            likeDrawIo.isEnabled = true
                            //Refresh the screen again

                        } else if (response.body()?.msg == "Meme liked successfully.") {


                            Log.e("ADapter", "In  Like")
                            likeDrawIo.isLiked = true
                            numLikes.text = (numLikes.text.toString().toInt() + 1).toString()
                            likeDrawIo.isEnabled = true
                            //Refresh the screen

                        }
                    }
                })
        }

        private fun getCompleteImage(
            _homeMeme: Meme_World,
            photoView: ImageEditorView,
            context: Context
        ) {

            //Builder
            val photoVieGlobal = Photo.Builder(context, photoView)
                .setPinchTextScalable(false)
                .build()

            val holders = _homeMeme.templateId.numPlaceholders
            val c = 2 * holders - 1

            for (i in 0..c step 2) {


                val color = _homeMeme.templateId.textColorCode.elementAt(i / 2)
                val size = _homeMeme.templateId.textSize.elementAt(i / 2)
                val colorInt = Color.parseColor(color)


                val pl = _homeMeme.placeholders[i / 2]

                val x1 =
                    _homeMeme.templateId.coordinates.elementAt(i).x
                val y1 =
                    _homeMeme.templateId.coordinates.elementAt(i).y

                val x2 =
                    _homeMeme.templateId.coordinates.elementAt(i + 1).x
                val y2 =
                    _homeMeme.templateId.coordinates.elementAt(i + 1).y

                photoVieGlobal.addOldText(null, pl, colorInt, size.toFloat(), x1, y1, x2, y2)
            }


        }


    }


}

interface OnItemClickListenerMemeWorld {
    fun onItemClicked(_meme: Meme_World)
}


