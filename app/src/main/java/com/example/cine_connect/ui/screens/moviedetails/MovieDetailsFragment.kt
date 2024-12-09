package com.example.cine_connect.ui.screens.moviedetails

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.example.cine_connect.data.models.MovieDetailsResponse
import com.example.cine_connect.data.models.Topic
import com.example.cine_connect.network.TmdbApiClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class MovieDetailsFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moviedetails, container, false)

        val imageUrl = arguments?.getString("imageUrl")
        imageUrl?.let { loadImage(view, it) }

        setupNavigationButtons(view)





        val movieId = arguments?.getInt("movieId") ?: -1
        if (movieId != -1) {
            fetchMovieDetails(movieId)
            fetchTopicsFromFirebase(movieId, view.findViewById(R.id.topics_containerDetails))
            fetchReviewsFromFirebase(movieId)
            setupReviewsSection(view)
        } else {
            Log.e("MovieDetailsFragment", "ID do filme inválido")
        }



        val heartButton: LinearLayout = view.findViewById(R.id.heart_button)
        heartButton.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: return@setOnClickListener
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let {
                addFavorite(it.uid, movieId)
            }
        }




        val topicsContainer: LinearLayout = view.findViewById(R.id.topics_containerDetails)
        fetchTopicsFromFirebase(movieId, topicsContainer)

        return view
    }

    private fun loadImage(view: View, imageUrl: String) {
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(imageUrl).into(imageView)
    }

    private fun addFavorite(userId: String, movieId: Int) {
        val db = FirebaseFirestore.getInstance()

        val favoriteData = hashMapOf(
            "movieId" to movieId,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document("movie_$movieId")
            .set(favoriteData)
            .addOnSuccessListener {

                println("Filme adicionado aos favoritos com sucesso")
            }
            .addOnFailureListener { e ->

                println("Erro ao adicionar filme aos favoritos: ${e.message}")
            }
    }

    private fun setupNavigationButtons(view: View) {
        val buttonRating: MaterialButton = view.findViewById(R.id.button_rating)
        buttonRating.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("imageUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("posterUrl", posterUrl)
                putString("movieTitle", movieTitle)
            }
            findNavController().navigate(R.id.action_movieDetailsFragment_to_rateFragment, bundle)
        }

        val buttonTopics: MaterialButton = view.findViewById(R.id.button_topics)
        buttonTopics.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("imageUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("posterUrl", posterUrl)
                putString("movieTitle", movieTitle)
            }

            findNavController().navigate(R.id.action_movieDetailsFragment_to_topicsFragment, bundle)
        }
    }

    private fun setupReviewsSection(view: View) {
        val cardReviews: LinearLayout = view.findViewById(R.id.reviews_section)
        cardReviews.setOnClickListener {
            val movieId = arguments?.getInt("movieId") ?: 0
            val posterUrl = arguments?.getString("imageUrl") ?: ""
            val movieTitle = arguments?.getString("movieTitle") ?: ""

            val bundle = Bundle().apply {
                putInt("movieId", movieId)
                putString("posterUrl", posterUrl)
                putString("movieTitle", movieTitle)
            }
            findNavController().navigate(R.id.action_movieDetailsFragment_to_listReviewsFragment, bundle)
        }
    }

    private fun fetchMovieDetails(movieId: Int) {
        lifecycleScope.launch {
            try {
                val apiKey = "06ebff010c5d4faf628283ca3f1ac421"
                val movieDetails = TmdbApiClient.apiService.getMovieDetails(movieId, apiKey, "pt-BR")
                updateUI(movieDetails)
            } catch (e: Exception) {
                Log.e("MovieDetailsFragment", "Erro ao buscar detalhes do filme: ${e.message}")
            }
        }
    }

    private fun updateUI(details: MovieDetailsResponse) {
        val titleTextView = view?.findViewById<TextView>(R.id.titlemovie)
        val ratingTextView = view?.findViewById<TextView>(R.id.describemovie)
        val descriptionTextView = view?.findViewById<TextView>(R.id.descricaotext)

        titleTextView?.text = details.title
        ratingTextView?.text = getString(R.string.rating_text, details.vote_average.toString())
        descriptionTextView?.text = details.overview
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
                    val userId = document.getString("userId") ?: "Desconhecido"

                    val userName = runBlocking { getUserName(userId) }
                    val commentCount = runBlocking { getCommentCount(movieId, document.id) }

                    val topic = Topic(
                        id = document.id,
                        title = title,
                        description = description,
                        userName = userName,
                        commentCount = commentCount
                    )
                    topics.add(topic)
                }

                updateTopicsUI(topics, topicsContainer)

                val numberOfTopicsTextView: TextView? = view?.findViewById(R.id.number_member)
                numberOfTopicsTextView?.text = "${querySnapshot.size()}"
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao obter tópicos: ", exception)

            }
    }
    private suspend fun getUserName(userId: String): String {
        val userDoc = firestore.collection("users").document(userId).get().await()
        return userDoc.getString("nome") ?: "Usuário desconhecido"
    }

    private suspend fun getCommentCount(movieId: Int, topicId: String): Int {
        val commentsSnapshot = firestore.collection("movies_topics")
            .document("movie_$movieId")
            .collection("topics")
            .document(topicId)
            .collection("comments")
            .get()
            .await()
        return commentsSnapshot.size()
    }

    private fun updateTopicsUI(topics: List<Topic>, topicsContainer: LinearLayout) {
        for (topic in topics.take(3)) {
            val topicView = LayoutInflater.from(requireContext()).inflate(R.layout.topic_card, null)
            topicView.findViewById<TextView>(R.id.topic_title).text = topic.title
            topicView.findViewById<TextView>(R.id.topic_description).text = topic.description
            topicView.findViewById<TextView>(R.id.user_creator).text = topic.userName
            topicView.findViewById<TextView>(R.id.topic_comments_count).text = "Comentários: ${topic.commentCount}"

            topicView.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("movieId", arguments?.getInt("movieId") ?: -1)
                    putString("topicId", topic.id)
                }
                findNavController().navigate(R.id.action_topicsFragment_to_topicDialoguesFragment, bundle)
            }

            val deleteButton: ImageButton = topicView.findViewById(R.id.button_delete_topic)
            deleteButton.setOnClickListener {
                showDeleteDialog(topic, topicView, topicsContainer)
            }
            topicsContainer.addView(topicView)
        }
    }

    private fun fetchReviewsFromFirebase(movieId: Int) {
        firestore.collection("movies_reviews")
            .document("movie_$movieId")
            .collection("reviews")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val numberOfReviewsTextView: TextView? = view?.findViewById(R.id.number_reviews)
                numberOfReviewsTextView?.text = "${querySnapshot.size()}"
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao obter avaliações: ", exception)
            }
    }

    private fun showDeleteDialog(topic: Topic, topicView: View, topicsContainer: LinearLayout) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Excluir Tópico")
            .setMessage("Tem certeza que deseja excluir o tópico '${topic.title}'?")
            .setPositiveButton("Excluir") { _, _ ->
                deleteTopicFromFirebase(topic, topicView, topicsContainer)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteTopicFromFirebase(topic: Topic, topicView: View, topicsContainer: LinearLayout) {
        val movieId = arguments?.getInt("movieId") ?: -1
        firestore.collection("movies_topics")
            .document("movie_$movieId")
            .collection("topics")
            .document(topic.id)
            .delete()
            .addOnSuccessListener {
                topicsContainer.removeView(topicView)
                Log.d("MovieDetailsFragment", "Tópico '${topic.title}' excluído com sucesso.")
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao excluir o tópico: ", exception)
            }
    }
}
