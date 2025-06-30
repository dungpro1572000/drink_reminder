package com.dungz.drinkreminder.ui.main.setuptime

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesMode
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import com.dungz.drinkreminder.utilities.formatToString
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SetupTimeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler
) :
    ViewModel() {
    var originalSetupTimeState = SetupTimeState.Default
    val eyeData = appRepository.getEyesInfo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        EyesMode(
            isNotificationOn = false,
            durationNotification = 35
        )
    )
    val drinkData = appRepository.getDrinkWaterInfo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        DrinkWaterModel(
            isNotificationOn = false,
            durationNotification = 60
        )
    )
    val exerciseData = appRepository.getExerciseInfo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ExerciseModel(
            isNotificationOn = false,
            durationNotification = 30
        )
    )

    val workingTime = appRepository.getWorkingTime()
        .stateIn(
            viewModelScope, SharingStarted.Eagerly,
            WorkingTime(
                morningStartTime = "08:00",
                afternoonEndTime = "17:00",
                morningEndTime = "12:00",
                afternoonStartTime = "13:00",
                repeatDay = emptyList()
            )
        )


    private val _uiState = MutableStateFlow(SetupTimeState.Default)
    val uiState: StateFlow<SetupTimeState> = _uiState


    init {
        viewModelScope.launch {
            combine(
                eyeData,
                drinkData,
                exerciseData
            ) { eyes, drink, exercise ->
                SetupTimeState(
                    morningTimerStart = workingTime.value?.morningStartTime ?: "08:00",
                    morningTimerEnd = workingTime.value?.morningEndTime ?: "12:00",
                    afternoonTimerStart = workingTime.value?.afternoonStartTime ?: "13:00",
                    afternoonTimerEnd = workingTime.value?.afternoonEndTime ?: "17:00",
                    eyesNotificationStatus = eyeData.value?.isNotificationOn == true,
                    eyesNotificationTime = eyeData.value?.durationNotification ?: 30,
                    drinkWaterNotificationStatus = drinkData.value?.isNotificationOn == true,
                    drinkWaterNotificationTime = drinkData.value?.durationNotification ?: 60,
                    exerciseNotificationStatus = exerciseData.value?.isNotificationOn == true,
                    exerciseNotificationTime = exerciseData.value?.durationNotification ?: 65,
                    repeatDay = workingTime.value?.repeatDay ?: emptyList()
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                SetupTimeState.Default
            ).collectLatest {
                _uiState.value = it
                originalSetupTimeState = it
            }
        }
    }

    fun resetData() {
        _uiState.value = originalSetupTimeState
    }

    fun updateMorningStartTime(time: String) {
        _uiState.value = _uiState.value.copy(morningTimerStart = time)
    }

    fun updateMorningEndTime(time: String) {
        _uiState.value = _uiState.value.copy(morningTimerEnd = time)
    }

    fun updateAfternoonStartTime(time: String) {
        _uiState.value = _uiState.value.copy(afternoonTimerStart = time)
    }

    fun updateAfternoonEndTime(time: String) {
        _uiState.value = _uiState.value.copy(afternoonTimerEnd = time)
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
        viewModelScope.launch {
            val workingTime = WorkingTimeModel(
                morningStartTime = _uiState.value.morningTimerStart,
                morningEndTime = _uiState.value.morningTimerEnd,
                afternoonStartTime = _uiState.value.afternoonTimerStart,
                afternoonEndTime = _uiState.value.afternoonTimerEnd,
                repeatDay = _uiState.value.repeatDay
            )
            appRepository.setWorkTime(workingTime)

            val nextEyesNotification = _uiState.value.morningTimerStart.convertStringTimeToDate()
                .apply {
                    time = time + _uiState.value.eyesNotificationTime * 60 * 1000
                }
            val eyesData = EyesMode(
                isNotificationOn = _uiState.value.eyesNotificationStatus,
                durationNotification = _uiState.value.eyesNotificationTime,
                nextNotificationTime = nextEyesNotification.formatToString()
            )

            appRepository.setEyeInfo(eyesData)
            alarmScheduler.setupAlarmDate(nextEyesNotification, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
            }, AppConstant.ID_EYES_RELAX)
            Log.d(
                "SetupTimeViewModel",
                "Eyes next notification time: ${eyesData.nextNotificationTime}"
            )

            val drinkNotificationTime = _uiState.value.morningTimerStart.convertStringTimeToDate()
                .apply {
                    time = time + _uiState.value.drinkWaterNotificationTime * 60 * 1000
                }
            val drinkData = DrinkWaterModel(
                isNotificationOn = _uiState.value.drinkWaterNotificationStatus,
                durationNotification = _uiState.value.drinkWaterNotificationTime,
                nextNotificationTime = drinkNotificationTime.formatToString()
            )

            Log.d(
                "SetupTimeViewModel",
                "Drink water next notification time: ${drinkData.nextNotificationTime}"
            )
            appRepository.setDrinkWaterInfo(drinkData)
            alarmScheduler.setupAlarmDate(drinkNotificationTime, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_DRINK_WATER)
            }, AppConstant.ID_DRINK_WATER)

            val nextExerciseNotification =
                _uiState.value.morningTimerStart.convertStringTimeToDate()
                    .apply {
                        time = time + _uiState.value.exerciseNotificationTime * 60 * 1000
                    }
            val exerciseData = ExerciseModel(
                isNotificationOn = _uiState.value.exerciseNotificationStatus,
                durationNotification = _uiState.value.exerciseNotificationTime,
                nextNotificationTime = nextExerciseNotification.formatToString()
            )
            appRepository.setExerciseInfo(exerciseData)
            alarmScheduler.setupAlarmDate(nextExerciseNotification, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EXERCISE)
            }, AppConstant.ID_EXERCISE)
            Log.d(
                "SetupTimeViewModel",
                "Exercise next notification time: ${exerciseData.nextNotificationTime}"
            )

            Log.d("SetupTimeViewModel", "Setup time saved: ${_uiState.value.repeatDay}")
        }
    }
}