package com.allmytribe.viewpagerbyrecyclerview

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.allmytribe.viewpagerbyrecyclerview.rv.PhotoManager
import com.allmytribe.viewpagerbyrecyclerview.viewpager.PhotoPagerDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var photoManager: PhotoManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("info", "用户已授权，刷新")
                photoManager.loadImages(this)
            } else {
                Log.e("info", "用户拒绝授权")
            }
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("info", "没权限，申请")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        } else {
            Log.e("info", "有权限，刷新")
            photoManager.loadImages(this)
        }
    }

    private fun initView() {
        photoManager = PhotoManager(this, photo_rv)
        photoManager.itemClickListener = {
            val dialog = PhotoPagerDialog(this@MainActivity, photoManager.photoList, it)
            dialog.show()
        }
        photo_rv.post { requestPermission() }
    }
}