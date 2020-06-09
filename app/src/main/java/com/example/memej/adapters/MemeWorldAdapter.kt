package com.example.memej.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.memej.R
import com.example.memej.Utils.DiffUtils.DiffUtilsMemeWorld
import com.example.memej.Utils.SessionManager
import com.example.memej.interfaces.RetrofitClient
import com.example.memej.responses.LikeOrNotResponse
import com.example.memej.responses.memeWorldResponses.Meme_World
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
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
        //Bind the fetched items with the view holding classes
        val apiservice = RetrofitClient.getAuthInstance()
        val sessionManager = SessionManager(context)
        val service = RetrofitClient.makeCallsForMemes(context)


        val memeImage = itemView.findViewById<ShapeableImageView>(R.id.cv_post)
        val memeTime = itemView.findViewById<MaterialTextView>(R.id.meme_timestamp)
        val memLikesNum = itemView.findViewById<TextView>(R.id.post_likes_num)
        val memeLike = itemView.findViewById<ImageView>(R.id.like_img)


        //The add tag and send will be in the other hand request
        //Implement Child Recycler View for the user post (Horizontal)

        fun bindPost(_meme: Meme_World, clickListener: OnItemClickListenerMemeWorld) {

            with(_meme) {
                //memeTags.text = tag
                //Load image
                //This is where the glide is loaded
                //Check this loader
                //Instance of user
                memeTime.text = _meme.lastUpdated             //To get the tag
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
                            var bitmap = Bitmap.createBitmap(resource)
                            Log.e("OKAYY", "cn " + resource)
                            Log.e("OKAYY", "cn " + bitmap)

                            var canvas = Canvas(bitmap)
                            Log.e("OKAYY", "cn " + canvas)

                            memeImage.draw(canvas)
                            memeImage.setImageBitmap(bitmap)


                            //We have the canvas and bitmap initialized. Retrueve the previous drawings on it
                            getCompleteImage(bitmap, canvas, _meme)

                        }
                    })


                //Check if the user has liked the meme previously or not
                //Call the user to get his id
                //Get instance for the username and id

                //Function to get the user
                //getProfileOfUser(_meme)

                //Like the meme
                itemView.findViewById<ImageView>(R.id.like_img).setOnClickListener {
                    //Like the meme


                    Log.e("Like Meme", "In Adapter Like")
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

                                    //Get the call the likes API
//                                    _meme.likes = _meme.likes - 1
                                    memLikesNum.text = _meme.likes.toString()
                                    //Set drawable into working
                                    memeLike.setImageResource(R.drawable.ic_like_empty)

//                                    //Get user instance and convert it in mutable list
//                                    val likers: MutableList<User> = _meme.likedBy.toMutableList()
//                                    val u = UserInstance(context)
//                                    likers.remove(u)
//                                    _meme.likedBy = likers.toList<User>()
//                                    Log.e("disLike Pass", response.body()?.msg.toString())
                                } else if (response.body()?.msg == "Meme liked successfully.") {
//                                    _meme.likes = _meme.likes + 1
                                    memLikesNum.text = _meme.likes.toString()
                                    memLikesNum.setTextColor(Color.RED)
                                    //Set drawable into working
                                    memeLike.setImageResource(R.drawable.ic_favorite)

                                    //Append the users list
                                    //Append in the user list of the ones who liked
                                    //Get user instance and convert it in mutable list
//                                    val likers: MutableList<User> = _meme.likedBy.toMutableList()
//                                    //Instance of users
//                                    val u = getProfileOfUser(_meme)
//                                    likers.add(u)
//                                    _meme.likedBy = likers.toList<User>()
//                                    Log.e("Like pass", response.body()?.msg.toString())
                                }

                            }
                        })
                }


                itemView.setOnClickListener {
                    clickListener.onItemClicked(_meme)
                    //This listener is for the whole item
                }
            }
        }

//        private fun getProfileOfUser(_meme: Meme_World): User {
//
//            var username: String? = ""
//            var userId: String? = ""
//
//            apiservice.getUser(accessToken = "Bearer ${sessionManager.fetchAcessToken()}")
//                .enqueue(object : Callback<ProfileResponse> {
//                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
//                        Log.e("MW", "Van not get profile, loaded default")
//
//                        memLikesNum.text = _meme.likes.toString()
//                        memeLike.setImageResource(R.drawable.ic_like_empty)
//                    }
//
//                    override fun onResponse(
//                        call: Call<ProfileResponse>,
//                        response: Response<ProfileResponse>
//                    ) {
//                        Log.e("MW", "Profile")
//
//                        //Get the id, username
//                        val u = User(
//                            response.body()?._id.toString(),
//                            response.body()?.username.toString()
//                        )
//
//                        username = response.body()?._id.toString()
//                        userId = response.body()?.username.toString()
//
//                        if (_meme.likedBy.contains(u)) {
//                            Log.e("MW", "has liked")
//
//                            //Already liked by the person
//                            memLikesNum.text = _meme.likes.toString()
//                            memLikesNum.setTextColor(Color.RED)
//                            //Set drawable into working
//                            memeLike.setImageResource(R.drawable.ic_favorite)
//                        } else {
//                            memLikesNum.text = _meme.likes.toString()
//                            Log.e("MW", "hasnt liked")
//
//                            memeLike.setImageResource(R.drawable.ic_like_empty)
//                        }
//
//                    }
//                })
//            //return user
//            return User(userId.toString(), username.toString())
//        }


        private fun getCompleteImage(bitmap: Bitmap?, canvas: Canvas, _meme: Meme_World) {

            //Extract every placeholder, color, textSize
            //Use the num of placeholders in the  meme image to view those
            //Set paint and size
            val i = 0
            for (i in 0 until _meme.templateId.numPlaceholders - 1) {

                val paint =
                    setPaint(
                        _meme.templateId.textColorCode[i].toString(),
                        _meme.templateId.textSize[i].toString()
                    )
                val pl = _meme.placeholders[i].toString()
                val x = _meme.templateId.coordinates.elementAt(i).x
                val y = _meme.templateId.coordinates.elementAt(i).y
                //Paint on canvas
                canvas.drawText(pl, x.toFloat(), y.toFloat(), paint)
            }

        }


        //Function to get paint again. This will be true for the value default color and the one chosen by the user
        private fun setPaint(color: String, size: String): Paint {
            //Where color is the string in #HEX code
            val paint = Paint()
            paint.color = Color.parseColor(color)
            paint.strokeWidth =
                setSize(size.toInt())                                       //Standard Chosen
            paint.style = Paint.Style.FILL                            //Default to be set
            paint.isAntiAlias = true
            paint.isDither = true


            return paint
        }

        private fun setSize(size: Int): Float {
            val size_req = size.toFloat()
            return size_req

        }


    }


}

interface OnItemClickListenerMemeWorld {
    fun onItemClicked(_meme: Meme_World)
}


