package com.adyen.android.assignment.utils

sealed class NetworkResource<T>(val data: T?, val message: String?) {
    class Success<T>(data: T): NetworkResource<T>(data, null)
    class Error<T>(message: String): NetworkResource<T>(null, message)
}