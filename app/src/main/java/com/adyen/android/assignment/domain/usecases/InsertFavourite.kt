package com.adyen.android.assignment.domain.usecases

import com.adyen.android.assignment.data.remote.api.model.AstronomyPicture
import com.adyen.android.assignment.data.mappers.FavouriteAstronomyPictureEntMapper
import com.adyen.android.assignment.data.repository.FavouriteDatabaseRepository

class InsertFavourite(
    private val repository: FavouriteDatabaseRepository
) {

    suspend operator fun invoke(astronomyPicture: AstronomyPicture){
        val aPod = FavouriteAstronomyPictureEntMapper.mapToFavouriteAstronomyPictureEntity(astronomyPicture)
        repository.insertFavouritePod(aPod)
    }
}