package com.example.memej.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.Instances.LoadImage
import com.example.memej.Instances.UserInstance
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeWorld
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.like.LikeButton
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


    }


    class MyViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        val sessionManager = SessionManager(context)
        val service = RetrofitClient.makeCallsForMemes(context)


        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.cv_post)
        val memeTime = itemView.findViewById<MaterialTextView>(R.id.meme_timestamp)
        val memLikesNum = itemView.findViewById<TextView>(R.id.post_likes_num)

        val likeDrawIo = itemView.findViewById<LikeButton>(R.id.starBtnMeme)


        fun bindPost(_meme: Meme_World, clickListener: OnItemClickListenerMemeWorld) {

            with(_meme) {

                //TimeStamp
                memeTime.text = _meme.lastUpdated             //To get the tag

                //Number of likes
                memLikesNum.text = likes.toString()

                //State of liked/not
                val userIns = UserInstance(ApplicationUtil.getContext())
                val user_likers = _meme.likedBy

                if (user_likers != null) {
                    likeDrawIo.isLiked = user_likers.contains(userIns)
                }


                //Get the image
                Glide.with(itemView)            //Add memeImage here
                    .asBitmap()
                    .load(_meme.templateId.imageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {

                            val canvas = Canvas(resource)

                            memeImage.draw(canvas)
                            memeImage.setImageBitmap(resource)


                            getCompleteImage(resource, canvas, _meme)

                        }
                    })

                //Like the meme
                likeDrawIo.setOnClickListener {
                    likeMeme(_meme)
                }


                itemView.setOnClickListener {
                    clickListener.onItemClicked(_meme)
                    //This listener is for the whole item
                }
            }
        }

        private fun likeMeme(_meme: Meme_World) {
            service.likeMeme(
                _meme._id,
                accessToken = "Bearer ${sessionManager.fetchAcessToken()}"
            )
                .enqueue(object : Callback<LikeOrNotResponse> {
                    override fun onFailure(call: Call<LikeOrNotResponse>, t: Throwable) {
                        //Not able to get
                        Log.e("Like Fail", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<LikeOrNotResponse>,
                        response: Response<LikeOrNotResponse>
                    ) {
                        //Get the response
                        if (response.body()?.msg == "Meme unliked successfully.") {

                            memLikesNum.text = _meme.likes.toString()
                            memLikesNum.setTextColor(Color.GRAY)
                            likeDrawIo.isLiked = false

                        } else if (response.body()?.msg == "Meme liked successfully.") {

                            memLikesNum.text = _meme.likes.toString()
                            memLikesNum.setTextColor(Color.RED)
                            likeDrawIo.isLiked = true
                        }

                    }
                })
        }


        private fun getCompleteImage(bitmap: Bitmap?, canvas: Canvas, _meme: Meme_World) {

            if (bitmap != null) {
                LoadImage().getCompleteImage(canvas, bitmap, _meme)
            }

        }


    }


}

interface OnItemClickListenerMemeWorld {
    fun onItemClicked(_meme: Meme_World)
}


