package com.adyen.android.assignment.data.mappers

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.database.model.FavouriteAstronomyPictureEntity

class FavouriteAstronomyPictureEntMapper {

    companion object {
        fun mapToFavouriteAstronomyPictureEntity(astronomyPicture: AstronomyPicture): FavouriteAstronomyPictureEntity {
            return FavouriteAstronomyPictureEntity(
                astronomyPicture.title,
                astronomyPicture.explanation,
                astronomyPicture.date,
                astronomyPicture.url,
                astronomyPicture.mediaType,
            )
        }
    }
}