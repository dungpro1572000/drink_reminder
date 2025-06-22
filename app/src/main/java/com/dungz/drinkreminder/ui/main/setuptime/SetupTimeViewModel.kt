package com.dungz.drinkreminder.ui.main.setuptime

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesMode
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SetupTimeViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
        val setupTimeState = mutableStateOf(SetupTimeState.Default)

    init {
        viewModelScope.launch {
            val workingTime = appRepository.getWorkingTime().firstOrNull() ?: WorkingTime(
                morningStartTime = "08:00",
                afternoonEndTime = "17:00",
                morningEndTime = "12:00",
                afternoonStartTime = "13:00",
                repeatDay = emptyList()
            )
            val eyesData = appRepository.getEyesInfo().firstOrNull() ?: EyesMode(
                isNotificationOn = false,
                durationNotification = 35
            )
            val drinkData = appRepository.getDrinkWaterInfo().firstOrNull()
                ?: DrinkWaterModel(
                    isNotificationOn = false,
                    durationNotification = 60
                )
            val exerciseData = appRepository.getExerciseInfo().firstOrNull()
                ?: ExerciseModel(
                    isNotificationOn = false,
                    durationNotification = 30
                )
            setupTimeState.value = setupTimeState.value.copy(
                morningTimerStart = workingTime.morningStartTime,
                morningTimerEnd = workingTime.morningEndTime,
                afternoonTimerStart = workingTime.afternoonStartTime,
                afternoonTimerEnd = workingTime.afternoonEndTime,
                eyesNotificationStatus = eyesData.isNotificationOn,
                eyesNotificationTime = eyesData.durationNotification,
                drinkWaterNotificationStatus = drinkData.isNotificationOn,
                drinkWaterNotificationTime = drinkData.durationNotification,
                exerciseNotificationStatus = exerciseData.isNotificationOn,
                exerciseNotificationTime = exerciseData.durationNotification,
                repeatDay = workingTime.repeatDay
            )
            Log.d("SetupTimeViewModel", "Initial state: ${setupTimeState.value}")
        }
    }

    fun saveSetupTime() {
        viewModelScope.launch {
            val workingTime = WorkingTimeModel(
                morningStartTime = setupTimeState.value.morningTimerStart,
                morningEndTime = setupTimeState.value.morningTimerEnd,
                afternoonStartTime = setupTimeState.value.afternoonTimerStart,
                afternoonEndTime = setupTimeState.value.afternoonTimerEnd,
                repeatDay = setupTimeState.value.repeatDay
            )
            appRepository.setWorkTime(workingTime)

            val eyesData = EyesMode(
                isNotificationOn = setupTimeState.value.eyesNotificationStatus,
                durationNotification = setupTimeState.value.eyesNotificationTime
            )
            appRepository.setEyeInfo(eyesData)

            val drinkData = DrinkWaterModel(
                isNotificationOn = setupTimeState.value.drinkWaterNotificationStatus,
                durationNotification = setupTimeState.value.drinkWaterNotificationTime
            )
            appRepository.setDrinkWaterInfo(drinkData)

            val exerciseData = ExerciseModel(
                isNotificationOn = setupTimeState.value.exerciseNotificationStatus,
                durationNotification = setupTimeState.value.exerciseNotificationTime
            )
            appRepository.setExerciseInfo(exerciseData)

            Log.d("SetupTimeViewModel", "Setup time saved: ${setupTimeState.value.repeatDay}")
        }
    }
}