package com.tari9bro.wallpapers.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException

object FileUtilsHelper {
    private const val TAG = "FileUtilsHelper"

    // Function to get a list of image filenames from the Assets folder
    fun getFilesFromAssets(context: Context): List<String> {
        val imageFilenames: MutableList<String> = ArrayList()
        val assetManager = context.assets
        try {
            // List all files in the specified folder path
            val files = assetManager.list("")
            for (file in files!!) {
                if (file.endsWith(".jpg") || file.endsWith(".png") || file.endsWith(".jpeg")) {
                    imageFilenames.add(file)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading images from Assets folder: " + e.message)
        }
        return imageFilenames
    }

    // Function to load a Bitmap image from the Assets folder using filename
    fun loadBitmapFromAssets(context: Context, filename: String?): Bitmap? {
        val assetManager = context.assets
        try {
            val inputStream = assetManager.open(filename!!)
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            Log.e(TAG, "Error loading bitmap from Assets folder: " + e.message)
        }
        return null
    }
}

