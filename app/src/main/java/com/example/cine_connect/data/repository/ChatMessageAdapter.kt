package com.example.cine_connect.data.repository

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cine_connect.R
import com.example.cine_connect.data.models.Message

class ChatMessageAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>() {

    // Lista de cores definidas
    private val colors = listOf(
        Color.parseColor("#261C1A"),
        Color.parseColor("#400101"),
        Color.parseColor("#D91E1E"),
        Color.parseColor("#BF2121"),
        Color.parseColor("#8C1818")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        // Configura o nome do remetente e a cor do texto
        holder.senderName.text = message.senderName
        val randomColor = colors.random()  // Escolhe uma cor aleatória
        holder.senderName.setTextColor(randomColor)

        // Configura o texto da mensagem
        holder.messageText.text = message.messageText

        // Carrega a imagem do perfil usando Glide com um placeholder
        Glide.with(holder.itemView.context)
            .load(message.profileImageUrl)
            .placeholder(R.drawable.profile_edituser)
            .into(holder.profileImage)
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderName: TextView = view.findViewById(R.id.sender_name)
        val messageText: TextView = view.findViewById(R.id.message_text)
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
    }
}
