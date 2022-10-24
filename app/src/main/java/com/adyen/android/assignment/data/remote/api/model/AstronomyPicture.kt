package com.adyen.android.assignment.data.remote.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AstronomyPicture (
    val title: String,
    val explanation: String,
    val date: String,
    val url: String,
    val mediaType: String,
): Parcelable