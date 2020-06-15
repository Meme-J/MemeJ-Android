package com.example.memej.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.Instances.LoadImage
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeWorld
import com.example.memej.Utils.PreferenceUtil
import com.example.memej.Utils.sessionManagers.SessionManager
import com.example.memej.entities.likeMemeBody
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.ProfileResponse
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.example.memej.responses.memeWorldResponses.User
import com.google.android.material.imageview.ShapeableImageView
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
    }


    class MyViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        val sessionManager =
            SessionManager(context)
        val service = RetrofitClient.makeCallsForMemes(context)
        private val preferenceUtils = PreferenceUtil

        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.cv_post)
        val memeTime = itemView.findViewById<MaterialTextView>(R.id.meme_timestamp)
        val likeDrawIo = itemView.findViewById<LikeButton>(R.id.starBtnMeme)


        fun bindPost(_meme: Meme_World, clickListener: OnItemClickListenerMemeWorld) {

            with(_meme) {

                //TimeStamp
                memeTime.text = _meme.lastUpdated             //To get the tag


                //State of liked/not
                //Use the saved user instance


                val username = preferenceUtils.getUserFromPrefernece().username
                val id = preferenceUtils.getUserFromPrefernece()._id
                val userIns = com.example.memej.responses.memeWorldResponses.User(id, username)
                val user_likers = _meme.likedBy

//                Log.e("Adapter", userIns.username.toString() + user_likers.toString())

                if (user_likers.contains(userIns)) {
                    likeDrawIo.isLiked = true
                } else if (!user_likers.contains(userIns) || user_likers.isEmpty()) {
                    likeDrawIo.isLiked = false
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
                        Toast.makeText(
                            ApplicationUtil.getContext(),
                            "Unable to like meme at the moment",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("ADapter", "In fail")

                    }

                    override fun onResponse(
                        call: Call<LikeOrNotResponse>,
                        response: Response<LikeOrNotResponse>
                    ) {

                        //Get the response
                        if (response.body()?.msg == "Meme unliked successfully.") {

                            Log.e("ADapter", "In resp")
                            likeDrawIo.isLiked = false

                        } else if (response.body()?.msg == "Meme liked successfully.") {

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


        fun UserInstance(): User {

            val ctx = ApplicationUtil.getContext()
            val apiservice = RetrofitClient.getAuthInstance()
            val sessionManager =
                SessionManager(ctx)


            var username: String? = ""
            var userId: String? = ""

            apiservice.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
                .enqueue(
                    object : Callback<ProfileResponse> {
                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            Log.e("MW", "Van not get profile, loaded default")

                        }

                        override fun onResponse(
                            call: Call<ProfileResponse>,
                            response: Response<ProfileResponse>
                        ) {
                            Log.e("MW", "Profile")


                            username = response.body()?.profile?._id.toString()
                            userId = response.body()?.profile?.username.toString()


                        }
                    })

            return User(userId.toString(), username.toString())
        }


    }


}

interface OnItemClickListenerMemeWorld {
    fun onItemClicked(_meme: Meme_World)
}


