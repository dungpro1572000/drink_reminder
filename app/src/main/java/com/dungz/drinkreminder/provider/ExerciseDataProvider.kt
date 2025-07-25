package com.dungz.drinkreminder.provider

import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExerciseDataProvider @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val appRepository: AppRepository
) {
    val coroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())
    val exerciseData = appRepository.getExerciseInfo().stateIn(
        coroutineScope, started = SharingStarted.Eagerly,
        initialValue = null
    )

    fun setExerciseData(
        nextNotificationTime: String = "08:40",
        isNotificationOn: Boolean = true,
        durationNotification: Int = 40,
        isChecked: Boolean = false,
    ) {
        val exerciseModel = com.dungz.drinkreminder.data.roomdb.model.ExerciseModel(
            nextNotificationTime = nextNotificationTime,
            isNotificationOn = isNotificationOn,
            durationNotification = durationNotification,
            isChecked = isChecked
        )

        coroutineScope.launch {
            appRepository.setExerciseInfo(exerciseModel)
        }
    }
}