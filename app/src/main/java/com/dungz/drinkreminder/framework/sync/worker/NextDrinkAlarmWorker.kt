package com.dungz.drinkreminder.framework.sync.worker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.impl.utils.EnqueueRunnable.enqueue
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import com.dungz.drinkreminder.utilities.formatToString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class NextDrinkAlarmWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val appRepository: AppRepository,
    private val alarmScheduler: AlarmScheduler,
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            // Get the next alarm times from the repository
            val drinkAlarm = appRepository.getDrinkWaterInfo().firstOrNull()

            val workingTime = appRepository.getWorkingTime().firstOrNull()
            if (drinkAlarm == null || workingTime == null) {
                return Result.failure()
            }
            val afternoonEndTime = workingTime.afternoonEndTime.convertStringTimeToDate()
            val workingDay = workingTime.repeatDay

            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

            val newDrinkTimer = drinkAlarm.nextNotificationTime.convertStringTimeToDate().apply {
                time = time + drinkAlarm.durationNotification * 60 * 1000
            }


            // Set up the alarms using the AlarmScheduler
            if (newDrinkTimer.before(afternoonEndTime) && workingDay.contains(today)) {
                appRepository.setDrinkWaterInfo(
                    drinkAlarm.copy(
                        nextNotificationTime = newDrinkTimer.formatToString(),
                        isChecked = false,
                    )
                )
                alarmScheduler.setupAlarmDate(
                    newDrinkTimer,
                    Bundle().apply {
                        putInt(AppConstant.ALARM_BUNDLE_ID, AppConstant.ID_DRINK_WATER)
                    }, AppConstant.ID_DRINK_WATER
                )
            } else {
                val newDayTime = workingTime.morningStartTime.convertStringTimeToDate().apply {
                    time = time + drinkAlarm.durationNotification * 60 * 1000
                }
                appRepository.setDrinkWaterInfo(
                    drinkAlarm.copy(
                        nextNotificationTime = newDayTime.formatToString(),
                        isChecked = false
                    )
                )
                val worker = OneTimeWorkRequest.Builder(SetUpEveryDayWorker::class.java)
                    .setInitialDelay(4, TimeUnit.HOURS)
                    .build()

                WorkManager.getInstance(appContext).enqueue(worker)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        val worker = OneTimeWorkRequest.Builder(NextDrinkAlarmWorker::class.java).build()
    }
}