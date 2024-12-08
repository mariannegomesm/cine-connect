package com.example.cine_connect.data.models

data class Topic(
    val id: String = "",
    val title: String,
    val description: String,
    val userName: String,
    val commentCount: Int
) {

    constructor(title: String, description: String, userName: String, commentCount: Int) : this(
        id = "",
        title = title,
        description = description,
        userName = userName,
        commentCount = commentCount
    )
}
