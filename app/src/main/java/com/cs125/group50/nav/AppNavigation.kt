package com.cs125.group50.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs125.group50.screens.DashboardScreen
import com.cs125.group50.screens.LoginScreen
import com.cs125.group50.screens.UserInfoInputScreen
import com.google.firebase.auth.FirebaseAuth
import com.cs125.group50.screens.DietInputScreen

@Composable
fun AppNavigation(navController: NavHostController, isUserLoggedIn: Boolean) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid.orEmpty()
    NavHost(navController = navController, startDestination = if (isUserLoggedIn) "dashboard" else "login") {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") {
            if (userId.isNotEmpty()) {
                DashboardScreen(navController, context)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }
        composable("userInfoInput") {
            if (userId.isNotEmpty()) {
                UserInfoInputScreen(navController, userId)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }

        // 输入饮食的界面
        composable("dietInput") {
            if (userId.isNotEmpty()) {
                DietInputScreen(navController, userId)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }
    }
}
