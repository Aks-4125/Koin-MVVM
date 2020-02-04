package com.aks4125.omdb.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aks4125.omdb.model.Search
import com.aks4125.omdb.network.NetworkBound
import com.aks4125.omdb.network.WebService
import com.aks4125.omdb.ui.MainActivity
import kotlinx.coroutines.*

class DetailViewModel(private val service: WebService) : ViewModel() { // TODO in progress

    private lateinit var job: Job
    val liveData: MutableLiveData<NetworkBound<Search>> by lazy { MutableLiveData<NetworkBound<Search>>() }

    /**
     * fetch movies on io thread and set response
     */
    fun fetchMovieDetail(movieId: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            liveData.postValue(NetworkBound.Loading())
            Log.d(MainActivity.LOGGER, "API call requested on ->" + Thread.currentThread().name)
            try {
                val res = service.getMovieDetail(movieId = movieId)
                res.body()?.let { movieResponse ->
                    if (res.code() == 200 && movieResponse.response) {
                        liveData.postValue(NetworkBound.Success(movieResponse))
                    }else{
                        liveData.postValue(NetworkBound.Error("Api Error"))
                    }
                    Log.d(MainActivity.LOGGER, "response ->" + Thread.currentThread().name)
                }
            } catch (exception: Exception) {
                liveData.postValue(NetworkBound.Error("Api Error"))
            }
        }
    }

    /**
     * view model will be cleared when user navigates to search screen once API request is in progress
     */
    override fun onCleared() {
        super.onCleared()
        job.isActive.let {
            Log.d(MainActivity.LOGGER, "job is no longer required")
            job.cancel()
        }
    }
}