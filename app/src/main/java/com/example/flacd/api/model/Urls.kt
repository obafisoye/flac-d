package com.example.flacd.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Urls(
    @Json(name = "last")
    val last: String,
    @Json(name = "next")
    val next: String
)