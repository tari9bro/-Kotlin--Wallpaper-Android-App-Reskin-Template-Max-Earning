package com.tari9bro.wallpapers.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest.Builder.build
import androidx.work.WorkRequest.Builder.setInitialDelay
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum.Builder.build
import java.util.concurrent.TimeUnit


class NotificationHelper(// 6 hours interval
    private val context: Context
) {
    private val NOTIFICATION_ID = 1

    fun showNotification(title: String?, message: String?, url: String?, icon: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText(message)
        notificationBuilder.setSmallIcon(icon)

        notificationBuilder.setAutoCancel(true)
        // Create a style for the expanded view with an image
        val style = NotificationCompat.BigPictureStyle()
            .bigPicture(BitmapFactory.decodeResource(context.resources, icon))
            .bigLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
        notificationBuilder.setStyle(style)
        // Create an intent to open the URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.setContentIntent(pendingIntent)

        // Set notification sound
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder.setSound(soundUri)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun startPeriodicTasks() {
        // Define the common interval for all methods (3 hours in milliseconds)
        val commonInterval = (3 * 60 * 60 * 1000).toLong()

        // Create periodic work requests for each method
        val periodicWorkRequest1: PeriodicWorkRequest = Builder(
            Method1Worker::class.java, commonInterval, TimeUnit.MILLISECONDS
        )
            .setInitialDelay(0, TimeUnit.MILLISECONDS) // Start immediately
            .build()

        val periodicWorkRequest2: PeriodicWorkRequest = Builder(
            Method2Worker::class.java, commonInterval, TimeUnit.MILLISECONDS
        )
            .setInitialDelay(
                (1 * 60 * 60 * 1000).toLong(),
                TimeUnit.MILLISECONDS
            ) // Start after 1 hour
            .build()

        val periodicWorkRequest3: PeriodicWorkRequest = Builder(
            Method3Worker::class.java, commonInterval, TimeUnit.MILLISECONDS
        )
            .setInitialDelay(
                (2 * 60 * 60 * 1000).toLong(),
                TimeUnit.MILLISECONDS
            ) // Start after 2 hours
            .build()

        // Enqueue the work requests
        WorkManager.getInstance(context).enqueue(periodicWorkRequest1)
        WorkManager.getInstance(context).enqueue(periodicWorkRequest2)
        WorkManager.getInstance(context).enqueue(periodicWorkRequest3)
    }

    companion object {
        const val CHANNEL_ID: String = "MyNotificationChannel"
        val CHANNEL_NAME: CharSequence = "My Notification Channel"
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                // Set additional properties for the notification channel (optional)
                channel.description = "Notification Channel for wallpapers "
                channel.enableVibration(true)

                val notificationManager = context.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
