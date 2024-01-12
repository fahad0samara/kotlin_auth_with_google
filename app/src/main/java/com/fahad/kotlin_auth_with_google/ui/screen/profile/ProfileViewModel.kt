package com.fahad.kotlin_auth_with_google.ui.screen.profile
import androidx.compose.runtime.State


import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahad.kotlin_auth_with_google.domain.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.fahad.kotlin_auth_with_google.domain.model.Response.Loading
import com.fahad.kotlin_auth_with_google.domain.model.Response.Success
import com.fahad.kotlin_auth_with_google.domain.repository.ProfileRepository

import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val repo: ProfileRepository
) : ViewModel() {

  // Exposing properties from the repository
  val displayName get() = repo.displayName
  val photoUrl get() = repo.photoUrl
  val email get() = repo.email

  // Mutable state variables for sign-out and revoke access responses
  private val _signOutResponse = mutableStateOf<Response<Boolean>>(Success(false))
  val signOutResponse: State<Response<Boolean>> get() = _signOutResponse

  private val _revokeAccessResponse = mutableStateOf<Response<Boolean>>(Success(false))
  val revokeAccessResponse: State<Response<Boolean>> get() = _revokeAccessResponse

  // Coroutine function to perform sign-out operation
  fun signOut() = viewModelScope.launch {
    _signOutResponse.value = Loading
    try {
      _signOutResponse.value = repo.signOut()
    } catch (e: Exception) {
      _signOutResponse.value = Response.Failure(e)
    }
  }

  // Coroutine function to perform revoke access operation
  fun revokeAccess() = viewModelScope.launch {
    _revokeAccessResponse.value = Loading
    try {
      _revokeAccessResponse.value = repo.revokeAccess()
    } catch (e: Exception) {
      _revokeAccessResponse.value = Response.Failure(e)
    }
  }
}

