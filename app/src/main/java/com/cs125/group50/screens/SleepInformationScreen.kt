package com.cs125.group50.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cs125.group50.nav.BaseScreen

@Composable
fun SleepInformationScreen(navController: NavHostController, userId: String) {
    BaseScreen(
        navController = navController,
        screenTitle = "Sleep",
        content = {
            SleepScrollContent(navController, userId)
        }
    )
}

@Composable
fun SleepScrollContent(navController: NavHostController, userId: String){
    Column(modifier = Modifier.fillMaxSize()) {
        // 这里是你的空白页面。你可以在这个 Column 中添加任何你想要的组件。
        // 例如，一个简单的文本提示，表明这是一个待开发的页面：
        Text(text = "Sleep Information Screen for User ID: $userId",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
        // 当你准备添加更多内容时，只需在这里继续添加即可。
    }
}
