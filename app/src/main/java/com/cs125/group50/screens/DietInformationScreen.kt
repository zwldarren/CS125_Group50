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
import com.cs125.group50.data.DietInfo
import com.cs125.group50.nav.BaseScreen
import com.cs125.group50.viewmodel.DietInformationViewModel

// 未添加到导航

@Composable
fun DietInformationScreen(navController: NavHostController, userId: String) {
    BaseScreen(
        navController = navController,
        screenTitle = "Diet",
        content = {
            DietScrollContent(navController, userId)
        }
    )
}

@Composable
fun DietScrollContent(navController: NavHostController, userId: String) {
    val viewModel: DietInformationViewModel = viewModel()
    LaunchedEffect(userId) {
        viewModel.loadDietInfo(userId)
    }
    val dietInfoList by viewModel.dietInfoList.collectAsState()

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {

        Spacer(modifier = Modifier.height(60.dp))

        dietInfoList.forEach { dietInfo ->
            DietInfoCard(dietInfo)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("dietInput") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Add New Diet Information")
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
fun DietInfoCard(dietInfo: DietInfo) { // 假设DietInformation是你饮食信息的数据类
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${dietInfo.date}", style = MaterialTheme.typography.bodyLarge)
            Text("Time: ${dietInfo.time}", style = MaterialTheme.typography.bodyMedium)
            Text("Meal Type: ${dietInfo.mealType}", style = MaterialTheme.typography.bodyMedium)
            Text("Food Name: ${dietInfo.foodName}", style = MaterialTheme.typography.bodyMedium)
            Text("Total calories: ${dietInfo.totalCalories}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
