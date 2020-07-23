package com.bignerdranch.android.photogallery.features.photoGallery

import android.app.Application
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import androidx.work.*
import com.bignerdranch.android.photogallery.data.model.GalleryItem
import com.bignerdranch.android.photogallery.data.model.GalleryType
import com.bignerdranch.android.photogallery.data.PhotoRepository
import com.bignerdranch.android.photogallery.app.GalleryPreferences
import com.bignerdranch.android.photogallery.features.polling.PollPhotosWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val POLL_WORK = "POLL_WORK"

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {

    private val photoRepository = PhotoRepository.get()

    //region LiveData
    private val galleryTypeLiveData = MutableLiveData(GalleryType.ONLINE)

    val galleryLiveData: LiveData<List<GalleryItem>> =
        Transformations.switchMap(galleryTypeLiveData) { galleryType ->
            if(galleryType == GalleryType.ONLINE) {
                onlineGalleryLiveData
            } else {
                favoriteGalleryLiveData
            }
        }

    private val onlineGalleryLiveData: LiveData<List<GalleryItem>>
    private val favoriteGalleryLiveData: LiveData<List<GalleryItem>>
    private val searchQueryLiveData = MutableLiveData("")
    //endregion

    init {
        searchQueryLiveData.value = GalleryPreferences.getStoredQuery(app)

        onlineGalleryLiveData =
            Transformations.switchMap(searchQueryLiveData) { searchTerm ->
                if(searchTerm.isBlank()) {
                    photoRepository.fetchInterestingPhotos()
                } else {
                    photoRepository.searchPhotos(searchTerm)
                }
            }

        favoriteGalleryLiveData = photoRepository.getGalleryItems()
    }

    //region Search
    fun searchPhotos(query: String) {
        changeQuery(query)

        GalleryPreferences.setStoredQuery(app, query)

        Timber.d("stored query: $query")
    }

    fun liveSearchPhotos(query: String) {
        changeQuery(query)

        if(query.isBlank()) {
            GalleryPreferences.setStoredQuery(app, "")
            Timber.d("empty query")
        }
    }

    fun setInitialQuery(searchView: SearchView) {
        val lastQuery: String = GalleryPreferences.getStoredQuery(app)

        searchView.apply {
            setQuery(lastQuery, true)
            if(lastQuery.isNotBlank()) isIconified = false
            clearFocus()
        }

        Timber.d("lastQuery: $lastQuery")
    }

    private fun changeQuery(query: String) {
        searchQueryLiveData.value = query
    }
    //endregion

    //region Polling
    fun togglePolling() {
        val pollingActive = GalleryPreferences.isPolling(app)

        if(pollingActive) {
            WorkManager.getInstance().cancelUniqueWork(POLL_WORK)
        } else {
            createPeriodicPollingWork()
        }

        GalleryPreferences.setPolling(app, !pollingActive)
    }

    private fun createPeriodicPollingWork() {
        Timber.d("Polling: setting up")

        val pollConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest
            .Builder(PollPhotosWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(pollConstraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            POLL_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
    //endregion

    fun switchGalleryTo(newGalleryType: GalleryType) {
        GalleryPreferences.setGalleryType(app, newGalleryType)
        galleryTypeLiveData.value = newGalleryType
    }

}