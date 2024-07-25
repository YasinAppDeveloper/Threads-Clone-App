package com.dreamcoder.threads.viewmodel

import UiState
import UserModel
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamcoder.threadscloneapp.util.IMAGE_BOX
import com.dreamcoder.threadscloneapp.util.SharePerfDataStore
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    storage: FirebaseStorage

) : ViewModel() {

    private val _registrationState = MutableStateFlow<UiState>(UiState.Empty)
    val registrationState: StateFlow<UiState> = _registrationState

    private val _users = mutableStateListOf<UserModel>()
    val users: List<UserModel> = _users

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    private val imageRef = storage.reference.child(IMAGE_BOX + "${UUID.randomUUID()}.jpg")

    fun userRegister(
        name: String,
        username: String,
        bio: String,
        email: String,
        password: String,
        uri: Uri,
        context: Context
    ) {

        if (name.isEmpty()) {
            _registrationState.value = UiState.ValidationError(UiState.Field.NAME,"Enter the name")
            return
        }
        if (username.isEmpty()) {
            _registrationState.value = UiState.ValidationError(UiState.Field.USERNAME,"Enter User Name")
            return
        }

        if (bio.isEmpty()) {
            _registrationState.value = UiState.ValidationError(UiState.Field.BIO,"Enter the some Bio")
            return
        }

        if (email.isEmpty()) {
            _registrationState.value = UiState.ValidationError(UiState.Field.EMAIL,"Enter the Email")
            return
        }

        if (password.isEmpty() && password.length > 6) {
            _registrationState.value = UiState.ValidationError(UiState.Field.PASSWORD,"Enter the strong 6 char password")
            return
        }

        _registrationState.value = UiState.Loading

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val id = auth.currentUser!!.uid
                            storeUserData(id, name, username, bio, email, password,uri, context)
                        } else {
                            _registrationState.value = UiState.Error(task.exception.toString())
                        }
                    }
            } catch (e: Exception) {
                _registrationState.value = UiState.Error(e.message.toString())
            }
        }
    }

    private fun storeUserData(
        id: String,
        name: String,
        username: String,
        bio: String,
        email: String,
        password: String,
        uri: Uri,
        context: Context
    ) {

        val user = UserModel(id,name,username,bio,email,password)
        database.reference.child(USER_DATA)
            .child(id).setValue(user)
            .addOnSuccessListener { task->
                _registrationState.value = UiState.Success("User Registered Successfully")
            }.addOnFailureListener { error->
                _registrationState.value = UiState.Error(error.localizedMessage!!.toString())
            }

        val imageStore = imageRef.putFile(uri)
        imageStore.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveUser(id,name, username, bio, email, password, uri,context)
            }
        }

    }

    private fun saveUser(
        id:String,
        name: String,
        username: String,
        bio: String,
        email: String,
        password: String,
        uri: Uri,
        context: Context
    ) {

        val user = UserModel(id,name,username,bio,email,password,uri.toString())
        database.reference.child(USER_DATA)
            .child(id).setValue(user)
            .addOnSuccessListener { task->
               SharePerfDataStore.userStorePerf(id,name,username,bio,uri.toString(),context)
                _registrationState.value = UiState.Success("User registered")
            }.addOnFailureListener { error->
                _registrationState.value = UiState.Error(error.localizedMessage!!.toString())
            }
    }

    // reset user
    fun restartViewModel() {
        _registrationState.value = UiState.Empty
    }

    // fetch user
    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        _loading.value = true

        database.reference.child(USER_DATA).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _users.clear()
                snapshot.children.mapNotNullTo(_users) {
                    it.getValue(UserModel::class.java)
                }
                _loading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _loading.value = false
            }
        })
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredUsers(): List<UserModel> {
        val query = searchQuery.value.lowercase()
        return if (query.isEmpty()) {
            users
        } else {
            users.filter {
                it.userName.lowercase().contains(query) ||
                        it.userName.uppercase().contains(query)
            }
        }
    }

}
