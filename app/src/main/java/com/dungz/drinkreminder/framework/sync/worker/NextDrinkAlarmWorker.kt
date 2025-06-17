package com.dungz.drinkreminder.framework.sync.worker

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import com.dungz.drinkreminder.utilities.formatToString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

@HiltWorker
class NextDrinkAlarmWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParameters: WorkerParameters,
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

            val newDrinkTimer = drinkAlarm.nextNotificationTime.convertStringTimeToDate().apply {
                time + drinkAlarm.durationNotification * 60 * 1000
            }

//            if (newDrinkTimer > workingTime.afternoonEndTime.toDa)
            val intent = Intent().apply {
                action = AppConstant.ALARM_ACTION_RECEIVER
                `package` = AppConstant.packageName
            }

            // Set up the alarms using the AlarmScheduler
            if (newDrinkTimer.before(afternoonEndTime)) {
                appRepository.setDrinkWaterInfo(
                    drinkAlarm.copy(
                        nextNotificationTime = newDrinkTimer.formatToString()
                    )
                )
                alarmScheduler.setupAlarmDate(newDrinkTimer, intent.apply {
                    putExtra(AppConstant.DRINK_WATER_BUNDLE_ID, AppConstant.ID_DRINK_WATER)
                }, AppConstant.ID_DRINK_WATER)
            } else {
                val newDayTime = workingTime.morningStartTime.convertStringTimeToDate().apply {
                    time + drinkAlarm.durationNotification * 60 * 1000
                }
                appRepository.setDrinkWaterInfo(
                    drinkAlarm.copy(
                        nextNotificationTime = newDayTime.formatToString()
                    )
                )
                alarmScheduler.setupAlarmDate(newDayTime, intent.apply {
                    putExtra(AppConstant.DRINK_WATER_BUNDLE_ID, AppConstant.ID_DRINK_WATER)
                }, AppConstant.ID_DRINK_WATER)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        val worker = OneTimeWorkRequest.Builder(NextDrinkAlarmWorker::class.java).setInitialDelay(
            2,
            TimeUnit.HOURS
        ).build()
    }
}