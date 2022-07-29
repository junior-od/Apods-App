package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.mappers.AstronomyPictureMapper
import com.adyen.android.assignment.utils.NetworkResource
import javax.inject.Inject

class PlanetaryRepositoryImpl @Inject constructor(private val planetaryService: PlanetaryService): PlanetaryRepository {
    override suspend fun getPods(): NetworkResource<List<AstronomyPicture>> {
        return try {
            val response = planetaryService.getPictures()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                val resultMapper = AstronomyPictureMapper.mapToAstronomyPicture(result)
                val sanitizeImagesResult = resultMapper.filter { pod -> pod.mediaType == "image" }

                sanitizeImagesResult.let {
                    NetworkResource.Success(it)
                }

            } else {
                NetworkResource.Error("Error Occurred")
            }
        } catch (e: Exception) {
            NetworkResource.Error(e.message ?: "An Error Occurred")
        }
    }


}