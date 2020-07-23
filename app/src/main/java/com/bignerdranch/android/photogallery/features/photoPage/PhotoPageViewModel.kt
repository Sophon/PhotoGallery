package com.bignerdranch.android.photogallery.features.photoPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.photogallery.data.model.GalleryItem
import com.bignerdranch.android.photogallery.data.PhotoRepository

class PhotoPageViewModel: ViewModel() {

    private val photoRepository = PhotoRepository.get()

    //region LiveData
    private var galleryItemIdLiveData = MutableLiveData<String>()
    var savedGalleryItemLiveData: LiveData<GalleryItem?> =
        Transformations.switchMap(galleryItemIdLiveData) { itemId ->
            photoRepository.getGalleryItem(itemId)
        }
    //endregion

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