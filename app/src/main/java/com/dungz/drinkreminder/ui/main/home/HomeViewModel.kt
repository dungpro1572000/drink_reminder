package com.dungz.drinkreminder.ui.main.home

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.calcTimeLeft
import com.dungz.drinkreminder.utilities.getTodayTime
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    val workingTime = appRepository.getWorkingTime()
        .stateIn(
            viewModelScope, SharingStarted.Eagerly,
            WorkingTime(
                startTime = "08:00",
                endTime = "12:00",
                repeatDay = emptyList()
            )
        )


    private val drinkWaterFlow = appRepository.getDrinkWaterInfo()
    private val eyesRelaxFlow = appRepository.getEyesInfo()
    private val exerciseFlow = appRepository.getExerciseInfo()
    private val tickerFlow = flow {
        while (true) {
            emit(Unit)
            delay(1000)
        }
    }

    val homeScreenState: StateFlow<HomeScreenState> = combine(
        tickerFlow,
        drinkWaterFlow,
        eyesRelaxFlow,
        exerciseFlow
    ) { _, drink, eyes, exercise ->

        HomeScreenState(
            drinkTimeLeft = if (drink?.nextNotificationTime == null) null else
                calcTimeLeft(drink.nextNotificationTime).toInt(),
            eyesRelaxTimeLeft = if (eyes?.nextNotificationTime == null) null else
                calcTimeLeft(eyes.nextNotificationTime).toInt(),
            exerciseTimeLeft = if (exercise?.nextNotificationTime == null) null else
                calcTimeLeft(
                    exercise.nextNotificationTime
                ).toInt(),
            drinkTime = drink?.nextNotificationTime,
            eyesRelaxTime = eyes?.nextNotificationTime,
            exerciseTime = exercise?.nextNotificationTime,
            isCheckedDrink = drink?.isChecked ?: false,
            isCheckedEyes = eyes?.isChecked ?: false,
            isCheckedExercise = exercise?.isChecked ?: false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        HomeScreenState.Default
    )

    fun setUpTime() {
        viewModelScope.launch {
            alarmScheduler.setupAlarmTimeMillis(5000L, Bundle().apply {
                putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
            }, AppConstant.ID_EYES_RELAX)
        }
    }

    fun saveRecordDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateDrinkChecked(true)
            appRepository.updateRecordDrinkTime(getTodayTime())
        }
    }

    fun saveRecordEyesRelax() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateEyesChecked(true)
            appRepository.updateRecordEyesRelaxTime(getTodayTime())
        }
    }

    fun saveRecordExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateExerciseChecked(true)
            appRepository.updateRecordExerciseTime(getTodayTime())
        }
    }

    fun setUpAlarm() {
        alarmScheduler.setupAlarmTimeMillis(5000L, Bundle().apply {
            putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
        }, AppConstant.ID_EYES_RELAX)
    }
}