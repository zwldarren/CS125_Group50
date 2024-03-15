package com.cs125.group50.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    val userInfoState by viewModel.userInfo.collectAsState()
    val healthAdvice by viewModel.healthAdvice.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item { Spacer(modifier = Modifier.height(50.dp)) }

        userInfoState?.let { userInfo -> // 显式地将userInfoState赋值给一个局部变量userInfo
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("User Information", style = MaterialTheme.typography.headlineSmall)
                        Divider(Modifier.padding(vertical = 8.dp))
                        UserInfoItem(label = "Gender", value = userInfo.gender)
                        UserInfoItem(label = "Height", value = "${userInfo.height} cm")
                        UserInfoItem(label = "Weight", value = "${userInfo.weight} kg")
                        UserInfoItem(label = "Age", value = "${userInfo.age}")
                        UserInfoItem(label = "Goal", value = userInfo.goal)
                        val bmi = calculateBMI(userInfo.height.toDouble(), userInfo.weight.toDouble())
                        UserInfoItem(label = "BMI", value = bmi.format(2))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            healthAdvice?.let {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Health Advice", style = MaterialTheme.typography.headlineSmall)
                            Divider(Modifier.padding(vertical = 8.dp))
                            Text(it, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            } ?: item {
                Text("Loading health advice...", style = MaterialTheme.typography.bodyLarge)
            }
        } ?: item {
            Text("No user information found. Please update your profile.",
                style = MaterialTheme.typography.bodyLarge)
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun UserInfoItem(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}


fun calculateBMI(height: Double, weight: Double): Double {
    val heightInMeters = height / 100
    return weight / (heightInMeters * heightInMeters)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)