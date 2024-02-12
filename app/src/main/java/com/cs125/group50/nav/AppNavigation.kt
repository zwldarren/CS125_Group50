package com.cs125.group50.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs125.group50.screens.DashboardScreen
import com.cs125.group50.screens.LoginScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    NavHost(navController = navController, startDestination = if (user == null) "login" else "dashboard") {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
    }
}
