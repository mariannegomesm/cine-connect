package com.example.cine_connect.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

object AuthHelper {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

// Aqui eu to utilizando o Authentication do proprio firebase pra cadastrar o cliente, basicamente tem o cadastro de e-mail e senha que é o cadastro dele, só que as informações adicionais ficam em um objeto chamado users na collection.
    suspend fun registerUser(email: String, senha: String): String? {
        return try {
            val result: AuthResult = auth.createUserWithEmailAndPassword(email, senha).await()
            result.user?.uid
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
// Aqui eu to passando as informações adicionais do usuario pra uma collection no firebase
    suspend fun userCadastroInfo(uid: String, nome: String, dataNascimento: String, email: String) {
        val userInfo = hashMapOf(
            "nome" to nome,
            "dataNascimento" to dataNascimento,
            "email" to email
        )
        firestore.collection("users").document(uid).set(userInfo).await()
    }

// Aqui eu to tentando fazer a authenticação de login
    suspend fun loginUser(email: String, senha: String): String? {
        return try {
            val result: AuthResult = auth.signInWithEmailAndPassword(email, senha).await()
            result.user?.uid
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
