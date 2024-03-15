package com.cs125.group50.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs125.group50.data.ActivityInfo
import com.cs125.group50.nav.BaseScreen
import com.cs125.group50.viewmodel.ActivityInformationViewModel

@Composable
fun ActivityInformationScreen(navController: NavHostController, userId: String) {
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

        Spacer(modifier = Modifier.height(60.dp))

        activityInfoList.forEach { activityInfo ->
            ActivityInfoCard(activityInfo)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("activityInput") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Add New Activity Information")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
fun ActivityInfoCard(activityInfo: ActivityInfo) { // 假设ActivityInformation是你活动信息的数据类
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Activity: ${activityInfo.activityType}", style = MaterialTheme.typography.bodyLarge)
            Text("Duration: ${activityInfo.duration} mins", style = MaterialTheme.typography.bodyMedium)
            Text("Date: ${activityInfo.date}", style = MaterialTheme.typography.bodyMedium)
            Text("Calories Burned: ${activityInfo.caloriesBurned}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
