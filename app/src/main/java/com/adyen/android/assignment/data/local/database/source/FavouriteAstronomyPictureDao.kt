package com.adyen.android.assignment.data.local.database.source

import androidx.room.*
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteAstronomyPictureDao {

    @Query("SELECT * FROM FavouriteAstronomyPictureEntity")
    fun getAllFavourites(): Flow<List<FavouriteAstronomyPictureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouritePod(favouriteAstronomyPictureEntity: FavouriteAstronomyPictureEntity)

    @Query("DELETE FROM FavouriteAstronomyPictureEntity WHERE title = :title AND explanation= :explanation AND mediaType= :mediaType AND url= :url AND date= :date")
    suspend fun deleteFavouritePod(title: String ,explanation: String, mediaType: String, url: String, date: String)

}