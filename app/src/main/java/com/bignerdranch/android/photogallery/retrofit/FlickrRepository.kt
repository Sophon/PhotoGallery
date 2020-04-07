package com.bignerdranch.android.photogallery.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.api.flickr.FlickrApi
import com.bignerdranch.android.photogallery.api.flickr.FlickrResponse
import com.bignerdranch.android.photogallery.model.GalleryItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val TAG = "FlickrRepository"

class FlickrRepository {
    //region Private vars
    private val flickrApi: FlickrApi
    //endregion

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }
}