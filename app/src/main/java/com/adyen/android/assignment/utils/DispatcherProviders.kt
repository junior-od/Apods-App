package com.adyen.android.assignment.utils

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProviders {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}