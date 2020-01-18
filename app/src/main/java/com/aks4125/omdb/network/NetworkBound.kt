package com.aks4125.omdb.network

sealed class NetworkBound<out T> {
    class Success<out T>(val data: T) : NetworkBound<T>()
    class Error<out T>(val error: String) : NetworkBound<T>()
    class Loading<out T> : NetworkBound<T>()
}