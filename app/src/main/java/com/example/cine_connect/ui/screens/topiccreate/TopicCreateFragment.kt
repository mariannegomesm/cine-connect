package com.example.cine_connect.ui.screens.topiccreate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class TopicCreateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TopicCreateFragment", "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TopicCreateFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_topic_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // val bannerImageView: ImageView = view.findViewById(R.id.banner_image)
        // val titleTextView: TextView = view.findViewById(R.id.titlemovie)
        // titleTextView.text = movieTitle
        // if (bannerUrl.isNotEmpty()) {
        //     Glide.with(this)
        //         .load(bannerUrl)
        //         .into(bannerImageView)
        // }
        fun onPause() {
            super.onPause()
            Log.d("TopicCreateFragment", "onPause called")
        }
        super.onViewCreated(view, savedInstanceState)
        Log.d("TopicCreateFragment", "onViewCreated called")

        val movieId = arguments?.getInt("movieId") ?: 0
        val movieTitle = arguments?.getString("movieTitle") ?: "Título do Filme"
        val bannerUrl = arguments?.getString("bannerUrl") ?: ""

        val topicEditText: EditText = view.findViewById(R.id.topic_edit_text)
        val descriptionEditText: EditText = view.findViewById(R.id.topic_description)


        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val buttonSaveTopic: MaterialButton = view.findViewById(R.id.button_topic)
        buttonSaveTopic.setOnClickListener {
            Log.d("TopicCreateFragment", "Button clicked")

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val topicContent = topicEditText.text.toString().trim()
                val topicDescription = descriptionEditText.text.toString().trim()

                if (topicContent.isNotEmpty() && topicDescription.isNotEmpty()) {
                    Log.d("TopicCreateFragment", "User ID: $userId")

                    val topic = hashMapOf(
                        "description" to topicDescription,
                        "movieId" to movieId,
                        "timestamp" to currentTime,
                        "title" to topicContent,
                        "userId" to userId,
                    )


                    FirebaseFirestore.getInstance()
                        .collection("movies_topics")
                        .document("movie_$movieId")
                        .collection("topics")
                        .add(topic)
                        .addOnSuccessListener {
                            Log.d("TopicCreateFragment", "Topic posted successfully")
                            findNavController().navigate(R.id.action_topicCreateFragment_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Log.d("TopicCreateFragment", "Error posting topic")
                        }
                } else {
                    Log.d("TopicCreateFragment", "Topic content or description is empty")
                }
            } else {
                Log.d("TopicCreateFragment", "User not authenticated")
            }
        }
        fun onStart() {
            super.onStart()
            Log.d("TopicCreateFragment", "onStart called")
        }

    }
}