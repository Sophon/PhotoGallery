package com.bignerdranch.android.photogallery.view.webContent

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.PhotoRepository

class PhotoPageViewModel: ViewModel() {

    private val photoRepository = PhotoRepository.get()

    fun savePhoto(galleryItem: GalleryItem) {
        photoRepository.addGalleryItem(galleryItem)
    }
}