package com.fahad.kotlin_auth_with_google.ui.screen.profile
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

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

import com.fahad.kotlin_auth_with_google.domain.model.Response

@Composable
fun ProfileScreen(
  viewModel: ProfileViewModel = hiltViewModel(),
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(48.dp))

      Image(
        painter = rememberAsyncImagePainter(
          ImageRequest.Builder(LocalContext.current).data(data = viewModel.photoUrl).apply {
            crossfade(true)
            transformations(CircleCropTransformation())
          }.build()
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(96.dp)
          .clip(CircleShape)
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = viewModel.displayName,
        fontSize = 24.sp
      )
    }

    Text(
      text = viewModel.email,
      fontSize = 24.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Sign Out Button
    ActionButton(
      text = "Sign Out",
      onClick = { viewModel.signOut() },
      navController = navController,
      responseState = viewModel.revokeAccessResponse,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Revoke Access Button
    ActionButton(
      text = "Revoke Access",
      onClick = { viewModel.revokeAccess() },
      navController = navController,
      responseState = viewModel.revokeAccessResponse,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
  }
}


@Composable
fun ActionButton(
  text: String,
  onClick: () -> Unit,
  responseState: State<Response<Boolean>>,
  modifier: Modifier = Modifier,
  navController: NavController
) {
  Button(
    onClick = {
      onClick()

      navController.navigate("auth") {
        popUpTo("auth") { inclusive = true }
      }
    },
    modifier = modifier
  ) {
    when (responseState.value) {
      is Response.Loading -> {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          CircularProgressIndicator()
          Text(text = "Loading...")
        }
      }
      is Response.Success -> {
        Text(text = text)
      }
      is Response.Failure -> {
        Text(text = text)
      }
    }
  }
}
