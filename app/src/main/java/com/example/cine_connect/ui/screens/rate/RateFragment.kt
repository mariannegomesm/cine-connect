package com.example.cine_connect.ui.screens.rate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        val commentEditText: EditText = view.findViewById(R.id.topic_coment)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)

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
                        "rating" to rating
                    )

                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("reviews")
                        .add(review)
                        .addOnSuccessListener {
                            Log.d("RateFragment", "Review posted successfully")
                            findNavController().navigate(R.id.action_rateFragment_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Log.d("RateFragment", "Error posting review")
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
