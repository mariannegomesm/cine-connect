package com.example.cine_connect.network.model

data class MovieResponse(
    val results: List<Movie>
) {

}

data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val overview: String
)
