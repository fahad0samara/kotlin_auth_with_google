package com.fahad.kotlin_auth_with_google.data.repository

import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.USERS
import com.fahad.kotlin_auth_with_google.domain.model.Response.Failure
import com.fahad.kotlin_auth_with_google.domain.model.Response.Success
import com.fahad.kotlin_auth_with_google.domain.repository.ProfileRepository
import com.fahad.kotlin_auth_with_google.domain.repository.RevokeAccessResponse
import com.fahad.kotlin_auth_with_google.domain.repository.SignOutResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
  private val auth: FirebaseAuth,
  private var oneTapClient: SignInClient,
  private var signInClient: GoogleSignInClient,
  private val db: FirebaseFirestore, override val email: String
) : ProfileRepository {
  // Retrieve the display name and photo URL from the current user
  override val displayName = auth.currentUser?.displayName.toString()
  override val photoUrl = auth.currentUser?.photoUrl.toString()

  // Sign out the user and handle exceptions
  override suspend fun signOut(): SignOutResponse {
    return try {
      // Sign out from One Tap and Firebase authentication
      oneTapClient.signOut().await()
      auth.signOut()

      // Return success response
      Success(true)
    } catch (e: Exception) {
      // Return failure response in case of exceptions
      Failure(e)
    }
  }

  // Revoke access and delete user data from Firestore
  override suspend fun revokeAccess(): RevokeAccessResponse {
    return try {
      auth.currentUser?.apply {
        // Delete user data from Firestore
        db.collection(USERS).document(uid).delete().await()

        // Revoke access from Google Sign-In
        signInClient.revokeAccess().await()

        // Sign out from One Tap and delete the user account
        oneTapClient.signOut().await()
        delete().await()
      }

      // Return success response
      Success(true)
    } catch (e: Exception) {
      // Return failure response in case of exceptions
      Failure(e)
    }
  }
}
