package com.example.cine_connect.network

import com.example.cine_connect.data.models.MovieDetailsResponse
import com.example.cine_connect.network.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("3/search/movie")
    suspend fun searchMovies(
            @Query("api_key") apiKey: String,
            @Query("query") query: String,
            @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR"
    ): MovieDetailsResponse
}
