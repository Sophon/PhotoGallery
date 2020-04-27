package com.bignerdranch.android.photogallery

import android.app.Application
import timber.log.Timber

class PhotoGalleryApplication: Application() {

    //region Lifecycle
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
    //endregion
}