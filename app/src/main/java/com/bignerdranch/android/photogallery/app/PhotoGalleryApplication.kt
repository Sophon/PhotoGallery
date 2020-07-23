package com.bignerdranch.android.photogallery.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bignerdranch.android.photogallery.BuildConfig
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.data.PhotoRepository
import timber.log.Timber

const val NOTIFICATION_CHANNEL_ID = "photo_poll"

class PhotoGalleryApplication: Application() {

    //region Lifecycle
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel: NotificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            getSystemService(NotificationManager::class.java)!!
                .createNotificationChannel(notificationChannel)
        }

        PhotoRepository.initialize(this)
    }
    //endregion
}