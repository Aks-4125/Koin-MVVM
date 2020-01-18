package com.aks4125.omdb.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aks4125.omdb.model.Search
import com.aks4125.omdb.network.NetworkBound
import com.aks4125.omdb.network.WebService
import com.aks4125.omdb.ui.MainActivity.Companion.LOGGER
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MovieViewModel(private val service: WebService) : ViewModel() {
    private lateinit var job: Job
    val response: MutableLiveData<NetworkBound<List<Search>>> by lazy { MutableLiveData<NetworkBound<List<Search>>>() }

    /**
     * fetch movies on io thread and set response
     */
    fun fetchMovies(queryText: String) {
        job = CoroutineScope(IO).launch {
            response.postValue(NetworkBound.Loading())
            Log.d(LOGGER, "API call requested on ->" + Thread.currentThread().name)
            try {
                val res = service.getMovies(queryText)
                res.body()?.let { movieResponse ->
                    if (res.code() == 200 && movieResponse.response) {
                        response.postValue(NetworkBound.Success(movieResponse.search))
                    } else {
                        response.postValue(NetworkBound.Error(movieResponse.error))
                    }
                    Log.d(LOGGER, "response ->" + Thread.currentThread().name)
                }
            } catch (exception: Exception) {
                response.postValue(NetworkBound.Error("Api Error"))
            }
        }
    }


    /**
     * fetch movies on io thread and set response
     */
    fun fetchMovieDetail(movieId: String) {
        job = CoroutineScope(IO).launch {
            response.postValue(NetworkBound.Loading())
            Log.d(LOGGER, "API call requested on ->" + Thread.currentThread().name)
            try {
                val res = service.getMovieDetail(movieId = movieId)
                res.body()?.let { movieResponse ->
                    if (res.code() == 200 && movieResponse.response) {
                        response.postValue(NetworkBound.Success(arrayListOf(movieResponse)))
                    } else {
                        response.postValue(NetworkBound.Error("Api Error"))
                    }
                    Log.d(LOGGER, "response ->" + Thread.currentThread().name)
                }
            } catch (exception: Exception) {
                response.postValue(NetworkBound.Error("Api Error"))
            }
        }
    }


    /**
     * view model will be cleared when user navigates to search screen once API request is in progress
     */
    override fun onCleared() {
        super.onCleared()
        job.isActive.let {
            Log.d(LOGGER, "job is no longer required")
            job.cancel()
        }
    }
}