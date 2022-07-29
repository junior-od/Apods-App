package com.adyen.android.assignment.di

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.data.repository.PlanetaryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlanetaryModule {

    @Singleton
    @Provides
    fun providePlanetaryService(retrofit: Retrofit): PlanetaryService = retrofit
        .create(PlanetaryService::class.java)

    @Singleton
    @Provides
    fun providePlanetaryRepositoryImpl(planetaryService: PlanetaryService) = PlanetaryRepositoryImpl(planetaryService)
}