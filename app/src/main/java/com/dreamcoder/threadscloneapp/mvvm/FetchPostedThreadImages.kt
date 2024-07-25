package com.dreamcoder.threadscloneapp.mvvm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.dreamcoder.threads.model.ThreadsData
import com.dreamcoder.threadscloneapp.util.POST_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FetchPostedThreadImages @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
)  : ViewModel() {

    private val _images = mutableStateListOf<String>()
    val images: List<String> = _images


    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    fun fetchUserPostImages() {
        _loading.value = true
        val currentUserId = auth.currentUser?.uid ?: return

        database.reference.child(POST_DATA)
            .orderByChild("userId")
            .equalTo(currentUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.children.forEach { postSnapshot ->
                        val postData = postSnapshot.getValue(ThreadsData::class.java)
                        postData?.listOfPost?.let { imagesList ->
                            _images.addAll(imagesList)
                        }
                    }
                    _loading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _loading.value = false
                }
            })

    }

    fun signOut(){
        auth.signOut()
    }
}