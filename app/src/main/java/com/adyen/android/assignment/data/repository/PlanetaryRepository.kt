package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.utils.NetworkResource

interface PlanetaryRepository {

    suspend fun getPods(): NetworkResource<List<AstronomyPicture>>
}