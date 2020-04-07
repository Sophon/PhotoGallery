package com.bignerdranch.android.photogallery.viewModel.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.FlickrRepository

class PhotoGalleryViewModel: ViewModel() {
    //region Public vars
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    //endregion

    //region Private vars
    private val flickrRepository = FlickrRepository()
    //endregion

    init {
        galleryItemLiveData = flickrRepository.fetchInterestingPhotos()
    }
}