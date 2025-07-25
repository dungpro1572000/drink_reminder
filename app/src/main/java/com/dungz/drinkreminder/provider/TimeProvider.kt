package com.dungz.drinkreminder.provider

import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.data.roomdb.model.WorkingTimeModel
import com.dungz.drinkreminder.di.IoDispatcher
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeProvider @Inject constructor(private val appDb: AppRepository,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val alarmScheduler: AlarmScheduler,
    ) {
    val coroutineScope = CoroutineScope(ioDispatcher + SupervisorJob())
    val workingTime = appDb.getWorkingTime().stateIn(
        coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    fun setWorkingTime(startTime: String, endTime: String, repeatDay: List<Int>) {
        val workingModel = WorkingTimeModel(startTime, endTime, repeatDay)
        coroutineScope.launch {
            appDb.setWorkTime(workingModel)
        }
    }

    fun setUpReminderTime(){

    }


}