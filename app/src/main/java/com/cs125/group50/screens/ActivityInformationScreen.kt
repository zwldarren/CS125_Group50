package com.cs125.group50.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs125.group50.nav.BaseScreen
import com.cs125.group50.viewmodel.ActivityInformationViewModel

@Composable
fun ActivityInformationScreen(navController: NavHostController, userId: String){
    BaseScreen(
        navController = navController,
        screenTitle = "Exercise",
        content = {
            ActivityScrollContent(navController, userId)
        }
    )
}

@Composable
fun ActivityScrollContent(navController: NavHostController, userId: String) {
    val viewModel: ActivityInformationViewModel = viewModel()
    LaunchedEffect(true) {
        viewModel.loadActivityInfo(userId)
    }
    val activityInfoList by viewModel.activityInfoList.collectAsState()

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        Text("Activity Information", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        activityInfoList.forEach { activityInfo ->
            Text("Activity: ${activityInfo.activityType}, Duration: ${activityInfo.duration} mins, Date: ${activityInfo.date}, Calories Burned: ${activityInfo.caloriesBurned}")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { navController.navigate("activityInput") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Activity Information")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}
