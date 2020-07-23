package com.bignerdranch.android.photogallery.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchInterestingness(): Call<FlickrResponse>

    @GET("services/rest/?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>
}