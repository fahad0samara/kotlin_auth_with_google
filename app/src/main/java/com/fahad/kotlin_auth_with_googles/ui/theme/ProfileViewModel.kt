package com.fahad.kotlin_auth_with_googles.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.fahad.kotlin_auth_with_googles.domain.model.Response.Loading
import com.fahad.kotlin_auth_with_googles.domain.model.Response.Success
import com.fahad.kotlin_auth_with_googles.domain.repository.ProfileRepository
import com.fahad.kotlin_auth_with_googles.domain.repository.RevokeAccessResponse
import com.fahad.kotlin_auth_with_googles.domain.repository.SignOutResponse
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val repo: ProfileRepository
): ViewModel() {
  // Exposing properties from the repository
  val displayName get() = repo.displayName
  val photoUrl get() = repo.photoUrl

  // Mutable state variables for sign-out and revoke access responses
  var signOutResponse by mutableStateOf<SignOutResponse>(Success(false))
    private set
  var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Success(false))
    private set

  // Coroutine function to perform sign-out operation
  fun signOut() = viewModelScope.launch {
    signOutResponse = Loading
    signOutResponse = repo.signOut()
  }

  // Coroutine function to perform revoke access operation
  fun revokeAccess() = viewModelScope.launch {
    revokeAccessResponse = Loading
    revokeAccessResponse = repo.revokeAccess()
  }
}
