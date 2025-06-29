package com.dungz.drinkreminder.ui.main.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.entity.WorkingTime
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
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

    val drinkWaterFlow = appRepository.getDrinkWaterInfo()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val eyesRelaxFlow = appRepository.getEyesInfo()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val exerciseFlow = appRepository.getExerciseInfo()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}