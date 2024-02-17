package com.cs125.group50.screens

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs125.group50.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(navController: NavHostController, context: Context) {
    val dashboardViewModel = viewModel<DashboardViewModel>()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Welcome to the Dashboard")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            dashboardViewModel.connectGoogleFit(context)
        }) {
            Text("Connect to Google Fit")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}
