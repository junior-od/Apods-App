package com.adyen.android.assignment.data.mappers

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.api.model.AstronomyPictureResponse
import com.adyen.android.assignment.database.model.FavouriteAstronomyPictureEntity

class AstronomyPictureMapper {

    companion object {

        fun mapToAstronomyPicture(responseList: List<AstronomyPictureResponse>): List<AstronomyPicture> {
            val astronomyPictureList: MutableList<AstronomyPicture> = ArrayList()

            for (response in responseList) {
                astronomyPictureList.add(AstronomyPicture(response.title, response.explanation,
                    response.date, response.url, response.mediaType))
            }

            return astronomyPictureList
        }

        fun mapAPodEntityToAstronomyPicture(apodEntity: List<FavouriteAstronomyPictureEntity>): List<AstronomyPicture> {
            val astronomyPictureList: MutableList<AstronomyPicture> = ArrayList()

            for (response in apodEntity) {
                astronomyPictureList.add(AstronomyPicture(response.title, response.explanation,
                    response.date, response.url, response.mediaType))
            }

            return astronomyPictureList
        }
    }
}