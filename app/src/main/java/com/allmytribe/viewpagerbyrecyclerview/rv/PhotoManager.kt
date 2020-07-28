package com.allmytribe.viewpagerbyrecyclerview.rv

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.allmytribe.viewpagerbyrecyclerview.models.JXPhoto
import kotlinx.coroutines.*

class PhotoManager (ctx: Context, recyclerView: RecyclerView) {

    private var adapter: PhotoAdapter
    var photoList = ArrayList<JXPhoto>()
    var itemClickListener: Int2Unit? = null
    var itemLongClickListener: Int2Unit? = null
    var isEdit : Boolean = false    //是对Adapter isEdit的一个引用，本身状态无所谓
        get() = adapter.isEdit
        private set

    init {
        adapter = PhotoAdapter(ctx, photoList)
        recyclerView.adapter = adapter

        adapter.itemClickListener = {
            itemClickListener?.invoke(it)
        }
        adapter.itemLongClickListener = {
            itemLongClickListener?.invoke(it)
        }
    }

    // 切换编辑状态
    fun switchEditState() {
        adapter.isEdit = !adapter.isEdit
    }

    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    fun loadImages(ctx: Context) {
        Log.e("info", "loadImages")
        MainScope().launch {
            val array = loadImagesBackground(ctx)
            Log.e("info", "遍历完了")
            photoList.clear()
            photoList.addAll(array)
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun loadImagesBackground(ctx: Context) : ArrayList<JXPhoto> {
        val array = ArrayList<JXPhoto>()
        withContext(Dispatchers.IO) {
            // 图片
            var allColumn = arrayOf(MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DATE_ADDED)
            ctx.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                allColumn, null, null, null)?.use {
                it.moveToFirst()
                while (it.moveToNext()) {
                    val id  = it.getLong(0)
                    val displayName = it.getString(1)
                    val mimeType    = it.getString(2)
                    val dateAdded   = it.getLong(3)
                    val photo = JXPhoto(id, displayName, mimeType, dateAdded)
                    array.add(photo)
                }
                it.close()
            }

            // 视频
            allColumn = arrayOf(MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DATE_ADDED,
                MediaStore.Video.VideoColumns.DURATION)
            ctx.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                allColumn, null, null, null)?.use {
                it.moveToFirst()
                while (it.moveToNext()) {
                    val id  = it.getLong(0)
                    val displayName = it.getString(1)
                    val mimeType    = it.getString(2)
                    val dateAdded   = it.getLong(3)
                    val duration    = it.getLong(4)
                    val photo = JXPhoto(id, displayName, mimeType, dateAdded, true, duration)
                    array.add(photo)
                }
                it.close()
            }
            array.sortBy {
                -it.dateAdded
            }
        }
        return array
    }

}

