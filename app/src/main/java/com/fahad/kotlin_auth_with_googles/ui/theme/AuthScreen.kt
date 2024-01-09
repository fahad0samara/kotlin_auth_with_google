package com.fahad.kotlin_auth_with_googles.ui.theme

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.fahad.kotlin_auth_with_googles.ui.Constants.AUTH_SCREEN

@Composable
fun AuthScreen(
  viewModel: AuthViewModel = hiltViewModel(),
  navigateToProfileScreen: () -> Unit
) {
  val launcher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
    viewModel.handleSignInResult(result)
  }

  // Use remember to create a key that changes when IntentSenderRequest changes
  val oneTapSignInEffectKey = remember(viewModel.oneTapSignInResponse) { Object() }
  LaunchedEffect(oneTapSignInEffectKey) {
    viewModel.handleOneTapSignInResponse { result ->
      launcher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
    }
  }

  // Use remember to create a key that changes when Boolean changes
  val signInWithGoogleEffectKey = remember(viewModel.signInWithGoogleResponse) { Object() }
  LaunchedEffect(signInWithGoogleEffectKey) {
    viewModel.handleSignInWithGoogleResponse { signedIn ->
      if (signedIn) {
        navigateToProfileScreen()
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(50.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // AuthTopBar
    Text(
      text = AUTH_SCREEN,
      fontSize = 24.sp,
      fontWeight = FontWeight.Bold
    )

    // Loading indicator
    if (viewModel.isLoading) {
      CircularProgressIndicator(
        modifier = Modifier
          .size(50.dp)
          .padding(16.dp)
      )
    }

    // Spacer
    Spacer(modifier = Modifier.height(16.dp))

    // AuthContent
    SignInButton(onClick = { viewModel.oneTapSignIn() })

    // Spacer
    Spacer(modifier = Modifier.height(16.dp))











  }
}

