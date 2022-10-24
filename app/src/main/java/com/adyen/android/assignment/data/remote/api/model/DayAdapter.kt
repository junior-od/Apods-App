package com.adyen.android.assignment.data.remote.api.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate


class DayAdapter {
    @ToJson
    fun toJson(date: LocalDate): String = date.toString()

    /**
     * Maps the [AstronomyPictureResponse.date] json string to a [LocalDate]
     */
    @FromJson
    fun fromJson(date: String): LocalDate = LocalDate.parse(date)
}