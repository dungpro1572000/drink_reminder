package com.dungz.drinkreminder.ui.main.setuptime

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesModel
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.provider.DrinkDataProvider
import com.dungz.drinkreminder.provider.ExerciseDataProvider
import com.dungz.drinkreminder.provider.EyesDataProvider
import com.dungz.drinkreminder.provider.TimeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SetupTimeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler,
    private val drinkDataProvider: DrinkDataProvider,
    private val eyesDataProvider: EyesDataProvider,
    private val exerciseDataProvider: ExerciseDataProvider,
    private val timeProvider: TimeProvider,
) : ViewModel() {

    var originalSetupTimeState = SetupTimeState.Default

    // Initialize with default values to avoid nullability issues
    val eyeData = appRepository.getEyesInfo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        EyesModel(
            isNotificationOn = false,
            durationNotification = 35
        )
    )

    val drinkData = appRepository.getDrinkWaterInfo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        DrinkWaterModel(
            isNotificationOn = false,
            durationNotification = 40
        )
    )

    val exerciseData = appRepository.getExerciseInfo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ExerciseModel(
            isNotificationOn = false,
            durationNotification = 60
        )
    )

    val workingTime = appRepository.getWorkingTime().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        WorkingTime(
            startTime = "08:00",
            endTime = "12:00",
            repeatDay = emptyList()
        )
    )

    private val _uiState = MutableStateFlow(SetupTimeState.Default)
    val uiState: StateFlow<SetupTimeState> = _uiState.asStateFlow()

    // Combined flow for observing all data changes
    private val combinedDataFlow = combine(
        workingTime,
        eyeData,
        drinkData,
        exerciseData
    ) { workingTime, eyes, drink, exercise ->
        if (workingTime != null && eyes != null && drink != null && exercise != null) {
            SetupTimeState(
                startTime = workingTime.startTime,
                endTime = workingTime.endTime,
                eyesNotificationStatus = eyes.isNotificationOn,
                eyesNotificationTime = eyes.durationNotification,
                drinkWaterNotificationStatus = drink.isNotificationOn,
                drinkWaterNotificationTime = drink.durationNotification,
                exerciseNotificationStatus = exercise.isNotificationOn,
                exerciseNotificationTime = exercise.durationNotification,
                repeatDay = workingTime.repeatDay
            )
        } else {
            SetupTimeState.Default
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SetupTimeState.Default
    )

    fun initialize() {
        viewModelScope.launch {
            combinedDataFlow.collect { state ->
                _uiState.value = state
                originalSetupTimeState = state
            }
        }
    }

    fun resetData() {
        _uiState.value = originalSetupTimeState
    }

    // Individual update methods (kept for backward compatibility)
    fun updateMorningStartTime(time: String) {
        _uiState.value = _uiState.value.copy(startTime = time)
    }

    fun updateMorningEndTime(time: String) {
        _uiState.value = _uiState.value.copy(endTime = time)
    }

    fun updateEyesNotificationStatus(status: Boolean) {
        _uiState.value = _uiState.value.copy(eyesNotificationStatus = status)
    }

    fun updateEyesNotificationTime(time: Int) {
        _uiState.value = _uiState.value.copy(eyesNotificationTime = time)
    }

    fun updateDrinkWaterNotificationStatus(status: Boolean) {
        _uiState.value = _uiState.value.copy(drinkWaterNotificationStatus = status)
    }

    fun updateDrinkWaterNotificationTime(time: Int) {
        _uiState.value = _uiState.value.copy(drinkWaterNotificationTime = time)
    }

    fun updateExerciseNotificationStatus(status: Boolean) {
        _uiState.value = _uiState.value.copy(exerciseNotificationStatus = status)
    }

    fun updateExerciseNotificationTime(time: Int) {
        _uiState.value = _uiState.value.copy(exerciseNotificationTime = time)
    }

    fun updateRepeatDay(days: List<Int>) {
        _uiState.value = _uiState.value.copy(repeatDay = days)
    }

    fun saveSetupTime() {
        val currentState = _uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            try {
                timeProvider.setWorkingTime(
                    currentState.startTime,
                    currentState.endTime,
                    currentState.repeatDay
                )

                drinkDataProvider.setDrinkData(
                    durationNotification = currentState.drinkWaterNotificationTime,
                    isNotificationOn = currentState.drinkWaterNotificationStatus
                )

                eyesDataProvider.setEyesData(
                    durationNotification = currentState.eyesNotificationTime,
                    isNotificationOn = currentState.eyesNotificationStatus
                )

                exerciseDataProvider.setExerciseData(
                    durationNotification = currentState.exerciseNotificationTime,
                    isNotificationOn = currentState.exerciseNotificationStatus
                )

                Log.d(
                    "SetupTimeViewModel",
                    "Setup completed successfully for days: ${currentState.repeatDay}"
                )
            } catch (e: Exception) {
                Log.e("SetupTimeViewModel", "Error saving setup", e)
            }
        }
    }

}