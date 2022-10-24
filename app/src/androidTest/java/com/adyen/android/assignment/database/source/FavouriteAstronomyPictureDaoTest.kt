package com.adyen.android.assignment.database.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity
import com.adyen.android.assignment.data.local.database.source.FavouriteAstronomyPictureDao
import com.adyen.android.assignment.data.local.database.source.FavouriteAstronomyPictureDatabase
import com.adyen.android.assignment.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavouriteAstronomyPictureDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: FavouriteAstronomyPictureDatabase
    private lateinit var dao: FavouriteAstronomyPictureDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavouriteAstronomyPictureDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.favouriteAstronomyPictureDao
    }

    @After
    fun teardown(){
        database.close()
    }

    /**
     * ensure insertion of favourite astronomy picture
     * in the db works
     * */
    @Test
    fun insertFavouriteAstronomyPicture_returnsTrue() = runTest {
        val favouriteAstronomyPictureEntity = FavouriteAstronomyPictureEntity(
            "Stars",
            "Shine brighter than diamonds",
            "2021-10-11",
            "http://someurl",
            "image",
            id = 1
        )

        dao.insertFavouritePod(favouriteAstronomyPictureEntity)

        /**
            convert flow to livedata here and wait for default
            2 seconds to observe the data from db
        */
        val allFavouriteAstronomyPictures = dao.getAllFavourites()
                                            .asLiveData()
                                            .getOrAwaitValue()


        assert(allFavouriteAstronomyPictures.contains(favouriteAstronomyPictureEntity))

    }

    /**
     * ensure deletion of an astronomy picture item
     * from the db works
     * */
    @Test
    fun deleteFavouriteAstronomyPicture_returnsTrue() = runTest {
        val favouriteAstronomyPictureEntity = FavouriteAstronomyPictureEntity(
            "Stars",
            "Shine brighter than diamonds",
            "2021-10-11",
            "http://someurl",
            "image",
            id = 1
        )

        dao.insertFavouritePod(favouriteAstronomyPictureEntity)

        dao.deleteFavouritePod(favouriteAstronomyPictureEntity.title,
                                favouriteAstronomyPictureEntity.explanation,
                                favouriteAstronomyPictureEntity.mediaType,
                                favouriteAstronomyPictureEntity.url,
                                favouriteAstronomyPictureEntity.date
                            )

        /**
        convert flow to livedata here and wait for default
        2 seconds to observe the data from db
         */
        val allFavouriteAstronomyPictures = dao.getAllFavourites()
            .asLiveData()
            .getOrAwaitValue()


        assertThat(allFavouriteAstronomyPictures).doesNotContain(favouriteAstronomyPictureEntity)

    }

    /**
     * get favourite Astronomy pictures
     * with items after inserting
     * */
    @Test
    fun getFavouriteAstronomyPicturesWithItems_returnsTrue() = runTest {
        val favouriteAstronomyPictureEntity = FavouriteAstronomyPictureEntity(
            "Stars",
            "Shine brighter than diamonds",
            "2021-10-11",
            "http://someurl",
            "image",
            id = 1
        )

        dao.insertFavouritePod(favouriteAstronomyPictureEntity)

        /**
        convert flow to livedata here and wait for default
        2 seconds to observe the data from db
         */
        val allFavouriteAstronomyPictures = dao.getAllFavourites()
            .asLiveData()
            .getOrAwaitValue()

        assertThat(allFavouriteAstronomyPictures).isNotEmpty()

    }

}