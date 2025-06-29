package com.dungz.drinkreminder.ui.main.setuptime

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dungz.drinkreminder.R
import com.dungz.drinkreminder.data.roomdb.entity.DrinkWaterEntity
import com.dungz.drinkreminder.data.roomdb.entity.ExerciseEntity
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.ui.theme.NormalTextStyle
import com.dungz.drinkreminder.ui.theme.TitleTextStyle
import com.dungz.drinkreminder.ui.theme.blueBackgroundColor
import com.dungz.drinkreminder.ui.theme.borderColor
import com.dungz.drinkreminder.ui.theme.primaryButtonColor
import com.dungz.drinkreminder.ui.theme.primaryColor
import com.dungz.drinkreminder.ui.theme.whiteColor
import com.dungz.drinkreminder.ui.widget.Line
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import com.dungz.drinkreminder.utilities.convertToDay
import com.dungz.drinkreminder.utilities.formatTime
import com.dungz.drinkreminder.utilities.formatToString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupTimeScreen(
    viewModel: SetupTimeViewModel, navigateBack: () -> Unit = {}
) {
    val setUpTimeState = viewModel.uiState.collectAsState()
    val showTimeDialog = rememberSaveable { mutableStateOf(false) }
    val showMenusEyes = rememberSaveable { mutableStateOf(false) }
    val showMenusDrink = rememberSaveable { mutableStateOf(false) }
    val showMenusExercise = rememberSaveable { mutableStateOf(false) }
    val showSavedSuccess = rememberSaveable { mutableStateOf(false) }
    val snackBarState = remember { SnackbarHostState() }
    val setupTimeType = rememberSaveable { mutableStateOf(SetupTimeType.MorningStart) }
    val currentTime = Calendar.getInstance()
    val coroutineScope = rememberCoroutineScope()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    if (showSavedSuccess.value) {
        Dialog(
            onDismissRequest = { showSavedSuccess.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            LaunchedEffect(Unit) {
                delay(1000)
                showSavedSuccess.value = false
                navigateBack.invoke()
            }
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Icon(
                    painterResource(R.drawable.icon_done),
                    contentDescription = "",
                    Modifier.size(58.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text("Success", style = NormalTextStyle)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarState, snackbar = { data ->
                Snackbar(
                    containerColor = primaryButtonColor,
                    contentColor = Color.White,
                    snackbarData = data
                )
            })
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp),
                            painter = painterResource(R.drawable.icon_setting),
                            tint = primaryColor,
                            contentDescription = null
                        )
                        Text("Setup Time", style = TitleTextStyle)
                    }
                },
                actions = {
                    if (setUpTimeState.value != viewModel.originalSetupTimeState) {
                        Log.d(
                            "DungNT3544",
                            "Setup time has changed, showing save button ${setUpTimeState != viewModel.originalSetupTimeState} \n ${setUpTimeState.value} \n ${viewModel.originalSetupTimeState}"
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val startMorningTime =
                                            setUpTimeState.value.morningTimerStart.convertStringTimeToDate()
                                        val endMorningTime =
                                            setUpTimeState.value.morningTimerEnd.convertStringTimeToDate()
                                        val afternoonStartTime =
                                            setUpTimeState.value.afternoonTimerStart.convertStringTimeToDate()
                                        val afternoonEndTime =
                                            setUpTimeState.value.afternoonTimerEnd.convertStringTimeToDate()

                                        if (endMorningTime.before(startMorningTime) || afternoonStartTime.before(
                                                endMorningTime
                                            ) || afternoonEndTime.before(afternoonStartTime)
                                        ) {
                                            Log.d(
                                                "DungNT354",
                                                "${endMorningTime.before(startMorningTime)} and ${
                                                    afternoonStartTime.before(
                                                        endMorningTime
                                                    )
                                                } and ${afternoonEndTime.before(afternoonStartTime)}"
                                            )
                                            coroutineScope.launch {
                                                snackBarState.showSnackbar(
                                                    message = "Please check your time setup again",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        } else {
                                            viewModel.saveSetupTime()
                                            delay(300)
                                            showSavedSuccess.value = true
                                        }
                                    }
                                }, colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = primaryColor
                                )
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_done),
                                    contentDescription = "Save Setup Time"
                                )
                            }

                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Reset",
                                style = NormalTextStyle.copy(color = primaryColor),
                                modifier = Modifier
                                    .clickable {
                                        viewModel.resetData()
                                    }
                                    .padding(8.dp))
                        }
                    }
                },
            )
        }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(blueBackgroundColor)
        ) {
            if (showTimeDialog.value) {
                TimePickerDialog(
                    onDismiss = { showTimeDialog.value = false }, onConfirm = {
                        showTimeDialog.value = false
                        when (setupTimeType.value) {
                            SetupTimeType.MorningStart -> {
                                viewModel.updateMorningStartTime(formatTime(timePickerState))
                            }

                            SetupTimeType.MorningEnd -> {
                                viewModel.updateMorningEndTime(formatTime(timePickerState))
                            }

                            SetupTimeType.AfternoonStart -> {
                                viewModel.updateAfternoonStartTime(formatTime(timePickerState))
                            }

                            SetupTimeType.AfternoonEnd -> {
                                viewModel.updateAfternoonEndTime(formatTime(timePickerState))
                            }
                        }
                    }, content = {
                        TimeInput(state = timePickerState)
                    }, type = setupTimeType.value
                )
            }

            Column(
                Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .border(
                        width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp), text = "Morning Time", style = TitleTextStyle
                )
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextItem(
                        title = "Start Time",
                        value = setUpTimeState.value.morningTimerStart.convertStringTimeToDate()
                            .formatToString()
                    ) {
                        showTimeDialog.value = true
                        setupTimeType.value = SetupTimeType.MorningStart
                        timePickerState.hour =
                            setUpTimeState.value.morningTimerStart.substringBefore(":").toInt()
                        timePickerState.minute =
                            setUpTimeState.value.morningTimerStart.substringAfter(":").toInt()
                    }
                }
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextItem(
                        title = "End Time",
                        value = setUpTimeState.value.morningTimerEnd.convertStringTimeToDate()
                            .formatToString()
                    ) {
                        showTimeDialog.value = true
                        setupTimeType.value = SetupTimeType.MorningEnd
                        timePickerState.hour =
                            setUpTimeState.value.morningTimerEnd.substringBefore(":").toInt()
                        timePickerState.minute =
                            setUpTimeState.value.morningTimerEnd.substringAfter(":").toInt()
                    }
                }
            }
            Column(
                Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .border(
                        width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text("Afternoon Time", style = TitleTextStyle, modifier = Modifier.padding(8.dp))
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextItem(
                        title = "Start working time",
                        value = setUpTimeState.value.afternoonTimerStart.convertStringTimeToDate()
                            .formatToString()
                    ) {
                        showTimeDialog.value = true
                        setupTimeType.value = SetupTimeType.AfternoonStart
                        timePickerState.hour =
                            setUpTimeState.value.afternoonTimerStart.substringBefore(":").toInt()
                        timePickerState.minute =
                            setUpTimeState.value.afternoonTimerStart.substringAfter(":").toInt()
                    }
                }
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextItem(
                        title = "End working time",
                        value = setUpTimeState.value.afternoonTimerEnd.convertStringTimeToDate()
                            .formatToString()
                    ) {
                        showTimeDialog.value = true
                        setupTimeType.value = SetupTimeType.AfternoonEnd
                        timePickerState.hour =
                            setUpTimeState.value.afternoonTimerEnd.substringBefore(":").toInt()
                        timePickerState.minute =
                            setUpTimeState.value.afternoonTimerEnd.substringAfter(":").toInt()
                    }
                }
            }
            Column(
                Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .border(
                        width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(
                    "Notification Settings",
                    style = TitleTextStyle,
                    modifier = Modifier.padding(8.dp)
                )
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextItem(
                        title = "Eyes Notification",
                        value = "${setUpTimeState.value.eyesNotificationTime} minutes"
                    ) {
                        showMenusEyes.value = true
                    }
                    if (showMenusEyes.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Bottom)
                        ) {
                            DropdownMenu(expanded = showMenusEyes.value, onDismissRequest = {
                                showMenusEyes.value = false
                            }) {
                                EyesEntity.listDuration.forEach { duration ->
                                    DropdownMenuItem(
                                        text = { Text("$duration minutes") },
                                        onClick = {
                                            showMenusEyes.value = false
                                            viewModel.updateEyesNotificationTime(duration)
                                        })
                                }
                            }
                        }
                    }
                }
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextItem(
                        title = "Drink Notification",
                        value = "${setUpTimeState.value.drinkWaterNotificationTime} minutes"
                    ) {
                        showMenusDrink.value = true
                    }
                    if (showMenusDrink.value) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.Bottom)
                        ) {
                            DropdownMenu(
                                tonalElevation = 8.dp,
                                expanded = showMenusDrink.value,
                                onDismissRequest = { showMenusDrink.value = false }) {
                                DrinkWaterEntity.listDuration.forEach { duration ->
                                    DropdownMenuItem(
                                        text = { Text("$duration minutes") },
                                        onClick = {
                                            showMenusDrink.value = false
                                            viewModel.updateDrinkWaterNotificationTime(duration)
                                        })
                                }
                            }
                        }
                    }
                }
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextItem(
                        "Exercise Notification",
                        value = "${setUpTimeState.value.exerciseNotificationTime} minutes"
                    ) {
                        showMenusExercise.value = true
                    }
                    if (showMenusExercise.value) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.Bottom)
                        ) {
                            DropdownMenu(expanded = showMenusExercise.value, onDismissRequest = {
                                showMenusExercise.value = false
                            }) {
                                ExerciseEntity.listDuration.forEach { duration ->
                                    DropdownMenuItem(
                                        text = { Text("$duration minutes") },
                                        onClick = {
                                            showMenusExercise.value = false
                                            viewModel.updateExerciseNotificationTime(duration)
                                        })
                                }
                            }
                        }
                    }
                }
            }

            Column(
                Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .border(
                        width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(
                    "Notification Status", style = TitleTextStyle, modifier = Modifier.padding(8.dp)
                )
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Eyes Notification Status", style = MaterialTheme.typography.titleLarge)
                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = primaryColor,
                        ),
                        checked = setUpTimeState.value.eyesNotificationStatus,
                        onCheckedChange = {
                            viewModel.updateEyesNotificationStatus(it)
                        })
                }
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Drink Notification Status", style = MaterialTheme.typography.titleLarge)
                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = primaryColor,
                        ),
                        checked = setUpTimeState.value.drinkWaterNotificationStatus,
                        onCheckedChange = {
                            viewModel.updateDrinkWaterNotificationStatus(it)
                        })
                }
                Line(Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Exercise Notification Status", style = MaterialTheme.typography.titleLarge
                    )
                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = primaryColor,
                        ),
                        checked = setUpTimeState.value.exerciseNotificationStatus,
                        onCheckedChange = {
                            viewModel.updateExerciseNotificationStatus(it)
                        })
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text("Repeat Days", style = TitleTextStyle, modifier = Modifier.padding(8.dp))
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    (1..7).forEach { index ->
                        FilterChip(
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = whiteColor,
                                selectedContainerColor = primaryColor, // Màu nền khi selected

                                labelColor = Color.Black,
                                selectedLabelColor = Color.Black
                            ),
                            selected = setUpTimeState.value.repeatDay.contains(index),
                            onClick = {
                                val newList = if (setUpTimeState.value.repeatDay.contains(index)) {
                                    setUpTimeState.value.repeatDay - index
                                } else {
                                    setUpTimeState.value.repeatDay + index
                                }
                                viewModel.updateRepeatDay(newList)

                                Log.d(
                                    "SetupTimeScreen",
                                    "Selected days: ${setUpTimeState.value.repeatDay}"
                                )
                            },
                            label = { Text(index.convertToDay()) },
                        )
                    }
                }
            }

        }
    }

}

@Composable
fun TextItem(title: String, value: String, click: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(primaryColor)
                .padding(8.dp)
                .clickable {
                    click.invoke()
                })
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (SetupTimeType) -> Unit,
    content: @Composable () -> Unit,
    type: SetupTimeType
) {
    AlertDialog(onDismissRequest = onDismiss, dismissButton = {
        TextButton(onClick = { onDismiss() }) {
            Text("Dismiss")
        }
    }, confirmButton = {
        TextButton(onClick = { onConfirm(type) }) {
            Text("OK")
        }
    }, text = { content() })
}

enum class SetupTimeType {
    MorningStart, MorningEnd, AfternoonStart, AfternoonEnd
}

sealed class BottomNavItem(
    val route: String, val title: String
) {
    object Eyes : BottomNavItem("eye", "Eyes")
    object Drink : BottomNavItem("drink", "Drink")
    object Exercise : BottomNavItem("exercise", "Exercise")
}