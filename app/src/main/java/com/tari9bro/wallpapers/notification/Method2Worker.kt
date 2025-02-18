package com.tari9bro.wallpapers.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tari9bro.wallpapers.R

class Method2Worker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.showNotification(
            "join 1k playing this game",
            "put yourself in shoes of a skilled dunker.",
            "https://play.google.com/store/apps/details?id=com.nazzark.slamdunkthefirst",
            R.drawable.game2
        )
        return Result.success()
    }
}
