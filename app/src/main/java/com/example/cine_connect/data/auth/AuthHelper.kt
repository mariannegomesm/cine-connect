package com.example.cine_connect.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

object AuthHelper {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun registerUser(email: String, senha: String): String? {
        return try {
            val result: AuthResult = auth.createUserWithEmailAndPassword(email, senha).await()
            result.user?.uid
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun userCadastroInfo(uid: String, nome: String, dataNascimento: String, email: String) {
        val userInfo = hashMapOf(
            "nome" to nome,
            "dataNascimento" to dataNascimento,
            "email" to email
        )
        firestore.collection("users").document(uid).set(userInfo).await()
    }
}
