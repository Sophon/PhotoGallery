package com.bignerdranch.android.photogallery.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.photogallery.model.GalleryItem

@Database(entities = [ GalleryItem::class ], version=1)
abstract class GalleryItemDatabase: RoomDatabase() {

    abstract fun galleryItemDao(): GalleryItemDao
}