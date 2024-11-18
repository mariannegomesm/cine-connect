package com.example.cine_connect.ui.screens.rate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide // Usado para carregar a imagem do cartaz
import java.text.SimpleDateFormat
import java.util.*

class RateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RateFragment", "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("RateFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("RateFragment", "onViewCreated called")

        // Pegando os argumentos passados
        val movieId = arguments?.getInt("movieId") ?: 0
        val movieTitle = arguments?.getString("movieTitle") ?: "Título do Filme"
        val posterUrl = arguments?.getString("posterUrl") ?: ""

        // Referências de UI
        val commentEditText: EditText = view.findViewById(R.id.topic_coment)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val timestampTextView: TextView = view.findViewById(R.id.timestamp)
        val titleTextView: TextView = view.findViewById(R.id.titlefilmecreatereview)
        val posterImageView: ImageView = view.findViewById(R.id.posterImageView)

        titleTextView.text = movieTitle


        if (posterUrl.isNotEmpty()) {
            Glide.with(this)
                .load(posterUrl)
                .into(posterImageView)
        }

        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = sdf.format(Date(currentTime))
        timestampTextView.text = "Assistido em: $formattedDate"

        val buttonRating: MaterialButton = view.findViewById(R.id.button_rating)
        buttonRating.setOnClickListener {
            Log.d("RateFragment", "Button clicked")

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val comment = commentEditText.text.toString().trim()
                val rating = ratingBar.rating

                if (comment.isNotEmpty() && rating > 0) {
                    Log.d("RateFragment", "User ID: $userId")

                    val review = hashMapOf(
                        "userId" to userId,
                        "review" to comment,
                        "rating" to rating,
                        "timestamp" to currentTime,
                        "movieId" to movieId
                    )

                    FirebaseFirestore.getInstance()
                        .collection("movies_reviews")
                        .document("movie_$movieId")
                        .collection("reviews")
                        .add(review)
                        .addOnSuccessListener {
                            Log.d("RateFragment", "Review posted to movies_ratings")
                        }
                        .addOnFailureListener {
                            Log.d("RateFragment", "Error posting review to movies_ratings")
                        }


                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("reviews")
                        .add(review)
                        .addOnSuccessListener {
                            Log.d("RateFragment", "Review posted to user reviews")

                            findNavController().navigate(R.id.action_rateFragment_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Log.d("RateFragment", "Error posting review to user reviews")
                        }
                } else {
                    Log.d("RateFragment", "Comment is empty or rating is 0")
                }
            } else {
                Log.d("RateFragment", "User not authenticated")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("RateFragment", "onStart called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("RateFragment", "onPause called")
    }
}
