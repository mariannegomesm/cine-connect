package com.example.cineconnection.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _loginMessage = MutableLiveData<String>().apply {
        value = "Faça seu login"
    }
    val loginMessage: LiveData<String> = _loginMessage

    fun updateLoginMessage(message: String) {
        _loginMessage.value = message
    }
}
