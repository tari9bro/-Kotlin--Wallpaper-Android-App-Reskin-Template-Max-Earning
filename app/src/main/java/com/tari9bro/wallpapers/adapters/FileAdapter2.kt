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
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tari9bro.wallpapers.R
import java.io.IOException


class FileAdapter2(
    private val context: Context,
    private val fileList: List<String?>?,
    private val activity: Activity,
    var fragmentManager: FragmentManager?
) : RecyclerView.Adapter<FileAdapter2.FileViewHolder>() {
    var fileNameStr: String? = null

    var fileName: String? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_file2, parent, false)
        return FileViewHolder(itemView)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(
        holder: FileViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        fileName = fileList!![position]







        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName!!)


            // Create a Bitmap with the desired dimensions
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            if (originalBitmap != null) {
                val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, false)

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











        fileNameStr = fileName
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                this.fileNameStr = fileName
            }
        })
    }


    override fun getItemCount(): Int {
        return fileList!!.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fileImageView: ImageView = itemView.findViewById(R.id.fileImageView)
    }
}
