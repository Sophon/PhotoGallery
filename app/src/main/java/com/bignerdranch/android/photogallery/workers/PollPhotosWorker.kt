package com.bignerdranch.android.photogallery.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.FlickrRepository
import com.bignerdranch.android.photogallery.sharedPreferences.QueryPreferences
import timber.log.Timber

class PollPhotosWorker(private val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

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
            Timber.d("notification")
        }

        return Result.success()
    }
    //endregion

    //region Private funs
    private fun searchPhotos(query: String): List<GalleryItem> {
        return FlickrRepository().searchPhotosRequest(query)
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
    //endregion
}