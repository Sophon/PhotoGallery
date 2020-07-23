package com.bignerdranch.android.photogallery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.photogallery.data.model.GalleryItem
import com.bignerdranch.android.photogallery.data.database.GalleryItemDao

@Database(entities = [ GalleryItem::class ], version=1)
abstract class GalleryItemDatabase: RoomDatabase() {

    abstract fun galleryItemDao(): GalleryItemDao
}