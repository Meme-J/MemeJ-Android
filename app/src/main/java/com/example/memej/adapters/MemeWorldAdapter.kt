package com.example.memej.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        private val preferenceUtils = PreferenceUtil

        val memeTime = itemView.findViewById<MaterialTextView>(R.id.meme_timestamp)
        val likeDrawIo = itemView.findViewById<LikeButton>(R.id.starBtnMeme)
        val photoView = itemView.findViewById<ImageEditorView>(R.id.photoViewMemeWorld)
        val numLikes = itemView.findViewById<TextView>(R.id.num_likes_memeAdapter)


        fun bindPost(_meme: Meme_World, clickListener: OnItemClickListenerMemeWorld) {

            with(_meme) {

                //TimeStamp
                memeTime.text = _meme.lastUpdated             //To get the tag


                val username = preferenceUtils.getUserFromPrefernece().username
                val id = preferenceUtils.getUserFromPrefernece()._id
                val userIns = com.example.memej.responses.memeWorldResponses.User(id, username)
                val user_likers = _meme.likedBy

                Log.e(
                    "Users",
                    user_likers.toString() + userIns.toString() + username.toString() + id
                )
                if (user_likers.contains(userIns)) {
                    likeDrawIo.isLiked = true
                } else if (!user_likers.contains(userIns) || user_likers.isEmpty()) {
                    likeDrawIo.isLiked = false
                }

                //Get the image
                //Load the Image here
                photoView.source?.let {
                    Glide.with(itemView.context)
                        .load(_meme.templateId.imageUrl)
                        .dontAnimate()
                        .dontTransform()
                        .error(R.drawable.icon_placeholder)
                        .into(it)
                }

                getCompleteImage(_meme, photoView, itemView.context)
                numLikes.text = _meme.likes.toString()

                //Like the meme
                likeDrawIo.setOnLikeListener(object : OnLikeListener {
                    override fun liked(likeButton: LikeButton?) {
                        likeMeme(_meme)

                    }

                    override fun unLiked(likeButton: LikeButton?) {
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

                            Log.e("ADapter", "In resp")

                            likeDrawIo.isLiked = false


                            //Refresh the screen again

                        } else if (response.body()?.msg == "Meme liked successfully.") {

                            likeDrawIo.isLiked = true

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

                photoVieGlobal.addOldText(pl, colorInt, size.toFloat(), x1, y1, x2, y2)
            }


        }


    }


}

interface OnItemClickListenerMemeWorld {
    fun onItemClicked(_meme: Meme_World)
}


