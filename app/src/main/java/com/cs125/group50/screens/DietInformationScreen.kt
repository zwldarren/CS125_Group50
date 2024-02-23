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
import com.cs125.group50.viewmodel.DietInformationViewModel

// 未添加到导航
@Composable
fun DietInformationScreen(navController: NavHostController, userId: String) {
    val viewModel: DietInformationViewModel = viewModel()
    LaunchedEffect(userId) {
        viewModel.loadDietInfo(userId)
    }
    val dietInfoList by viewModel.dietInfoList.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Diet Information", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        dietInfoList.forEach { dietInfo ->
            // Display each diet information here
            Text("Date: ${dietInfo.date}, Time: ${dietInfo.time}, Meal Type: ${dietInfo.mealType}, Food Name: ${dietInfo.foodName}, Total calories: ${(dietInfo.foodAmount).toDouble()*(dietInfo.caloriesPerHundredGrams).toDouble()}")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { navController.navigate("dietInput") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Diet Information")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Cancel")
        }
    }
}
