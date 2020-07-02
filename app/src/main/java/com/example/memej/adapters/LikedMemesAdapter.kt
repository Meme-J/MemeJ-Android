package com.example.memej.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Keep
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeWorld
import com.example.memej.Utils.ErrorStatesResponse
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.likeMemeBody
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

@Keep
class LikedMemesAdapter(val context: Context, val itemClickListener: OnItemClickListenerLikeMeme) :
    PagedListAdapter<Meme_World, LikedMemesAdapter.MyViewHolder>(DiffUtilsMemeWorld()) {


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
        private val preferenceUtils = PreferenceUtil

        val memeTime = itemView.findViewById<MaterialTextView>(R.id.meme_timestamp)
        val likeDrawIo = itemView.findViewById<LikeButton>(R.id.starBtnMeme)
        val photoView = itemView.findViewById<ImageEditorView>(R.id.photoViewMemeWorld)
        val numLikes = itemView.findViewById<TextView>(R.id.num_likes_memeAdapter)


        fun bindPost(_meme: Meme_World, clickListener: OnItemClickListenerLikeMeme) {

            with(_meme) {

                //TimeStamp
                memeTime.text = _meme.lastUpdated             //To get the tag

                //This is not required
//
//                val username = preferenceUtils.getUserFromPrefernece().username
//                val id = preferenceUtils.getUserFromPrefernece()._id
//                val userIns = com.example.memej.responses.memeWorldResponses.User(id, username)
//                val user_likers = _meme.likedBy
//
//                if (user_likers.contains(userIns)) {
//                    likeDrawIo.isLiked = true
//                } else if (!user_likers.contains(userIns) || user_likers.isEmpty()) {
//                    likeDrawIo.isLiked = false
//                }

                //Get the image
                //Load the Image here
                likeDrawIo.isLiked = true
                numLikes.text = _meme.likes.toString()

                photoView.source?.let {
                    Glide.with(itemView.context)
                        .load(_meme.templateId.imageUrl)
                        .dontAnimate()
                        .dontTransform()
                        .error(R.drawable.icon_placeholder)
                        .into(it)
                }

                getCompleteImage(_meme, photoView, itemView.context)


                //Like the meme
                likeDrawIo.setOnLikeListener(object : OnLikeListener {
                    override fun liked(likeButton: LikeButton?) {
                        numLikes.text = (numLikes.text.toString().toInt() + 1).toString()
                        likeMeme(_meme)
                    }

                    override fun unLiked(likeButton: LikeButton?) {
                        numLikes.text = (numLikes.text.toString().toInt() - 1).toString()
                        likeMeme(_meme)
                    }
                })



                itemView.setOnClickListener {
                    clickListener.onItemClicked(_meme)
                    //This listener is for the whole item
                }
            }
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

                        //Get the response
                        //Notify the data set that it has been chnmaged
                        if (response.body()?.msg == "Meme unliked successfully.") {

                            Log.e("ADapter", "In  dislike")
                            likeDrawIo.isLiked = false


                        } else if (response.body()?.msg == "Meme liked successfully.") {

                            Log.e("ADapter", "In  Like")

                            likeDrawIo.isLiked = true

                        }
                    }
                })
        }


        private fun getCompleteImage(
            _homeMeme: Meme_World,
            photoView: ImageEditorView,
            context: Context
        ) {

            val holders = _homeMeme.templateId.numPlaceholders
            val c = 2 * holders - 1

            val mPhotBuilView = Photo.Builder(
                context = context, photoEditorView = photoView
            ).setPinchTextScalable(false)
                .build()

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


                mPhotBuilView.addOldText(
                    null,
                    pl,
                    colorInt,
                    size = size.toFloat(),
                    x1 = x1,
                    y1 = y1,
                    x2 = x2,
                    y2 = y2
                )
            }


        }


    }


}

interface OnItemClickListenerLikeMeme {
    fun onItemClicked(_meme: Meme_World)
}


