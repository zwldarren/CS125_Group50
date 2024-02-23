package com.cs125.group50.nav

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs125.group50.screens.ActivityInputScreen
import com.cs125.group50.screens.DashboardScreen
import com.cs125.group50.screens.DietInformationScreen
import com.cs125.group50.screens.LoginScreen
import com.cs125.group50.screens.UserInfoInputScreen
import com.google.firebase.auth.FirebaseAuth
import com.cs125.group50.screens.DietInputScreen

@Composable
fun AppNavigation(navController: NavHostController, isUserLoggedIn: Boolean) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid.orEmpty()
    NavHost(navController = navController, startDestination = if (isUserLoggedIn) "dashboard" else "login") {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") {
            if (userId.isNotEmpty()) {
                DashboardScreen(navController, context)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }
        composable("userInfoInput") {
            if (userId.isNotEmpty()) {
                UserInfoInputScreen(navController, userId)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }

        // 输入饮食的界面
        composable("dietInput") {
            if (userId.isNotEmpty()) {
                DietInputScreen(navController, userId)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }

        // 查看饮食相关的界面
        composable("dietInfo") {
            if (userId.isNotEmpty()) {
                DietInformationScreen(navController, userId)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }

        // 输入活动的界面
        composable("activityInput") {
            if (userId.isNotEmpty()) {
                ActivityInputScreen(navController, userId)
            } else {
                // 处理未登录或用户ID为空的情况
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(navController: NavHostController,
               screenTitle: String,
               content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = screenTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}


data class BottomNavItem(
    val title: String,
    val icon: ImageVector?,
    val route: String
)
@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, "dashboard"),
        BottomNavItem("Diet", null, "dietInput"), // 圆形
        BottomNavItem("Exercise", null, "exercise"), // 正方形
        BottomNavItem("Sleep", null, "sleep"), // 三角形
        BottomNavItem("Menu", Icons.Filled.Menu, "menu")
    )
    NavigationBar {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item.title) {
                        "Diet" -> DrawDiamondIcon()
                        "Exercise" -> DrawDiamondIcon()
                        "Sleep" -> DrawDiamondIcon()
                        else -> item.icon?.let { Icon(it, contentDescription = item.title) }
                    }
                },
                label = { Text(item.title) },
                selected = item.route == currentRoute,
                onClick = {
                    navController.navigate(item.route) {
                        // 当导航到一个新的目的地时，清空回退栈直到第一个目的地
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // 避免创建目的地的多个实例
                        launchSingleTop = true
                        // 恢复状态
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun DrawDiamondIcon() {
    val iconColor = MaterialTheme.colorScheme.onSurface
    Canvas(modifier = Modifier.size(17.dp)) {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height / 2f)
            lineTo(size.width / 2f, size.height)
            lineTo(0f, size.height / 2f)
            close()
        }
        drawPath(
            path = path,
            color = iconColor
        )
    }
}