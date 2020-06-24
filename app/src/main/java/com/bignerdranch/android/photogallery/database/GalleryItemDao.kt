package com.bignerdranch.android.photogallery.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bignerdranch.android.photogallery.model.GalleryItem

@Dao
interface GalleryItemDao {

    @Query("SELECT * FROM galleryitem")
    fun getGalleryItems(): LiveData<List<GalleryItem>>

    @Insert
    fun addGalleryItem(galleryItem: GalleryItem)
}