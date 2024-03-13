package com.cs125.group50.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.cs125.group50.nav.BaseScreen
import com.cs125.group50.viewmodel.DashboardViewModel
import com.cs125.group50.viewmodel.DashboardViewModelFactory


@Composable
fun DashboardScreen(navController: NavHostController, context: Context, userId: String) {
//    val viewModel: DashboardViewModel = viewModel()
    val viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(context))

    LaunchedEffect(userId) {
        viewModel.loadUserInfo(userId)
        viewModel.updateRecommendation()
    }

    BaseScreen(
        navController = navController,
        screenTitle = "Main",
        content = {
            // 添加额外的Spacer来避免内容被AppBar遮挡
//            Spacer(modifier = Modifier.height(64.dp))
            MainScrollContent(viewModel)
        }
    )
}

@Composable
fun MainScrollContent(viewModel: DashboardViewModel) {
    val userInfo by viewModel.userInfo.collectAsState()
    val healthAdvice by viewModel.healthAdvice.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(50.dp))

//        Text("Welcome to the Dashboard", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))
        if (userInfo != null) {
            Text("Gender: ${userInfo?.gender}")
            Text("Height: ${userInfo?.height} cm")
            Text("Weight: ${userInfo?.weight} kg")
            Text("Age: ${userInfo?.age}")

            val bmi = userInfo?.let { calculateBMI(it.height.toDouble(), it.weight.toDouble()) }
//            Text("BMI: $bmi")
            if (bmi != null) {
                Text("BMI: ${bmi.format(2)}")
            }

            Spacer(modifier = Modifier.height(16.dp))
            healthAdvice?.let {
                Text(text = it, style = MaterialTheme.typography.bodyLarge)
            } ?: Text(text = "Loading...", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text("No user information found. Please update your profile.",
                style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun calculateBMI(height: Double, weight: Double): Double {
    val heightInMeters = height / 100
    return weight / (heightInMeters * heightInMeters)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)