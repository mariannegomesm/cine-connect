package com.example.cine_connect.network

import com.example.cine_connect.data.models.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    fun getPopularMovies(

    ): Call<List<Movie>>
}
