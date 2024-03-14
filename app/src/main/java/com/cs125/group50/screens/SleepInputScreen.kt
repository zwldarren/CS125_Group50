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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs125.group50.viewmodel.SleepInputViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun SleepInputScreen(navController: NavHostController, userId: String) {
    val context = LocalContext.current
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var endTime by remember { mutableStateOf(LocalTime.now().plusHours(8)) } // Default to 8 hours later for sleep end time
    val viewModel: SleepInputViewModel = viewModel()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
            Text(text = "Select Sleep Start Time: ${startTime.toString()}")
        }
        // End Time Picker
        TextButton(onClick = { showEndTimePicker = true }) {
            Text(text = "Select Sleep End Time: ${endTime.toString()}")
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
                // Assuming viewModel has a method to save sleep data
                viewModel.saveSleepInfo(userId,startTime,endTime,selectedDate)
                navController.popBackStack()
            },
            // You could add validation to ensure end time is after start time, etc.
            enabled = true
        ) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cancel button to return to the previous screen
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Cancel")
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
}