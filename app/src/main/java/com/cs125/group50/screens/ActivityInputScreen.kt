package com.cs125.group50.screens

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs125.group50.viewmodel.ActivityInputViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ActivityInputScreen(navController: NavHostController, userId: String) {
    val context = LocalContext.current
    var activityType by remember { mutableStateOf("") }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var endTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) } // 默认增加一小时
    val activityTypes = listOf("Walking", "Running", "Cycling", "Swimming")
    var expanded by remember { mutableStateOf(false) }
    val viewModel: ActivityInputViewModel = viewModel()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var caloriesBurned by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            OutlinedTextField(
                value = activityType,
                onValueChange = { /* ReadOnly */ },
                label = { Text("Activity Type") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown", Modifier.clickable { expanded = !expanded })
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                activityTypes.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            activityType = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = caloriesBurned,
            onValueChange = { caloriesBurned = it },
            label = { Text("Calories Burned") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // 仅允许输入数字
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { showDatePicker = true }) {
            Text(text = "Select Date: ${selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}")
        }


        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            android.app.DatePickerDialog(
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

        // Start Time Picker
        TextButton(onClick = { showStartTimePicker = true }) {
            Text(text = "Select Start Time: ${startTime.toString()}")
        }
        // End Time Picker
        TextButton(onClick = { showEndTimePicker = true }) {
            Text(text = "Select End Time: ${endTime.toString()}")
        }

        TimePickerDialogLogic(showStartTimePicker, onTimeSelected = { time ->
            startTime = time
            showStartTimePicker = false
        })

        TimePickerDialogLogic(showEndTimePicker, onTimeSelected = { time ->
            endTime = time
            showEndTimePicker = false
        })

        Button(
            onClick = {
                val caloriesBurnedInt = caloriesBurned.toIntOrNull() ?: 0
                viewModel.saveActivityInfo(userId, activityType, startTime, endTime, selectedDate, caloriesBurnedInt)
                navController.popBackStack()
            },
            enabled = activityType.isNotEmpty()
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

@Composable
fun TimePickerDialogLogic(showPicker: Boolean, onTimeSelected: (LocalTime) -> Unit) {
    val context = LocalContext.current
    if (showPicker) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                onTimeSelected(LocalTime.of(hour, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24 hour time
        ).show()
    }
}
