package com.dungz.drinkreminder.provider

import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.model.EyesModel
import com.dungz.drinkreminder.di.IoDispatcher
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import com.dungz.drinkreminder.utilities.formatToString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class EyesDataProvider @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler,
    private val timeProvider: TimeProvider,
) {

    val coroutineScope =
        kotlinx.coroutines.CoroutineScope(ioDispatcher + kotlinx.coroutines.SupervisorJob())
    val eyesData = appRepository.getEyesInfo().stateIn(
        coroutineScope, started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
        initialValue = null
    )

    suspend fun setEyesData(
        isNotificationOn: Boolean = true,
        durationNotification: Int = 40,
        isChecked: Boolean = false,
    ) {
            val workingTime = timeProvider.workingTime.firstOrNull()
            if (workingTime != null) {
                val inComingAlarm = workingTime.startTime.convertStringTimeToHHmm().apply {
                    time += durationNotification * 60 * 1000
                }
                val nextInComingAlarm = inComingAlarm.apply {
                    time += durationNotification * 60 * 1000
                }
                val eyesModel = EyesModel(
                    inComingAlarm = inComingAlarm.formatToString(),
                    nextInComingAlarm = nextInComingAlarm.formatToString(),
                    isNotificationOn = isNotificationOn,
                    durationNotification = durationNotification,
                    isChecked = isChecked
                )
                appRepository.setEyeInfo(eyesModel)

        }
    }

    fun setUpReminderTime(){
        coroutineScope.launch {
            val workingTime = timeProvider.workingTime.firstOrNull()
            if (workingTime != null) {

            }
        }
    }

}