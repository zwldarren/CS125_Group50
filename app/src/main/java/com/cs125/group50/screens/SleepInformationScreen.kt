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
import com.cs125.group50.data.SleepInfo
import com.cs125.group50.nav.BaseScreen
import com.cs125.group50.viewmodel.SleepInformationViewModel

@Composable
fun SleepInformationScreen(navController: NavHostController, userId: String) {
    BaseScreen(
        navController = navController,
        screenTitle = "Sleep Information",
        content = {
            SleepScrollContent(navController, userId)
        }
    )
}

@Composable
fun SleepScrollContent(navController: NavHostController, userId: String) {
    val viewModel: SleepInformationViewModel = viewModel()
    LaunchedEffect(userId) {
        viewModel.loadSleepInfo(userId)
    }
    val sleepInfoList by viewModel.sleepInfoList.collectAsState()

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {

        Spacer(modifier = Modifier.height(60.dp))

        sleepInfoList.forEach { sleepInfo ->
            SleepInfoCard(sleepInfo)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("sleepInput") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Add New Sleep Information")
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
fun SleepInfoCard(sleepInfo: SleepInfo) { // 假设SleepInformation是你的睡眠信息数据类
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${sleepInfo.date}", style = MaterialTheme.typography.bodyLarge)
            Text("Start Time: ${sleepInfo.startTime}", style = MaterialTheme.typography.bodyMedium)
            Text("End Time: ${sleepInfo.endTime}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}