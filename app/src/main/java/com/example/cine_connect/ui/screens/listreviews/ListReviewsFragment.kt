package com.example.cine_connect.ui.screens.listreviews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.data.models.Review
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class ListReviewsFragment : Fragment() {

    private var movieId: Int = -1
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt("movieId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_reviews, container, false)


        if (movieId != -1) {
            fetchReviewsFromFirebase(movieId)
        } else {
            Log.e("ListReviewsFragment", "ID do filme inválido")
        }

        val buttonReview: Button = view.findViewById(R.id.add_reviews)
        buttonReview.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("posterUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("posterUrl", posterUrl)
                putString("movieTitle", movieTitle)
            }
            findNavController().navigate(R.id.action_listReviewsFragment_to_rateFragment, bundle)
        }

        return view
    }


    private fun fetchReviewsFromFirebase(movieId: Int) {
        firestore.collection("movies_reviews")
            .document("movie_$movieId")
            .collection("reviews")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()
                for (document in querySnapshot.documents) {

                    val rating = document.getDouble("rating")?.toFloat() ?: 0f
                    val reviewText = document.getString("review") ?: ""
                    val userId = document.getString("userId") ?: ""

                    fetchUserName(userId) { userName ->

                        val review = Review(userName, reviewText, rating)
                        reviews.add(review)

                        updateReviewsUI(reviews)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao obter reviews: ", exception)
            }
    }

    private fun fetchUserName(userId: String, callback: (String) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("nome") ?: "Usuário Desconhecido"
                callback(userName)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao obter o nome do usuário: ", exception)
                callback("Usuário Desconhecido")
            }
    }

    private fun updateReviewsUI(reviews: List<Review>) {
        val container = view?.findViewById<LinearLayout>(R.id.reviews_container)
        val noReviewsMessage = view?.findViewById<TextView>(R.id.no_reviews_message)

        container?.removeAllViews()


        if (reviews.isEmpty()) {

            noReviewsMessage?.visibility = View.VISIBLE
        } else {
            noReviewsMessage?.visibility = View.GONE
            reviews.forEach { review ->
                val reviewView = layoutInflater.inflate(R.layout.review_card, container, false)
                val reviewCreator = reviewView.findViewById<TextView>(R.id.review_creator1)
                val reviewDescription = reviewView.findViewById<TextView>(R.id.review_description1)
                val ratingBar = reviewView.findViewById<RatingBar>(R.id.ratingBar)

                reviewCreator.text = "/@${review.creator}"
                reviewDescription.text = review.description
                ratingBar.rating = review.rating

                container?.addView(reviewView)
            }
        }
    }
}