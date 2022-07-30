package com.adyen.android.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.repository.PlanetaryRepositoryImpl
import com.adyen.android.assignment.data.usecases.FavouriteDbUseCases
import com.adyen.android.assignment.utils.Constants
import com.adyen.android.assignment.utils.DispatcherProviders
import com.adyen.android.assignment.utils.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodsViewModel @Inject constructor(
    private val planetaryRepositoryImpl: PlanetaryRepositoryImpl,
    private val dispatchers: DispatcherProviders,
    private val podUseCases: FavouriteDbUseCases
): ViewModel()  {

    sealed class PodsEvent {
        class Success(val latestPods: List<AstronomyPicture>, val favouritePods: List<AstronomyPicture>) : PodsEvent()
        class Error(val errorText: String): PodsEvent()
        object Loading: PodsEvent()
        object Empty: PodsEvent()
    }

    private var getFavouritesJob: Job? = null

    private var filterBy: Constants.PodsFilter = Constants.PodsFilter.TITLE

    private var tempLatestPods: MutableList<AstronomyPicture> = ArrayList()
    private var tempFavouritePods: MutableList<AstronomyPicture> = ArrayList()

    private val _fetchLatestAndFavourites = MutableStateFlow<PodsEvent>(PodsEvent.Empty)
    val fetchLatestAndFavourites: StateFlow<PodsEvent> = _fetchLatestAndFavourites

   init {
       getFavourites()
   }

    fun pinFavourite() {
        updateLatestAndFavouritesState()
    }

    private fun updateLatestAndFavouritesState(){
        val latestAndFavourites = getLatestAndFavourites()
        latestAndFavourites.let {
            /* index 0 returns latest list
            *  index 1 returns favourites list */
            _fetchLatestAndFavourites.value = PodsEvent.Success(it[0], it[1])
        }
    }

    private fun getFavourites() {
        //to cancel the old coroutine observing the database every time the method is called
        getFavouritesJob?.cancel()
        getFavouritesJob = podUseCases.getFavouriteUseCase(filterBy).onEach {
                            favourites ->
                             tempFavouritePods = favourites.toMutableList()
                        }.launchIn(viewModelScope)
    }

    fun fetchLatest() {
        viewModelScope.launch(dispatchers.io) {
            _fetchLatestAndFavourites.value = PodsEvent.Loading

            when (val response = planetaryRepositoryImpl.getPods()) {
                is NetworkResource.Error -> {
                    response.message?.let {
                        _fetchLatestAndFavourites.value = PodsEvent.Error(it)
                    }
                }

                is NetworkResource.Success -> {

                    tempLatestPods =  response.data?.toMutableList() ?: ArrayList()

                    updateLatestAndFavouritesState()
                }
            }

        }
    }

    private fun getLatestAndFavourites(): List<List<AstronomyPicture>>{
        val latestAndFavourites:  MutableList<List<AstronomyPicture>> = ArrayList()
        //pop favourites if it exists in the data list from api
        val filterUnFavouriteLatest = tempLatestPods.filter { item -> !tempFavouritePods.contains(item) }

        //add filter sort value on the list
        val latestFilterSort = addFilter(filterUnFavouriteLatest, filterBy)
        val favouriteFilterSort = addFilter(tempFavouritePods, filterBy)

        latestAndFavourites.add(latestFilterSort)
        latestAndFavourites.add(favouriteFilterSort)

        return latestAndFavourites
    }

    fun applyFilter(filterSet: Constants.PodsFilter) {
        filterBy = filterSet

        updateLatestAndFavouritesState()
    }

    private fun addFilter(list: List<AstronomyPicture>, filterSet: Constants.PodsFilter): List<AstronomyPicture>{
        return when (filterSet) {
            Constants.PodsFilter.TITLE -> {
                list.sortedBy { it.title.lowercase() }
            }

            Constants.PodsFilter.DATE -> {
                list.sortedByDescending { it.date }
            }
        }
    }
}