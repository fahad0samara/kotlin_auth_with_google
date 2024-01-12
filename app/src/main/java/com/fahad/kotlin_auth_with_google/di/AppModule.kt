package com.fahad.kotlin_auth_with_google.di

import android.app.Application
import android.content.Context
import com.fahad.kotlin_auth_with_google.R

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.SIGN_IN_REQUEST
import com.fahad.kotlin_auth_with_google.ui.Utils.Constants.SIGN_UP_REQUEST
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.fahad.kotlin_auth_with_google.data.repository.AuthRepositoryImpl
import com.fahad.kotlin_auth_with_google.data.repository.ProfileRepositoryImpl
import com.fahad.kotlin_auth_with_google.domain.repository.AuthRepository
import com.fahad.kotlin_auth_with_google.domain.repository.ProfileRepository

import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

  // Helper function to create a sign-in request for One Tap
  private fun createSignInRequest(app: Application, filterByAuthorizedAccounts: Boolean): BeginSignInRequest {
    return BeginSignInRequest.builder()
      .setGoogleIdTokenRequestOptions(
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
          .setSupported(true)
          .setServerClientId(app.getString(R.string.google_web_client_id))
          .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
          .build())
      .setAutoSelectEnabled(true)
      .build()
  }

  // Provides FirebaseAuth instance
  @Provides
  fun provideFirebaseAuth() = Firebase.auth

  // Provides FirebaseFirestore instance
  @Provides
  fun provideFirebaseFirestore() = Firebase.firestore

  // Provides One Tap client
  @Provides
  fun provideOneTapClient(
    @ApplicationContext
    context: Context
  ) = Identity.getSignInClient(context)

  // Provides the sign-in request for One Tap with filtering by authorized accounts
  @Provides
  @Named(SIGN_IN_REQUEST)
  fun provideSignInRequest(app: Application) = createSignInRequest(app, true)

  // Provides the sign-in request for One Tap without filtering by authorized accounts
  @Provides
  @Named(SIGN_UP_REQUEST)
  fun provideSignUpRequest(app: Application) = createSignInRequest(app, false)

  // Provides Google Sign-In options
  @Provides
  fun provideGoogleSignInOptions(app: Application) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(app.getString(R.string.google_web_client_id))
    .requestEmail()
    .build()

  // Provides Google Sign-In client
  @Provides
  fun provideGoogleSignInClient(app: Application, options: GoogleSignInOptions) =
    GoogleSignIn.getClient(app, options)

  // Provides AuthRepository implementation
  @Provides
  fun provideAuthRepository(
    auth: FirebaseAuth,
    oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    signUpRequest: BeginSignInRequest,
    db: FirebaseFirestore
  ): AuthRepository = AuthRepositoryImpl(auth, oneTapClient, signInRequest, signUpRequest, db)

  // Provides ProfileRepository implementation
  @Provides
  fun provideProfileRepository(
    auth: FirebaseAuth,
    oneTapClient: SignInClient,
    signInClient: GoogleSignInClient,
    db: FirebaseFirestore
  ): ProfileRepository = ProfileRepositoryImpl(auth, oneTapClient, signInClient, db, email = auth.currentUser?.email.toString())
}
