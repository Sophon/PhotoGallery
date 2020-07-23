package com.bignerdranch.android.photogallery.app

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.bignerdranch.android.photogallery.data.model.GalleryType

private const val PREF_SEARCH_QUERY = "query"
private const val LAST_PHOTO_ID = "id"
private const val PREF_IS_POLLING = "isPolling"
private const val PREF_GALLERY_TYPE = "galleryType"

object GalleryPreferences {

    //region Query
    fun getStoredQuery(context: Context): String {
        val preference: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        return preference.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }

    fun getLastPhotoId(context: Context): String {
        val preference: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        return preference.getString(LAST_PHOTO_ID, "")!!
    }

    fun setLastPhotoId(context: Context, lastPhotoId: String) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(LAST_PHOTO_ID, lastPhotoId)
            .apply()
    }
    //endregion

    //region Polling
    fun isPolling(context: Context): Boolean {
        return PreferenceManager
            .getDefaultSharedPreferences(context)
            .getBoolean(PREF_IS_POLLING, false)
    }

    fun setPolling(context: Context, isPolling: Boolean) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PREF_IS_POLLING, isPolling)
            .apply()
    }
    //endregion

    //region Gallery
    fun getGalleryType(context: Context): GalleryType {
        val galleryString = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getString(PREF_GALLERY_TYPE, "")

        return GalleryType.toGalleryType(galleryString)
    }

    fun setGalleryType(context: Context, galleryType: GalleryType) {
        PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_GALLERY_TYPE, galleryType.toString())
            .apply()
    }
    //endregion
}