package com.dungz.drinkreminder.provider

import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.model.EyesModel
import com.dungz.drinkreminder.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class EyesDataProvider @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val appRepository: AppRepository
) {

    val coroutineScope =
        kotlinx.coroutines.CoroutineScope(ioDispatcher + kotlinx.coroutines.SupervisorJob())
    val eyesData = appRepository.getEyesInfo().stateIn(
        coroutineScope, started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
        initialValue = null
    )

    fun setEyesData(
        nextNotificationTime: String = "08:40",
        isNotificationOn: Boolean = true,
        durationNotification: Int = 40,
        isChecked: Boolean = false,
    ) {
        val eyesModel = EyesModel(
            nextNotificationTime = nextNotificationTime,
            isNotificationOn = isNotificationOn,
            durationNotification = durationNotification,
            isChecked = isChecked
        )

        coroutineScope.launch {
            appRepository.setEyeInfo(eyesModel)
        }
    }

}