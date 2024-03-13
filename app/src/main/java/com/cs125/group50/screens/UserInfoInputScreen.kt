package com.cs125.group50.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs125.group50.viewmodel.UserInfoInputViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions


@Composable
fun UserInfoInputScreen(navController: NavHostController, userId: String) {
    val viewModel: UserInfoInputViewModel = viewModel()
    var gender by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var expandedGender by remember { mutableStateOf(false) }
    var expandedGoal by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female")
    val goalOptions = listOf("Sleep", "Exercise", "Diet")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Box {
            OutlinedTextField(
                value = gender,
                onValueChange = { /* ReadOnly */ },
                label = { Text("Gender") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, "dropdown", Modifier.clickable { expandedGender = !expandedGender })
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false },
                modifier = Modifier.fillMaxWidth() // 确保 DropdownMenu 和 TextField 宽度一致
            ) {
                genderOptions.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            gender = label
                            expandedGender = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { if (it.matches("^\\d{0,3}$".toRegex())) age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { if (it.matches("^\\d{0,3}$".toRegex())) height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { if (it.matches("^\\d{0,3}$".toRegex())) weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedTextField(
                value = goal,
                onValueChange = { /* ReadOnly */ },
                label = { Text("Goal") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, "dropdown", Modifier.clickable { expandedGoal = !expandedGoal })
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expandedGoal,
                onDismissRequest = { expandedGoal = false },
                modifier = Modifier.fillMaxWidth() // 确保 DropdownMenu 和 TextField 宽度一致
            ) {
                goalOptions.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            goal = label
                            expandedGoal = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val heightInt = height.toIntOrNull()
                val weightInt = weight.toIntOrNull()
                val ageInt = age.toIntOrNull()

                // 检查所有字段是否都有信息
                if (goal.isNotEmpty() && gender.isNotEmpty() && heightInt != null && weightInt != null && ageInt != null) {
                    viewModel.saveUserInfo(userId, gender, heightInt, weightInt, ageInt, goal)
                    navController.popBackStack()
                }
            },
            // 当任何一个字段为空时，禁用按钮
            enabled = gender.isNotEmpty() && height.isNotEmpty() && weight.isNotEmpty() && age.isNotEmpty()
        ) {
            Text("Save Information")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // 取消当前编辑，并返回上一个界面
            navController.popBackStack()
        }) {
            Text("Cancel")
        }
    }
}