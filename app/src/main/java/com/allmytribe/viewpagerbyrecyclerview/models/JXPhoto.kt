package com.allmytribe.viewpagerbyrecyclerview.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * 图片实体类
 */
data class JXPhoto(
    var id: Long,    //ID
    var displayName: String,    //名称
    var mimeType: String,   //类型
    var dateAdded: Long,    //创建时间
    var isVideo: Boolean = false,   //是不是视频
    var duration: Long = 0,      //如果是视频，时长，毫秒值
    var isChecked: Boolean = false   //编辑时是否选中
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()!!,
        source.readString()!!,
        source.readLong(),
        1 == source.readInt(),
        source.readLong(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(displayName)
        writeString(mimeType)
        writeLong(dateAdded)
        writeInt((if (isVideo) 1 else 0))
        writeLong(duration)
        writeInt((if (isChecked) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<JXPhoto> = object : Parcelable.Creator<JXPhoto> {
            override fun createFromParcel(source: Parcel): JXPhoto = JXPhoto(source)
            override fun newArray(size: Int): Array<JXPhoto?> = arrayOfNulls(size)
        }
    }
}

val JXPhoto.durationStr: String
    get() {
        val seconds = duration/1000
        val second = seconds%60
        val min = seconds/60
        val secondStr = if (second < 10) "0$second" else "$second"
        return "${min}:$secondStr"
    }

val JXPhoto.imageUri: Uri
    get() {
        val baseUri = Uri.parse("content://media/external/images/media")
        return Uri.withAppendedPath(baseUri, id.toString())
    }

val JXPhoto.videoUri: Uri
    get() {
        val baseUri = Uri.parse("content://media/external/video/media")
        return Uri.withAppendedPath(baseUri, id.toString())
    }

val JXPhoto.uri: Uri
    get() {
        return if (isVideo) videoUri else imageUri
    }