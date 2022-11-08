package com.adyen.android.assignment.data.usecases

import com.adyen.android.assignment.data.remote.api.model.AstronomyPicture
import com.adyen.android.assignment.data.mappers.FavouriteAstronomyPictureEntMapper
import com.adyen.android.assignment.data.repository.FakeFavouriteDatabaseRepositoryTest
import com.adyen.android.assignment.domain.usecases.InsertFavourite
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertFavouriteTest{

    private lateinit var insertFavourite: InsertFavourite
    private lateinit var fakeFavouriteDatabaseRepositoryTest: FakeFavouriteDatabaseRepositoryTest

    @Before
    fun setup(){
        fakeFavouriteDatabaseRepositoryTest = FakeFavouriteDatabaseRepositoryTest()
        insertFavourite = InsertFavourite(fakeFavouriteDatabaseRepositoryTest)

    }

    /**
     * ensure insert favourite
     * */
    @Test
    fun `insert favourite astronomy picture returns true`() = runTest {
        val astronomyPicture = AstronomyPicture(
            "Stars",
            "Shine brighter than diamonds",
            "2022-10-11",
            "http://someurl",
            "image"
        )
        val astronomyPictureMapper = FavouriteAstronomyPictureEntMapper.mapToFavouriteAstronomyPictureEntity(astronomyPicture)
        fakeFavouriteDatabaseRepositoryTest.insertFavouritePod(astronomyPictureMapper)

        //get favourites
        val favourites = fakeFavouriteDatabaseRepositoryTest.getAllFavourites().first()

        assertThat(favourites).contains(astronomyPictureMapper)

    }


}