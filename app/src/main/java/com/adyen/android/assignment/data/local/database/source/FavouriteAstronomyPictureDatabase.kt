package com.adyen.android.assignment.data.local.database.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adyen.android.assignment.data.local.database.model.FavouriteAstronomyPictureEntity

@Database(entities = [FavouriteAstronomyPictureEntity::class], version = 1)
abstract class FavouriteAstronomyPictureDatabase: RoomDatabase() {

    abstract val favouriteAstronomyPictureDao: FavouriteAstronomyPictureDao

    companion object {
        const val DATABASE_NAME = "fav_db"
    }
}