package com.bignerdranch.android.photogallery.view.webContent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.retrofit.PhotoRepository

class PhotoPageViewModel: ViewModel() {

    private val photoRepository = PhotoRepository.get()
    private var galleryItemIdLiveData = MutableLiveData<String>()
    var savedGalleryItemLiveData: LiveData<GalleryItem?> =
        Transformations.switchMap(galleryItemIdLiveData) { itemId ->
            photoRepository.getGalleryItem(itemId)
        }

    fun favoritePhoto(galleryItem: GalleryItem) {
        photoRepository.saveGalleryItem(galleryItem)
    }

    fun unfavoritePhoto(galleryItem: GalleryItem) {
        photoRepository.unsaveGalleryItem(galleryItem)
    }

    fun checkIfPhotoFavorited(galleryItemId: String) {
        galleryItemIdLiveData.value = galleryItemId
    }
}