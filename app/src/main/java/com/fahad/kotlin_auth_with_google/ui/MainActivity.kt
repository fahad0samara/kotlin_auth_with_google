package com.fahad.kotlin_auth_with_google.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fahad.kotlin_auth_with_google.ui.theme.Kotlin_auth_with_googleTheme
import com.fahad.kotlin_auth_with_google.ui.navigation.NavGraph
import com.fahad.kotlin_auth_with_google.ui.screen.start.AuthViewModel

import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private lateinit var navController: NavHostController
  private val viewModel by viewModels<AuthViewModel>()

  @OptIn(ExperimentalAnimationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(
        Color.TRANSPARENT, Color.TRANSPARENT
      ),
      navigationBarStyle = SystemBarStyle.light(
        Color.TRANSPARENT, Color.TRANSPARENT
      )

    )

    setContent {
      Kotlin_auth_with_googleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          navController = rememberAnimatedNavController()
          NavGraph(
            navController = navController,

          )

          // Observe changes in authentication state
          LaunchedEffect(viewModel.isUserAuthenticated) {
            if (viewModel.isUserAuthenticated) {
              navController.navigate("profile")
            } else {
              navController.navigate("auth")
            }
          }
        }
      }
    }
  }
}

