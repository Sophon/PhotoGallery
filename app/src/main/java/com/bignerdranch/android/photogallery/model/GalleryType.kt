package com.bignerdranch.android.photogallery.model

import java.lang.Exception

enum class GalleryType {
    LOCAL,
    ONLINE;

    companion object {
        fun toGalleryType(galleryTypeString: String?): GalleryType {
            return try {
                valueOf(galleryTypeString ?: "")
            } catch(e: Exception) {
                ONLINE
            }
        }
    }
}