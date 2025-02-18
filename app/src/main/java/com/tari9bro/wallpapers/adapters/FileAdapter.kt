package com.tari9bro.wallpapers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.tari9bro.wallpapers.R
import com.tari9bro.wallpapers.activity.MainActivity
import com.tari9bro.wallpapers.ads.Ads
import com.tari9bro.wallpapers.fragments.ImageFragment
import com.tari9bro.wallpapers.helpers.PreferencesHelper
import java.io.IOException


class FileAdapter(
    private val context: Context, private val fileList: List<String>, // Desired placeholder width
    private val placeholderWidth: Int, // Desired placeholder height
    private val placeholderHeight: Int, private val activity: Activity
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    private var pref: PreferencesHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: FileViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val fileName = fileList[position]
        pref = PreferencesHelper(activity)
        if (!containsDigitThree(position)) {
            holder.badge.visibility = View.GONE
        } else {
            holder.badge.visibility = View.VISIBLE
        }
        // Load image from assets folder using AssetManager
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)


            // Create a Bitmap with the desired dimensions
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            if (originalBitmap != null) {
                val scaledBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    placeholderWidth,
                    placeholderHeight,
                    false
                )

                // Create a RoundedBitmapDrawable from the Bitmap and set it as the placeholder
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    context.resources, scaledBitmap
                )
                roundedBitmapDrawable.cornerRadius =
                    16f // Adjust the corner radius as per your preference
                holder.fileImageView.setImageDrawable(roundedBitmapDrawable)

                // Remember to recycle the originalBitmap to release memory
                originalBitmap.recycle()
            } else {
                // If decoding fails, set a default image
                holder.fileImageView.setImageResource(R.drawable.default_image)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            holder.fileImageView.setImageResource(R.drawable.default_image)
        }





        holder.itemView.setOnClickListener {
            pref!!.SaveInt("position", position)
            handleClick()
            if (holder.badge.visibility == View.VISIBLE) {
                val fragmentManager = (context as MainActivity).supportFragmentManager
                val imageFragment = ImageFragment.newInstanceWithBadge(fileName, R.drawable.star)
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, imageFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                val fragmentManager = (context as MainActivity).supportFragmentManager
                val imageFragment = ImageFragment.newInstance(fileName)
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, imageFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fileImageView: ImageView = itemView.findViewById(R.id.fileImageView)
        var badge: TextView = itemView.findViewById(R.id.badge)
    }

    private fun handleClick() {
        val pref = PreferencesHelper(activity)
        val ads = Ads(activity, context)


        // Load the click count from SharedPreferences
        var clickCount = pref.LoadInt("clickCount")
        clickCount++
        pref.SaveInt("clickCount", clickCount)
        // Check if the click count is a multiple of 7
        if (clickCount % 7 == 0) {
            // Show the interstitial ad when the click count reaches a multiple of 7
            ads.playInterstitialAd()
        }
    }

    companion object {
        fun containsDigitThree(number: Int): Boolean {
            val numberString = number.toString()
            return numberString.contains("3")
        }
    }
}
