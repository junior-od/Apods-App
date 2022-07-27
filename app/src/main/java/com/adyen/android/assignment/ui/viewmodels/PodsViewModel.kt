package com.adyen.android.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.repository.PlanetaryRepositoryImpl
import com.adyen.android.assignment.utils.Constants
import com.adyen.android.assignment.utils.DispatcherProviders
import com.adyen.android.assignment.utils.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodsViewModel @Inject constructor(
    private val planetaryRepositoryImpl: PlanetaryRepositoryImpl,
    private val dispatchers: DispatcherProviders
): ViewModel()  {

    sealed class PodsEvent {
        class Success(val latestPods: List<AstronomyPicture>, val favouritePods: List<AstronomyPicture>) : PodsEvent()
        class Error(val errorText: String): PodsEvent()
        object Loading: PodsEvent()
        object Empty: PodsEvent()
    }

    private var filterBy: Constants.PodsFilter = Constants.PodsFilter.TITLE

    private var tempLatestPods: MutableList<AstronomyPicture> = ArrayList()

    private val _fetchLatestAndFavourites = MutableStateFlow<PodsEvent>(PodsEvent.Empty)
    val fetchLatestAndFavourites: StateFlow<PodsEvent> = _fetchLatestAndFavourites

    private val _filterLatestAndFavourites = MutableStateFlow<PodsEvent>(PodsEvent.Empty)
    val filterLatestAndFavourites: StateFlow<PodsEvent> = _filterLatestAndFavourites

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
                    response.data?.let {

                        tempLatestPods = it.filter {pod -> pod.mediaType == "image" }.toMutableList() //sanitize list for images only

                        tempLatestPods.let { list ->
                            val tempLatestPodsByDefaultFilterValue = addFilter(list, filterBy)

                            tempLatestPodsByDefaultFilterValue.let { filteredLatestData ->


                                _fetchLatestAndFavourites.value = PodsEvent.Success(filteredLatestData, ArrayList())
                            }
                        }

                    }
                }
            }

        }
    }

    fun applyFilter(filterSet: Constants.PodsFilter) {
        viewModelScope.launch(dispatchers.io) {
            filterBy = filterSet
            tempLatestPods.let {
                list -> val tempLatestPodsByFilterValue = addFilter(list, filterSet)
                tempLatestPodsByFilterValue.let { filteredLatestData ->
                    _filterLatestAndFavourites.value =  PodsEvent.Success(filteredLatestData, ArrayList())
                }
            }
        }
    }

    private fun addFilter(list: List<AstronomyPicture>, filterSet: Constants.PodsFilter): List<AstronomyPicture>{
        return when (filterSet) {
            Constants.PodsFilter.TITLE -> {
                list.sortedBy { it.title }
            }

            Constants.PodsFilter.DATE -> {
                list.sortedByDescending { it.date }
            }
        }
    }
}