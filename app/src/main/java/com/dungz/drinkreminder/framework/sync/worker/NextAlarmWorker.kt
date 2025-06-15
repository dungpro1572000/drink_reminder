package com.dungz.drinkreminder.framework.sync.worker

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dungz.drinkreminder.data.repository.AppRepository
import com.dungz.drinkreminder.framework.sync.alarm.AlarmScheduler
import com.dungz.drinkreminder.utilities.AppConstant
import com.dungz.drinkreminder.utilities.convertStringTimeToDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltWorker
class NextAlarmWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val appRepository: AppRepository
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            // Get the next alarm times from the repository
            val drinkAlarm = appRepository.getDrinkWaterInfo().firstOrNull()
            val eyeTime = appRepository.getEyesInfo().firstOrNull()
            val exerciseTime = appRepository.getExerciseInfo().firstOrNull()
            val workingTime = appRepository.getWorkingTime().firstOrNull()
            if (drinkAlarm == null || eyeTime == null || exerciseTime == null ||workingTime == null) {
                return Result.failure()
            }
            val newDrinkTimer = drinkAlarm.nextNotificationTime.convertStringTimeToDate().apply {
                time + drinkAlarm.durationNotification * 60 * 1000
            }
            val newEyeTimer = eyeTime.nextNotificationTime.convertStringTimeToDate().apply {
                time + eyeTime.durationNotification * 60 * 1000
            }
            val newExerciseTimer =
                exerciseTime.nextNotificationTime.convertStringTimeToDate().apply {
                    time + exerciseTime.durationNotification * 60 * 1000
                }
            // Check if any of the next alarm times are null


//            if (newDrinkTimer > workingTime.afternoonEndTime.toDa)
            val intent = Intent().apply {
                action = AppConstant.ALARM_ACTION_RECEIVER
                `package` = AppConstant.packageName
            }

            // Set up the alarms using the AlarmScheduler
            val alarmScheduler = AlarmScheduler(context = appContext, appRepository)
            alarmScheduler.setupAlarmDate(newDrinkTimer, intent.apply {
                putExtra(AppConstant.DRINK_WATER_BUNDLE_ID, AppConstant.ID_DRINK_WATER)
            }, AppConstant.ID_DRINK_WATER)
            alarmScheduler.setupAlarmDate(newEyeTimer, intent.apply {
                putExtra(AppConstant.EYES_RELAX_BUNDLE_ID, AppConstant.ID_EYES_RELAX)
            }, AppConstant.ID_EYES_RELAX)
            alarmScheduler.setupAlarmDate(newExerciseTimer, intent.apply {
                putExtra(AppConstant.EXERCISE_BUNDLE_ID, AppConstant.ID_EXERCISE)
            }, AppConstant.ID_EXERCISE)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

}