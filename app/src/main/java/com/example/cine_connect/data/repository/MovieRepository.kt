package com.example.cine_connect.data.repository
import com.example.cine_connect.data.models.Movie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MovieRepository(private val firestore: FirebaseFirestore) {

    suspend fun getMovies(): List<Movie> {
        val moviesList = mutableListOf<Movie>()

        try {
            val snapshot = firestore.collection("movies").get().await()
            for (document in snapshot.documents) {
                val movie = document.toObject(Movie::class.java)
                movie?.let { moviesList.add(it) }
            }
        } catch (e: Exception) {

        }

        return moviesList
    }
}
