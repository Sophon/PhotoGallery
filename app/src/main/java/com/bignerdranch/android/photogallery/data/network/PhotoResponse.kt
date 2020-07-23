package com.bignerdranch.android.photogallery.data.network

import com.bignerdranch.android.photogallery.data.model.GalleryItem
import com.google.gson.annotations.SerializedName

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}