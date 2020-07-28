package com.allmytribe.viewpagerbyrecyclerview.rv

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allmytribe.viewpagerbyrecyclerview.models.JXPhoto
import kotlin.math.abs

class PhotoPagerManager (ctx: Context, val recyclerView: RecyclerView) {
    var photoArr = ArrayList<JXPhoto>()
        set(value) {
            field.clear()
            field.addAll(value)
            adapter.notifyDataSetChanged()
        }
    var selectedIndex = 0
        set(value) {
            field = value
            recyclerView.post {
                if (hasDragged)
                    recyclerView.smoothScrollToPosition(value)
                else
                    recyclerView.scrollToPosition(value)
            }
        }

    var itemClickListener: Unit2Unit? = null
    private var screenWidth = 0 //屏幕宽度
    private var hasDragged = false //滑动过
    private var adapter: PhotoPagerAdapter

    init {
        screenWidth = ctx.widthPixels
        adapter = PhotoPagerAdapter(ctx, photoArr)
        adapter.itemClickListener = {
            itemClickListener?.invoke()
        }

        val linearLayoutManager = LinearLayoutManager(ctx)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        // 滑动停止后定位到具体某个页面
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        hasDragged = true
                    }
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (recyclerView.childCount > 0) {
                            val lm = recyclerView.layoutManager ?: return
                            if (lm is LinearLayoutManager) {
                                val firstPos = lm.findFirstVisibleItemPosition()

                                selectedIndex = if (recyclerView.childCount == 1) {
                                    firstPos
                                } else {
                                    var firstChild: View? = recyclerView.getChildAt(firstPos)
                                    var childPos = firstPos
                                    if (firstChild == null) {
                                        firstChild = recyclerView.getChildAt(0)
                                        childPos = 0
                                    }
                                    val curX = firstChild!!.x - screenWidth * childPos

                                    if (abs(curX) >= screenWidth/2 && firstPos < photoArr.size - 1) {
                                        firstPos + 1
                                    } else {
                                        firstPos
                                    }
                                }
                            }
                        }
                        hasDragged = false
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                    }
                }

            }
        })
    }

}


/**
 * 屏幕宽度
 */
val Context.widthPixels : Int
    get() = resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 */
val Context.heightPixels : Int
    get() = resources.displayMetrics.heightPixels