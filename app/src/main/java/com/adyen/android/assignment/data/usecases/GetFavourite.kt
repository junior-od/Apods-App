package com.adyen.android.assignment.data.usecases

import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.mappers.AstronomyPictureMapper
import com.adyen.android.assignment.data.repository.FavouriteDatabaseRepository
import com.adyen.android.assignment.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavourite(
    private val repository: FavouriteDatabaseRepository
) {

    //decide the filter to apply when getting favourites from db
    operator fun invoke(podsFilter: Constants.PodsFilter = Constants.PodsFilter.TITLE): Flow<List<AstronomyPicture>>{

        return repository.getAllFavourites().map {
            favourties ->
            when (podsFilter) {
                Constants.PodsFilter.TITLE ->
                  AstronomyPictureMapper.mapAPodEntityToAstronomyPicture(favourties).sortedBy { it.title }
                Constants.PodsFilter.DATE ->
                    AstronomyPictureMapper.mapAPodEntityToAstronomyPicture(favourties).sortedByDescending { it.date }
            }
        }

    }

}