package com.bignerdranch.android.photogallery.features.notifications

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.bignerdranch.android.photogallery.features.polling.PollPhotosWorker
import timber.log.Timber

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Notification: received broadcast ${intent.action}")

        if(resultCode != Activity.RESULT_OK) {
            Timber.i("Notification: foreground activity canceled broadcast")
            return
        }

        val notification: Notification =
            intent.getParcelableExtra(PollPhotosWorker.NOTIFICATION)

        NotificationManagerCompat.from(context).notify(0, notification)
    }
}