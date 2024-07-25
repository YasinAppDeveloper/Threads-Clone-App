package com.dreamcoder.threads.viewmodel

import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _registrationState = MutableStateFlow<UiState>(UiState.Empty)
    val loginState: StateFlow<UiState> = _registrationState

    fun userLogin(email: String, password: String) {

        if (email.isEmpty()) {
            _registrationState.value = UiState.ValidationError(UiState.Field.EMAIL,"Enter the Email")
            return
        }

        if (password.isEmpty() && password.length > 6) {
            _registrationState.value = UiState.ValidationError(UiState.Field.EMAIL,"Enter the strong 6 char password")
            return
        }

        _registrationState.value = UiState.Loading

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        _registrationState.value = UiState.Success("Login Successfully")
                    }.addOnFailureListener {
                        _registrationState.value = UiState.Error(it.message.toString())
                    }
            }catch (e:Exception){
                _registrationState.value = UiState.Error(e.localizedMessage!!.toString())
            }
        }
    }
    fun restartViewModel() {
        _registrationState.value = UiState.Empty
    }
}