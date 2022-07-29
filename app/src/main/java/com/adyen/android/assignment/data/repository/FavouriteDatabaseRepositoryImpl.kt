package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.database.model.FavouriteAstronomyPictureEntity
import com.adyen.android.assignment.database.source.FavouriteAstronomyPictureDao
import kotlinx.coroutines.flow.Flow

class FavouriteDatabaseRepositoryImpl(private val dao: FavouriteAstronomyPictureDao): FavouriteDatabaseRepository {
    override fun getAllFavourites(): Flow<List<FavouriteAstronomyPictureEntity>> {
        return dao.getAllFavourites()
    }

    override suspend fun insertFavouritePod(favouriteAstronomyPictureEntity: FavouriteAstronomyPictureEntity) {
        return dao.insertFavouritePod(favouriteAstronomyPictureEntity)
    }

    override suspend fun deleteFavouritePod(title: String ,explanation: String, mediaType: String, url: String, date: String) {
        return dao.deleteFavouritePod(title ,explanation, mediaType, url, date)
    }

}