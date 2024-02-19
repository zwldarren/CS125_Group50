package com.cs125.group50.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.clickable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs125.group50.viewmodel.DietInputViewModel

// 目前先用中文方便检查，后续修改为英文
@Composable
fun DietInputScreen(navController: NavHostController) {
    val context = LocalContext.current
    var mealType by remember { mutableStateOf("") }
    var foodName by remember { mutableStateOf("") }
    var caloriesPerHundredGrams by remember { mutableStateOf("") }
    var foodAmount by remember { mutableStateOf("") }
    val mealTypes = listOf("早餐", "午餐", "晚餐", "其他")
    var expanded by remember { mutableStateOf(false) }
    val viewModel: DietInputViewModel = viewModel()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            OutlinedTextField(
                value = mealType,
                onValueChange = { /* ReadOnly */ },
                label = { Text("餐饮类型") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown", Modifier.clickable { expanded = !expanded })
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth() // 确保 DropdownMenu 和 TextField 宽度一致
            ) {
                mealTypes.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            mealType = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("食物名称") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = caloriesPerHundredGrams,
            onValueChange = { caloriesPerHundredGrams = it },
            label = { Text("每百克卡路里") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = foodAmount,
            onValueChange = { foodAmount = it },
            label = { Text("食物单位（百克）") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // 调用ViewModel来显示饮食信息，后续确定饮食相关的数据库结构后会修改成提示输入成功
                viewModel.showDietInfo(mealType, foodName, caloriesPerHundredGrams, foodAmount) { info ->
                    Toast.makeText(context, info, Toast.LENGTH_LONG).show()
                }
            },
            // 检查所有字段是否填写
            enabled = mealType.isNotEmpty() && foodName.isNotEmpty() && caloriesPerHundredGrams.isNotEmpty() && foodAmount.isNotEmpty()
        ) {
            Text("提交")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 添加取消按钮，用于返回上一个界面
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("取消")
        }
    }
}



