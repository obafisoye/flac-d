package com.example.flacd.api

import com.example.flacd.api.model.AlbumData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface AlbumServices{

    // Retrieves a list of the most wanted albums based on a specific type, sorted by user demand.
    @GET("search")
    fun getMostWantedAlbums(
        @Query("token") token: String,
        @Query("type") type: String = "master",
        @Query("sort") sort: String = "want",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 50
    ): Call<AlbumData>

    // Retrieves a list of albums that match with the query
    @GET("search")
    fun searchAlbumByQuery(
        @Query("q") q: String,
        @Query("token") token: String,
        @Query("type") type: String = "master",
        @Query("format") format: String = "album",
        @Query("per_page") perPage: Int = 10,
    ): Call <AlbumData>

    // Retrieves a list of most wanted albums by style
    @GET("search")
    fun getAlbumsByStyle(
        @Query("token") token: String,
        @Query("style") style: String,
        @Query("type") type: String = "master",
        @Query("sort") sort: String = "want",
        @Query("per_page") perPage: Int = 7,
    ): Call <AlbumData>
}