package com.adyen.android.assignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.data.repository.PlanetaryRepositoryImpl
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

    private var tempLatestPods: MutableList<AstronomyPicture> = ArrayList()

    private val _fetchLatestAndFavourites = MutableStateFlow<PodsEvent>(PodsEvent.Empty)
    val fetchLatestAndFavourites: StateFlow<PodsEvent> = _fetchLatestAndFavourites

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

                        _fetchLatestAndFavourites.value = PodsEvent.Success(tempLatestPods, ArrayList())
                    }
                }
            }

        }
    }
}