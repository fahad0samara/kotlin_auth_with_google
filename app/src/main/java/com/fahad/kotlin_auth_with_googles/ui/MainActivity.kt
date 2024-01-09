package com.fahad.kotlin_auth_with_googles.ui
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.fahad.kotlin_auth_with_googles.ui.theme.navigation.NavGraph
import com.fahad.kotlin_auth_with_googles.ui.theme.navigation.Screen
import com.fahad.kotlin_auth_with_googles.ui.theme.AuthViewModel
import com.fahad.kotlin_auth_with_googles.ui.theme.Kotlin_auth_with_googlesTheme

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private lateinit var navController: NavHostController
  private val viewModel by viewModels<AuthViewModel>()

  @OptIn(ExperimentalAnimationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Kotlin_auth_with_googlesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          navController = rememberAnimatedNavController()
          NavGraph(
            navController = navController,
            viewModel = viewModel
          )

          // Observe changes in authentication state
          LaunchedEffect(viewModel.isUserAuthenticated) {
            if (viewModel.isUserAuthenticated) {
              navController.navigate(Screen.ProfileScreen.route)
            } else {
              navController.navigate(Screen.AuthScreen.route)
            }
          }
        }
      }
    }
  }
}
