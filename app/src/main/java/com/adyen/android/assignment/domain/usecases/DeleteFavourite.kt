package com.adyen.android.assignment.domain.usecases

import com.adyen.android.assignment.data.remote.api.model.AstronomyPicture
import com.adyen.android.assignment.data.repository.FavouriteDatabaseRepository

class DeleteFavourite(
    private val repository: FavouriteDatabaseRepository
) {

   suspend operator fun invoke(astronomyPicture: AstronomyPicture){

       repository.deleteFavouritePod(astronomyPicture.title,
            astronomyPicture.explanation,
            astronomyPicture.mediaType,
            astronomyPicture.url,
            astronomyPicture.date)
   }
}