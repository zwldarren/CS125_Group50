package com.cs125.group50.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs125.group50.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DashboardScreen(navController: NavHostController, context: Context) {
    val dashboardViewModel = viewModel<DashboardViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // 添加padding增加边距
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the Dashboard")

        Spacer(modifier = Modifier.height(16.dp))

        // 使按钮宽度一致并设置统一的高度
        val buttonModifier = Modifier
            .fillMaxWidth()
            .height(48.dp)

        Button(
            onClick = { dashboardViewModel.connectGoogleFit(context) },
            modifier = buttonModifier
        ) {
            Text("Connect to Google Fit")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("userInfoInput") },
            modifier = buttonModifier
        ) {
            Text("Enter User Info")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("dietInput") },
            modifier = buttonModifier
        ) {
            Text("Diet Input")
        }

        // 使用Spacer来填充中间的空间，将登出按钮推到底部
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = buttonModifier
        ) {
            Text("Logout")
        }
    }
}
