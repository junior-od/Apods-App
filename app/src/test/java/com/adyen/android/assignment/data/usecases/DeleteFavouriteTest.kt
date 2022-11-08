package com.adyen.android.assignment.data.usecases

import com.adyen.android.assignment.data.repository.FakeFavouriteDatabaseRepositoryTest
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity
import com.adyen.android.assignment.domain.usecases.DeleteFavourite
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class DeleteFavouriteTest {

    private lateinit var deleteFavourite: DeleteFavourite
    private lateinit var fakeFavouriteDatabaseRepositoryTest: FakeFavouriteDatabaseRepositoryTest

    @Before
    fun setup() {
        fakeFavouriteDatabaseRepositoryTest = FakeFavouriteDatabaseRepositoryTest()
        deleteFavourite = DeleteFavourite(fakeFavouriteDatabaseRepositoryTest)

        runTest{
            fakeFavouriteDatabaseRepositoryTest.insertFavouritePod(
                FavouriteAstronomyPictureEntity(
                    "Stars",
                    "Shine brighter than diamonds",
                    "2022-10-11",
                    "http://someurl",
                    "image",
                    id = 1
                )
            )
        }

    }

    /**
     * ensure remove favourite
     * */
    @Test
    fun `delete favourite astronomy picture returns true`() = runTest {
        val favouriteAstronomyPictureEntity = FavouriteAstronomyPictureEntity(
                "Stars",
                "Shine brighter than diamonds",
                "2022-10-11",
                "http://someurl",
                "image",
                 id = 1
            )

        fakeFavouriteDatabaseRepositoryTest.deleteFavouritePod(
            title = favouriteAstronomyPictureEntity.title,
            explanation = favouriteAstronomyPictureEntity.explanation,
            date = favouriteAstronomyPictureEntity.date,
            url = favouriteAstronomyPictureEntity.url,
            mediaType = favouriteAstronomyPictureEntity.mediaType
        )

        //get favourites
        val favourites = fakeFavouriteDatabaseRepositoryTest.getAllFavourites().first()

        assertThat(favourites).doesNotContain(favouriteAstronomyPictureEntity)


    }

}