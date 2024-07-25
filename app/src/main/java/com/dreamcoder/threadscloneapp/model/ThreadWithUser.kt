package com.dreamcoder.threads.model

import UserModel

data class ThreadWithUser(
    val thread: ThreadsData,
    val user: UserModel
)
