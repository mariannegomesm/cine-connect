package com.example.cine_connect.ui.screens.home

import android.util.Log 
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cine_connect.network.model.Movie
import com.example.cine_connect.repository.TmdbRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val tmdbRepository = TmdbRepository()

    private val _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>>
        get() = _nowPlayingMovies

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies

    fun fetchNowPlayingMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val movieResponse = tmdbRepository.getNowPlayingMovies(apiKey)
                _nowPlayingMovies.postValue(movieResponse.results)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Erro ao buscar filmes em cartaz: ${e.message}")
            }
        }
    }

    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val movieResponse = tmdbRepository.getPopularMovies(apiKey)
                _popularMovies.postValue(movieResponse.results)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Erro ao buscar filmes populares: ${e.message}")
            }
        }
    }
}

