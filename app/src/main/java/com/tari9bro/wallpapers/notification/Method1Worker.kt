package com.tari9bro.wallpapers.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tari9bro.wallpapers.R


class Method1Worker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        // Method 1 to be executed here
        val notificationHelper = NotificationHelper(applicationContext)

        notificationHelper.showNotification(
            "Ball Rolling Game ",
            "you can't win this game",
            "https://play.google.com/store/apps/details?id=com.nezzak.mygame",
            R.drawable.game1
        )
        return Result.success()
    }
} // Create similar Worker classes for other methods (Method2Worker, Method3Worker, etc.)

