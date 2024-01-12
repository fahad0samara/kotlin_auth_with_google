package com.fahad.kotlin_auth_with_google.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.CREATED_AT
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.DISPLAY_NAME
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.EMAIL
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.PHOTO_URL
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.SIGN_IN_REQUEST
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.SIGN_UP_REQUEST
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.USERS
import com.fahad.kotlin_auth_with_google.domain.model.Response.Failure
import com.fahad.kotlin_auth_with_google.domain.model.Response.Success
import com.fahad.kotlin_auth_with_google.domain.repository.AuthRepository
import com.fahad.kotlin_auth_with_google.domain.repository.OneTapSignInResponse
import com.fahad.kotlin_auth_with_google.domain.repository.SignInWithGoogleResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class AuthRepositoryImpl @Inject constructor(
  private val auth: FirebaseAuth,
  private val oneTapClient: SignInClient,
  @Named(SIGN_IN_REQUEST)
  private val signInRequest: BeginSignInRequest,
  @Named(SIGN_UP_REQUEST)
  private val signUpRequest: BeginSignInRequest,
  private val db: FirebaseFirestore
) : AuthRepository {

  // Check if the user is authenticated in Firebase
  override val isUserAuthenticatedInFirebase: Boolean
    get() = auth.currentUser != null

  // Perform One Tap sign-in with Google and handle exceptions
  override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
    return try {
      // Initiate the One Tap sign-in process
      val signInResult = oneTapClient.beginSignIn(signInRequest).await()
      Success(signInResult)
    } catch (signInException: Exception) {
      try {
        // If One Tap sign-in fails, attempt sign-up
        val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
        Success(signUpResult)
      } catch (signUpException: Exception) {
        // If both sign-in and sign-up fail, return a failure response
        Failure(signUpException)
      }
    }
  }

  // Sign in with Google using Firebase and handle exceptions
  override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse {
    return try {
      // Perform Firebase sign-in using the provided Google credentials
      val authResult = auth.signInWithCredential(googleCredential).await()

      // Check if the user is a new user
      val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

      if (isNewUser) {
        // If it's a new user, add the user to Firestore
        addUserToFirestore()
      }

      // Return success response
      Success(true)
    } catch (signInException: Exception) {
      // If sign-in with Firebase fails, return a failure response
      Failure(signInException)
    }
  }

  // Add the current user to Firestore
  private suspend fun addUserToFirestore() {
    auth.currentUser?.apply {
      try {
        // Convert the FirebaseUser to a user map
        val user = toUser()

        // Set user data in Firestore
        db.collection(USERS).document(uid).set(user).await()
      } catch (e: Exception) {
        // Log and handle the error if adding user to Firestore fails
        Log.e(TAG, "Error adding user to Firestore", e)

        // Return a failure response
        Failure(e)
      }
    }
  }
}

// Extension function to convert FirebaseUser to a map
fun FirebaseUser?.toUser(): Map<String, Any?> {
  return mapOf(
    DISPLAY_NAME to this?.displayName,
    EMAIL to this?.email,
    PHOTO_URL to this?.photoUrl?.toString(),
    CREATED_AT to serverTimestamp(),
  )
}
