package com.bignerdranch.android.photogallery.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.api.flickr.FlickrApi
import com.bignerdranch.android.photogallery.api.flickr.FlickrResponse
import com.bignerdranch.android.photogallery.api.flickr.PhotoInterceptor
import com.bignerdranch.android.photogallery.api.flickr.PhotoResponse
import com.bignerdranch.android.photogallery.model.GalleryItem
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrRepository"

class FlickrRepository {
    //region Private vars
    private val flickrApi: FlickrApi
    private lateinit var flickrRequest: Call<FlickrResponse>
    //endregion

    init {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    //region Public funs
    fun fetchInterestingPhotos()
        : LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        flickrRequest = flickrApi.fetchInterestingness()

        flickrRequest.enqueue(
            object: Callback<FlickrResponse> {
                override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                    Log.e(TAG, "fetch(): failed to fetch photos", t)
                }

                override fun onResponse(
                    call: Call<FlickrResponse>,
                    response: Response<FlickrResponse>
                ) {
                    val photoResponse: PhotoResponse? = response.body()?.photos
                    var galleryItems: List<GalleryItem> =
                        photoResponse?.galleryItems ?: mutableListOf()
                    galleryItems = galleryItems.filterNot { it.url.isBlank() }

                    Log.d(TAG, "fetch(): galleryItems loaded, ${galleryItems.size} items")

                    responseLiveData.value = galleryItems
                }
            }
        )

        return responseLiveData
    }
    //endregion
}