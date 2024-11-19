package com.example.cine_connect.repository

import com.example.cine_connect.network.TmdbApiClient
import com.example.cine_connect.network.model.MovieResponse

class TmdbRepository {

    private val tmdbApiService = TmdbApiClient.apiService

    suspend fun getNowPlayingMovies(apiKey: String): MovieResponse {
        return tmdbApiService.getNowPlayingMovies(apiKey)
    }

    suspend fun getPopularMovies(apiKey: String): MovieResponse {
        return tmdbApiService.getPopularMovies(apiKey)
    }

    suspend fun searchMovies(
            apiKey: String,
            query: String,
            language: String = "pt-BR"
    ): MovieResponse {
        return tmdbApiService.searchMovies(apiKey, query, language)
    }
}
