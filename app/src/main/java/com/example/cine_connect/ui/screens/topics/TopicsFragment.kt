package com.example.cine_connect.ui.screens.topics
import kotlinx.coroutines.runBlocking
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cine_connect.R
import com.example.cine_connect.data.models.Topic
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
            .collection("messages")
            .get()
            .await()
        return commentsSnapshot.size()
    }

    private fun updateTopicsUI(topics: List<Topic>, topicsContainer: LinearLayout) {
        for (topic in topics) {
            val topicView = LayoutInflater.from(requireContext()).inflate(R.layout.topic_card, null)
            topicView.findViewById<TextView>(R.id.topic_title).text = topic.title
            topicView.findViewById<TextView>(R.id.topic_description).text = topic.description
            topicView.findViewById<TextView>(R.id.user_creator).text = topic.userName
            topicView.findViewById<TextView>(R.id.topic_comments_count).text = "Comentários: ${topic.commentCount}"

            topicView.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("movieId", movieId)
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
        firestore.collection("movies_topics")
            .document("movie_$movieId")
            .collection("topics")
            .document(topic.id)
            .delete()
            .addOnSuccessListener {
                topicsContainer.removeView(topicView)
                Log.d("TopicsFragment", "Tópico '${topic.title}' excluído com sucesso.")
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao excluir o tópico: ", exception)
            }
    }
}