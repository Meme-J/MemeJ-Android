package com.example.memej.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.Keep
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memej.R
import com.example.memej.Utils.ApplicationUtil
import com.example.memej.Utils.DiffUtils.DiffUtilsHomeMeme
import com.example.memej.responses.homeMememResponses.Meme_Home
import com.example.memej.textProperties.ConversionUtil
import com.example.memej.textProperties.lib.ImageEditorView
import com.example.memej.textProperties.lib.Photo
import com.google.android.material.textview.MaterialTextView

@Keep
class HomeMemeAdapter(val itemClickListener: OnItemClickListenerHome) :

    PagedListAdapter<Meme_Home, HomeMemeAdapter.MyViewHolder>(DiffUtilsHomeMeme()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_home_meme, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        getItem(position)?.let { holder.bindPost(it, itemClickListener) }
        holder.setIsRecyclable(false)

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Bind the fetched items with the view holding classes


        val memeTimeLU = itemView.findViewById<MaterialTextView>(R.id.home_meme_timestamp)
        val load = itemView.findViewById<ProgressBar>(R.id.loading_panel)
        val photoView = itemView.findViewById<ImageEditorView>(R.id.photoViewHomeMain)
        val context = ApplicationUtil.getContext()

        fun bindPost(_homeMeme: Meme_Home, clickListener: OnItemClickListenerHome) {

            memeTimeLU.text = ConversionUtil.convertTimeToEpoch(_homeMeme.lastUpdated.toString())

            //Load the Image here
            load.visibility = View.VISIBLE
            photoView.source?.let {
                Glide.with(itemView.context)
                    .load(_homeMeme.templateId.imageUrl)
                    .dontAnimate()
                    .dontTransform()
                    .error(R.drawable.icon_placeholder)
                    .into(it)
            }

            load.visibility = View.GONE
            getCompleteImage(_homeMeme, photoView, itemView.context)

            itemView.setOnClickListener {
                clickListener.onItemClicked(_homeMeme)
            }
        }


        private fun getCompleteImage(
            _homeMeme: Meme_Home,
            photoView: ImageEditorView,
            context: Context
        ) {
            val currentStage = _homeMeme.stage
            val c = 2 * currentStage - 1
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
                    text = pl,
                    colorCodeTextView = colorInt,
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


interface OnItemClickListenerHome {
    fun onItemClicked(_homeMeme: Meme_Home)
}


