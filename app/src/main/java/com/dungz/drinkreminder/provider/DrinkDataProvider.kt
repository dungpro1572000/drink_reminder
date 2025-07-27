package com.dungz.drinkreminder.provider

import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.di.IoDispatcher
import com.dungz.drinkreminder.utilities.convertStringTimeToHHmm
import com.dungz.drinkreminder.utilities.formatToString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DrinkDataProvider @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val appRepository: AppRepository
) {
    val coroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())
    val drinkData = appRepository.getDrinkWaterInfo().stateIn(
        coroutineScope, started = SharingStarted.Eagerly,
        null
    )

    suspend fun setDrinkData(
        isNotificationOn: Boolean = true,
        durationNotification: Int = 40,
        isChecked: Boolean = false,
    ) {
        val workingTime = appRepository.getWorkingTime().firstOrNull()
        if (workingTime != null) {
            val inComingAlarm = workingTime.startTime.convertStringTimeToHHmm().apply {
                time += durationNotification * 60 * 1000
            }
            val nextInComingAlarm = inComingAlarm.apply {
                time += durationNotification * 60 * 1000
            }
            val drinkModel = DrinkWaterModel(
                inComingAlarm = inComingAlarm.formatToString(),
                nextInComingAlarm = nextInComingAlarm.formatToString(),
                isNotificationOn = isNotificationOn,
                durationNotification = durationNotification,
                isChecked = isChecked
            )

            appRepository.setDrinkWaterInfo(drinkModel)
        }
    }


}