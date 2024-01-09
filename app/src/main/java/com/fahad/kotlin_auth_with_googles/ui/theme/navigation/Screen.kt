package com.fahad.kotlin_auth_with_googles.ui.theme.navigation

import com.fahad.kotlin_auth_with_googles.ui.Constants.AUTH_SCREEN
import com.fahad.kotlin_auth_with_googles.ui.Constants.PROFILE_SCREEN

sealed class Screen(val route: String) {
    data object AuthScreen: Screen(AUTH_SCREEN)
    data object ProfileScreen: Screen(PROFILE_SCREEN)
}