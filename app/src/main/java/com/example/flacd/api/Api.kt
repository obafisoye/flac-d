package com.example.flacd.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {

    // Discogs API base url
    private val BASE_URL = "https://api.discogs.com/database/"

    // Moshi object to parse JSON data
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Retrofit instance for Discogs API
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    // Lazy initialization of the AlbumServices interface
    val retrofitService: AlbumServices by lazy {
        retrofit.create(AlbumServices::class.java)
    }


}