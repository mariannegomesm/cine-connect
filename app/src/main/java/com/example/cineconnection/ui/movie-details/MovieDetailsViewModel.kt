package com.example.cineconnection.ui.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieDetailsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Detalhes do Filme" 
    }
    val text: LiveData<String> = _text
}
