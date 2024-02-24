package com.cs125.group50.screens

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

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.*

@Composable
fun DietInputScreen(navController: NavHostController, userId: String) {
    val context = LocalContext.current
    var mealType by remember { mutableStateOf("") }
    var foodName by remember { mutableStateOf("") }
    var caloriesPerHundredGrams by remember { mutableStateOf("") }
    var foodAmount by remember { mutableStateOf("") }
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Other")
    var expanded by remember { mutableStateOf(false) }
    val viewModel: DietInputViewModel = viewModel()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }


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
                label = { Text("Meal Type") },
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
            label = { Text("Food Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = caloriesPerHundredGrams,
            onValueChange = { caloriesPerHundredGrams = it },
            label = { Text("Calories per 100g") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = foodAmount,
            onValueChange = { foodAmount = it },
            label = { Text("Food Amount (100g)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { showDatePicker = true }) {
            Text(text = "Select Date: ${selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}")
        }

        // Button to show the time picker
        TextButton(onClick = { showTimePicker = true }) {
            Text(text = "Select Time: ${selectedTime.format(DateTimeFormatter.ISO_LOCAL_TIME)}")
        }

        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            showDatePicker = false
        }

        // Show the time picker dialog
        if (showTimePicker) {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    selectedTime = LocalTime.of(hour, minute)
                    showTimePicker = false
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24 hour time
            ).show()
            showTimePicker = false
        }

        Button(
            onClick = {
                // 调用ViewModel来显示饮食信息，后续确定饮食相关的数据库结构后会修改成提示输入成功
                viewModel.saveDietInfo(userId, mealType, foodName, caloriesPerHundredGrams, foodAmount, selectedDate, selectedTime)
                navController.popBackStack()

            },
            // 检查所有字段是否填写
            enabled = mealType.isNotEmpty() && foodName.isNotEmpty() && caloriesPerHundredGrams.isNotEmpty() && foodAmount.isNotEmpty()
        ) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 添加取消按钮，用于返回上一个界面
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Cancel")
        }
    }
}



