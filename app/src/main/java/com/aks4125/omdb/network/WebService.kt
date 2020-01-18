package com.aks4125.omdb.network

import com.aks4125.omdb.model.Movie
import com.aks4125.omdb.model.Search
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WebService {
    //@Query("apiKey") String key, @Query("s") String query, @Query("type") String type, @Query("page") int page
    @GET("/")
    suspend fun getMovies(
        @Query("s") query: String,
        @Query("apiKey") key: String = ApiConstants.API_KEY,
        @Query("type") lang: String = ApiConstants.TYPE,
        @Query("page") page: Int = 1
    ): Response<Movie>

    @GET("/")
    suspend fun getMovieDetail(
        @Query("i") movieId: String,
        @Query("apiKey") key: String = ApiConstants.API_KEY,
        @Query("plot") plot: String = ApiConstants.PLOT_TYPE
    ): Response<Search>


}