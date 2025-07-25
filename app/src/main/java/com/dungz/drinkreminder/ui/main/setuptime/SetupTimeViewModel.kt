package com.dungz.drinkreminder.ui.main.setuptime

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.RecordCompleteEntity
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesModel
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import com.dungz.drinkreminder.utilities.formatToString
import com.dungz.drinkreminder.utilities.getTodayTime
import com.dungz.drinkreminder.utilities.minuteBetween2Date
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
    private val alarmScheduler: AlarmScheduler
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

    // Batch state updates to reduce recomposition
    fun updateTimeRange(startTime: String, endTime: String) {
        _uiState.value = _uiState.value.copy(
            startTime = startTime,
            endTime = endTime
        )
    }

    fun updateEyesSettings(status: Boolean, time: Int) {
        _uiState.value = _uiState.value.copy(
            eyesNotificationStatus = status,
            eyesNotificationTime = time
        )
    }

    fun updateDrinkWaterSettings(status: Boolean, time: Int) {
        _uiState.value = _uiState.value.copy(
            drinkWaterNotificationStatus = status,
            drinkWaterNotificationTime = time
        )
    }

    fun updateExerciseSettings(status: Boolean, time: Int) {
        _uiState.value = _uiState.value.copy(
            exerciseNotificationStatus = status,
            exerciseNotificationTime = time
        )
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
                // Save working time
                saveWorkingTime(currentState)

                // Calculate total morning minutes once
                val totalMorningMinutes = minuteBetween2Date(
                    currentState.startTime,
                    currentState.endTime
                )

                // Insert record for today
                appRepository.insertRecord(RecordCompleteEntity(getTodayTime()))

                // Save notification settings and setup alarms
                saveEyesSettings(currentState, totalMorningMinutes)
                saveDrinkWaterSettings(currentState, totalMorningMinutes)
                saveExerciseSettings(currentState, totalMorningMinutes)

                Log.d("SetupTimeViewModel", "Setup completed successfully for days: ${currentState.repeatDay}")
            } catch (e: Exception) {
                Log.e("SetupTimeViewModel", "Error saving setup", e)
            }
        }
    }

    private suspend fun saveWorkingTime(state: SetupTimeState) {
        val workingTime = WorkingTimeModel(
            startTime = state.startTime,
            endTime = state.endTime,
            repeatDay = state.repeatDay
        )
        appRepository.setWorkTime(workingTime)
    }

    private suspend fun saveEyesSettings(state: SetupTimeState, totalMinutes: Int) {
        val nextNotificationTime = state.startTime.convertStringTimeToHHmm().apply {
            time = time + state.eyesNotificationTime * 60 * 1000
        }

        val eyesData = EyesModel(
            isNotificationOn = state.eyesNotificationStatus,
            durationNotification = state.eyesNotificationTime,
            nextNotificationTime = nextNotificationTime.formatToString()
        )

        appRepository.setEyeInfo(eyesData)

        // Only schedule alarm if notifications are enabled
        if (state.eyesNotificationStatus) {
            alarmScheduler.setupAlarmDate(
                nextNotificationTime,
                Bundle().apply { putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX) },
                AppConstant.ID_EYES_RELAX
            )
        }

        // Update total time calculation
        val eyeDataValue = eyeData.value
        val totalEyesTime = totalMinutes / (eyeDataValue?.durationNotification ?: 35)
        appRepository.updateTotalEyesRelaxTime(totalEyesTime, getTodayTime())

        Log.d("SetupTimeViewModel", "Eyes next notification: ${eyesData.nextNotificationTime}")
    }

    private suspend fun saveDrinkWaterSettings(state: SetupTimeState, totalMinutes: Int) {
        val nextNotificationTime = state.startTime.convertStringTimeToHHmm().apply {
            time = time + state.drinkWaterNotificationTime * 60 * 1000
        }

        val drinkWaterData = DrinkWaterModel(
            isNotificationOn = state.drinkWaterNotificationStatus,
            durationNotification = state.drinkWaterNotificationTime,
            nextNotificationTime = nextNotificationTime.formatToString()
        )

        appRepository.setDrinkWaterInfo(drinkWaterData)

        // Only schedule alarm if notifications are enabled
        if (state.drinkWaterNotificationStatus) {
            alarmScheduler.setupAlarmDate(
                nextNotificationTime,
                Bundle().apply { putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_DRINK_WATER) },
                AppConstant.ID_DRINK_WATER
            )
        }

        // Update total time calculation
        val drinkDataValue = drinkData.value
        val totalDrinkTime = totalMinutes / (drinkDataValue?.durationNotification ?: 60)
        appRepository.updateTotalDrinkTime(totalDrinkTime, getTodayTime())

        Log.d("SetupTimeViewModel", "Drink water next notification: ${drinkWaterData.nextNotificationTime}")
    }

    private suspend fun saveExerciseSettings(state: SetupTimeState, totalMinutes: Int) {
        val nextNotificationTime = state.startTime.convertStringTimeToHHmm().apply {
            time = time + state.exerciseNotificationTime * 60 * 1000
        }

        val exerciseDataModel = ExerciseModel(
            isNotificationOn = state.exerciseNotificationStatus,
            durationNotification = state.exerciseNotificationTime,
            nextNotificationTime = nextNotificationTime.formatToString()
        )

        appRepository.setExerciseInfo(exerciseDataModel)

        // Only schedule alarm if notifications are enabled
        if (state.exerciseNotificationStatus) {
            alarmScheduler.setupAlarmDate(
                nextNotificationTime,
                Bundle().apply { putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EXERCISE) },
                AppConstant.ID_EXERCISE
            )
        }

        // Update total time calculation
        val exerciseDataValue = exerciseData.value
        val totalExerciseTime = totalMinutes / (exerciseDataValue?.durationNotification ?: 30)
        appRepository.updateTotalExerciseTime(totalExerciseTime, getTodayTime())

        Log.d("SetupTimeViewModel", "Exercise next notification: ${exerciseDataModel.nextNotificationTime}")
    }
}