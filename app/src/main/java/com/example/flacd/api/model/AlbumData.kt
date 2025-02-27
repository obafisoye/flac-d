package com.example.flacd.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents an album's information as received from the Discogs API
 */
@JsonClass(generateAdapter = true)
data class AlbumData(
//    @Json(name = "pagination")
//    val pagination: Pagination,
    @Json(name = "results")
    val results: List<Album>
)