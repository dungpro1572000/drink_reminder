package com.dungz.drinkreminder.ui.main.setuptime

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupTimeScreen() {
    val showDialog = rememberSaveable { mutableStateOf(true) }
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    Scaffold() { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Row {
                Text(
                    "morning start working time setup",
                    style = MaterialTheme.typography.titleLarge
                )
                if (showDialog.value) {
                    TimePickerDialog(
                        onDismiss = { },
                        onConfirm = { }
                    ) {
                        TimeInput(
                            state = timePickerState,
                        )
                    }
                }
            }

            Row {
                Text("morning end working time setup", style = MaterialTheme.typography.titleLarge)
            }

            Row {
                Text(
                    "afternoon start working time setup",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row {
                Text(
                    "afternoon end working time setup",
                    style = MaterialTheme.typography.titleLarge
                )
            }

        }
    }

}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

sealed class BottomNavItem(
    val route: String,
    val title: String
) {
    object Eyes : BottomNavItem("eye", "Eyes")
    object Drink : BottomNavItem("drink", "Drink")
    object Exercise : BottomNavItem("exercise", "Exercise")
}