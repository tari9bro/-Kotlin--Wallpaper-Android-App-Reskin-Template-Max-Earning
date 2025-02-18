package com.tari9bro.wallpapers.helpers

import android.app.Dialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Button
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.tari9bro.wallpapers.R
import java.io.IOException


class WallpaperSetter(private val context: Context) {
    private val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)

    fun setWallpaperToHomeScreen(bitmap: Bitmap) {
        setWallpaper(bitmap, WallpaperManager.FLAG_SYSTEM)
    }

    fun setWallpaperToLockScreen(bitmap: Bitmap) {
        setWallpaper(bitmap, WallpaperManager.FLAG_LOCK)
    }

    fun setWallpaperToBoth(bitmap: Bitmap) {
        setWallpaper(bitmap, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
    }

    private fun setWallpaper(bitmap: Bitmap, flags: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, flags)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun showWallpaperDialog(fileName: String?) {
        try {
            val inputStream = context.assets.open(fileName!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Create a RoundedBitmapDrawable from the Bitmap
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                context.resources, bitmap
            )
            roundedBitmapDrawable.cornerRadius =
                16f // Adjust the corner radius as per your preference

            // Create and configure the custom Material Dialog
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.fragment_material_dialog)
            dialog.setCancelable(true)

            val setToHomeBtn = dialog.findViewById<Button>(R.id.setToHomeBtn)
            val setToLockBtn = dialog.findViewById<Button>(R.id.setToLockBtn)
            val setToBothBtn = dialog.findViewById<Button>(R.id.setToBothBtn)


            // Set wallpaper when buttons are clicked
            setToHomeBtn.setOnClickListener {
                setWallpaperToHomeScreen(bitmap)
                dialog.dismiss()
            }

            setToLockBtn.setOnClickListener {
                setWallpaperToLockScreen(bitmap)
                dialog.dismiss()
            }

            setToBothBtn.setOnClickListener {
                setWallpaperToBoth(bitmap)
                dialog.dismiss()
            }

            dialog.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
