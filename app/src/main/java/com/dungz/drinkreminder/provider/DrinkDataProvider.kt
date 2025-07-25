package com.dungz.drinkreminder.provider

import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.model.DrinkWaterModel
import com.dungz.drinkreminder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    fun setDrinkData(
        nextNotificationTime: String = "08:40",
        isNotificationOn: Boolean = true,
        durationNotification: Int = 40,
        isChecked: Boolean = false,
    ) {
        val drinkWaterModel = DrinkWaterModel(
            nextNotificationTime = nextNotificationTime,
            isNotificationOn = isNotificationOn,
            durationNotification = durationNotification,
            isChecked = isChecked
        )

        coroutineScope.launch {
            appRepository.setDrinkWaterInfo(drinkWaterModel)
        }
    }


}