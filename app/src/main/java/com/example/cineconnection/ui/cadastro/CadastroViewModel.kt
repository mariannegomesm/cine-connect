package com.example.cineconnection.ui.cadastro  // Alterar o pacote conforme necessário

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CadastroViewModel : ViewModel() {

    private val _message = MutableLiveData<String>().apply {
        value = "Este é o Fragmento de Cadastro"
    }
    val message: LiveData<String> = _message

}
