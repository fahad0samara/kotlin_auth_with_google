package com.fahad.kotlin_auth_with_googles.ui.theme.navigation


import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fahad.kotlin_auth_with_googles.ui.theme.navigation.Screen
import com.fahad.kotlin_auth_with_googles.ui.theme.AuthScreen

import com.fahad.kotlin_auth_with_googles.ui.theme.AuthViewModel
import com.fahad.kotlin_auth_with_googles.ui.theme.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: AuthViewModel) {
  NavHost(navController = navController, startDestination = Screen.AuthScreen.route) {
    composable(Screen.AuthScreen.route) {
      AuthScreen(navigateToProfileScreen = { navController.navigate(Screen.ProfileScreen.route) })
    }
    composable(Screen.ProfileScreen.route) {
      ProfileScreen(navController = navController)
    }
  }
}