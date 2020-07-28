package com.allmytribe.viewpagerbyrecyclerview.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.allmytribe.viewpagerbyrecyclerview.R
import com.allmytribe.viewpagerbyrecyclerview.models.JXPhoto
import com.allmytribe.viewpagerbyrecyclerview.models.uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_photo_pager_picture.view.*
import kotlinx.android.synthetic.main.item_photo_pager_video.view.*

typealias Unit2Unit = () -> Unit

class PhotoPagerAdapter (val ctx: Context, val photoList: ArrayList<JXPhoto>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemClickListener: Unit2Unit? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemViewType.Video.ordinal -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_photo_pager_video, parent, false)
                view.setOnClickListener {
                    itemClickListener?.invoke()
                }
                VideoViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(ctx).inflate(R.layout.item_photo_pager_picture, parent, false)
                view.setOnClickListener {
                    itemClickListener?.invoke()
                }
                PictureViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (photoList[position].isVideo) {
            return ItemViewType.Video.ordinal
        }
        return ItemViewType.Picture.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewHolder -> holder.videoView.setVideoURI(photoList[position].uri)
            is PictureViewHolder -> Glide.with(ctx)
                .load(photoList[position].uri)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.photo)
                .into(holder.pictureIv)
        }
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pictureIv: AppCompatImageView = itemView.photo_pager_iv
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: VideoView = itemView.photo_pager_video
    }

    enum class ItemViewType {
        Picture, Video
    }
}