package com.bignerdranch.android.photogallery.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bignerdranch.android.photogallery.NOTIFICATION_CHANNEL_ID
import com.bignerdranch.android.photogallery.PhotoGalleryActivity
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.PhotoRepository
import com.bignerdranch.android.photogallery.sharedPreferences.QueryPreferences
import timber.log.Timber

class PollPhotosWorker(private val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    companion object {
        const val ACTION_SHOW_NOTIFICATION =
            "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION"

        const val PERM_PRIVATE = "com.bignerdranch.android.photogallery.PRIVATE"

        const val NOTIFICATION = "NOTIFICATION"
    }

    //region Overrides
    override fun doWork(): Result {
        //search for photos
        val query: String = QueryPreferences.getStoredQuery(context)

        if(query.isBlank()) {
            Timber.d("no query, no work to be done")

            return Result.success()
        }

        val photos: List<GalleryItem> = searchPhotos(query)
        if(photos.isEmpty()) { return Result.success() }

        //compare the newest photo with the id of the last photo
        val lastPhotoId: String = QueryPreferences.getLastPhotoId(context)
        if(thereIsNewPhoto(photos, lastPhotoId)) {
            createBackgroundNotification()
        }

        return Result.success()
    }
    //endregion

    //region Private funs
    private fun searchPhotos(query: String): List<GalleryItem> {
        return PhotoRepository.get().searchPhotosRequest(query)
            .execute()
            .body()
            ?.photos
            ?.galleryItems
            ?: emptyList()
    }

    private fun thereIsNewPhoto(
        photos: List<GalleryItem>,
        lastPhotoId: String
    ): Boolean {
        val newestPhotoId: String =  photos.first().id

        return if(newestPhotoId != lastPhotoId) {
            Timber.d("new photo: $newestPhotoId")

            QueryPreferences.setLastPhotoId(context, newestPhotoId)

            true
        } else {
            Timber.d("old photo: $newestPhotoId")

            false
        }
    }

    private fun createBackgroundNotification() {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            PhotoGalleryActivity.newIntent(context),
            0
        )

        val resources = context.resources

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.new_pictures_title))
            .setContentTitle(resources.getString(R.string.new_pictures_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationIntent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(NOTIFICATION, notification)
        }

        context.sendOrderedBroadcast(notificationIntent, PERM_PRIVATE)
    }
    //endregion
}