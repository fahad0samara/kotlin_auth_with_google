package com.fahad.kotlin_auth_with_google.ui.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.fahad.kotlin_auth_with_google.ui.screen.profile.ProfileScreen
import com.fahad.kotlin_auth_with_google.ui.screen.start.StartScreen

@Composable
fun NavGraph(navController: NavHostController,) {


  NavHost(navController = navController, startDestination = "auth") {
    composable("auth") {
      StartScreen(navController = navController)


    }
    composable("profile") {
      ProfileScreen(navController = navController,)
    }
  }
}