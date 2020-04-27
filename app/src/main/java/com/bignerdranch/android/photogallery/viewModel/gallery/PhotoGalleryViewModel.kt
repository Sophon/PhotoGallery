package com.bignerdranch.android.photogallery.viewModel.gallery

import android.app.Application
import androidx.lifecycle.*
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.FlickrRepository

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {
    //region Public vars
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    //endregion

    //region Private vars
    private val flickrRepository = FlickrRepository()
    private val searchQueryLiveData = MutableLiveData("")
    //endregion

    init {
        galleryItemLiveData =
            Transformations.switchMap(searchQueryLiveData) { searchTerm ->
                if(searchTerm.isBlank()) {
                    flickrRepository.fetchInterestingPhotos()
                } else {
                    flickrRepository.searchPhotos(searchTerm)
                }
            }
    }

    //region Public funs
    fun searchPhotos(query: String) {
        searchQueryLiveData.value = query
    }
    //endregion
}