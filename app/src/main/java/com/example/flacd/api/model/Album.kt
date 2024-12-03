package com.example.flacd.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents an album's information as received from the Discogs API
 */
@JsonClass(generateAdapter = true)
data class Album(
//    @Json(name = "barcode")
//    val barcode: List<String>,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "country")
    val country: String?=null,
    @Json(name = "cover_image")
    val coverImage: String?=null,
    @Json(name = "format")
    val format: List<String>?=null,
    @Json(name = "genre")
    val genre: List<String>?=null,
    @Json(name = "label")
    val label: List<String>?=null,
    @Json(name = "style")
    val style: List<String>?=null,
    @Json(name = "thumb")
    val thumb: String?=null,
    @Json(name = "title")
    val title: String?=null,
    @Json(name = "type")
    val type: String?=null,
    @Json(name = "year")
    val year: String?=null
)