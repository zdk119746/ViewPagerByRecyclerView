package com.allmytribe.viewpagerbyrecyclerview.rv

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.allmytribe.viewpagerbyrecyclerview.R
import com.allmytribe.viewpagerbyrecyclerview.models.JXPhoto
import com.allmytribe.viewpagerbyrecyclerview.models.durationStr
import com.allmytribe.viewpagerbyrecyclerview.models.imageUri
import com.allmytribe.viewpagerbyrecyclerview.models.videoUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_photo.view.*

typealias Int2Unit = (Int) -> Unit

class PhotoAdapter(private val ctx: Context, var photoList: ArrayList<JXPhoto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isEdit = false //是否处于编辑状态
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    var itemClickListener: Int2Unit? = null
    var itemLongClickListener: Int2Unit? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.item_photo, parent, false)
        view.setOnClickListener {
            val position: Int = it.tag as Int
            itemClickListener?.invoke(position)
        }
        view.setOnLongClickListener {
            val position: Int = it.tag as Int
            itemLongClickListener?.invoke(position)
            true
        }

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (photoList.size <= position) return
            holder.itemView.tag = position  // 给ItemClickListener设置tag
            val photo = photoList[position]
            // 怎么显示缩略图？
            Log.e("info", "mimeType: ${photo.mimeType}")

            if (photo.isVideo) {
                Glide.with(ctx)
                    .load(photo.videoUri)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade(300)
                    .into(holder.photoImgView)
                holder.videoImgView.visibility  = View.VISIBLE
                holder.durationTv.visibility    = View.VISIBLE
                holder.durationTv.text  = photo.durationStr
            } else {
                Glide.with(ctx)
                    .load(photo.imageUri)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.photo)
                    .crossFade(300)
                    .into(holder.photoImgView)
                holder.videoImgView.visibility  = View.INVISIBLE
                holder.durationTv.visibility    = View.INVISIBLE
            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoImgView = itemView.photo_img
        var videoImgView = itemView.video_img
        var durationTv = itemView.duration_tv
    }

}
