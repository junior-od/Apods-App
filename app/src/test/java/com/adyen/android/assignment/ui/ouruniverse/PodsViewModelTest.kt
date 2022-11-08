package com.adyen.android.assignment.ui.ouruniverse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.adyen.android.assignment.MainCoroutineRule
import com.adyen.android.assignment.MockResponseFileReader
import com.adyen.android.assignment.TestServiceGenerator
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity
import com.adyen.android.assignment.data.remote.api.PlanetaryService
import com.adyen.android.assignment.data.repository.FakeFavouriteDatabaseRepositoryTest
import com.adyen.android.assignment.data.repository.FavouriteDatabaseRepository
import com.adyen.android.assignment.data.repository.PlanetaryRepositoryImpl
import com.adyen.android.assignment.domain.usecases.DeleteFavourite
import com.adyen.android.assignment.domain.usecases.FavouriteDbUseCases
import com.adyen.android.assignment.domain.usecases.GetFavourite
import com.adyen.android.assignment.domain.usecases.InsertFavourite
import com.adyen.android.assignment.getOrAwaitValue
import com.adyen.android.assignment.utils.DispatcherProviders
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PodsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var server: MockWebServer

    private val responsesPath = "responses/"

    private lateinit var podsViewModel: PodsViewModel

    private lateinit var dispatcherProviders: DispatcherProviders

    private lateinit var favouriteDbUseCasesTest: FavouriteDbUseCases

    lateinit var deleteFavourite: DeleteFavourite
    private lateinit var getFavourite: GetFavourite
    lateinit var insertFavourite: InsertFavourite

    private lateinit var favouriteDatabaseRepository: FavouriteDatabaseRepository

    lateinit var planetaryRepository: PlanetaryRepositoryImpl

    lateinit var planetaryService: PlanetaryService



    @Before
    fun setUp() {
        //mock server behaviour
        server = MockWebServer()

        val baseUrl = server.url("/").toString()

        planetaryService = TestServiceGenerator.getService(PlanetaryService::class.java, baseUrl)

        planetaryRepository = PlanetaryRepositoryImpl(planetaryService)

        //setup testdispatcher instance
        dispatcherProviders = object: DispatcherProviders {
            override val main: CoroutineDispatcher
                get() = TestCoroutineDispatcher()
            override val io: CoroutineDispatcher
                get() = TestCoroutineDispatcher()
            override val default: CoroutineDispatcher
                get() = TestCoroutineDispatcher()
            override val unconfined: CoroutineDispatcher
                get() = TestCoroutineDispatcher()
        }

        //setup db use case

        favouriteDatabaseRepository = FakeFavouriteDatabaseRepositoryTest()

        deleteFavourite = DeleteFavourite(favouriteDatabaseRepository)
        insertFavourite = InsertFavourite(favouriteDatabaseRepository)
        getFavourite = GetFavourite(favouriteDatabaseRepository)

        favouriteDbUseCasesTest = FavouriteDbUseCases(
            getFavouriteUseCase = getFavourite,
            insertFavourite = insertFavourite,
            deleteFavourite = deleteFavourite
        )

        val favouriteToInsert = mutableListOf<FavouriteAstronomyPictureEntity>()

        favouriteToInsert.add(
            FavouriteAstronomyPictureEntity(
                "Stars",
                "Shine brighter than diamonds",
                "2022-10-11",
                "http://someurl",
                "image",
                id = 1
            )
        )

        favouriteToInsert.shuffle()

        runTest {
            favouriteToInsert.forEach {
                favouriteDatabaseRepository.insertFavouritePod(it)
            }
        }

        //mock view model instance
        podsViewModel = PodsViewModel(
            planetaryRepositoryImpl = planetaryRepository,
            dispatchers = dispatcherProviders,
            podUseCases = favouriteDbUseCasesTest
        )

    }

    /**
     * test that app fetch latest pods and
     * favourite pods successfully
     * */
    @Test
    fun `fetch favourites and latest pods and display to the view  is successful`() = runBlocking {

        //get response
        val responseBody = MockResponseFileReader.content("${responsesPath}PlanetarySuccessResponse.json")

        //enqueue the fake response
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        podsViewModel.fetchLatest()

        //check that its loading
        val loading = podsViewModel.fetchLatestAndFavourites.asLiveData().getOrAwaitValue()
        assertThat(loading).isEqualTo(PodsViewModel.PodsEvent.Loading)

        delay(20)

        val fetchEvent = podsViewModel.fetchLatestAndFavourites.asLiveData().getOrAwaitValue()

        //check that it emits success result
        assertThat((fetchEvent as PodsViewModel.PodsEvent.Success).latestPods).isNotNull()
        assertThat(fetchEvent.favouritePods).isNotNull()
    }

    /**
     * test that the app returns Error Pod Event
     * due to invalid api key or no internet
     * */
    @Test
    fun `invalid api key or no internet returns Error Pod Event`() = runBlocking {
        //get response
        val responseBody = MockResponseFileReader.content("${responsesPath}PlanetaryInvalidApiResponse.json")

        //enqueue the fake response
        server.enqueue(MockResponse().setResponseCode(403).setBody(responseBody))
        podsViewModel.fetchLatest()

        //check that its loading
        val loading = podsViewModel.fetchLatestAndFavourites.asLiveData().getOrAwaitValue()
        assertThat(loading).isEqualTo(PodsViewModel.PodsEvent.Loading)

        delay(20)

        val fetchEvent = podsViewModel.fetchLatestAndFavourites.asLiveData().getOrAwaitValue()

        assertThat((fetchEvent as PodsViewModel.PodsEvent.Error).errorText).isNotEmpty()

    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}