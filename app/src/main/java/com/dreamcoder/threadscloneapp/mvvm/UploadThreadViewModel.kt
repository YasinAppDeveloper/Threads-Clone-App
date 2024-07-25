package com.dreamcoder.threads.viewmodel

import UiState
import UserModel
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamcoder.threads.model.ThreadWithUser
import com.dreamcoder.threads.model.ThreadsData
import com.dreamcoder.threadscloneapp.util.POST_DATA
import com.dreamcoder.threadscloneapp.util.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UploadThreadViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val database: FirebaseDatabase
) : ViewModel() {

    private var postKey: String? = null

    private val _postUiState = MutableStateFlow<UiState>(UiState.Empty)
    val postUiState: StateFlow<UiState> get() = _postUiState

    private val _listOfThreads = MutableStateFlow<List<ThreadWithUser>>(emptyList())
    val listOfThreads: StateFlow<List<ThreadWithUser>> get() = _listOfThreads
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private var lastKey: String? = null
    private var isEndReached = false

    fun uploadPost(caption: String, imagesUriList: List<Uri>) {
        if (caption.isEmpty()) {
            _postUiState.value = UiState.ValidationError(UiState.Field.BIO, "")
            return
        }
        _postUiState.value = UiState.Loading

        viewModelScope.launch {
            val uploadedImageUrls = uploadImages(imagesUriList)
            savePostData(caption, uploadedImageUrls)
        }
    }

    private suspend fun uploadImages(imagesUriList: List<Uri>): List<String> {
        val uploadedImageUrls = mutableListOf<String>()
        for (uri in imagesUriList) {
            val imageRef = storage.reference.child("POST_IMAGES/${UUID.randomUUID()}.jpg")
            try {
                imageRef.putFile(uri).await()
                val downloadUrl = imageRef.downloadUrl.await().toString()
                uploadedImageUrls.add(downloadUrl)
            } catch (e: Exception) {
                _postUiState.value = UiState.Error("Error uploading image: ${e.message}")
            }
        }
        return uploadedImageUrls
    }

    private suspend fun savePostData(caption: String, uploadedImageUrls: List<String>) {
        val userId = auth.currentUser?.uid ?: return
        val postId = UUID.randomUUID().toString()
        val threadModel = ThreadsData(
            postId, userId, caption, uploadedImageUrls, timeStamp = System.currentTimeMillis()
        )

        postKey = database.reference.child(POST_DATA).push().key
        postKey?.let {
            try {
                database.reference.child(POST_DATA).child(it).setValue(threadModel).await()
                _postUiState.value = UiState.Success("")
            } catch (e: Exception) {
                _postUiState.value = UiState.Error("Error saving post data: ${e.message}")
            }
        } ?: run {
            _postUiState.value = UiState.Error("Error generating post key")
        }
    }

    fun fetchThreads(pageSize: Int = 10) {
        if (_loading.value || isEndReached) return

        _loading.value = true

        val query = if (lastKey == null) {
            database.reference.child(POST_DATA).orderByKey().limitToFirst(pageSize)
        } else {
            database.reference.child(POST_DATA).orderByKey().startAfter(lastKey)
                .limitToFirst(pageSize)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    isEndReached = true
                    _loading.value = false
                    return
                }

                val newThreads = mutableListOf<ThreadWithUser>()
                snapshot.children.forEach { threadSnapshot ->
                    val thread = threadSnapshot.getValue(ThreadsData::class.java) ?: return
                    fetchUserDetails(thread.userId) { user ->
                        newThreads.add(ThreadWithUser(thread, user))
                        if (newThreads.size == snapshot.children.count()) {
                            _listOfThreads.value += newThreads
                            lastKey = threadSnapshot.key
                            _loading.value = false
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _loading.value = false
            }
        })
    }

    private fun fetchUserDetails(userId: String, callback: (UserModel) -> Unit) {
        database.reference.child(USER_DATA).child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java) ?: UserModel()
                    callback(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(UserModel())
                }
            })
    }
}
