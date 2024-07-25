package com.dreamcoder.threadscloneapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharePerfDataStore {
    @SuppressLint("CommitPrefEdits")
    fun userStorePerf(
        id: String,
        name: String,
        username: String,
        bio: String,
        profileImage: String,
        context: Context
    ) {
        val sharedPreferences = context.getSharedPreferences("sharePerf", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.putString("name", name)
        editor.putString("username", username)
        editor.putString("bio", bio)
        editor.putString("profileImage", profileImage)
        editor.apply()
    }

    fun getUserId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("sharePerf", MODE_PRIVATE)
        return sharedPreferences.getString("id", "")!!
    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("sharePerf", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("sharePerf", MODE_PRIVATE)
        return sharedPreferences.getString("username", "")!!
    }

    fun getUserBio(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("sharePerf", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!
    }

    fun getUserprofileImage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("sharePerf", MODE_PRIVATE)
        return sharedPreferences.getString("profileImage", "")!!
    }
}