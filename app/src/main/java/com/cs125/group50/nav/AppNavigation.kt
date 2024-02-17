package com.cs125.group50.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs125.group50.screens.DashboardScreen
import com.cs125.group50.screens.LoginScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController, isUserLoggedIn: Boolean) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = if (isUserLoggedIn) "dashboard" else "login") {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController, context) }
    }
}
