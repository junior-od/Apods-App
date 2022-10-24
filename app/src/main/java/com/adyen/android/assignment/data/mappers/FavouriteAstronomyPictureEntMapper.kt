package com.adyen.android.assignment.data.mappers

import com.adyen.android.assignment.data.remote.api.model.AstronomyPicture
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity

object FavouriteAstronomyPictureEntMapper {

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