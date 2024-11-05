package com.example.flacd.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
//    @Json(name = "barcode")
//    val barcode: List<String>,

    @Json(name = "id")
    val id: Int,
    @Json(name = "country")
    val country: String,
    @Json(name = "cover_image")
    val coverImage: String,
    @Json(name = "format")
    val format: List<String>,
    @Json(name = "genre")
    val genre: List<String>,
    @Json(name = "label")
    val label: List<String>,
    @Json(name = "style")
    val style: List<String>,
    @Json(name = "thumb")
    val thumb: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "year")
    val year: String
)