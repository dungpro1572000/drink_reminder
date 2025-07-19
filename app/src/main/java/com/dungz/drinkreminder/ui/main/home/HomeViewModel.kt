package com.dungz.drinkreminder.ui.main.home

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.getTodayTime
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
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

    val drinkWaterFlow = appRepository.getDrinkWaterInfo()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val eyesRelaxFlow = appRepository.getEyesInfo()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val exerciseFlow = appRepository.getExerciseInfo()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
}