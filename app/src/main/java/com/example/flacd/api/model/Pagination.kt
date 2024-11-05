package com.example.flacd.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(
    @Json(name = "items")
    val items: Int,
    @Json(name = "page")
    val page: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "per_page")
    val perPage: Int,
    @Json(name = "urls")
    val urls: Urls
)