package com.adyen.android.assignment

import com.adyen.android.assignment.data.remote.api.model.DayAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TestServiceGenerator {

    fun <T> getService(serviceClass: Class<T>, baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(getMoshiConverterFactory())
        .build()
        .create(serviceClass)

    private fun getMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi
            .Builder()
            .add(DayAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return MoshiConverterFactory.create(moshi)
    }



}