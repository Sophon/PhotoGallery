package com.bignerdranch.android.photogallery.viewModel.gallery

import android.app.Application
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.PhotoRepository
import com.bignerdranch.android.photogallery.sharedPreferences.QueryPreferences
import timber.log.Timber

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {
    //region Public vars
    val onlineGalleryLiveData: LiveData<List<GalleryItem>>
    val offlineGalleryLiveData: LiveData<List<GalleryItem>>
    //endregion

    //region Private vars
    private val photoRepository = PhotoRepository.get()
    private val searchQueryLiveData = MutableLiveData("")
    //endregion

    init {
        searchQueryLiveData.value = QueryPreferences.getStoredQuery(app)

        onlineGalleryLiveData =
            Transformations.switchMap(searchQueryLiveData) { searchTerm ->
                if(searchTerm.isBlank()) {
                    photoRepository.fetchInterestingPhotos()
                } else {
                    photoRepository.searchPhotos(searchTerm)
                }
            }

        offlineGalleryLiveData = photoRepository.getGalleryItems()
    }

    //region Public funs
    fun searchPhotos(query: String) {
        changeQuery(query)

        QueryPreferences.setStoredQuery(app, query)

        Timber.d("stored query: $query")
    }

    fun liveSearchPhotos(query: String) {
        changeQuery(query)

        if(query.isBlank()) {
            QueryPreferences.setStoredQuery(app, "")
            Timber.d("empty query")
        }
    }

    fun setInitialQuery(searchView: SearchView) {
        val lastQuery: String = QueryPreferences.getStoredQuery(app)

        searchView.apply {
            setQuery(lastQuery, true)
            if(lastQuery.isNotBlank()) isIconified = false
            clearFocus()
        }

        Timber.d("lastQuery: $lastQuery")
    }
    //endregion

    //region Private funs
    private fun changeQuery(query: String) {
        searchQueryLiveData.value = query
    }
    //endregion
}