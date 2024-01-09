package com.fahad.kotlin_auth_with_googles.ui.theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

import com.fahad.kotlin_auth_with_googles.domain.model.Response
import com.fahad.kotlin_auth_with_googles.ui.theme.navigation.Screen

@Composable
fun ProfileScreen(
  viewModel: ProfileViewModel = hiltViewModel(),
  navController: NavController
) {
  // UI composition using Jetpack Compose
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Profile Content
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(48.dp))

      // Profile Image
      Image(
        painter = rememberAsyncImagePainter(
          ImageRequest.Builder(LocalContext.current).data(data = viewModel.photoUrl).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            transformations(CircleCropTransformation())
          }).build()
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(96.dp)
          .clip(CircleShape)
      )

      // Spacer
      Spacer(modifier = Modifier.height(16.dp))

      // Display Name
      Text(
        text = viewModel.displayName,
        fontSize = 24.sp
      )
    }

    // Spacer
    Spacer(modifier = Modifier.height(16.dp))

    // Sign Out Button
    Button(
      onClick = {
        // Initiate sign-out process when the button is clicked
        viewModel.signOut()

        // Navigate to the authentication screen after sign-out
        navController.navigate(Screen.AuthScreen.route)
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      // UI for Sign Out button with loading indicator and different states
      when (val signOutState = viewModel.signOutResponse) {
        is Response.Loading -> {
          CircularProgressIndicator()
        }
        is Response.Success -> {
          Text(text = "Sign Out")
        }
        is Response.Failure -> {
          Text(text = "Sign Out")
          // Handle failure if needed
        }
      }
    }

    // Spacer
    Spacer(modifier = Modifier.height(8.dp))

    // Revoke Access Button
    Button(
      onClick = {
        // Initiate revoke access process when the button is clicked
        viewModel.revokeAccess()

        // Navigate to the authentication screen after revoke access
        navController.navigate(Screen.AuthScreen.route)
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      // UI for Revoke Access button with loading indicator and different states
      when (val revokeAccessState = viewModel.revokeAccessResponse) {
        is Response.Loading -> {
          CircularProgressIndicator()
        }
        is Response.Success -> {
          Text(text = "Revoke Access")
        }
        is Response.Failure -> {
          Text(text = "Revoke Access")
          // Handle failure if needed
        }
      }
    }

    // Spacer
    Spacer(modifier = Modifier.height(16.dp))
  }
}

