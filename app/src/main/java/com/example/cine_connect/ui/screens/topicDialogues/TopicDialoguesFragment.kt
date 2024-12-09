package com.example.cine_connect.ui.screens.topicDialogues

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.example.cine_connect.data.models.Message
import com.example.cine_connect.data.repository.ChatMessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TopicDialoguesFragment : Fragment() {

    private var movieId: Int = 0
    private var topicId: String = ""

    private val messages = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatMessageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var sendButton: ImageButton

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movieId", 0)
            topicId = it.getString("topicId", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.topic_dialogues, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_chat)
        recyclerView.layoutManager = LinearLayoutManager(context)
        inputMessage = view.findViewById(R.id.input_message)
        sendButton = view.findViewById(R.id.button_send_message)

        sendButton.setOnClickListener {
            val messageText = inputMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                Log.d("TopicDialoguesFragment", "Mensagem enviada: $messageText")
                sendMessage(messageText)
            } else {
                Log.d("TopicDialoguesFragment", "Mensagem vazia não enviada")
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatAdapter = ChatMessageAdapter(messages)
        recyclerView.adapter = chatAdapter

        loadMessages()
    }

    private fun sendMessage(messageText: String) {
        scope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val userDoc = userId?.let { db.collection("users").document(it).get().await() }

                if (userDoc != null) {
                    val nome = userDoc.getString("nome") ?: "Usuário Desconhecido"
                    val profileImageUrl = userDoc.getString("profileImageUrl")
                        ?: "https://example.com/default.jpg"

                    val newMessage = Message(
                        senderName = nome,
                        messageText = messageText,
                        profileImageUrl = profileImageUrl,
                        timestamp = System.currentTimeMillis(),
                        userId = userId
                    )

                    val messageRef = db.collection("movies_topics")
                        .document("movie_$movieId")
                        .collection("topics")
                        .document(topicId)
                        .collection("messages")
                        .document()
                    messageRef.set(newMessage).await()
                    Log.d("TopicDialoguesFragment", "Mensagem enviada com sucesso")

                    withContext(Dispatchers.Main) {
                        messages.add(newMessage)
                        chatAdapter.notifyItemInserted(messages.size - 1)
                        recyclerView.scrollToPosition(messages.size - 1)
                        inputMessage.text.clear()
                    }
                } else {
                    Log.d("TopicDialoguesFragment", "Documento do usuário não encontrado")
                }
            } catch (e: Exception) {
                Log.e("TopicDialoguesFragment", "Erro ao enviar mensagem: $e")
            }
        }
    }

    private fun loadMessages() {
        scope.launch {
            try {
                Log.d("DEBUG", "Iniciando carregamento de mensagens")

                val messageSnapshot = db.collection("movies_topics")
                    .document("movie_$movieId")
                    .collection("topics")
                    .document(topicId)
                    .collection("messages")
                    .orderBy("timestamp")
                    .get()
                    .await()

                Log.d("DEBUG", "Mensagens carregadas: ${messageSnapshot.size()}")

                val loadedMessages = mutableListOf<Message>()

                for (messageDoc in messageSnapshot.documents) {
                    val messageText = messageDoc.getString("messageText") ?: "Mensagem vazia"
                    val profileImageUrl = messageDoc.getString("profileImageUrl")
                        ?: "https://cdn-icons-png.flaticon.com/512/5987/5987462.png"
                    val senderName = messageDoc.getString("senderName") ?: "Usuário Desconhecido"
                    val timestamp = messageDoc.getLong("timestamp") ?: System.currentTimeMillis()
                    val userId = messageDoc.getString("userId") ?: "Desconhecido"

                    Log.d("DEBUG", "Mensagem carregada: sender=$senderName, texto=$messageText")

                    val message = Message(
                        senderName = senderName,
                        messageText = messageText,
                        profileImageUrl = profileImageUrl,
                        timestamp = timestamp,
                        userId = userId
                    )

                    loadedMessages.add(message)
                }

                withContext(Dispatchers.Main) {
                    messages.clear()
                    messages.addAll(loadedMessages)
                    chatAdapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(messages.size - 1)
                    Log.d("DEBUG", "Mensagens atualizadas no RecyclerView")
                }
            } catch (e: Exception) {
                Log.e("TopicDialoguesFragment", "Erro ao carregar mensagens: $e")
            }
        }
    }
}
