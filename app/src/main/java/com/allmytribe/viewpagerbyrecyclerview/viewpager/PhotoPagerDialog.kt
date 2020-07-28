package com.allmytribe.viewpagerbyrecyclerview.viewpager

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.WindowManager
import com.allmytribe.viewpagerbyrecyclerview.R
import com.allmytribe.viewpagerbyrecyclerview.models.JXPhoto
import com.allmytribe.viewpagerbyrecyclerview.rv.PhotoPagerManager
import kotlinx.android.synthetic.main.dialog_photo_pager.view.*

class PhotoPagerDialog (ctx: Context, val photoList: ArrayList<JXPhoto>, val selectIndex: Int)
    : Dialog(ctx) {

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.dialog_photo_pager, null)
        val manager = PhotoPagerManager(ctx, view.photo_pager_rv)
        manager.photoArr = photoList
        manager.selectedIndex = selectIndex
        manager.itemClickListener = { dismiss() }
        setContentView(view)
    }

    override fun show() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window?.decorView?.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        window?.attributes = lp
        window?.decorView?.setBackgroundColor(Color.BLACK)
        super.show()
    }

}