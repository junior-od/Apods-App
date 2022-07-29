package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.database.model.FavouriteAstronomyPictureEntity
import kotlinx.coroutines.flow.Flow

interface FavouriteDatabaseRepository {
    fun getAllFavourites(): Flow<List<FavouriteAstronomyPictureEntity>>

    suspend fun insertFavouritePod(favouriteAstronomyPictureEntity: FavouriteAstronomyPictureEntity)

    suspend fun deleteFavouritePod(title: String ,explanation: String, mediaType: String, url: String, date: String)
}