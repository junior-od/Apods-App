package com.adyen.android.assignment.data.usecases

import com.adyen.android.assignment.data.repository.FakeFavouriteDatabaseRepositoryTest
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity
import com.adyen.android.assignment.utils.Constants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavouriteTest {

    private lateinit var getFavourite: GetFavourite
    private lateinit var fakeFavouriteDatabaseRepositoryTest: FakeFavouriteDatabaseRepositoryTest

    @Before
    fun setup() {
        fakeFavouriteDatabaseRepositoryTest = FakeFavouriteDatabaseRepositoryTest()
        getFavourite = GetFavourite(fakeFavouriteDatabaseRepositoryTest)

        val favouritesToInsert = mutableListOf<FavouriteAstronomyPictureEntity>()
        favouritesToInsert.add(
            FavouriteAstronomyPictureEntity(
                "Stars",
                "Shine brighter than diamonds",
                "2022-10-11",
                "http://someurl",
                "image",
                id = 1
            )
        )
        favouritesToInsert.add(
            FavouriteAstronomyPictureEntity(
                "Zari Moon",
                "Moon brighter than diamonds",
                "2009-10-11",
                "http://someurlmoon",
                "image",
                id = 2
            )
        )
        favouritesToInsert.add(
            FavouriteAstronomyPictureEntity(
                "Aba Sun",
                "Sun brighter than diamonds",
                "2021-01-11",
                "http://someurlsun",
                "image",
                id = 3
            )
        )

        favouritesToInsert.shuffle()

        //
        runTest {
            favouritesToInsert.forEach { fakeFavouriteDatabaseRepositoryTest.insertFavouritePod(it) }
        }

    }

    /**
     * ensure favourites order by
     * title in ascending order
     * */
    @Test
    fun `order favourites by title ascending returns correct ascending order`() = runTest{
        //get first emission
        val result = getFavourite.invoke(Constants.PodsFilter.TITLE).first()

        for (i in 0..result.size - 2) {
            assertThat(result[i].title.lowercase() <= result[i+1].title.lowercase()).isTrue()
        }
    }

    /**
     * ensure favourites order by
     * date in descending order
     */
    @Test
    fun `order favourites by date descending returns correct descending order`() = runTest {
        //get first emission
        val result = getFavourite.invoke(Constants.PodsFilter.DATE).first()

        for (i in 0..result.size -2) {
            assertThat(result[i].date >= result[i+1].date).isTrue()
        }

    }

}