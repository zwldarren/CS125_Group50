package com.cs125.group50.screens

import androidx.compose.foundation.layout.*
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
import com.cs125.group50.viewmodel.ActivityInformationViewModel

@Composable
fun ActivityInformationScreen(navController: NavHostController, userId: String) {
    val viewModel: ActivityInformationViewModel = viewModel()
    LaunchedEffect(true) {
        viewModel.loadActivityInfo()
    }
    val activityInfoList by viewModel.activityInfoList.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Activity Information", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        activityInfoList.forEach { activityInfo ->
            Text("Activity: ${activityInfo.activityType}, Duration: ${activityInfo.duration}, Distance: ${activityInfo.distance}")
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
            Text("Cancel")
        }
    }
}
