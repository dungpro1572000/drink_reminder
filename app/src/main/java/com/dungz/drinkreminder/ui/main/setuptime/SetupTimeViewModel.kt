package com.dungz.drinkreminder.ui.main.setuptime

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.EyesEntity
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.data.roomdb.model.ExerciseModel
import com.dungz.drinkreminder.data.roomdb.model.EyesMode
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
    private val setupTimeState = mutableStateOf(SetupTimeState.Default)
    private val workingTimeFlow = appRepository.getWorkingTime().stateIn(
        initialValue = WorkingTime(
            morningStartTime = "08:00",
            afternoonEndTime = "17:00",
            morningEndTime = "12:00",
            afternoonStartTime = "13:00",
            repeatDay = emptyList()
        ),
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )
    val state get() = setupTimeState

    init {
        viewModelScope.launch {
            val workingTime = workingTimeFlow.firstOrNull() ?: WorkingTime(
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
            state.value = state.value.copy(
                morningTimerStart = workingTime.morningStartTime,
                morningTimerEnd = workingTime.morningEndTime,
                afternoonTimerStart = workingTime.afternoonStartTime,
                afternoonTimerEnd = workingTime.afternoonEndTime,
                eyesNotificationStatus = eyesData.isNotificationOn,
                eyesNotificationTime = eyesData.durationNotification,
                drinkWaterNotificationStatus = drinkData.isNotificationOn,
                drinkWaterNotificationTime = drinkData.durationNotification,
                exerciseNotificationStatus = exerciseData.isNotificationOn,
                exerciseNotificationTime = exerciseData.durationNotification
            )
        }
    }
}