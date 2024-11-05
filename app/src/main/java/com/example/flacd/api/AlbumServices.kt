package com.example.flacd.api

import com.example.flacd.api.model.AlbumData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumServices{
    @GET("search")
    fun getMostWantedAlbums(
        @Query("token") token: String,
        @Query("type") type: String = "release",
        @Query("sort") sort: String = "want",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 50
    ): Call<AlbumData>
}