package com.example.cine_connect.ui.screens.topics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.data.models.Topic
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

class TopicsFragment : Fragment() {

    private var movieId: Int = 0
    private var movieTitle: String = ""
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TopicsFragment", "onCreate called")

        arguments?.let {
            movieId = it.getInt("movieId", 0)
            movieTitle = it.getString("movieTitle", "Título do Filme") ?: "Título do Filme"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TopicsFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TopicsFragment", "onViewCreated called")
        Log.d("TopicsFragment", "ID do projeto: $movieId")


        val addTopicButton: MaterialButton = view.findViewById(R.id.add_topics)
        addTopicButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("movieTitle", movieTitle)
            }
            findNavController().navigate(R.id.action_topicsFragment_to_topicCreateFragment, bundle)
        }

        val topicsContainer: LinearLayout = view.findViewById(R.id.topics_container)
        fetchTopicsFromFirebase(movieId, topicsContainer)
    }

    private fun fetchTopicsFromFirebase(movieId: Int, topicsContainer: LinearLayout) {
        firestore.collection("movies_topics")
            .document("movie_$movieId")
            .collection("topics")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val topics = mutableListOf<Topic>()
                for (document in querySnapshot.documents) {
                    val title = document.getString("title") ?: "Sem título"
                    val description = document.getString("description") ?: "Sem descrição"

                    val topic = Topic(title, description)
                    topics.add(topic)
                }

                updateTopicsUI(topics, topicsContainer)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao obter tópicos: ", exception)
            }
    }

    private fun updateTopicsUI(topics: List<Topic>, topicsContainer: LinearLayout) {
        for (topic in topics) {
            val topicView = LayoutInflater.from(requireContext()).inflate(R.layout.topic_card, null)
            topicView.findViewById<TextView>(R.id.topic_title).text = topic.title
            topicView.findViewById<TextView>(R.id.topic_description).text = topic.description

            topicsContainer.addView(topicView)
        }
    }
}

