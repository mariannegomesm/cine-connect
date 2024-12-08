package com.example.cine_connect.data.models

data class Message(
    val senderName: String,
    val messageText: String,
    val profileImageUrl: String,
    val timestamp: Long,
    val userId: String
)
