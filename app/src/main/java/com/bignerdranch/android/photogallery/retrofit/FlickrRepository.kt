package com.bignerdranch.android.photogallery.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.api.flickr.FlickrApi
import com.bignerdranch.android.photogallery.api.flickr.FlickrResponse
import com.bignerdranch.android.photogallery.api.flickr.PhotoResponse
import com.bignerdranch.android.photogallery.model.GalleryItem
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrRepository"

class FlickrRepository {
    //region Private vars
    private val flickrApi: FlickrApi
    private lateinit var flickrRequest: Call<FlickrResponse>
    //endregion

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
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
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<FlickrResponse>,
                    response: Response<FlickrResponse>
                ) {
                    val photoResponse: PhotoResponse? = response.body()?.photos
                    var galleryItems: List<GalleryItem> =
                        photoResponse?.galleryItems ?: mutableListOf()
                    galleryItems = galleryItems.filterNot { it.url.isBlank() }

                    responseLiveData.value = galleryItems
                }
            }
        )

        return responseLiveData
    }
    //endregion
}