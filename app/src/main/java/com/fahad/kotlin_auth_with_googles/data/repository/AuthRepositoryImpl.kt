package com.fahad.kotlin_auth_with_googles.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.fahad.kotlin_auth_with_googles.ui.Constants.CREATED_AT
import com.fahad.kotlin_auth_with_googles.ui.Constants.DISPLAY_NAME
import com.fahad.kotlin_auth_with_googles.ui.Constants.EMAIL
import com.fahad.kotlin_auth_with_googles.ui.Constants.PHOTO_URL
import com.fahad.kotlin_auth_with_googles.ui.Constants.SIGN_IN_REQUEST
import com.fahad.kotlin_auth_with_googles.ui.Constants.SIGN_UP_REQUEST
import com.fahad.kotlin_auth_with_googles.ui.Constants.USERS
import com.fahad.kotlin_auth_with_googles.domain.model.Response.Failure
import com.fahad.kotlin_auth_with_googles.domain.model.Response.Success
import com.fahad.kotlin_auth_with_googles.domain.repository.AuthRepository
import com.fahad.kotlin_auth_with_googles.domain.repository.OneTapSignInResponse
import com.fahad.kotlin_auth_with_googles.domain.repository.SignInWithGoogleResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {
    override val isUserAuthenticatedInFirebase = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Success(signUpResult)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply {
            val user = toUser()
            db.collection(USERS).document(uid).set(user).await()
        }
    }
}

fun FirebaseUser.toUser() = mapOf(
    DISPLAY_NAME to displayName,
    EMAIL to email,
    PHOTO_URL to photoUrl?.toString(),
    CREATED_AT to serverTimestamp()
)