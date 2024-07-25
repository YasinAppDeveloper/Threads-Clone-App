package com.dreamcoder.threads.model


data class ThreadsData(
    val postId: String,
    val userId: String,
    val caption: String,
    val listOfPost: List<String>,
    val timeStamp: Long,
    val likeCount: Int = 0,
    val commentCount: Int = 0
) {
    constructor() : this("", "", "", mutableListOf(), 0, 0, 0)
}