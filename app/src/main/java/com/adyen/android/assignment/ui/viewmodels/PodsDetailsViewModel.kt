package com.adyen.android.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.usecases.FavouriteDbUseCases
import com.adyen.android.assignment.utils.DispatcherProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodsDetailsViewModel @Inject constructor(
    private val dispatchers: DispatcherProviders,
    private val podUseCases: FavouriteDbUseCases
): ViewModel() {

   fun addFavorite(astronomyPicture: AstronomyPicture) {
       viewModelScope.launch(dispatchers.io) {
           podUseCases.insertFavourite.invoke(astronomyPicture)
       }
   }

   fun removeFavorite(astronomyPicture: AstronomyPicture) {
       viewModelScope.launch(dispatchers.io) {
           podUseCases.deleteFavourite.invoke(astronomyPicture)
       }
   }
}