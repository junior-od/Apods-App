package com.adyen.android.assignment.di

import android.app.Application
import androidx.room.Room
import com.adyen.android.assignment.data.repository.FavouriteDatabaseRepository
import com.adyen.android.assignment.data.repository.FavouriteDatabaseRepositoryImpl
import com.adyen.android.assignment.domain.usecases.DeleteFavourite
import com.adyen.android.assignment.domain.usecases.FavouriteDbUseCases
import com.adyen.android.assignment.domain.usecases.GetFavourite
import com.adyen.android.assignment.domain.usecases.InsertFavourite
import com.adyen.android.assignment.data.local.database.source.FavouriteAstronomyPictureDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFavouriteAstronomyPictureDatabase(app: Application): FavouriteAstronomyPictureDatabase {
        return Room.databaseBuilder(app,
            FavouriteAstronomyPictureDatabase::class.java,
            FavouriteAstronomyPictureDatabase.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideFavouriteDatabaseRepositoryImpl(database: FavouriteAstronomyPictureDatabase): FavouriteDatabaseRepository {
        return FavouriteDatabaseRepositoryImpl(database.favouriteAstronomyPictureDao)
    }

    @Singleton
    @Provides
    fun provideFavouriteDbUseCases(repository: FavouriteDatabaseRepository): FavouriteDbUseCases {
        return FavouriteDbUseCases(
            getFavouriteUseCase = GetFavourite(repository),
            deleteFavourite = DeleteFavourite(repository),
            insertFavourite = InsertFavourite(repository)
        )
    }


}