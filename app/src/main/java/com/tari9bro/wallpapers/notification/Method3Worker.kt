package com.tari9bro.wallpapers.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tari9bro.wallpapers.R

class Method3Worker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val notificationHelper = NotificationHelper(applicationContext)

        notificationHelper.showNotification(
            "you can't be hero on this game",
            "high-octane world of crime-fighting and intense gunplay",
            "https://play.google.com/store/apps/details?id=com.nezzak.bluebeetle",
            R.drawable.game3
        )
        return Result.success()
    }
}
