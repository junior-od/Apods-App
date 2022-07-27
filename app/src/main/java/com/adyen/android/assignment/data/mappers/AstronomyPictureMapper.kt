package com.adyen.android.assignment.data.mappers

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.api.model.AstronomyPictureResponse

class AstronomyPictureMapper {

    companion object {

        fun mapToAstronomyPicture(responseList: List<AstronomyPictureResponse>): List<AstronomyPicture> {
            var astronomyPictureList: MutableList<AstronomyPicture> = ArrayList()

            for (response in responseList) {
                astronomyPictureList.add(AstronomyPicture(response.title, response.explanation,
                    response.date, response.url, response.mediaType))
            }

            return astronomyPictureList
        }
    }
}