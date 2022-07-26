package com.adyen.android.assignment.data.remote.api.model

import com.squareup.moshi.Json
import java.time.LocalDate

data class AstronomyPictureResponse(
    @Json(name = "service_version") val serviceVersion: String,
    val title: String,
    val explanation: String,
    val date: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "hdurl") val hdUrl: String?,
    val url: String,
)