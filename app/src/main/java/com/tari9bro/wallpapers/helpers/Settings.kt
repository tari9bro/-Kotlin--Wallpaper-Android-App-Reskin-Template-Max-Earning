package com.tari9bro.wallpapers.helpers

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.work.WorkRequest.Builder.build
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tari9bro.wallpapers.R
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum.Builder.build
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum.Builder.dialogProperties
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class Settings(private val activity: Activity, private val context: Context) {
    private val prefs = PreferencesHelper(activity)


    fun exitTheApp() {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle(R.string.exit_dialog_title)
        dialog.setIcon(R.drawable.ic_exit)
        dialog.setMessage(R.string.exit_dialog_msg)
        dialog.setCancelable(false)
        dialog.setPositiveButton(R.string.yes) { dialogInterface: DialogInterface?, i: Int -> activity.finish() }
        dialog.setNegativeButton(R.string.no) { dialogInterface: DialogInterface?, i: Int ->
            Toast.makeText(
                context, activity.getString(R.string.cancel_exit), Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }

    fun noInternetDialog(lifecycle: Lifecycle?) {
        val builder: Builder = Builder(
            activity, lifecycle
        )
        //getLifecycle()
        val properties: DialogPropertiesPendulum = builder.dialogProperties

        properties.connectionCallback = object : ConnectionCallback {
            // Optional
            override fun hasActiveConnection(hasActiveConnection: Boolean) {
                // ...
            }
        }

        properties.cancelable = false // Optional
        properties.noInternetConnectionTitle = activity.getString(R.string.n1) // Optional
        properties.noInternetConnectionMessage = activity.getString(R.string.n2) // Optional
        properties.showInternetOnButtons = true // Optional
        properties.pleaseTurnOnText = activity.getString(R.string.n3) // Optional
        properties.wifiOnButtonText = "Wifi" // Optional
        properties.mobileDataOnButtonText = "Mobile data" // Optional

        properties.onAirplaneModeTitle = activity.getString(R.string.n1) // Optional
        properties.onAirplaneModeMessage = activity.getString(R.string.n4) // Optional
        properties.pleaseTurnOffText = activity.getString(R.string.n5) // Optional
        properties.airplaneModeOffButtonText = activity.getString(R.string.n6) // Optional
        properties.showAirplaneModeOffButtons = true // Optional

        builder.build()
    }

    fun moreApps() {
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(activity.getString(R.string.developer_search))
                )
            )
        } catch (exeption: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(activity.getString(R.string.developer_id))
                )
            )
        }
    }

    fun sharTheApp() {
        val url = activity.getString(R.string.app_link)
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plane")
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.share_dialog_msg))
        intent.putExtra(Intent.EXTRA_TEXT, url)
        activity.startActivity(
            Intent.createChooser(
                intent,
                activity.getString(R.string.share_dialog_title)
            )
        )
    }


    fun showPermissionDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle("Permission Needed")
            .setMessage("This app requires access to external storage to function properly. Do you want to grant the necessary permission?")
            .setPositiveButton("Yes") { dialogInterface, i ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                    1001
                )
                prefs.SaveBool("Permission", true)
            }
            .setNegativeButton("No") { dialogInterface, i ->
                prefs.SaveBool("Permission", false)
                activity.finish()
            }
            .show()
    }

    fun saveImage(assetFileName: String?) {
        try {
            // Open the asset file
            val inputStream = context.assets.open(assetFileName!!)

            // Create a directory in external storage to save the image
            val directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "images"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Create a file to save the image
            val imageFile = File(directory, assetFileName)

            // Copy the image from assets to the file
            val outputStream: OutputStream = FileOutputStream(imageFile)
            val buffer = ByteArray(1024)
            var length: Int
            while ((inputStream.read(buffer).also { length = it }) > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.close()
            inputStream.close()

            // Tell the media scanner to scan the new image file
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.absolutePath), null, null)

            Log.d("ImageUtils", "Image saved to gallery: $assetFileName")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ImageUtils", "Error saving image to gallery: " + e.message)
        }
    }
}
