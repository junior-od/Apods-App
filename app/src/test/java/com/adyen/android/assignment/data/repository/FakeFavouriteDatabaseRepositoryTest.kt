package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.database.model.FavouriteAstronomyPictureEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * create a fake of the FavouriteDatabaseRepository for test
 * without manipulating with actual database of the app
 * */
class FakeFavouriteDatabaseRepositoryTest: FavouriteDatabaseRepository {


    private val favouritesAstronomyPictures = mutableListOf<FavouriteAstronomyPictureEntity>()

    override fun getAllFavourites(): Flow<List<FavouriteAstronomyPictureEntity>> {
        return flow { emit(favouritesAstronomyPictures) }
    }

    override suspend fun insertFavouritePod(favouriteAstronomyPictureEntity: FavouriteAstronomyPictureEntity) {
        favouritesAstronomyPictures.add(favouriteAstronomyPictureEntity)
    }

    override suspend fun deleteFavouritePod(
        title: String,
        explanation: String,
        mediaType: String,
        url: String,
        date: String
    ) {
        // find the item that has all the pod details
        val findPod = favouritesAstronomyPictures.filter { it.title.equals(title)
                && it.explanation.equals(explanation)
                && it.mediaType.equals(mediaType)
                && it.url.equals(url)
                && it.date.equals(date)}

        if (findPod.isNotEmpty()){
            favouritesAstronomyPictures.remove(findPod[0])
        }
    }


}