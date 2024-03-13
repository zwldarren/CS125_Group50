package com.cs125.group50.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs125.group50.nav.BaseScreen
import com.cs125.group50.viewmodel.DashboardViewModel
import com.cs125.group50.viewmodel.DashboardViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(navController: NavHostController, userId: String, context: Context) {
    BaseScreen(
        navController = navController,
        screenTitle = "Menu",
        content = {
            MenuScrollContent(navController, userId, context)
        }
    )
}

@Composable
fun MenuScrollContent(navController: NavHostController, userId: String, context: Context) {
    val factory = DashboardViewModelFactory(context)
    val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)
    val coroutineScope = rememberCoroutineScope()

    val hasAllPermissions = dashboardViewModel.hasAllPermissions.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract(),
        onResult = { _ ->
            dashboardViewModel.checkPermissions()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // 添加padding增加边距
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        // 使按钮宽度一致并设置统一的高度
        val buttonModifier = Modifier
            .fillMaxWidth()
            .height(48.dp)


        if (!hasAllPermissions.value) {
            Button(
                onClick = {
                    dashboardViewModel.requestPermissions(permissionLauncher)
                },
                modifier = buttonModifier
            ) {
                Text("Request Permission")
            }

            Spacer(modifier = Modifier.height(8.dp))
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

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    dashboardViewModel.synchronizeHealthData(context)
                }
            },
            modifier = buttonModifier
        ) {
            Text("Synchronize Health Data")
        }

    }
}