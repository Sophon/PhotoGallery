package com.bignerdranch.android.photogallery.api.flickr

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchInterestingness(): Call<FlickrResponse>
}